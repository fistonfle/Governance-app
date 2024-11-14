package com.example.Governance_app.controllers;

import com.example.Governance_app.dtos.CommentDTO;
import com.example.Governance_app.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Get all comments
    @GetMapping
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }

    // Get a comment by ID
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new comment
    @PostMapping
    public CommentDTO createComment(@RequestBody CommentDTO comment) {
        return commentService.createComment(comment);
    }

    // Update an existing comment
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO updatedComment) {
        return commentService.updateComment(id, updatedComment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a comment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (commentService.deleteComment(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
