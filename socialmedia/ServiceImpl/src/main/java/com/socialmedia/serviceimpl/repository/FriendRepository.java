package com.socialmedia.serviceimpl.repository;

import com.socialmedia.serviceimpl.model.Friend;
import com.socialmedia.serviceimpl.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FriendRepository extends JpaRepository<Friend, UUID>, JpaSpecificationExecutor<Friend> {
    Friend findBySenderEmailAndReceiverEmail(String senderEmail, String receiverEmail);
    Page<Friend> findAllFriendBySender(User sender, Pageable pageable);
    Page<Friend> findAllFriendByReceiver(User receiver, Pageable pageable);//Test
    @Query(value = "select friend_id, receiver_id, sender_id, status" +
            " from friends" +
            " where sender_id=?1 or receiver_id=?2",nativeQuery = true)
    Page<Friend> findAllFriendBySenderIdOrReceiverId(UUID sender_id, UUID receiver_id, Pageable pageable);
}
