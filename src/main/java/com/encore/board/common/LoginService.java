package com.encore.board.common;


import com.encore.board.author.domain.Author;
import com.encore.board.author.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LoginService  implements UserDetailsService {

    private final AuthorService authorService;

    public LoginService(AuthorService authorService) {
        this.authorService = authorService;
    }

//    내부적으로 로그인시 유저의 정보를 세션에 저장하기 위해 필요한 코드.
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            log.info("username : {}", username);
            Author author = authorService.findByEmail(username);
    //        매개변수 : userEmail, password, 권한(authorities)

    //        권한이 여러개있을수도 있어서 []
            List<GrantedAuthority> authorities = new ArrayList<>();
    //        Role_권한 패턴으로 스프링에서 기본적으로 권한 체크 ex)ROLE_ADMIN
            authorities.add(new SimpleGrantedAuthority("ROLE_"+author.getRole()));
    //       해당 메서드에서 return 되는 User객체는 Session 메모리에 저장소에 저장되어, 계속 사용 가능
            return new User(author.getEmail(), author.getPassword(),authorities);
        }
}
