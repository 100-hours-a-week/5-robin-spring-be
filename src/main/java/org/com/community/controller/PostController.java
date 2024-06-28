package org.com.community.controller;

import org.com.community.domain.Post;
import org.com.community.dto.CreatePostRequestDTO;
import org.com.community.dto.UpdatePostRequestDTO;
import org.com.community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> findAllPost() {
        List<Post> response = postService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findPost(@PathVariable Long id) {
        Post response = postService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Post> addPost(@RequestBody CreatePostRequestDTO request) {
        Post saved = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody UpdatePostRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
        Post updated = postService.updatePost(id, request, userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Post deleted = postService.deletePost(id, userDetails.getUsername());
        return ResponseEntity.ok(deleted);
    }
}
