package com.encore.board.post.dto.Post;

import lombok.Data;

@Data
public class PostCreateReqDto {
    private String title;
    private String contents;
    private String email;
    private String appointment;

    private String appointmentTime;
}
