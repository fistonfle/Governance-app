package com.example.Governance_app.services;

import com.example.Governance_app.dtos.MessageDTO;
import com.example.Governance_app.models.Message;
import com.example.Governance_app.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Retrieve all messages as DTOs
    public List<MessageDTO> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Retrieve a message by ID and convert to DTO
    public Optional<MessageDTO> getMessageById(Long id) {
        return messageRepository.findById(id).map(this::convertToDTO);
    }

    // Create a new message from DTO
    public MessageDTO createMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setSenderId(messageDTO.getSenderId());
        message.setReceiverId(messageDTO.getReceiverId());
        message.setContent(messageDTO.getContent());
        message.setCreatedAt(LocalDateTime.now());
        message = messageRepository.save(message);
        return convertToDTO(message);
    }

    // Update an existing message with data from DTO
    public Optional<MessageDTO> updateMessage(Long id, MessageDTO updatedMessageDTO) {
        return messageRepository.findById(id).map(message -> {
            message.setContent(updatedMessageDTO.getContent());
            message.setSenderId(updatedMessageDTO.getSenderId());
            message.setReceiverId(updatedMessageDTO.getReceiverId());
            message = messageRepository.save(message);
            return convertToDTO(message);
        });
    }

    // Delete a message by ID
    public boolean deleteMessage(Long id) {
        return messageRepository.findById(id).map(message -> {
            messageRepository.delete(message);
            return true;
        }).orElse(false);
    }

    // Helper method to convert Message to MessageDTO
    private MessageDTO convertToDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getSenderId(),
                message.getReceiverId(),
                message.getContent(),
                message.getCreatedAt(),
                message.getSenderName() // Assuming getSenderName() is implemented in the Message model
        );
    }
}
