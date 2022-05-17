package com.intech.chat.handler;

import com.intech.chat.dto.response.ErrorResponse;
import com.intech.chat.exception.IncorrectNiknameException;
import com.intech.chat.exception.UserExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException exc) {
        ErrorResponse badResponse = new ErrorResponse().setError("unauthorized")
                .setErrorDescription(exc.getMessage());
        log.warn(Arrays.toString(exc.getStackTrace()));
        return new ResponseEntity<>(badResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ErrorResponse> handleRegisterUserExistException(UserExistException exc) {
        ErrorResponse badRequestResponse = new ErrorResponse().setError("invalid_request")
                .setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectNiknameException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectNicknameException(IncorrectNiknameException exc) {
        ErrorResponse badRequestResponse = new ErrorResponse().setError("invalid_request")
                .setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
}
