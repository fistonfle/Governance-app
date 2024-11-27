package com.example.Governance_app.services;

import com.example.Governance_app.dtos.CommentDTO;
import com.example.Governance_app.models.Comment;
import com.example.Governance_app.models.Post;
import com.example.Governance_app.models.User;
import com.example.Governance_app.repositories.CommentRepository;
import com.example.Governance_app.repositories.PostRepository;
import com.example.Governance_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    // Convert a Comment entity to CommentDTO
    private CommentDTO convertToDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUser().getUsername() // Assuming User has a getUsername() method
        );
    }

    // Convert a CommentDTO to Comment entity
    private Comment convertToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        // Set fields from DTO to entity
        comment.setContent(commentDTO.getContent());

        User user = userRepository.findById(commentDTO.getUserId()).orElseThrow();
        comment.setUser(user);

        comment.setCreatedAt(LocalDateTime.now());
        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow();
        comment.setPost(post);
        // Set other fields as necessary, such as user and post associations
        return comment;
    }

    // Get all comments and return as DTOs
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get a comment by ID and return as DTO
    public Optional<CommentDTO> getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(this::convertToDTO);
    }

    // Create a new comment using CommentDTO
    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment newComment = convertToEntity(commentDTO);
        newComment = commentRepository.save(newComment);
        return convertToDTO(newComment);
    }

    // Update an existing comment using CommentDTO
    public Optional<CommentDTO> updateComment(Long id, CommentDTO updatedCommentDTO) {
        return commentRepository.findById(id).map(comment -> {
            comment.setContent(updatedCommentDTO.getContent());
            // Update other fields if needed
            Comment updatedComment = commentRepository.save(comment);
            return convertToDTO(updatedComment);
        });
    }

    // Delete a comment by ID
    public boolean deleteComment(Long id) {
        return commentRepository.findById(id).map(comment -> {
            commentRepository.delete(comment);
            return true;
        }).orElse(false);
    }
}
