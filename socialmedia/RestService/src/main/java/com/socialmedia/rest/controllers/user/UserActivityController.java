package com.socialmedia.rest.controllers.user;

import com.socialmedia.api.dto.ResponseDto;
import com.socialmedia.api.dto.post.page.PagePostDto;
import com.socialmedia.api.service.PostService;
import com.socialmedia.api.specification.SearchCriteria;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/socialmedia/activity")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Activity Controller", description = "Allows you to view users activity")
public class UserActivityController {
    private final PostService postService;

    @GetMapping
    @Operation(
            summary = "Get user activity using search criteria." +
                    " Example searchCriteria: {key:user.email,value:vadyushanovik@gmail.com,operation:EQUAL}"
    )
    public ResponseEntity<?> getActivityPost(@PageableDefault(sort = "publicationDateTime", direction = Sort.Direction.DESC,
            page = 0, size = 10) Pageable pageable,
                                      @RequestBody List<SearchCriteria> searchCriteria){
        final Page<PagePostDto> page = postService.getPost(pageable, searchCriteria);
        return ResponseEntity.ok(new ResponseDto<>(true, page));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all posts"
    )
    public ResponseEntity<?> getAll(@PageableDefault(sort = "publicationDateTime", direction = Sort.Direction.DESC,
            page = 0, size = 10) Pageable pageable){
        final Page<PagePostDto> page = postService.getAllPosts(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true, page));
    }

}
