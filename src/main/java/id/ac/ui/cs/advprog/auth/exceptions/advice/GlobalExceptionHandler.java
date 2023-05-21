package id.ac.ui.cs.advprog.auth.exceptions.advice;

import id.ac.ui.cs.advprog.auth.exceptions.*;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {DataHarianDoesNotExistException.class, MakananDoesNotExistException.class})
    public ResponseEntity<Object> dataHarianAndMakananNotAvailable(Exception exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        var baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {UserAlreadyExistException.class})
    public ResponseEntity<Object> userExist() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        var baseException = new ErrorTemplate(
                "User with the same username already exist",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {InvalidUsernameOrPasswordException.class})
    public ResponseEntity<Object> invalidUsernameOrPassword() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        var baseException = new ErrorTemplate(
                "Invalid username/password supplied",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {JwtException.class, AuthenticationException.class, UsernameNotFoundException.class})
    public ResponseEntity<Object> credentialsError(Exception exception) {
        HttpStatus badRequest = HttpStatus.UNAUTHORIZED;
        var baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }


    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> generalError(Exception exception) {
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        var baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }


}

