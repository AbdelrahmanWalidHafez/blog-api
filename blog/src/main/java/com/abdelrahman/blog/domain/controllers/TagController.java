package com.abdelrahman.blog.domain.controllers;

import com.abdelrahman.blog.domain.DTOs.CreateTagRequest;
import com.abdelrahman.blog.domain.DTOs.TagResponse;
import com.abdelrahman.blog.domain.mappers.TagMapper;
import com.abdelrahman.blog.domain.model.Tag;
import com.abdelrahman.blog.domain.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;
    @GetMapping
    public ResponseEntity<List<TagResponse>>getAllTags(){
        List<Tag> tags=tagService.getTags();
        List<TagResponse> tagResponses=tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(tagResponses);
    }

    @PostMapping
    public  ResponseEntity<List<TagResponse>> createTags(@RequestBody CreateTagRequest createTagRequest){
        List<Tag>savedTags=tagService.createTags(createTagRequest.getNames());
        List<TagResponse> tagResponses=savedTags.stream().map(tagMapper::toTagResponse).toList();
        return new ResponseEntity<>(tagResponses, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id){
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
