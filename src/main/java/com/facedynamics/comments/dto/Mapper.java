package com.facedynamics.comments.dto;

import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.dto.reaction.ReactionSaveDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Reaction;
import org.springframework.data.domain.Page;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    CommentSaveDTO commentToCommentDTO(Comment comment);
    CommentReturnDTO commentToReturnDTO(Comment comment);
    ReactionSaveDTO reactionToSaveDTO(Reaction reaction);
    ReactionReturnDTO reactionToReturnDTO(Reaction reaction);
    default Page<CommentReturnDTO> commentToReturnDTO(Page<Comment> comments) {
        return comments.map(this::commentToReturnDTO);
    }
    default Page<ReactionReturnDTO> reactionToReturnDTO(Page<Reaction> reactions) {
        return reactions.map(this::reactionToReturnDTO);
    }
}
