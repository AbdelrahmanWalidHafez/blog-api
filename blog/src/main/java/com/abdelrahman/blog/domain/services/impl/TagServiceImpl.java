package com.abdelrahman.blog.domain.services.impl;

import com.abdelrahman.blog.domain.model.Tag;
import com.abdelrahman.blog.domain.repos.TagRepository;
import com.abdelrahman.blog.domain.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }
    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        List<Tag>existingTags=tagRepository.findByNameIn(tagNames);
        Set<String>existingTagNames=existingTags
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
        List<Tag>newTags=tagNames.stream()
                .filter(name->!existingTags.contains(name))
                .map(name->Tag.builder().name(name).posts(new HashSet<>()).build())
                .toList();
        List<Tag> savedTags=new ArrayList<>();
        if (!newTags.isEmpty()){
            savedTags=tagRepository.saveAll(newTags);
        }
        savedTags.addAll(existingTags);
        return savedTags;
    }

    @Override
    @Transactional
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag -> {
            if(!tag.getPosts().isEmpty()){
                throw  new IllegalStateException("Cannot delete Tag associated with posts");
            }
            tagRepository.deleteById(id);
        });

    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id).orElseThrow(()->new EntityNotFoundException("No Tag found with id"+id));
    }

    @Override
    public List<Tag> getTagByIds(Set<UUID> ids) {
        List<Tag> foundTags=tagRepository.findAllById(ids);
        if (foundTags.size()!= ids.size()){
            throw new EntityNotFoundException("Not all specified Tag ids exist");
        }
        return foundTags;
    }
}
