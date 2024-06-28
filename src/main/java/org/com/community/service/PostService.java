package org.com.community.service;


import lombok.RequiredArgsConstructor;
import org.com.community.dao.UserDAO;
import org.com.community.domain.Post;
import org.com.community.domain.User;
import org.com.community.dto.CreatePostRequestDTO;
import org.com.community.dto.UpdatePostRequestDTO;
import org.com.community.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    private UserDAO userDAO;
    //GET
    public List<Post> findAll() {
        return postRepository.findAllPosts();
    }

    @Transactional
    public Post findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found"+ id));
        post.incrementHitsCount();
        return postRepository.save(post);
    }

    //POST
    public Post createPost(CreatePostRequestDTO request) {
        User user = userDAO.findById(request.getUserId());
        System.out.println(user);
        Post post = request.toEntity(user);
        return postRepository.save(post);
    }

    //PATCH
    @Transactional
    public Post updatePost(Long id, UpdatePostRequestDTO request, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
        User user = userDAO.findByEmail(email);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User does not have permission to delete this comment");
        }
        post.updatePost(request.getTitle(), request.getContent(), request.getFilePath());
        return postRepository.save(post);
    }
    //DELETE
    @Transactional
    public Post deletePost(Long id, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
        User user = userDAO.findByEmail(email);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User does not have permission to delete this comment");
        }
        post.deletePost();
        return postRepository.save(post);
    }
}
