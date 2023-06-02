package com.socialmedia.serviceimpl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia.api.dto.post.PostDto;
import com.socialmedia.api.dto.post.UpdatePostDto;
import com.socialmedia.api.dto.post.page.PagePostDto;
import com.socialmedia.api.service.PostService;
import com.socialmedia.api.specification.GenericSpecification;
import com.socialmedia.api.specification.SearchCriteria;
import com.socialmedia.serviceimpl.exception.DoNotMatchException;
import com.socialmedia.serviceimpl.exception.NoEmptyException;
import com.socialmedia.serviceimpl.exception.NotFoundException;
import com.socialmedia.serviceimpl.model.Post;
import com.socialmedia.serviceimpl.model.User;
import com.socialmedia.serviceimpl.repository.PostRepository;
import com.socialmedia.serviceimpl.repository.UserRepository;
import com.socialmedia.serviceimpl.utils.Convertor;
import com.socialmedia.serviceimpl.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final Convertor convertor;

    @Value("${spring.path.to.image}")
    private String standardPathToImage;

    /**
     *
     * @param postDto include:title,post text, image(path to you image.Example:D:\Downloads\image.jpg)
     */
    @Override
    public void addPost(PostDto postDto) {
        Post post = objectMapper.convertValue(postDto, Post.class);
        post.setUser(userRepository.findUserByEmail(SecurityUtil.getCurrentUserLogin()).orElseThrow(() ->
                new NotFoundException("User not found.")));
        post.setImage(crateImageForPost(postDto.getImage()));
        post.setPublicationDateTime(LocalDateTime.now());
        postRepository.save(post);
    }

    /**
     *
     * @param postDto include:title,post text, image(If you don't update image, just keep value empty)
     * @param post_id id your post
     * @return update post
     */
    @Override
    public UpdatePostDto updatePost(UpdatePostDto postDto, UUID post_id){
        if (post_id != null){
            if (!Objects.equals(findPostById(post_id).getUser().getEmail(),
                    SecurityUtil.getCurrentUserLogin())){
                throw new DoNotMatchException("Post cannot be update: Current user email do not match with email from post");
            }
        }else {
            throw new NoEmptyException("Post id cannot be empty");
        }
        Post updatePost = objectMapper.convertValue(postDto, Post.class);
        updatePost.setPost_id(post_id);
        if (updatePost.getImage().isEmpty()){
            updatePost.setImage(findPostById(post_id).getImage());
        }else {
            updatePost.setImage(crateImageForPost(postDto.getImage()));
        }
        updatePost.setUser(userRepository.findUserByEmail(SecurityUtil.getCurrentUserLogin()).orElseThrow(() ->
                new NotFoundException("User not found.")));
        updatePost.setPublicationDateTime(findPostById(post_id).getPublicationDateTime());
        postRepository.save(updatePost);
        return postDto;
    }

    @Override
    public void deletePost(UUID post_id){
        if (post_id == null){
            throw new NoEmptyException("Post id cannot be null");
        }else {
            if (!Objects.equals(findPostById(post_id).getUser().getEmail(),
                    SecurityUtil.getCurrentUserLogin())){
                throw new DoNotMatchException("Post cannot be delete: Current user email do not match with email from post");
            }
        }
        postRepository.deleteById(post_id);
    }

    @Override
    public Page<PagePostDto> getPost(Pageable pageable, List<SearchCriteria> searchCriteria){
        if (searchCriteria.isEmpty()){
            throw new NoEmptyException("Search criteria cannot be empty");
        }
        GenericSpecification<Post> genericSpecification = new GenericSpecification<>();
        searchCriteria.stream().map(searchCriterion -> new SearchCriteria(searchCriterion.getKey(),
                searchCriterion.getValue(), searchCriterion.getOperation())).forEach(genericSpecification::add);
        Page<Post> posts = postRepository.findAll(genericSpecification,pageable);
        return convertor.mapEntityPageIntoDtoPage(posts, PagePostDto.class);
    }

    @Override
    public Page<PagePostDto> getUserPost(Pageable pageable){
        Page<Post> posts = postRepository.findAllPostByUserEmail(SecurityUtil.getCurrentUserLogin(),pageable);
        return convertor.mapEntityPageIntoDtoPage(posts, PagePostDto.class);
    }

    @Override
    public Page<PagePostDto> getAllPosts(Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);
        return convertor.mapEntityPageIntoDtoPage(posts, PagePostDto.class);
    }

    private String crateImageForPost(String pathToImage){
        var imageId = UUID.randomUUID().toString();
        var imageFormat = ".jpg";
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(pathToImage));
            FileUtils.writeByteArrayToFile(new File(createNewPathToImage(imageId,imageFormat)),fileContent);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return imageId + imageFormat;
    }

    private String createNewPathToImage(String imageId, String imageFormat){
        return standardPathToImage + imageId + imageFormat;
    }

    private Post findPostById(UUID post_id){
        Optional<Post> post = postRepository.findById(post_id);
        return post.orElseThrow(() ->
                new NotFoundException("Post with id:" + post_id + " not found."));
    }

}
