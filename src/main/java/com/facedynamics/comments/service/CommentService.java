package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.Mapper;
import com.facedynamics.comments.dto.comment.CommentDeleteDTO;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.dto.post.PostDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.feign.FeignClientMockImpl;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReactionsRepository reactionsRepository;
    private final FeignClientMockImpl feignMock;
    private Mapper mapper;
    private Notification notification;

    public CommentSaveDTO save(Comment comment) {
        PostDTO postDTO = feignMock.getPostById(comment.getPostId());
        checkIfParentExists(comment, postDTO);
        Comment savedComment = commentRepository.save(comment);
        notification.send(savedComment, postDTO);
        return mapper.commentToCommentDTO(savedComment);
    }

    public Page<CommentReturnDTO> findById(int id, boolean post, Pageable page) {
        if (post) {
            return findCommentsByPostId(id, page);
        } else {
            return new PageImpl<>(List.of(findByCommentId(id)));
        }
    }

    @Transactional
    public CommentDeleteDTO deleteByCommentId(int id) {
        return new CommentDeleteDTO(commentRepository.deleteById(id));
    }

    @Transactional
    public CommentDeleteDTO deleteByPostId(int postId) {
        deleteReactionsForPost(postId);
        return new CommentDeleteDTO(commentRepository.deleteByPostId(postId));
    }

    public Page<CommentReturnDTO> findCommentsByPostId(int postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findCommentsByPostId(postId, pageable);
        if (comments.isEmpty()) {
            throw new NotFoundException("Comments for post with id "
                    + postId + " were not found");
        }
        return mapper.commentToReturnDTO(comments);
    }

    private int deleteReactionsForPost(int postId) {
        return reactionsRepository.deleteByEntityIdAndEntityType(postId, EntityType.post);

    public CommentReturnDTO findByCommentId(int id) {
        return mapper.commentToReturnDTO(commentRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Comment with id - " + id + " was not found");
        }));
    }

    private void checkIfParentExists(Comment comment, PostDTO postDTO) {
        if (comment.getParentId() != null) {
            commentRepository.findById(comment.getParentId()).orElseThrow(() -> {
                throw new NotFoundException("Comment with id - " + comment.getParentId() + " was not found");
            });
        } else if (postDTO.getUserId() == 0) {
            throw new NotFoundException("Post with id - " + comment.getPostId() + " was not found");
        }
    }
}
