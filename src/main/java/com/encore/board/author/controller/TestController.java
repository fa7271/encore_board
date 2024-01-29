package com.encore.board.author.controller;


import com.encore.board.author.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j // log 어노테이션 (롬복)
public class TestController {

//    private static final Logger log = LoggerFactory.getLogger(LogTestController.class)

    @Autowired
    private AuthorService authorService;
    @GetMapping("log/test1")
    public String testMethod1() {
        log.debug("OK1");
        log.info("OK2");
        log.error("OK3");
        return "OK";
    }

    @GetMapping("exeption/test1/{id}")
    public String exceptionTestMethod1(@PathVariable Long id) {
        authorService.findById(id);
        log.debug("OK1");
        log.info("OK2");
        log.error("OK3");
        return "OK";
    }
}
