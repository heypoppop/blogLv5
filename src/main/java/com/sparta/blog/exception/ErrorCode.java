package com.sparta.blog.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    DUPLICATED_USERNAME(400, "닉넴은 선착순이다."),
    INVALID_ADMIN_TOKEN(401, "어드민 토큰 틀렸다."),
    NOT_FOUND_BOARD(400, "게시판 없다."),
    NOT_USER_BOARD(400, "님 게시물 아님."),
    TOKEN_ERROR(403, "토큰 틀렸다."),
    AUTHENTICATION_ERROR(403, "권한 없다."),
    NOT_USER_COMMENT(400, "님 댓글 아님."),
    NOT_FOUND_COMMENT(400, "댓글 없다.");
    private final int statusCode;
    private final String message;
}
