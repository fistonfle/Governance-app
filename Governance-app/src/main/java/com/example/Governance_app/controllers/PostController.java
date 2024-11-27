package com.example.Governance_app.controllers;


import com.example.Governance_app.dtos.PostDTO;
import com.example.Governance_app.models.Post;
import com.example.Governance_app.models.Tag;
import com.example.Governance_app.models.User;
import com.example.Governance_app.repositories.UserRepository;
import com.example.Governance_app.services.PostService;
import com.example.Governance_app.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;
    @Autowired
    private UserRepository userRepository;

    // Get all posts
    @GetMapping("/fetch")
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    // Get a post by ID
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new post
    @PostMapping("/create-post")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        // Convert DTO to entity
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());

        User user = userRepository.findById(postDTO.getUserId()).orElseThrow();

        post.setUser(user);

        // Add tags to the post
        Set<Tag> tags = new HashSet<>();
        for (String tagName : postDTO.getTags()) {
            Tag tag = tagService.findOrCreateByName(tagName); // Assume a service to find or create tags
            tags.add(tag);
        }
        post.setTags(tags);

        postService.createPost(post); // Save the post

        return ResponseEntity.ok(postDTO);
    }

    // Update an existing post
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long id, @RequestBody PostDTO updatedPost) {
        System.out.println("Received PUT request for ID: " + id);
        System.out.println("Request body: " + updatedPost);
        Optional<Post> existingPost = postService.updatePost(id, updatedPost);

        if (existingPost.isPresent()) {
            Post post = existingPost.get();

            // Update tags for the post
            Set<Tag> updatedTags = new HashSet<>();
            for (String tagName : updatedPost.getTags()) {
                Tag tag = tagService.findOrCreateByName(tagName); // Find or create tag
                updatedTags.add(tag);
            }
            post.setTags(updatedTags); // Set updated tags

            postService.createPost(post); // Save updated post


            return ResponseEntity.ok(PostDTO.fromPost(post)); // Return updated post
        }

        return ResponseEntity.notFound().build(); // If post doesn't exist
    }

    // Delete a post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (postService.deletePost(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get posts by hashtag (tag)
    @GetMapping("/hashtag/{tagName}")
    public ResponseEntity<List<PostDTO>> getPostsByTag(@PathVariable String tagName) {
        List<Post> results = tagService.findPostsByTag(tagName);

        List<PostDTO> posts = PostDTO.fromList(results);  // Convert posts to DTOs

        if (posts.isEmpty()) {
            return ResponseEntity.notFound().build();  // If no posts found
        }

        return ResponseEntity.ok(posts);  // Return posts associated with the tag
    }
}
