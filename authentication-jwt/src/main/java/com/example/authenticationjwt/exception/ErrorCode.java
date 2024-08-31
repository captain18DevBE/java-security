package com.example.authenticationjwt.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    DO_NOT_MATCH_PASSWORD(1009, "Username or Passwords do not match", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(10010, "You do not permission", HttpStatus.FORBIDDEN),
    AUTHENTICATION_FAILED(10011, "Authentication failed", HttpStatus.UNAUTHORIZED),
    VERIFICATION_FAILED(10012, "Verification failed", HttpStatus.UNAUTHORIZED),
    PERMISSION_DENIED(10013, "Permission denied", HttpStatus.FORBIDDEN),
    PERMISSION_NOT_EXISTED(10014, "Permission not existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(10015, "Role not existed", HttpStatus.BAD_REQUEST),
    ROLE_ALREADY_EXISTED(10016, "Role already existed", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    int code;
    String message;
    HttpStatusCode statusCode;
}
