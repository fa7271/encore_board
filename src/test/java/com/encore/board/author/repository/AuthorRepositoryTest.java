package com.encore.board.author.repository;


import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


// @DataJpaTest를 사용하면 매 테스트가 종료되면 자동으로 DB원상복구
// 모든 스프링빈을 생성하지 않고, DB테스트 특화 어노테이션
@DataJpaTest

// test 파일에서 yml을 찾으려고 한다. 하지만 test패키지 에는 없다.
//replace = AutoConfigureTestDatabase.Replace.Any : H2Db(스프링 내장 인메모리)가 기본설정이다
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)


// rollback 안돼서 별도의 롤백 코드, 어노테이션이 필요하다.
// SpringBootTest 은 실제 스프링 실행과 동일하게 스프링빈생성 및 주입한다.
//@SpringBootTest
//@Transactional
@ActiveProfiles("test") // applications-test.yml 를 찾아 설정값 세팅

public class AuthorRepositoryTest {
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("save 메소드 테스트")
    public void authorSaveTest() {

//         given prepare 준비
        Author author = Author.builder()
                .name("wkdwnsgur")
                .email("fa724@ds.com")
                .password("!23421")
                .role(Role.ADMIN)
                .build();

//        execute when 실행
        authorRepository.save(author);
        Author authordb = authorRepository.findByEmail("fa724@ds.com").orElse(null);

//        then 검증
//        오류의 원인 파악, null처리, 가시적으로 성공/실패 여부 확인
        Assertions.assertThat(author.getEmail()).isEqualTo(authordb.getEmail());
    }
}
