package com.example.Governance_app.services;

import com.example.Governance_app.models.Post;
import com.example.Governance_app.models.Tag;
import com.example.Governance_app.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // Find a tag by its name, or create it if it doesn't exist
    public Tag findOrCreateByName(String name) {
        Optional<Tag> existingTag = tagRepository.findByName(name);
        if (existingTag.isPresent()) {
            return existingTag.get(); // Return existing tag if found
        } else {
            Tag newTag = new Tag(name); // Create a new tag
            return tagRepository.save(newTag); // Save and return the new tag
        }
    }

    // Find posts by tag name
    public List<Post> findPostsByTag(String tagName) {
        Optional<Tag> tag = tagRepository.findByName(tagName);
        if (tag.isPresent()) {
            System.out.println(
                    "Tag found: " + tag.get().getName() + " with " + tag.get().getPosts().size() + " posts"
            );
            // Convert the PersistentSet to a List
            return new ArrayList<>(tag.get().getPosts());  // Convert Set to List
        }
        return List.of(); // Return an empty list if the tag is not found
    }


    // Save a new tag
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }
}
