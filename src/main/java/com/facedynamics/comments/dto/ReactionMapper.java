package com.facedynamics.comments.dto;

import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.dto.reaction.ReactionSaveDTO;
import com.facedynamics.comments.entity.Reaction;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ReactionMapper {

    ReactionSaveDTO toSaveDTO(Reaction reaction);
    ReactionReturnDTO toReturnDTO(Reaction reaction);
    default Page<ReactionReturnDTO> toReturnDTO(Page<Reaction> reactions) {
        return reactions.map(this::toReturnDTO);
    }
}
