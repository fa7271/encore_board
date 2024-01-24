package com.encore.board.post.domain;

import com.encore.board.author.domain.Author;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String contents;

//    author_id 는 DB의 컬럼명, 별다른 옵션 없으면 author의 pk에 fk가 설정
//    @JoinColumn(nullable = false,name = "author_email",referenceColumnName = "email)
//    Post 입장에서 한 개의 id에서 Post는 여러개가 나올 수 있다.
//    post객체 입장에서는 한 사람이 여러개 글을 쓸 수 있으므로 N:1
//    즉시로딩이 디폴트, -> 지연로딩 n대 1이기때문에 즉시로딩 해도 상관없다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="author_id")
    private Author author;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedTime;

    @Builder
    public Post(String title, String contents, Author author) {
        this.title = title;
        this.contents = contents;
        this.author = author;
//        author 객체의 posts 를 초기화시켜준 후
//        this.author.getPosts().add(this); setter 안 쓰고 하는 법
    }

    public void updatePost(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
