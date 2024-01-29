package com.encore.board.post.service;

import com.encore.board.post.domain.Post;
import com.encore.board.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Transactional
public class PostScheduler {

    private final PostRepository postRepository;

    public PostScheduler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

//    초 분 시간 일 월 요일  형태로 스케쥴링 설정
//    *: 매 초 (분/시 등을 )의미
//    특정숫자 : 특정숫자의 초(분/시) 등을 의미
//    0/숫자 : 특정 숫자마다
//    ex) 0 0 * * * * >> 매일 0분 0초에 스케쥴링 시작
//    ex) 0 0/1 * * * * >> 매일 1분마다 0초에 스케쥴링 시작
//    ex) 0 0 11 * * * >> 매일 11시에 스케쥴링
    @Scheduled(cron = "0 0/1 * * * *")
    public void postSchedule() {
        System.out.println("--- 스케줄러시작 ---");
        Page<Post> posts = postRepository.findByAppointment("Y", Pageable.unpaged());

        String appointment = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        for (Post p : posts.getContent()) {
            LocalDateTime now = LocalDateTime.now();
            if (p.getAppointmentTime().isBefore(now)) {
                p.updateAppointment(null);
//                postReposito  ry.save(p);
            }
        }
        System.out.println("=== 스케쥴러 시작===");
    }
}
