package com.sparta.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class RestApiException {
    private int statusCode;
    private String errorMessage;

    public static ResponseEntity<RestApiException> toResponseEntity(ErrorCode e) {
        return ResponseEntity.status(e.getStatusCode()).body(new RestApiException(e.getStatusCode(), e.getMessage()));
    }
}
