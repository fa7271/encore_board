package com.encore.board.author.controller;


import com.encore.board.author.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


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

    @GetMapping("/userinfo/test")
    public String userInfoTeset(HttpServletRequest request) {
//        로그인 유저정보 얻는 방식
//        방법1. session attribute를 통해 접근
        String email1 = request.getSession().getAttribute("email").toString();
        System.out.println("email1 = " + email1);
//        방법2. session에서 Authentication 객체를접근
        SecurityContext springContext = (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        String email2 = springContext.getAuthentication().getName();
        System.out.println("email2 = " + email2);
//        방법3. SecurityContextHolder에서 Authentication객체를 접근 (제일 많이씀)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println("email = " + email);
        return "OK";
    }
}
