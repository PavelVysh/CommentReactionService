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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/dataForTests.sql"})
class CommentsApplicationTests {
	@Autowired
	private ReactionsService reactionsService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private MockMvc mvc;

	@Test
	void contextLoads() {
	}
	@Test
	void getComments() {
		Comment comment = new Comment();
		comment.setPostId(2);
		comment.setId(200);
		comment.setUserId(123);
		comment.setText("hye hyo");
		commentService.save(comment);

		assertNotNull("Comment not found", commentService.findCommentsByPostId(2));
	}
	@Test
	void getReplies() {
		Reply reply = new Reply();
		reply.setText("hyp hye");
		reply.setCommentId(5);

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
	@Test
	void replyControllerTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/replies/{id}", 1))
				.andExpect(status().isOk());

	}
}
