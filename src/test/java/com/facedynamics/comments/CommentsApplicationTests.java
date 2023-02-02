package com.facedynamics.comments;

import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.CommentService;
import com.facedynamics.comments.service.ReactionsService;
import com.facedynamics.comments.service.ReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
class CommentsApplicationTests {
	@Autowired
	private ReactionsService reactionsService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ReplyService replyService;

	@Test
	void contextLoads() {
	}
	@Test
	void getComments() {
		Comment comment = new Comment();
		comment.setPostId(2);
		commentService.save(comment);

		assertNotNull("Comment not found", commentService.findCommentsByPostId(2));
	}
	@Test
	void getReplies() {
		Reply reply = new Reply();
		reply.setCommentId(1);
		reply.setId(1);

		replyService.save(reply);

		assertNotNull("Reply not found", replyService.findRepliesByCommentId(2));
	}
	@Test
	void getReactions() {
		Reaction reaction = new Reaction(1, 2, 3, EntityType.repost, true);
		reactionsService.save(reaction);

		assertNotNull("Reaction not found",
				reactionsService.findReactionsForEntity(3, EntityType.repost, true));
	}
}
