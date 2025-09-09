package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.configuration.DtoMapper;
import com.fuxuras.patisoru.dto.auth.EmailVerification;
import com.fuxuras.patisoru.dto.auth.RegisterRequest;
import com.fuxuras.patisoru.dto.auth.LoginRequest;
import com.fuxuras.patisoru.dto.auth.LoginResponse;
import com.fuxuras.patisoru.entities.Role;
import com.fuxuras.patisoru.entities.Status;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.entities.VerificationToken;
import com.fuxuras.patisoru.exceptions.InvalidTokenException;
import com.fuxuras.patisoru.exceptions.UserAlreadyExistsException;
import com.fuxuras.patisoru.repositories.RoleRepository;
import com.fuxuras.patisoru.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final DtoMapper mapper;

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + registerRequest.getEmail() + " already exists.");
        }

        User user = mapper.RegisterRequestToUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Default role 'ROLE_USER' not found in the database"));
        user.getRoles().add(userRole);
        user.setStatus(Status.PENDING);

        User savedUser = userService.save(user);
        emailService.sendAuthMail(savedUser);
    }

    @Transactional
    public void verifyUser(EmailVerification emailVerification) {
        VerificationToken token = verificationTokenRepository.findByUserEmail(emailVerification.getEmail())
                .orElseThrow(() -> new InvalidTokenException("Verification token not found for this email."));

        if (!token.getToken().equals(emailVerification.getToken())) {
            throw new InvalidTokenException("Invalid verification token.");
        }

        User user = userService.findByEmail(emailVerification.getEmail());

        user.setStatus(Status.ACTIVE);
        userService.save(user);
        // Optionally, delete the token after successful verification
        // verificationTokenRepository.delete(token);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User user = userService.findByEmail(loginRequest.getEmail());

        String jwtToken = jwtService.generateToken(user);

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return LoginResponse.builder()
                .accessToken(jwtToken)
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}