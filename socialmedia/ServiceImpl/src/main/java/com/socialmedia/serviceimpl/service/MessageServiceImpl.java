package com.socialmedia.serviceimpl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia.api.dto.message.MessageDto;
import com.socialmedia.api.dto.message.page.PageMessageDto;
import com.socialmedia.api.service.MessageService;
import com.socialmedia.serviceimpl.exception.DoNotMatchException;
import com.socialmedia.serviceimpl.exception.NoEmptyException;
import com.socialmedia.serviceimpl.exception.NotFoundException;
import com.socialmedia.serviceimpl.exception.NotFriendException;
import com.socialmedia.serviceimpl.model.Friend;
import com.socialmedia.serviceimpl.model.Message;
import com.socialmedia.serviceimpl.model.User;
import com.socialmedia.serviceimpl.model.status.Status;
import com.socialmedia.serviceimpl.repository.FriendRepository;
import com.socialmedia.serviceimpl.repository.MessageRepository;
import com.socialmedia.serviceimpl.repository.UserRepository;
import com.socialmedia.serviceimpl.utils.Convertor;
import com.socialmedia.serviceimpl.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;
    private final Convertor convertor;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    /**
     *
     * @param messageDto include: text message, friend_request_id from Friend.class(friend_id)
     * @param friendEmail Recipient email
     */
    @Override
    public void sendMessage(MessageDto messageDto, String friendEmail){
        if (!friendEmail.isEmpty()){
            Friend friend = friendRepository.findById(messageDto.getFriendRequest_id()).orElseThrow(() ->
                    new NotFoundException("Friend not found"));
            if (friend.getStatus().equals(Status.ACCEPTED.name())){
                Message message = objectMapper.convertValue(messageDto, Message.class);
                message.setSender(userRepository.findUserByEmail(SecurityUtil.getCurrentUserLogin()).orElseThrow(() ->
                        new NotFoundException("User not found")));
                message.setRecipient(userRepository.findUserByEmail(friendEmail).orElseThrow(() ->
                        new NotFoundException("User with email:" + friendEmail + " not found")));
                message.setPublicationDateTime(LocalDateTime.now());
                messageRepository.save(message);
            }else {
                throw new NotFriendException("You are not friend");
            }
        }else {
            throw new NoEmptyException("Email recipient cannot be empty");
        }
    }

    @Override
    public Page<PageMessageDto> getAllMyMessage(Pageable pageable){
        User user = findUserByEmail(SecurityUtil.getCurrentUserLogin());
        Page<Message> messages = messageRepository.findAllMessageBySenderIdOrReceiverId(
                user.getUser_id(),
                user.getUser_id(),
                pageable);
        return convertor.mapEntityPageIntoDtoPage(messages,PageMessageDto.class);
    }

    @Override
    public void deleteMessage(UUID message_id){
        if (message_id != null){
            Message message = messageRepository.findById(message_id).orElseThrow(() ->
                    new NotFoundException("Message with id:" + message_id + " not found"));
            if (Objects.equals(SecurityUtil.getCurrentUserLogin(),message.getSender().getEmail())){
                messageRepository.delete(message);
            }else {
                throw new DoNotMatchException("You cannot delete message: Emails do not match");
            }
        }else {
            throw new NoEmptyException("Message id cannot be null");
        }
    }

    private User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User with email:" + email + " not found"));
    }
}
