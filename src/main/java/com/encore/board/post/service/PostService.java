package com.encore.board.post.service;

import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.Post.PostCreateReqDto;
import com.encore.board.post.dto.Post.PostDetailResDto;
import com.encore.board.post.dto.Post.PostListResDto;
import com.encore.board.post.dto.Post.PostUpdateReqDto;
import com.encore.board.post.repository.PostRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void delete(long id) {
        postRepository.deleteById(id);

    }

    public List<PostListResDto> findAll() {
        List<Post> all = postRepository.findAll();
        ArrayList<PostListResDto> postlists = new ArrayList<>();

        for (Post x : all) {
            PostListResDto postListResDto = new PostListResDto();
            postListResDto.setId(x.getId());
            postListResDto.setTitle(x.getTitle());

            postlists.add(postListResDto);
        }
        return postlists;
    }

    public void save(PostCreateReqDto postCreateReqDto) {
        Post post = new Post(
                postCreateReqDto.getTitle(),
                postCreateReqDto.getContents()

        );

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
