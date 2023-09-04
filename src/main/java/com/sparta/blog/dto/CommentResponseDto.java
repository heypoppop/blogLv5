package com.sparta.blog.dto;

import com.sparta.blog.entity.Comment;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class CommentResponseDto {
    long id;
    String comment;
    String username;
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.username = comment.getUser().getUsername();

    }
}
