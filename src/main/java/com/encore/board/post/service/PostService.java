package com.encore.board.post.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.Post.PostCreateReqDto;
import com.encore.board.post.dto.Post.PostDetailResDto;
import com.encore.board.post.dto.Post.PostListResDto;
import com.encore.board.post.dto.Post.PostUpdateReqDto;
import com.encore.board.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final RedissonClient redissonClient;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository, RedissonClient redissonClient) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
        this.redissonClient = redissonClient;
    }

    public void delete(long id) {
        postRepository.deleteById(id);

    }

    public List<PostListResDto> findAll(Pageable pageable) {
        List<Post> posts = postRepository.findAllFetchJoin();
        ArrayList<PostListResDto> postlists = new ArrayList<>();

        for (Post post : posts) {
            PostListResDto postListResDto = new PostListResDto();
            postListResDto.setId(post.getId());
            postListResDto.setTitle(post.getTitle());

            postListResDto.setAuthor_email(post.getAuthor() == null ? "익명" : post.getAuthor().getEmail());
            postlists.add(postListResDto);
        }
        return postlists;
    }


//    *** paging ***
    public Page<PostListResDto> findAllPaging(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(p->
                new PostListResDto(
                        p.getId(),
                        p.getTitle(),
                        p.getAuthor() == null ? "익명" : p.getAuthor().getEmail()
                ));
    }

//    *** Y 가 아닌애들을 찾는다
//    @Cacheable(value = "postList", key ="#id")
    public Page<PostListResDto> findByAppointment(Pageable pageable) {
        Page<Post> posts = postRepository.findByAppointment(null,pageable);

        return posts.map(p->
                new PostListResDto(
                        p.getId(),
                        p.getTitle(),
                        p.getAuthor() == null ? "익명" : p.getAuthor().getEmail()
                ));
    }

    public void save(PostCreateReqDto postCreateReqDto, String email) throws IllegalArgumentException{

        Author author = authorRepository.findByEmail(email).orElse(null);
        LocalDateTime localDateTime = null;
        String appointment = null;
        if (postCreateReqDto.getAppointment().equals("Y") && !postCreateReqDto.getAppointmentTime().isEmpty()) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            localDateTime = LocalDateTime.parse(postCreateReqDto.getAppointmentTime(), dateTimeFormatter);
            LocalDateTime now = LocalDateTime.now();
            if (localDateTime.isBefore(now)) {
                throw new IllegalArgumentException("시간정보 잘못입력");
            }
            appointment = "Y";
        }
        Post post = Post.builder()
                .title(postCreateReqDto.getTitle())
                .contents(postCreateReqDto.getContents())
                .author(author)
                .appointment(appointment)
                .appointmentTime(localDateTime)
                .build();

        postRepository.save(post);
    }

    public PostDetailResDto findById(long id) throws EntityNotFoundException {

        Post post = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        PostDetailResDto postDetailResDto = new PostDetailResDto();
        postDetailResDto.setId(post.getId());
        postDetailResDto.setCreatedTime(post.getCreatedTime());
        postDetailResDto.setTitle(post.getTitle());
        postDetailResDto.setContents(post.getContents());
        return postDetailResDto;
    }

    public void update(long id, PostUpdateReqDto postUpdateReqDto) throws EntityNotFoundException {
        log.info("업데이트 시작");
        Post post = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        String lockName = "MEMBER" + id;

        RLock lock = redissonClient.getLock(lockName);

        long waitTime = 5L;
        long leaseTime = 3L;
        TimeUnit timeUnit = TimeUnit.SECONDS;

        try {
            boolean available = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (!available) {
                throw new IllegalArgumentException("락 획득 실패");
            }
//            락 획득후 수행
            post.updatePost(postUpdateReqDto.getTitle(), postUpdateReqDto.getContents());
            postRepository.save(post);

        } catch (InterruptedException i) {
            throw new IllegalArgumentException("락 얻으려고 했는데 뺏어감");
        } finally {
            try {
                lock.unlock();
                 log.info("unlock complete: {}", lock.getName());
            } catch (IllegalMonitorStateException e) {
                throw new IllegalArgumentException("이미 종료된 락입니다.");
            }
        }
    }
}
