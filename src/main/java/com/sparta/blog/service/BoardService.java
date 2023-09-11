package com.sparta.blog.service;

import com.sparta.blog.dto.BoardRequestDto;
import com.sparta.blog.dto.BoardResponseDto;
import com.sparta.blog.dto.MessageResponseDto;
import com.sparta.blog.entity.*;
import com.sparta.blog.exception.CustomException;
import com.sparta.blog.exception.ErrorCode;
import com.sparta.blog.repository.BoardLikeRepository;
import com.sparta.blog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;


    // 전체 조회
    public List<BoardResponseDto> getBoard() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    //생성
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User user) {
        Board board = boardRepository.save(new Board(boardRequestDto, user));
        return new BoardResponseDto(board);
    }

    // 게시물 id로 조회
    public BoardResponseDto getBoardById(Long id) {
        Board board = boardRepository.findBoardById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOARD));
        return new BoardResponseDto(board);
    }

    // 수정
    @Transactional
    public ResponseEntity<MessageResponseDto> updateBoard(Long id, BoardRequestDto boardRequestDto, User user) {
        Board board = findBoard(id);
        // 어드민 or 작성자 체크
        if (user.getRole().equals(UserRoleEnum.ADMIN) || board.getUser().getUsername().equals(user.getUsername())) {
            board.update(boardRequestDto, user);
            MessageResponseDto message = new MessageResponseDto(HttpStatus.OK.value(), " 게시물 수정 성공" );
            return ResponseEntity.status(200).body(message);}

        throw new CustomException(ErrorCode.NOT_USER_COMMENT);

    }


    // 삭제
    public ResponseEntity<MessageResponseDto> deleteBoard(Long id, User user) {
        Board board = findBoard(id);

        // 어드민 or 작성자 체크
        if (user.getRole().equals(UserRoleEnum.ADMIN) || board.getUser().getUsername().equals(user.getUsername())) {
            boardRepository.delete(board);
            MessageResponseDto message = new MessageResponseDto(HttpStatus.OK.value(), " 게시물 수정 성공" );
            return ResponseEntity.status(200).body(message);}

        throw new CustomException(ErrorCode.NOT_USER_COMMENT);


    }

    public ResponseEntity<MessageResponseDto> likeBoard(Long id, User user) {
        Board board = findBoard(id);
        Optional<BoardLike> like = boardLikeRepository.findByUserIdAndBoardId(user.getId(), id);
        if (like.isEmpty()) {
            boardLikeRepository.save(new BoardLike(user, board));
            MessageResponseDto message = new MessageResponseDto(HttpStatus.OK.value(), " 게시물 좋아요 성공" );
            return ResponseEntity.status(200).body(message);
        }
        boardLikeRepository.delete(like.get());
        return ResponseEntity.status(200).body(new MessageResponseDto(HttpStatus.OK.value(), " 게시물 좋아요 취소" ));
    }

    // DB에서 찾기
    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOARD));
    }

}

