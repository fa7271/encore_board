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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    public List<PostListResDto> findAll() {
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

    public void save(PostCreateReqDto postCreateReqDto) throws EntityNotFoundException{
        Author author = authorRepository.findByEmail(postCreateReqDto.getEmail()).orElse(null);
        Post post = Post.builder()
                .title(postCreateReqDto.getTitle())
                .contents(postCreateReqDto.getContents())
                .author(author)
                .build();
//        Post post = new Post(
//                postCreateReqDto.getTitle(),
//                postCreateReqDto.getContents()
//        );

        author.updateAuthor("dirtychecking test", "1234");
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
