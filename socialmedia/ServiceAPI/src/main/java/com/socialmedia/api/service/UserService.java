package com.socialmedia.api.service;

import com.socialmedia.api.dto.user.UserDto;
import com.socialmedia.api.dto.user.page.PageUserDto;
import com.socialmedia.api.dto.auth.AuthenticationResponseDto;
import com.socialmedia.api.specification.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    void addUser(UserDto userDto);
    AuthenticationResponseDto authenticationUser(String email);
}
