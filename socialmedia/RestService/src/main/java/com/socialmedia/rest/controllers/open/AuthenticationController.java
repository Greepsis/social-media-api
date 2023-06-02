package com.socialmedia.rest.controllers.open;

import com.socialmedia.api.dto.ResponseDto;
import com.socialmedia.api.dto.post.page.PagePostDto;
import com.socialmedia.api.dto.user.UserDto;
import com.socialmedia.api.dto.user.page.PageUserDto;
import com.socialmedia.api.dto.auth.AuthenticationRequestDto;
import com.socialmedia.api.dto.auth.AuthenticationResponseDto;
import com.socialmedia.api.service.UserService;
import com.socialmedia.api.specification.SearchCriteria;
import com.socialmedia.rest.security.jwt.JwtToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/socialmedia/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication Controller", description = "Allows you to login or register a user")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtToken jwtToken;
    private final UserService userService;


    @PostMapping("/signin")
    @Operation(
            summary = "Sign in user. Return token. Use token to access to another controller"
    )
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequestDto requestDto){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(),
                    requestDto.getPassword()));
            AuthenticationResponseDto authenticationResponseDto = userService.authenticationUser(requestDto.getEmail());
            authenticationResponseDto.setToken(jwtToken.createToken(requestDto.getEmail()));
            return new ResponseEntity<>(new ResponseDto<>(true, authenticationResponseDto), HttpStatus.OK);
        }catch (AuthenticationException authenticationException){
            return new ResponseEntity<>(new ResponseDto<>(false,
                    "Invalid email or password.Check the correctness of the entered data"), HttpStatus.BAD_REQUEST);

        }

    }

    @PostMapping("/signup")
    @Operation(
            summary = "Sign up user"
    )
    public ResponseEntity<?> registration(@Valid @RequestBody UserDto userDto){
        try {
            userService.addUser(userDto);
            return new ResponseEntity<>(new ResponseDto<>(true,"User:" + userDto.getEmail() + " has been created."),
                    HttpStatus.CREATED);
        }catch (AuthenticationException exception){
            return new ResponseEntity<>(new ResponseDto<>(false,
                    "Invalid email or password.Check the correctness of the entered data."),HttpStatus.BAD_REQUEST);
        }
    }



}
