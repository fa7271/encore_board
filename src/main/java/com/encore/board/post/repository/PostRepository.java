package com.encore.board.post.repository;

import com.encore.board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedTimeDesc(); // spring data jpa 에서 지원

//    select p.* from post p left join author a on p.author_id= a.id
    @Query("select p from Post p left join p.author") // jpql 문
    List<Post> findAllJoin();

//    select p.*,a.* from post p left join author a on p.author_id= a.id
    @Query("select p from Post p left join fetch p.author")
    List<Post> findAllFetchJoin();
//    Page<Post> findByAuthor(Author author, Pageable pageable);
}
