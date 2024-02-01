package com.encore.board.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

//    WebSecurityConfigurerAdpter 상속 방방식은 deprecated(지원종료) 되었다.

@Configuration
@EnableWebSecurity() // spring security 설정을 customizing 하기 위함.

// 사전, 사후에 인증/권한 검사 어노테이션 사용 가능 authorController @PreAuthorize 쓰려고
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig {
    @Bean
    public SecurityFilterChain myFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
//                csrf 보안 공격에 대한 설정은 하지 않는다.
                .csrf().disable()
//                url마다 인증처리 할지 말지 설정한다.
                .authorizeRequests()
//                인증 미 적용 url
                .antMatchers("/", "/author/create", "/author/login-page")
                .permitAll()
//                그 외에 인증 모두 필요
                .anyRequest().authenticated()
                .and()
//                만약에 세션을 사용하지 않으면 아래 내용 설정, 즉 세션이 기본임
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .formLogin()
                .loginPage("/author/login-page")
//                스프링 내장 메서드 사용하기 위해 /doLogin url 사용
                .loginProcessingUrl("/doLogin")
                .usernameParameter("email")
                .passwordParameter("pw")
                .successHandler(new LoginSuccessHandler())
                .and()
                .logout()
//                스프링 내자 doLogout 기능 사
                .logoutUrl("/doLogout")
                .and()
                .build();

    }
}
