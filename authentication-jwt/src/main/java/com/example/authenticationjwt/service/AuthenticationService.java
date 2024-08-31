package com.example.authenticationjwt.service;

import com.example.authenticationjwt.entity.Role;
import com.example.authenticationjwt.entity.User;
import com.example.authenticationjwt.exception.AppException;
import com.example.authenticationjwt.exception.AuthenticationException;
import com.example.authenticationjwt.exception.ErrorCode;
import com.example.authenticationjwt.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dto.request.authentication.IntrospectRequest;
import dto.response.authentication.AuthenticatedResponse;
import dto.response.authentication.IntrospectResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticatedResponse authenticate(String username, String password) {
        AuthenticatedResponse authenticatedResponse = new AuthenticatedResponse();
        User user = userRepository.findByUsername(username);

        if (user != null) {
            var authenticated = passwordEncoder.matches(password, user.getPassword());
            if (authenticated) {
                authenticatedResponse.setAuthenticated(true);
                authenticatedResponse.setToken(generateToken(user));
                return authenticatedResponse;
            }
            throw new AppException(ErrorCode.DO_NOT_MATCH_PASSWORD);
        } else {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request)
    {
        String token = request.getToken();
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);
            return IntrospectResponse.builder()
                    .valid(verified && expiryTime.after(new Date()))
                    .build();
        }  catch (JOSEException | ParseException e) {
            throw new AppException(ErrorCode.VERIFICATION_FAILED);
        }
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("https://github.com/captain18DevBE")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
                }
            });
        }
        return joiner.toString();
    }
}
