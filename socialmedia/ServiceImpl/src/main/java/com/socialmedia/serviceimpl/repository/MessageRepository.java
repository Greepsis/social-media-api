package com.socialmedia.serviceimpl.repository;

import com.socialmedia.serviceimpl.model.Friend;
import com.socialmedia.serviceimpl.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID>, JpaSpecificationExecutor<Message> {
    @Query(value = "select message_id, recipient_id, sender_id, publication_date_time, text_message" +
            " from messages" +
            " where sender_id=?1 or recipient_id=?2",nativeQuery = true)
    Page<Message> findAllMessageBySenderIdOrReceiverId(UUID sender_id, UUID recipient_id, Pageable pageable);
}
