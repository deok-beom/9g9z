package com.sparta.moviecomunnity.controller;

import com.sparta.moviecomunnity.dto.CommentCreateRequestDto;
import com.sparta.moviecomunnity.dto.CommentRequestDto;
import com.sparta.moviecomunnity.entity.Post;
import com.sparta.moviecomunnity.exception.CustomException;
import com.sparta.moviecomunnity.exception.ServerResponse;
import com.sparta.moviecomunnity.security.UserDetailsImpl;
import com.sparta.moviecomunnity.service.CommentService;

import com.sparta.moviecomunnity.service.PostService;
import com.sparta.moviecomunnity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.moviecomunnity.exception.ResponseCode.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    // 댓글 작성
    @ResponseBody
    @PostMapping("")
    public ResponseEntity<ServerResponse> createComment(@RequestBody CommentCreateRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String comment = commentRequestDto.getContent();
        if (comment.trim().equals("")) {
            throw new CustomException(INVALID_CONTENT);
        }
        Post post = postService.findPost(commentRequestDto.getPostId());
        commentService.createComment(commentRequestDto, post, userDetails.getUser());
        return ServerResponse.toResponseEntity(SUCCESS_CREATE);
    }

    // 댓글 수정
    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<ServerResponse> editComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String comment = commentRequestDto.getContent();
        if (comment.trim().equals("")) {
            throw new CustomException(INVALID_CONTENT);
        }

        commentService.editComment(id, commentRequestDto, userDetails.getUsername());
        return ServerResponse.toResponseEntity(SUCCESS_EDIT);
    }


    // 댓글 삭제
    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<ServerResponse> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 게시글 삭제
        commentService.deleteComment(id, userDetails.getUsername(), userDetails.getUser().getRole());
        return ServerResponse.toResponseEntity(SUCCESS_DELETE);
    }

}
