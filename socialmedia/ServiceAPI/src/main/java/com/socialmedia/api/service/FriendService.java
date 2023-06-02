package com.socialmedia.api.service;

import com.socialmedia.api.dto.friend.PageFriendDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FriendService {
     void sendFriendRequest(String email);
     Page<PageFriendDto> getUserSentFriendRequest(Pageable pageable);
     Page<PageFriendDto> getUserReceiverFriendRequest(Pageable pageable);
     void acceptedFriendRequest(UUID friend_id);
     void rejectedFriendRequest(UUID friend_id);
     void deleteFriend(UUID friend_id);
     Page<PageFriendDto> getMyFriends (Pageable pageable);
}
