package com.facedynamics.comments.dto;

import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.entity.Comment;
import org.springframework.data.domain.Page;

@org.mapstruct.Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentSaveDTO toSaveDTO(Comment comment);
    CommentReturnDTO toReturnDTO(Comment comment);
    default Page<CommentReturnDTO> toReturnDTO(Page<Comment> comments) {
        return comments.map(this::toReturnDTO);
    }
}
