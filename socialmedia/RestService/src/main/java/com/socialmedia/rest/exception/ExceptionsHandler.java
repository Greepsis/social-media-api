package com.socialmedia.rest.exception;

import com.socialmedia.api.dto.ResponseDto;
import com.socialmedia.serviceimpl.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(
            {
                    InvalidPasswordException.class,
                    NameAlreadyExistsException.class,
                    EmailAlreadyExistsException.class,
                    AlreadyAcceptedException.class,
                    AlreadyExistsException.class,
                    CannotUnsubscribeException.class,
                    DeniedRequestException.class,
                    DoNotMatchException.class,
                    NoEmptyException.class,
                    NotFoundException.class,
                    NotFriendException.class
            }
    )
    public ResponseEntity<Object> handlerRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>
                (new ResponseDto<>(false, ex.getMessage()),  HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(
            {
                    MethodArgumentNotValidException.class
            }
    )
    public ResponseEntity<Object> handlerNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>
                (new ResponseDto<>(false, ex.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList())),
                        HttpStatus.BAD_REQUEST);
    }



}
