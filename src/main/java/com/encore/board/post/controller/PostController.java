package com.encore.board.post.controller;

import com.encore.board.post.dto.Post.PostCreateReqDto;
import com.encore.board.post.dto.Post.PostDetailResDto;
import com.encore.board.post.dto.Post.PostUpdateReqDto;
import com.encore.board.post.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/create")
    public String postCreate() {
        return "post/post-create";
    }

    @PostMapping("/post/create")
    public String postSave(PostCreateReqDto postCreateReqDto) {
        postService.save(postCreateReqDto);
        return "redirect:/post/list";
    }

    @PostMapping("/post/{id}/update")
    public String postUpdate(@PathVariable long id, PostUpdateReqDto postUpdateReqDto) {
        postService.update(id, postUpdateReqDto);
        return "redirect:/post/detail/" + id;
    }

    @GetMapping("/post/list")
    public String postList(Model model) {
        model.addAttribute("postList", postService.findAll());
        return "post/post-list";
    }

    @GetMapping("/post/detail/{id}")
    public String postDetail(Model model, @PathVariable long id) {
        PostDetailResDto byId = postService.findById(id);
        model.addAttribute("post", postService.findById(id));
        return "post/post-detail";
    }
    @GetMapping("/post/delete/{id}")
    public String postDelete(@PathVariable long id) {
        postService.delete(id);
        return "redirect:/post/list";
    }
}
