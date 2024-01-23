package com.encore.board.post.dto.Post;

import lombok.Data;

@Data
public class PostCreateReqDto {
    private String title;
    private String contents;
}
