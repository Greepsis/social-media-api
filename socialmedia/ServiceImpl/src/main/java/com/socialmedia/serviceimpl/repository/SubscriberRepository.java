package com.socialmedia.serviceimpl.repository;

import com.socialmedia.serviceimpl.model.Friend;
import com.socialmedia.serviceimpl.model.Subscriber;
import com.socialmedia.serviceimpl.model.User;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, UUID>, JpaSpecificationExecutor<Subscriber> {
    Subscriber findByFollowerEmailAndFollowingEmail(String followerEmail, String followingEmail);
    Page<Subscriber> findAllSubscriberByFollower(User follower, Pageable pageable);
}
