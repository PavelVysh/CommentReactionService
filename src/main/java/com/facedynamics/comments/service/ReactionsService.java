package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.repository.ReactionsRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class ReactionsService {
    private ReactionsRepository repository;

    public ReactionsService(ReactionsRepository repository) {
        this.repository = repository;
    }
    public void createReaction(Reaction reaction) {
        repository.save(reaction);
    }
    public List<Reaction> findReactionsForEntity(int entityId, boolean type) {
        return repository.findAllByEntityIdAndLike(entityId, type);
    }
    public void deleteReactionById(int reactionId) {
        repository.deleteById(reactionId);
    }
    public List<Reaction> findReactionsByUserId(int userId, boolean type) {
        return repository.findAllByUserIdAndLike(userId, type);
    }
}
