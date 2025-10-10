package com.chuwa.redbook.service;

import com.chuwa.redbook.payload.CommentDto;
import org.springframework.data.domain.Page;

public interface CommentService {
    CommentDto createComment(Long postId, CommentDto dto);
    Page<CommentDto> getCommentsByPost(Long postId, int pageNo, int pageSize, String sortBy, String sortDir);
    CommentDto getComment(Long postId, Long commentId);
    CommentDto updateComment(Long postId, Long commentId, CommentDto dto);
    void deleteComment(Long postId, Long commentId);
}
