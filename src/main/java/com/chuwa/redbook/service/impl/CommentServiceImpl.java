package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.CommentRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CommentDto;
import com.chuwa.redbook.service.CommentService;
import com.chuwa.redbook.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired private CommentRepository commentRepository;
    @Autowired private PostRepository postRepository;

    private Post findPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    private CommentDto toDto(Comment c) {
        CommentDto dto = new CommentDto();
        dto.setId(c.getId());
        dto.setContent(c.getContent());
        dto.setPostId(c.getPost().getId());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        return dto;
    }

    private void apply(Comment c, CommentDto dto) {
        c.setContent(dto.getContent());
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto dto) {
        Post post = findPostOrThrow(postId);
        Comment c = new Comment();
        apply(c, dto);
        c.setPost(post);
        return toDto(commentRepository.save(c));
    }

    @Override
    public Page<CommentDto> getCommentsByPost(Long postId, int pageNo, int pageSize, String sortBy, String sortDir) {
        boolean asc = "asc".equalsIgnoreCase(sortDir);
        Sort sort = asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return commentRepository.findByPostId(postId, pageable).map(this::toDto);
    }


    @Override
    public CommentDto getComment(Long postId, Long commentId) {
        findPostOrThrow(postId);
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        if (!c.getPost().getId().equals(postId)) {
            throw new ResourceNotFoundException("Comment", "postId", postId);
        }
        return toDto(c);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto dto) {
        findPostOrThrow(postId);
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        if (!c.getPost().getId().equals(postId)) {
            throw new ResourceNotFoundException("Comment", "postId", postId);
        }
        apply(c, dto);
        return toDto(commentRepository.save(c));
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        findPostOrThrow(postId);
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        if (!c.getPost().getId().equals(postId)) {
            throw new ResourceNotFoundException("Comment", "postId", postId);
        }
        commentRepository.delete(c);
    }
}
