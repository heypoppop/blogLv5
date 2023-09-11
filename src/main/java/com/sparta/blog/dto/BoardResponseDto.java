package com.sparta.blog.dto;

import com.sparta.blog.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class BoardResponseDto {

    private Long id;
    private String username;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer likeCount;
    private List<CommentResponseDto> commentList = new ArrayList<>();


    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.username = board.getUser().getUsername();
        this.likeCount = board.getLikessList().size();
        board.getCommentsList().forEach(comment -> commentList.add(new CommentResponseDto(comment)));
        Collections.reverse(commentList);

    }
}
