package com.socialmedia.serviceimpl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia.api.dto.user.UserDto;
import com.socialmedia.api.dto.auth.AuthenticationResponseDto;
import com.socialmedia.api.service.UserService;
import com.socialmedia.serviceimpl.exception.EmailAlreadyExistsException;
import com.socialmedia.serviceimpl.exception.InvalidPasswordException;
import com.socialmedia.serviceimpl.exception.NameAlreadyExistsException;
import com.socialmedia.serviceimpl.exception.NotFoundException;
import com.socialmedia.serviceimpl.model.Role;
import com.socialmedia.serviceimpl.model.User;
import com.socialmedia.serviceimpl.repository.RoleRepository;
import com.socialmedia.serviceimpl.repository.UserRepository;
import com.socialmedia.serviceimpl.utils.Convertor;
import com.socialmedia.serviceimpl.utils.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;
    private final Convertor convertor;

    /**
     *
     * @param userDto include:name,email,password
     */
    @Override
    public void addUser(UserDto userDto) {
        if (userRepository.existsByName(userDto.getName())){
            throw new NameAlreadyExistsException("User with name:" + userDto.getName() + " already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new EmailAlreadyExistsException("User with email:" + userDto.getEmail() + " already exists");
        }
        if (!validator.validatePassword(userDto.getPassword())){
            throw new InvalidPasswordException("Password not valid");
        }
        User user = objectMapper.convertValue(userDto, User.class);
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null){
            userRole = checkRoleExist();
        }
        user.setRoles(Collections.singleton(userRole));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }

    @Override
    public AuthenticationResponseDto authenticationUser(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User with email:" + email + " not found."));
        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setEmail(user.getEmail());
        return authenticationResponseDto;
    }
}
