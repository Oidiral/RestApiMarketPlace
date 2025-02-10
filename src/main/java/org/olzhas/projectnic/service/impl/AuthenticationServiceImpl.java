package org.olzhas.projectnic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olzhas.projectnic.dto.JwtAuthRefreshToken;
import org.olzhas.projectnic.dto.JwtAuthResponse;
import org.olzhas.projectnic.dto.SignInRequest;
import org.olzhas.projectnic.dto.SignUpRequest;
import org.olzhas.projectnic.email.EmailSender;
import org.olzhas.projectnic.entity.User;
import org.olzhas.projectnic.entity.VerificationToken;
import org.olzhas.projectnic.exception.AccessDeniedException;
import org.olzhas.projectnic.exception.AlreadyExistException;
import org.olzhas.projectnic.exception.NotFoundException;
import org.olzhas.projectnic.exception.TokenException;
import org.olzhas.projectnic.repository.UserRepository;
import org.olzhas.projectnic.repository.VerificationTokenRepository;
import org.olzhas.projectnic.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final EmailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${server.port}")
    private int serverPort;

    @Transactional
    @Override
    public void signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail()) || userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new AlreadyExistException("User already exist");
        }
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .isActive(false)
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(signUpRequest.getRole())
                .build();
        sendVerificationEmail(user);
        log.info("Sign up successful, email: {}", user.getEmail());
        userRepository.save(user);
    }


    @Override
    @Transactional
    public JwtAuthResponse signIn(SignInRequest signInRequest) {

        User user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(()
                -> new NotFoundException("User not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("Role", user.getRole());
        claims.put("", user.isActive());

        if (user.getVerificationTokens().getExpiresAt().isBefore(LocalDateTime.now()) && !user.isEnabled() || user.getVerificationTokens() == null) {
            log.error("User is not active");
            verificationTokenRepository.delete(user.getVerificationTokens());
            sendVerificationEmail(user);
            throw new AccessDeniedException("confirm email access, please try again, we will sent you verification email");
        } else if (!user.isEnabled()) {
            throw new AccessDeniedException("User is not active or enabled");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                signInRequest.getPassword()));


        String token = jwtService.generateToken(user, claims);
        String refreshToken = jwtService.generateRefreshToken(user);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setToken(token);
        jwtAuthResponse.setRefreshToken(refreshToken);

        return jwtAuthResponse;
    }

    @Override
    public JwtAuthResponse refreshToken(JwtAuthRefreshToken jwtAuthRefreshToken) {
        User userMail = userRepository.findByEmail(jwtService.extractUsername(jwtAuthRefreshToken.getToken())).orElseThrow(()
                -> new NotFoundException("User not found"));
        if (jwtService.validateToken(jwtAuthRefreshToken.getToken(), userMail)) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", userMail.getId());
            claims.put("Role", userMail.getRole());
            claims.put("", userMail.isActive());
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setToken(jwtService.generateToken(userMail, claims));
            jwtAuthResponse.setRefreshToken(jwtAuthRefreshToken.getToken());
            return jwtAuthResponse;
        }
        throw new NotFoundException("Token not found");
    }


    @Override
    public String sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .users(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        String link = "http://localhost:" + serverPort + "/api/v1/auth/confirm?token=" + token;
        emailSender.send(user.getEmail(), emailbuilder(link, user.getFirstName()));
        verificationTokenRepository.save(verificationToken);
        log.info("email sent");
        return token;
    }

    @Override
    @Transactional
    public void confirmVerificationEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(() ->
                new NotFoundException("Token not found"));
        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenException("Token expired");
        }
        if (verificationToken.getConfirmedAt() != null) {
            throw new TokenException("Token confirmed");
        }
        verificationToken.setConfirmedAt(LocalDateTime.now());
        User user = verificationToken.getUsers();
        user.setActive(true);
        userRepository.save(user);
    }


    private String emailbuilder(String link, String name) {
        return "<head>\n" +
                "    <title>Confirm Your Email</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            font-size: 16px;\n" +
                "            color: #333;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            background-color: #f7f7f7;\n" +
                "        }\n" +
                "        .email-container {\n" +
                "            width: 100%;\n" +
                "            padding: 20px;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "        }\n" +
                "        .email-content {\n" +
                "            width: 600px;\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        .email-header {\n" +
                "            background-color: #4A90E2;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            color: #ffffff;\n" +
                "        }\n" +
                "        .email-header h1 {\n" +
                "            font-size: 24px;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "        .email-body {\n" +
                "            padding: 30px;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "        .email-body p {\n" +
                "            font-size: 16px;\n" +
                "            line-height: 1.5;\n" +
                "            color: #555;\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "        .email-body .highlight {\n" +
                "            font-size: 18px;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        .email-body .activate-button {\n" +
                "            background-color: #4A90E2;\n" +
                "            color: #ffffff;\n" +
                "            padding: 15px 25px;\n" +
                "            font-size: 16px;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "        .email-body .activate-button:hover {\n" +
                "            background-color: #3a78b2;\n" +
                "        }\n" +
                "        .email-footer {\n" +
                "            background-color: #f7f7f7;\n" +
                "            padding: 10px;\n" +
                "            text-align: center;\n" +
                "            font-size: 12px;\n" +
                "            color: #888;\n" +
                "        }\n" +
                "        .email-image {\n" +
                "            width: 100%;\n" +
                "            height: auto;\n" +
                "            display: block;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"email-container\">\n" +
                "    <div class=\"email-content\">\n" +
                "        <!-- Header -->\n" +
                "        <div class=\"email-header\">\n" +
                "            <h1>Confirm Your Email</h1>\n" +
                "        </div>\n" +
                "        <!-- Image -->\n" +
                "        <img src=\"https://steamuserimages-a.akamaihd.net/ugc/958588498871326563/8EFD474A9DDF16861A0229822586B65BAFF70E83/?imw=5000&imh=5000&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false\" alt=\"Welcome Image\" class=\"email-image\" />\n" +
                "        <!-- Body -->\n" +
                "        <div class=\"email-body\">\n" +
                "            <p class=\"highlight\">Hello," + name + ",</p>\n" +
                "            <p>Thank you for signing up! Please click the button below to activate your account:</p>\n" +
                "            <div style=\"text-align: center; margin: 20px 0;\">\n" +
                "                <a href=\"" + link + "\" class=\"activate-button\">Activate Now</a>\n" +
                "            </div>\n" +
                "            <p>The link will expire in <strong>30 minutes</strong>.</p>\n" +
                "            <p>See you soon!</p>\n" +
                "        </div>\n" +
                "        <!-- Footer -->\n" +
                "        <div class=\"email-footer\">\n" +
                "            © 2024 Your Company. All rights reserved.\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>";
    }


}
