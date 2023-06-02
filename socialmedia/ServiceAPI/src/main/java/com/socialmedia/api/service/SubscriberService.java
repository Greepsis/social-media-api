package com.socialmedia.api.service;

import com.socialmedia.api.dto.subscriber.PageSubscriberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriberService {
    void startFollowing(String followingEmail);
    void deleteSubscribing(String followerEmail, String followingEmail);
    void unSubscribe(String followingEmail);
    Page<PageSubscriberDto> getUserSubscribers(Pageable pageable);
}
