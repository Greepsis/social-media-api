package com.socialmedia.serviceimpl.service;

import com.socialmedia.api.dto.subscriber.PageSubscriberDto;
import com.socialmedia.api.service.SubscriberService;
import com.socialmedia.serviceimpl.exception.AlreadyExistsException;
import com.socialmedia.serviceimpl.exception.CannotUnsubscribeException;
import com.socialmedia.serviceimpl.exception.NoEmptyException;
import com.socialmedia.serviceimpl.exception.NotFoundException;
import com.socialmedia.serviceimpl.model.Friend;
import com.socialmedia.serviceimpl.model.Subscriber;
import com.socialmedia.serviceimpl.model.User;
import com.socialmedia.serviceimpl.model.status.Status;
import com.socialmedia.serviceimpl.repository.FriendRepository;
import com.socialmedia.serviceimpl.repository.SubscriberRepository;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final Convertor convertor;

    @Override
    public void startFollowing(String followingEmail) {
        User follower = findUserByEmail(SecurityUtil.getCurrentUserLogin());
        User following = findUserByEmail(followingEmail);
        if (checkSubscribing(follower.getEmail(),following.getEmail()) == null){
            Subscriber subscribers = new Subscriber();
            subscribers.setFollower(follower);
            subscribers.setFollowing(following);
            subscriberRepository.save(subscribers);
        }else {
            throw new AlreadyExistsException("Subscribing already exists");
        }
    }

    @Override
    public void deleteSubscribing(String followerEmail, String followingEmail){
        Subscriber subscriber = checkSubscribing(followerEmail, followingEmail);
        subscriberRepository.deleteById(subscriber.getSubscriber_id());
    }

    @Override
    public void unSubscribe(String followingEmail){
        if (!followingEmail.isEmpty()){
            Friend friend = checkFriendRequest(SecurityUtil.getCurrentUserLogin(),followingEmail);
            if (checkFriendRequest(SecurityUtil.getCurrentUserLogin(),followingEmail) == null){
                Subscriber subscriber = checkSubscribing(SecurityUtil.getCurrentUserLogin(),followingEmail);
                subscriberRepository.deleteById(subscriber.getSubscriber_id());
            }else if (Objects.equals(SecurityUtil.getCurrentUserLogin(),friend.getSender().getEmail())
                    && friend.getStatus().equals(Status.EXPECTATION.name())
                    || friend.getStatus().equals(Status.REJECTED.name())){
                Subscriber subscriber = checkSubscribing(SecurityUtil.getCurrentUserLogin(),followingEmail);
                subscriberRepository.deleteById(subscriber.getSubscriber_id());
            }else {
                throw new CannotUnsubscribeException("You cannot unsubscribe: try remove your friend or check your friend request");
            }
        }else {
            throw new NoEmptyException("Following email cannot be empty");
        }
    }

    @Override
    public Page<PageSubscriberDto> getUserSubscribers(Pageable pageable){
        Page<Subscriber> subscribers = subscriberRepository.findAllSubscriberByFollower(
                findUserByEmail(SecurityUtil.getCurrentUserLogin()),pageable);
        return convertor.mapEntityPageIntoDtoPage(subscribers, PageSubscriberDto.class);
    }

    private Friend checkFriendRequest(String senderEmail, String receiverEmail){
        return friendRepository.findBySenderEmailAndReceiverEmail(senderEmail,receiverEmail);
    }

    private Subscriber checkSubscribing(String followerEmail, String followingEmail){
        return subscriberRepository.findByFollowerEmailAndFollowingEmail(followerEmail,followingEmail);
    }

    private User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User with email:" + email + " not found."));
    }
}
