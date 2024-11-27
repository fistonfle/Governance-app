package com.example.Governance_app.services;

import com.example.Governance_app.dtos.PostDTO;
import com.example.Governance_app.models.Post;
import com.example.Governance_app.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<PostDTO> getAllPosts() {
         List<Post> posts = postRepository.findAll();
            return PostDTO.fromList(posts);
    }

    public Optional<PostDTO> getPostById(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return Optional.empty();
        }
        return Optional.of(PostDTO.fromPost(post));
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> updatePost(Long id, PostDTO updatedPost) {
        return postRepository.findById(id).map(post -> {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            // Set other fields as needed
            return postRepository.save(post);
        });
    }

    public boolean deletePost(Long id) {
        return postRepository.findById(id).map(post -> {
            postRepository.delete(post);
            return true;
        }).orElse(false);
    }
}
