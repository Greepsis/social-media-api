package com.socialmedia.rest.controllers.user;

import com.socialmedia.api.dto.ResponseDto;
import com.socialmedia.api.dto.friend.PageFriendDto;
import com.socialmedia.api.dto.subscriber.PageSubscriberDto;
import com.socialmedia.api.service.SubscriberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/socialmedia/subscribe")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Subscriber Controller", description = "Allows you unsubscribe from user,view all your subscribers posts")
public class UserSubscribeController {
    private final SubscriberService subscriberService;

    @DeleteMapping("/{followingEmail}")
    @Operation(
            summary = "Unsubscribe from user, which did not accept or rejected you friend request"
    )
    public ResponseEntity<?> unsubscribeFromUser(@PathVariable String followingEmail){
        subscriberService.unSubscribe(followingEmail);
        return new ResponseEntity<>(new ResponseDto<>(true,"You unsubscribe from user: " + followingEmail),
                HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "Get all your subscribers"
    )
    public ResponseEntity<?> getMySubscribers(@PageableDefault Pageable pageable) {
        Page<PageSubscriberDto> page = subscriberService.getUserSubscribers(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true, page));
    }

}
