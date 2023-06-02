package com.socialmedia.api.service;

import com.socialmedia.api.dto.post.PostDto;
import com.socialmedia.api.dto.post.UpdatePostDto;
import com.socialmedia.api.dto.post.page.PagePostDto;
import com.socialmedia.api.specification.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PostService {
    void addPost(PostDto postDto);
    void deletePost(UUID post_id);
    UpdatePostDto updatePost(UpdatePostDto postDto, UUID post_id);
    Page<PagePostDto> getPost(Pageable pageable, List<SearchCriteria> searchCriteria);
    Page<PagePostDto> getUserPost(Pageable pageable);
    Page<PagePostDto> getAllPosts(Pageable pageable);

}
