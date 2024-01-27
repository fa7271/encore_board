package com.encore.board.post.controller;

import com.encore.board.post.dto.Post.PostCreateReqDto;
import com.encore.board.post.dto.Post.PostDetailResDto;
import com.encore.board.post.dto.Post.PostUpdateReqDto;
import com.encore.board.post.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public String postSave(Model model,PostCreateReqDto postCreateReqDto) {
        try {
            postService.save(postCreateReqDto);
            return "redirect:/post/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "post/post-create";
        }

    }

    @PostMapping("/post/{id}/update")
    public String postUpdate(@PathVariable long id, PostUpdateReqDto postUpdateReqDto) {
        postService.update(id, postUpdateReqDto);
        return "redirect:/post/detail/" + id;
    }



//    @GetMapping("/post/list")
////    localhost:8080/post/list?size=xx&page=xx&sort==xx
//    public String postList(Model model, @PageableDefault(size = 5, sort = "createdTime",direction = Sort.Direction.DESC) Pageable pageable) {
//        model.addAttribute("postList", postService.findAllPaging(pageable));
//        return "post/post-list";
//    }


    @GetMapping("/post/list")
//    localhost:8080/post/list?size=xx&page=xx&sort==xx
    public String postList(Model model, @PageableDefault(size = 5, sort = "createdTime",direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("postList", postService.findByAppointment(pageable));
        return "post/post-list";
    }

/*

    //    *** paging json test***
    @GetMapping("/json/post/list")
//    localhost:8080/post/list?size=xx&page=xx&sort==xx
    @ResponseBody
    public Page<PostListResDto> postList(Pageable pageable) {
        Page<PostListResDto> postListResDtos = postService.findAlljson(pageable);
        return postListResDtos;
    }
*/

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
