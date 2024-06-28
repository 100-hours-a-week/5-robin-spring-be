package org.com.community.service;

import lombok.RequiredArgsConstructor;
import org.com.community.dao.UserDAO;
import org.com.community.domain.Comment;
import org.com.community.domain.Post;
import org.com.community.domain.User;
import org.com.community.dto.CreateCommentRequestDTO;
import org.com.community.dto.CreatePostRequestDTO;
import org.com.community.dto.UpdateCommentRequestDTO;
import org.com.community.repository.CommentRepository;
import org.com.community.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    @Autowired
    private UserDAO userDAO;

    public List<Comment> findPostComments(Long id) {
        return commentRepository.findAllCommentsByPostIdAndDelFlag(id);
    }
    //POST
    public Comment createComment(Long id ,CreateCommentRequestDTO request, String email) {
        User user = userDAO.findByEmail(email);
        Post post = postRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Post not found"));
        Comment comment = request.toEntity(user, post);

        comment = commentRepository.save(comment);

        post.incrementCommentsCount();
        postRepository.save(post);

        return comment;
    }

    //PATCH
    @Transactional
    public Comment updateComment(Long id, UpdateCommentRequestDTO request, String email) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
        User user = userDAO.findByEmail(email);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User does not have permission to update this comment");
        }
        comment.updateComment(request.getContent());
        return commentRepository.save(comment);
    }
    //DELETE
    @Transactional
    public Comment deleteComment(Long id, Long postId, String email) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("Post not found"));
        User user = userDAO.findByEmail(email);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User does not have permission to delete this comment");
        }
        comment.deleteComment();
        comment = commentRepository.save(comment);

        post.decrementCommentsCount();
        postRepository.save(post);

        return comment;
    }
}
