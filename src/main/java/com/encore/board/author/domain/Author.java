package com.encore.board.author.domain;


import com.encore.board.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor // jpa에서 데이터를 조립할때. 필요
@Getter
//@Builder
//@AllArgsConstructor
// 위에 같이 모든 매개변수가 있는 생성자를 생헝하는 어노테이션과 Builer를 클래스에 붙여
// 모든 매개변수가 있는 생성자 위에 Builder어노테이션을 붙인것과 같은 효과가 있다.
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 20)
    private String name;
    @Column(length = 20, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

//    author 를 조회할때 post객체가 필요할시에 선언
//    list쓰는 이유는 1:N이 될 수도 있기 때문이다.
//    mappedBy 연관관계의 주인을 명시하고, fk 를 관리하는 변수명을 명시한다. 일반적으로 부모에서 건다.
//    LAZY 지연로딩, EAGER 즉시로ㅂ
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Setter // cascade = CascadeType.ALL test 위한 설정
    private List<Post> posts;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedTime;


    // 생성자 방식으로 만들면 ,
    // 1. 유연성이 떨어진다, 순서를 지켜줘야한다.
    // -> builder 패턴 사용


//    builder 어노테이션을 통해 빌더패턴으로 객체 생성,
//    매개변수의 세팅순서, 매개변수의 개수 등을 유연하게 세팅
    @Builder
    public Author(String name, String email, String password, Role role, List<Post> posts) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.posts = posts;
    }

    public void updateAuthor(String name, String password) {
        this.name = name;
        this.password = password;
    }
}

