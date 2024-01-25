package com.encore.board.post;

import com.encore.board.author.dto.Author.AuthorDetailResDto;
import com.encore.board.author.service.AuthorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// 1. Web MVC controller 계층을 테스트, 모든 스프링 빈을 생성하고 주입하지는 않는다.
//@WebMvcTest(AuthorController.class)

// 2.
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
//    @WithMockUser // 시큐리티 의존성 추가 필요
    @DisplayName("authorDetail 테스트")
    void authorDetailTest() throws Exception {
        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
        authorDetailResDto.setName("testname");
        authorDetailResDto.setEmail("124asf");
        authorDetailResDto.setPassword("124");
        Mockito.when(authorService.findAuthorDetail(1L)).thenReturn(authorDetailResDto);

        // 엔드포인트가 "/author/1/circle/dto" 인 곳에서 findAuthorDetail메소드를 사용하고 있기 때문에
        mockMvc.perform(MockMvcRequestBuilders.get("/author/1/circle/dto"))
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(jsonPath("$.name","testname"));

    }


}
