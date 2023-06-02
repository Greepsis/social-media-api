package com.socialmedia.rest.controllers.user;

import com.socialmedia.api.dto.ResponseDto;
import com.socialmedia.api.dto.friend.PageFriendDto;
import com.socialmedia.api.service.FriendService;
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
@RequestMapping("/socialmedia/friend")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Friend Controller", description = "Allows you to view,add,delete,subscribe to friend")
public class UserFriendController {
    private final FriendService friendService;

    @PostMapping("/sent/request/{email}")
    @Operation(
            summary = "Allows you send friend request to another user.Automatically subscribes you to the recipient"
    )
    public ResponseEntity<?> sendFriendRequest(@PathVariable String email){
        friendService.sendFriendRequest(email);
        return new ResponseEntity<>(new ResponseDto<>(true, "Friend request sent."), HttpStatus.OK);
    }


    @PostMapping("/accepted/{friendRequest_id}")
    @Operation(
            summary = "Allows you to accept friend request.Automatically subscribes you to the sender "
    )
    public ResponseEntity<?> acceptedMyFriendRequest(@PathVariable UUID friendRequest_id){
        friendService.acceptedFriendRequest(friendRequest_id);
        return new ResponseEntity<>(new ResponseDto<>(true, "Friend request was accepted."), HttpStatus.OK);
    }

    @PostMapping("/rejected/{friendRequest_id}")
    @Operation(
            summary = "Allows you to reject friend request"
    )
    public ResponseEntity<?> rejectedMyFriendRequest(@PathVariable UUID friendRequest_id){
        friendService.rejectedFriendRequest(friendRequest_id);
        return new ResponseEntity<>(new ResponseDto<>(true, "Friend request was rejected."), HttpStatus.OK);
    }

    @DeleteMapping("/{friendRequest_id}")
    @Operation(
            summary = "Allows you delete your friend. Automatically unsubscribes you from friend"
    )
    public ResponseEntity<?> deleteFriend(@PathVariable UUID friendRequest_id){
        friendService.deleteFriend(friendRequest_id);
        return new ResponseEntity<>(new ResponseDto<>(true,"Friendship has been removed :("),
                HttpStatus.OK);
    }

    @GetMapping("/my/friend")
    @Operation(
            summary = "Allows you to view all your friend"
    )
    public ResponseEntity<?> getMyFriends(@PageableDefault Pageable pageable){
        final Page<PageFriendDto> page = friendService.getMyFriends(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true, page));
    }



    @GetMapping("/my/request")
    @Operation(
            summary = "Allows you to view your friend request"
    )
    public ResponseEntity<?> getMyFriendRequest(@PageableDefault(sort = "status", direction = Sort.Direction.ASC,
            page = 0, size = 10) Pageable pageable) {
        Page<PageFriendDto> page = friendService.getUserSentFriendRequest(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true, page));
    }

    @GetMapping("/my/response")
    @Operation(
            summary = "Allows you to view your friend response"
    )
    public ResponseEntity<?> getMyFriendResponse(@PageableDefault(sort = "status", direction = Sort.Direction.ASC,
            page = 0, size = 10) Pageable pageable) {
        Page<PageFriendDto> page = friendService.getUserReceiverFriendRequest(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true, page));
    }

}
