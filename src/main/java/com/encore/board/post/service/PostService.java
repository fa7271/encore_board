package com.encore.board.post.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.Post.PostCreateReqDto;
import com.encore.board.post.dto.Post.PostDetailResDto;
import com.encore.board.post.dto.Post.PostListResDto;
import com.encore.board.post.dto.Post.PostUpdateReqDto;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

//    public PostService(PostRepository postRepository) {
//        this.postRepository = postRepository;
//    }

    public void delete(long id) {
        postRepository.deleteById(id);

    }

    public List<PostListResDto> findAll(Pageable pageable) {
//        List<Post> all = postRepository.findAllByOrderByCreatedTimeDesc();
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
//        List<Post> all = postRepository.findAllByOrderByCreatedTimeDesc();
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostListResDto> postListResDtos
                = posts.map(p->
                new PostListResDto(
                        p.getId(),
                        p.getTitle(),
                        p.getAuthor() == null ? "익명" : p.getAuthor().getEmail()
                )); // page에 형태를 모르기 때문에 for문을 사용하지 않는다. > Page 안에 Map 객체를 지원한다.


        return postListResDtos;
    }

//    *** Y 가 아닌애들을 찾는다
    public Page<PostListResDto> findByAppointment(Pageable pageable) {
//        List<Post> all = postRepository.findAllByOrderByCreatedTimeDesc();
        Page<Post> posts = postRepository.findByAppointment(null,pageable);
        Page<PostListResDto> postListResDtos
                = posts.map(p->
                new PostListResDto(
                        p.getId(),
                        p.getTitle(),
                        p.getAuthor() == null ? "익명" : p.getAuthor().getEmail()
                )); // page에 형태를 모르기 때문에 for문을 사용하지 않는다. > Page 안에 Map 객체를 지원한다.


        return postListResDtos;
    }

    public void save(PostCreateReqDto postCreateReqDto) throws IllegalArgumentException{
        Author author = authorRepository.findByEmail(postCreateReqDto.getEmail()).orElse(null);
        LocalDateTime localDateTime = null;
        String appointment = null;
        if (postCreateReqDto.getAppointment().equals("Y") && !postCreateReqDto.getAppointmentTime().isEmpty()) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            localDateTime = LocalDateTime.parse(postCreateReqDto.getAppointmentTime(), dateTimeFormatter);
            LocalDateTime now = localDateTime.now();
            System.out.println(localDateTime);
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
//        Post post = new Post(
//                postCreateReqDto.getTitle(),
//                postCreateReqDto.getContents()
//        );

//        author.updateAuthor("dirtychecking test", "1234");
//        더티체킹 테스트

        postRepository.save(post);
    }

    public PostDetailResDto findById(long id) throws EntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        PostDetailResDto postDetailResDto = new PostDetailResDto();
        postDetailResDto.setId(post.getId());
        postDetailResDto.setCreatedTime(post.getCreatedTime());
        postDetailResDto.setTitle(post.getTitle());
        postDetailResDto.setContents(post.getContents());
        return postDetailResDto;
    }

    public void update(long id, PostUpdateReqDto postUpdateReqDto) throws EntityNotFoundException{
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        post.updatePost(postUpdateReqDto.getTitle(), postUpdateReqDto.getContents());
        postRepository.save(post);

    }
}
