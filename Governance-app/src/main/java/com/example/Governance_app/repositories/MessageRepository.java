package com.example.Governance_app.repositories;

import com.example.Governance_app.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}