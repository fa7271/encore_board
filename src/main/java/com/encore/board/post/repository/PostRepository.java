package com.encore.board.post.repository;

import com.encore.board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    List<Post> findAllByOrderByCreatedTimeDesc(); // spring data jpa 에서 지원


    //    pageable 객체 안에 pageNumber(page = n), page마다개수(size = n), 정렬(sort = createdTime,desc)
//    page객체 : List<Post> + 해당 Page의 각종 정보
    Page<Post> findAll(Pageable pageable);

//    @Override
//    <S extends Post> Page<S> findAll(Example<S> example, Pageable pageable);

    //    select p.* from post p left join author a on p.author_id= a.id
    @Query("select p from Post p left join p.author") // jpql 문
    List<Post> findAllJoin();

//    select p.*,a.* from post p left join author a on p.author_id= a.id
    @Query("select p from Post p left join fetch p.author")
    List<Post> findAllFetchJoin();


//    y가 아닌 것들을 가져옴
    Page<Post> findByAppointment(String appointment, Pageable pageable);
}
