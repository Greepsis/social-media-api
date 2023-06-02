package com.socialmedia.serviceimpl.service;

import com.socialmedia.api.dto.friend.PageFriendDto;
import com.socialmedia.api.service.FriendService;
import com.socialmedia.api.service.SubscriberService;
import com.socialmedia.serviceimpl.exception.*;
import com.socialmedia.serviceimpl.model.Friend;
import com.socialmedia.serviceimpl.model.User;
import com.socialmedia.serviceimpl.model.status.Status;
import com.socialmedia.serviceimpl.repository.FriendRepository;
import com.socialmedia.serviceimpl.repository.UserRepository;
import com.socialmedia.serviceimpl.utils.Convertor;
import com.socialmedia.serviceimpl.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final SubscriberService subscriberService;
    private final Convertor convertor;

    /**
     *
     * @param email user email who are you sending a friend request to
     */

    @Override
    public void sendFriendRequest(String email) {
        User friendRequest = findUserByEmail(SecurityUtil.getCurrentUserLogin());
        User friendResponse = findUserByEmail(email);
        if (checkFriendRequest(friendRequest.getEmail(), friendResponse.getEmail()) == null){
            Friend friend = new Friend();
            friend.setSender(friendRequest);
            friend.setReceiver(friendResponse);
            friend.setStatus(Status.EXPECTATION.name());
            getSubscribe(friendResponse.getEmail());
            friendRepository.save(friend);
        }else {
            throw new AlreadyExistsException("Friend request already exists");
        }
    }


    @Override
    public Page<PageFriendDto> getUserSentFriendRequest(Pageable pageable){
        Page<Friend> friendsRequest = friendRepository.findAllFriendBySender(
                findUserByEmail(SecurityUtil.getCurrentUserLogin()),
                pageable);
        return convertor.mapEntityPageIntoDtoPage(friendsRequest, PageFriendDto.class);
    }

    @Override
    public Page<PageFriendDto> getUserReceiverFriendRequest(Pageable pageable){
        Page<Friend> friendsResponse = friendRepository.findAllFriendByReceiver(
                findUserByEmail(SecurityUtil.getCurrentUserLogin()),
                pageable);
        return convertor.mapEntityPageIntoDtoPage(friendsResponse, PageFriendDto.class);
    }

    /**
     *
     * @param friend_id friend_request_id from Friend.class(friend_id)
     */
    @Override
    public void acceptedFriendRequest(UUID friend_id){
        if (friend_id != null){
            if (!Objects.equals(findFriendRequestById(friend_id).getReceiver().getEmail(), SecurityUtil.getCurrentUserLogin())){
                throw new DoNotMatchException("Friend request cannot be accepted:" +
                        " Current user email do not match with email from friend request");
            }
        }else {
            throw new NoEmptyException("Friend id cannot be null");
        }
        Friend friendRequest = findFriendRequestById(friend_id);
        if (Objects.equals(friendRequest.getStatus(), Status.EXPECTATION.name())){
            friendRequest.setStatus(Status.ACCEPTED.name());
            getSubscribe(friendRequest.getSender().getEmail());
            friendRepository.save(friendRequest);
        } else if (Objects.equals(friendRequest.getStatus(), Status.ACCEPTED.name())){
            throw new AlreadyAcceptedException("You have already accepted the friend request");
        } else {
            throw new DeniedRequestException("You have already denied the friend request");
        }
    }

    @Override
    public Page<PageFriendDto> getMyFriends (Pageable pageable){
        User user = findUserByEmail(SecurityUtil.getCurrentUserLogin());
        Page<Friend> friends = friendRepository.findAllFriendBySenderIdOrReceiverId(
                user.getUser_id(),
                user.getUser_id(),
                pageable);
        return convertor.mapEntityPageIntoDtoPage(friends, PageFriendDto.class);
    }

    /**
     *
     * @param friend_id friend_request_id from Friend.class(friend_id)
     */
    @Override
    public void rejectedFriendRequest(UUID friend_id){
        if (friend_id != null){
            if (!Objects.equals(findFriendRequestById(friend_id).getReceiver().getEmail(), SecurityUtil.getCurrentUserLogin())){
                throw new DoNotMatchException("Friend request cannot be rejected:" +
                        " Current user email do not match with email from friend request");
            }
        }else {
            throw new NoEmptyException("Friend id cannot be null");
        }
        Friend friendRequest = findFriendRequestById(friend_id);
        if (Objects.equals(friendRequest.getStatus(), Status.EXPECTATION.name())){
            friendRequest.setStatus(Status.REJECTED.name());
            friendRepository.save(friendRequest);
        } else if (Objects.equals(friendRequest.getStatus(), Status.REJECTED.name())){
            throw new AlreadyAcceptedException("You have already rejected the friend request");
        }
    }

    /**
     *
     * @param friend_id friend_request_id from Friend.class(friend_id)
     */
    @Override
    public void deleteFriend(UUID friend_id){
        if (friend_id != null){
            if (!Objects.equals(findFriendRequestById(friend_id).getStatus(),Status.ACCEPTED.name())){
                throw new NotFriendException("You are not friends");
            }
        }else{
            throw new NoEmptyException("Friend id cannot be null");
        }
        Friend friend = findFriendRequestById(friend_id);
        subscriberService.deleteSubscribing(friend.getReceiver().getEmail(),friend.getSender().getEmail());
        friendRepository.deleteById(friend_id);
    }

    private void getSubscribe(String followingEmail){
        subscriberService.startFollowing(followingEmail);
    }


    private Friend findFriendRequestById(UUID friend_id){
        Optional<Friend> friendRequest = friendRepository.findById(friend_id);
        return friendRequest.orElseThrow(() ->
                new NotFoundException("Friend request with id:" + friend_id + " not found"));
    }


    private User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User with email:" + email + " not found."));
    }
    private Friend checkFriendRequest(String senderEmail, String receiverEmail){
        return friendRepository.findBySenderEmailAndReceiverEmail(senderEmail,receiverEmail);
    }
}
