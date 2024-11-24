package org.olzhas.projectnic.service;

import org.olzhas.projectnic.dto.JwtAuthRefreshToken;
import org.olzhas.projectnic.dto.JwtAuthResponse;
import org.olzhas.projectnic.dto.SignInRequest;
import org.olzhas.projectnic.dto.SignUpRequest;
import org.olzhas.projectnic.entity.User;


public interface AuthenticationService {
    void signUp(SignUpRequest signUpRequest);

    JwtAuthResponse signIn(SignInRequest signInRequest);

    JwtAuthResponse refreshToken(JwtAuthRefreshToken jwtAuthRefreshToken);

    String sendVerificationEmail(User user);

    void confirmVerificationEmail(String users);

}
