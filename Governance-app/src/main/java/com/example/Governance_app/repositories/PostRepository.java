package com.example.Governance_app.repositories;

import com.example.Governance_app.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
