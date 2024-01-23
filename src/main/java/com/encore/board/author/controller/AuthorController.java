package com.encore.board.author.controller;

import com.encore.board.author.dto.Author.AuthorSaveReqDto;
import com.encore.board.author.dto.Author.AuthorUpdateReqDto;
import com.encore.board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String authorSave(AuthorSaveReqDto authorSaveReqDto) {
        authorService.save(authorSaveReqDto);
        return "redirect:/author/list";
    }

    @GetMapping("/author/list")
    public String authorList(Model model) {
        model.addAttribute("authorList", authorService.findAll());
        return "author/author-list";
    }
    @GetMapping("/author/detail/{id}")
    public String authorDetail(Model model, @PathVariable long id) {
        model.addAttribute("author", authorService.findByID(id));
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
}

