package com.example.Governance_app.repositories;

import com.example.Governance_app.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
