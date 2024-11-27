package com.example.Governance_app.dtos;

import com.example.Governance_app.models.Comment;
import com.example.Governance_app.models.Post;
import com.example.Governance_app.models.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostDTO {
    private Long id;        // Unique identifier for the post
    private String title;
    private String content;
    private List<String> tags;  // A list of tags such as "Trending"
    private Long userId;  // The user who created the post
    private LocalDateTime createdAt;  // The date and time the post was created
    private List<CommentDTO> comments; // A list of comments on the post
    // Constructor
    public PostDTO(Long id, String title, String content, List<String> tags, Long userId,LocalDateTime createdAt, List<CommentDTO> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.userId = userId;
        this.createdAt = createdAt;
        this.comments = comments;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Optionally, you can add a method to check for specific tags like "Trending"
    public boolean isTrending() {
        return tags != null && tags.contains("Trending");
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    // Convert Post to PostDTO
    public static PostDTO fromPost(Post post) {
        // Convert Set<Tag> to List<String>
        List<String> tags = post.getTags().stream()
                .map(Tag::getName)  // Assuming Tag has a `getName()` method
                .collect(Collectors.toList());

        List<CommentDTO> comments = post.getComments().stream()
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());

        // Map all fields, including id and userId
        return new PostDTO(post.getId(), post.getTitle(), post.getContent(), tags, post.getUser().getId(),post.getCreatedAt(),comments);
    }


    // Convert List<Post> to List<PostDTO>
    public static List<PostDTO> fromList(List<Post> posts) {
        return posts.stream()
                .map(PostDTO::fromPost)
                .collect(Collectors.toList());
    }

    // Convert PostDTO to Post
    public Post toPost() {
        // Convert List<String> to Set<Tag>
        Set<Tag> tagSet = tags.stream()
                .map(Tag::new)  // Assuming Tag has a constructor that takes a name
                .collect(Collectors.toSet());

        // Map all fields, including id and userId
        return new Post(null, title, content, null, tagSet);
    }
}
