package io.mountblue.blogger.restController;

import io.mountblue.blogger.model.Comment;
import io.mountblue.blogger.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    // Get all comments for a post
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // Get a specific comment
    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(
            @PathVariable Long postId,
            @PathVariable Long commentId) {

        Comment comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    // Add a comment to a post
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Map<String, String>> addComment(
            @PathVariable Long postId,
            @RequestBody Comment comment) {

        commentService.addComment(comment, postId);

        Map<String, String> response = Map.of(
                "message", "Comment added successfully"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Update a comment
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Map<String, String>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody Comment comment,
            Authentication authentication) {

        commentService.updateCommentWithAuthorization(commentId, comment, authentication);

        Map<String, String> response = Map.of(
                "message", "Comment updated successfully"
        );

        return ResponseEntity.ok(response);
    }

    // Delete a comment
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Authentication authentication) {

        commentService.deleteCommentWithAuthorization(commentId, authentication);
        return ResponseEntity.noContent().build();
    }
}
