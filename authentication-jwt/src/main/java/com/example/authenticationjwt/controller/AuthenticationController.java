package com.example.authenticationjwt.controller;

import com.example.authenticationjwt.service.AuthenticationService;
import dto.request.authentication.AuthenticationRequest;
import dto.request.authentication.IntrospectRequest;
import dto.response.ApiResponse;
import dto.response.authentication.AuthenticatedResponse;
import dto.response.authentication.IntrospectResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/v1/authenticate")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ApiResponse<AuthenticatedResponse> Login(@RequestBody @Valid AuthenticationRequest request) {
        AuthenticatedResponse response = authenticationService.authenticate(request.getUsername(), request.getPassword());
        return ApiResponse.<AuthenticatedResponse>builder()
                .result(response)
                .build();
    }

    @PostMapping(value = "/introspect", consumes = "application/json", produces = "application/json")
    public ApiResponse<IntrospectResponse> validateToken(@RequestBody @Valid IntrospectRequest request) {
        var resp = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(resp)
                .build();
    }
}
