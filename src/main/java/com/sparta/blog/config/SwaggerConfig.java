package com.sparta.blog.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "게시판 API 명세서",
                description = "게시판 서비스 명세서",
                version = "v3"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
}
