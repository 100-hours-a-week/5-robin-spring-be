package org.com.community.controller;

import org.com.community.domain.Comment;
import org.com.community.domain.Post;
import org.com.community.dto.CreateCommentRequestDTO;
import org.com.community.dto.CreatePostRequestDTO;
import org.com.community.dto.UpdateCommentRequestDTO;
import org.com.community.dto.UpdatePostRequestDTO;
import org.com.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Comment>> findPostComments(@PathVariable Long postId) {
        List<Comment> response = commentService.findPostComments(postId);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody CreateCommentRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
        Comment saved = commentService.createComment(postId, request , userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updatePost(@PathVariable Long commentId, @RequestBody UpdateCommentRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
        Comment updated = commentService.updateComment(commentId, request, userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Comment> deletePost(@PathVariable Long commentId, @PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        Comment deleted = commentService.deleteComment(commentId, postId, userDetails.getUsername());
        return ResponseEntity.ok(deleted);
    }
}
