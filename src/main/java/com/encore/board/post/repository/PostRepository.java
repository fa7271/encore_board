package com.encore.board.post.repository;

import com.encore.board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedTimeDesc();
//    Page<Post> findByAuthor(Author author, Pageable pageable);
}
