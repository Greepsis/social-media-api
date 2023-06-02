package com.socialmedia.rest.controllers.user;

import com.socialmedia.api.dto.post.PostDto;
import com.socialmedia.api.dto.post.UpdatePostDto;
import com.socialmedia.api.dto.post.page.PagePostDto;
import com.socialmedia.api.dto.ResponseDto;
import com.socialmedia.api.service.PostService;
import com.socialmedia.api.specification.SearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/socialmedia/post")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Post Controller", description = "Allows you add,update,view,delete posts")
public class UserPostController {
    private final PostService postService;

    @PostMapping
    @Operation(
            summary = "Allows you add post.Example image:D://Downloads//image.jpg"
    )
    public ResponseEntity<?> addPost(@RequestBody @Valid PostDto postDto){
        postService.addPost(postDto);
        return new ResponseEntity<>(new ResponseDto<>(true, "Post has been added."), HttpStatus.OK);
    }

    @PutMapping("/{post_id}")
    @Operation(
            summary = "Allows you update post.If you don't update your image, just keep image empty"
    )
    public ResponseEntity<?> updatePost(@RequestBody UpdatePostDto postDto,
                                        @PathVariable UUID post_id){
        return new ResponseEntity<>(new ResponseDto<>(true, postService.updatePost(postDto,post_id)),
                HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "Allows you to view post by search criteria" )
    public ResponseEntity<?> getPosts(@PageableDefault(sort = "publicationDateTime", direction = Sort.Direction.DESC,
                                                       page = 0, size = 10) Pageable pageable,
                                      @RequestBody List<SearchCriteria> searchCriteria){
        final Page<PagePostDto> page = postService.getPost(pageable, searchCriteria);
        return ResponseEntity.ok(new ResponseDto<>(true, page));
    }

    @GetMapping("/myPost")
    @Operation(
            summary = "Allows you to view all your posts"
    )
    public ResponseEntity<?> getUserPosts(@PageableDefault(sort = "publicationDateTime", direction = Sort.Direction.DESC,
            page = 0, size = 10) Pageable pageable) {
        Page<PagePostDto> page = postService.getUserPost(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true, page));
    }

    @DeleteMapping("/{post_id}")
    @Operation(
            summary = "Allows you delete your post"
    )
    public ResponseEntity<?> deletePost(@PathVariable UUID post_id){
        postService.deletePost(post_id);
        return new ResponseEntity<>(new ResponseDto<>(true,"Post with id:" + post_id + " was removed."),
                HttpStatus.OK);
    }
}
