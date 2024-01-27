package com.encore.board.author.controller;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.Author.AuthorDetailResDto;
import com.encore.board.author.dto.Author.AuthorSaveReqDto;
import com.encore.board.author.dto.Author.AuthorUpdateReqDto;
import com.encore.board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthorController {

    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @GetMapping("/author/create")
    public String authorCreate() {
        return "author/author-create";
    }
    @PostMapping("/author/create")
    public String authorSave(Model model, AuthorSaveReqDto authorSaveReqDto) {
        try {
            authorService.save(authorSaveReqDto);
            return "redirect:/author/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/author/author-create";
        }
    }

    @GetMapping("/author/list")
    public String authorList(Model model) {
        model.addAttribute("authorList", authorService.findAll());
        return "author/author-list";
    }
    @GetMapping("/author/detail/{id}")
    public String authorDetail(Model model, @PathVariable long id) {
        model.addAttribute("author", authorService.findAuthorDetail(id));
        return "author/author-detail";
    }

//    @PostMapping("/author/update")
    @PostMapping("/author/{id}/update")
    public String authorUpdate(@PathVariable long id, AuthorUpdateReqDto authorUpdateReqDto) {
        authorService.update(id, authorUpdateReqDto);
        return "redirect:/author/detail/" + id;
    }

    @GetMapping("/author/delete/{id}")
    public String authorDelete(@PathVariable long id) {
        authorService.delete(id);
        return "redirect:/author/list";
    }

//    entity 순환참조 이슈 테스트
    @GetMapping("/author/{id}/circle/issue")
    @ResponseBody
//    연관관계가 있는 Author 엔티티를 json으로 직렬화를 하게 될 경우
//    순환참조 이슈 발생하므로, dto, 사용필요
    public Author circleIssueTest1(@PathVariable Long id) {

        return authorService.findById(id);
    }

    @GetMapping("/author/{id}/circle/dto")
    @ResponseBody
//    연관관계가 있는 Author 엔티티를 json으로 직렬화를 하게 될 경우
//    순환참조 이슈 발생하므로, dto, 사용필요
    public AuthorDetailResDto circleIssueTest2(@PathVariable Long id) {
        return authorService.findAuthorDetail(id);
    }

}

