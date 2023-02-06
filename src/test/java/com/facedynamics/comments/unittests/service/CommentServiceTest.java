package com.facedynamics.comments.unittests.service;

import com.facedynamics.comments.dto.CommentDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import com.facedynamics.comments.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private static CommentRepository commentRepository;
    @Mock
    private static ReactionsRepository reactionsRepository;
    @InjectMocks
    private static CommentService commentService;

    @Test
    void saveCommentTest() {
        Comment comment = new Comment();
        comment.setText("sample");
        comment.setUserId(123);
        comment.setPostId(321);

        Comment commentWithId = new Comment();
        commentWithId.setText("sample");
        commentWithId.setUserId(123);
        commentWithId.setPostId(321);
        commentWithId.setId(1);


        when(commentRepository.save(comment)).thenReturn(commentWithId);

        CommentDTO savedComment = commentService.save(comment);

        assertEquals("text of saved comment if off", comment.getText(), savedComment.getText());
        assertEquals("id doesn't equals" ,1, commentWithId.getId());
    }
}
