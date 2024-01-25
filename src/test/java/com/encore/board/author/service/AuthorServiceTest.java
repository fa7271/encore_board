package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import com.encore.board.author.dto.Author.AuthorDetailResDto;
import com.encore.board.author.dto.Author.AuthorUpdateReqDto;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @MockBean // 가짜 객체를 만드는 작업을 mocking이라 한다
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Update 메소드 Test")
    void updateTest() {
        Long authorId = 1L;
        Author author = Author.builder()
                .name("updatetest")
                .role(Role.ADMIN)
                .password("updatetest")
                .email("nsdf@n.com")
                .build();
        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.ofNullable(author));
        AuthorUpdateReqDto authorUpdateReqDto = new AuthorUpdateReqDto();
        authorUpdateReqDto.setName("ssss");
        authorUpdateReqDto.setPassword("1234");

        authorService.update(authorId, authorUpdateReqDto);

        Assertions.assertEquals(author.getName(), authorUpdateReqDto.getName());
        Assertions.assertEquals(author.getPassword(), authorUpdateReqDto.getPassword());
    }


    @Test
    @DisplayName("findAuthorDetail 메소드")
    void findAuthorDetail() {
        Long authorId = 1L;

        List<Post> posts = new ArrayList<>();
        Post post = Post.builder()
                .title("제목")
                .contents("내용")
                .build();
        posts.add(post);
        Author author = Author.builder()
                .name("updatetest")
                .role(Role.ADMIN)
                .password("updatetest")
                .email("nsdf@n.com")
                .posts(posts)
                .build();
        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.ofNullable(author));
        AuthorDetailResDto authorDetail = authorService.findAuthorDetail(authorId);

        Assertions.assertEquals(author.getName(),authorDetail.getName());
        Assertions.assertEquals(author.getPosts().size(), authorDetail.getCounts());


    }
    @Test
    @DisplayName("findAll 메소드 Test")
    void findAllTest() {

//        Mock Repository 기능 기현
        List<Author> authors = new ArrayList<>();
        authors.add(new Author());
        authors.add(new Author());

//        authorRepository.findAll() 을 호출하면 여기서 생성한 가짜객체 authors 를 return 한다.
        Mockito.when(authorRepository.findAll()).thenReturn(authors);
//        검증
        Assertions.assertEquals(2, authorService.findAll().size());
    }
}
