package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.MessageResponseDto;
import com.sparta.blog.entity.*;
import com.sparta.blog.exception.CustomException;
import com.sparta.blog.exception.ErrorCode;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.LikeRepository;
import com.sparta.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    // 댓글 작성
    public ResponseEntity<MessageResponseDto> createComment(CommentRequestDto commentRequestDto, User user) {
        Board board = boardRepository.findById(commentRequestDto.getBoardId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOARD));
        commentRepository.save(new Comment(commentRequestDto, user, board));
        MessageResponseDto message = new MessageResponseDto(HttpStatus.OK.value(), user.getUsername() + " 댓글 작성 성공");
        return ResponseEntity.status(200).body(message);
    }

    // 수정
    @Transactional
    public ResponseEntity<MessageResponseDto> updateComment(Long id, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(id);

        // 어드민 or 작성자 체크
        if (user.getRole().equals(UserRoleEnum.ADMIN) || comment.getUser().getUsername().equals(user.getUsername())) {
            comment.update(commentRequestDto, user);
            MessageResponseDto message = new MessageResponseDto(HttpStatus.OK.value(), " 댓글 수정 성공");
            return ResponseEntity.status(200).body(message);
        }

        throw new CustomException(ErrorCode.NOT_USER_COMMENT);
    }

    // 삭제
    public ResponseEntity<MessageResponseDto> deleteComment(Long id, User user) {
        Comment comment = findComment(id);

        // 어드민 or 작성자 체크
        if (user.getRole().equals(UserRoleEnum.ADMIN) || comment.getUser().getUsername().equals(user.getUsername())) {
            commentRepository.delete(comment);
            MessageResponseDto message = new MessageResponseDto(HttpStatus.OK.value(), " 댓글 삭제 성공");
            return ResponseEntity.status(200).body(message);
        }

        throw new CustomException(ErrorCode.NOT_USER_COMMENT);

    }


    public ResponseEntity<MessageResponseDto> likeComment(Long id, User user) {
        Comment comment = findComment(id);
        Optional<Like> like = likeRepository.findByUserIdAndBoardId(user.getId(), id);
        if (like.isEmpty()) {
            likeRepository.save(new Like(user, comment.getBoard(), comment));
            MessageResponseDto message = new MessageResponseDto(HttpStatus.OK.value(), " 게시물 좋아요 성공" );
            return ResponseEntity.status(200).body(message);
        }
        likeRepository.delete(like.get());
        return ResponseEntity.status(200).body(new MessageResponseDto(HttpStatus.OK.value(), " 게시물 좋아요 취소" ));
    }

    // DB에서 찾기
    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
    }

}
