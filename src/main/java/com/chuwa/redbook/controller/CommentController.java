package com.chuwa.redbook.controller;

import com.chuwa.redbook.payload.CommentDto;
import com.chuwa.redbook.service.CommentService;
import com.chuwa.redbook.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentDto dto) {
        return new ResponseEntity<>(commentService.createComment(postId, dto), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<CommentDto> getAllComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return commentService.getCommentsByPost(postId, pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        return commentService.getComment(postId, commentId);
    }

    @PutMapping("/{commentId}")
    public CommentDto updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentDto dto) {
        return commentService.updateComment(postId, commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Commnet entity deleted successfully.", HttpStatus.OK);
    }
}
