package com.facedynamics.comments.dto;

import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.dto.reaction.ReactionSaveDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Reaction;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    CommentSaveDTO commentToCommentDTO(Comment comment);
    CommentReturnDTO commentToReturnDTO(Comment comment);
    List<CommentReturnDTO> commentToReturnDTO(List<Comment> commentList);
    ReactionSaveDTO reactionToSaveDTO(Reaction reaction);
    ReactionReturnDTO reactionToReturnDTO(Reaction reaction);
    List<ReactionReturnDTO> reactionToReturnDTO(List<Reaction> reactions);
}