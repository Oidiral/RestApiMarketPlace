package org.olzhas.projectnic.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.olzhas.projectnic.dto.JwtAuthRefreshToken;
import org.olzhas.projectnic.dto.JwtAuthResponse;
import org.olzhas.projectnic.dto.SignInRequest;
import org.olzhas.projectnic.dto.SignUpRequest;
import org.olzhas.projectnic.service.impl.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        authenticationServiceImpl.signUp(signUpRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        authenticationServiceImpl.confirmVerificationEmail(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/signIn")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        return ResponseEntity.ok(authenticationServiceImpl.signIn(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refresh(@RequestBody JwtAuthRefreshToken jwtAuthRefreshToken) {
        return ResponseEntity.ok(authenticationServiceImpl.refreshToken(jwtAuthRefreshToken));
    }
}
