package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.configuration.DtoMapper;
import com.fuxuras.patisoru.dto.*;
import com.fuxuras.patisoru.entities.Status;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.entities.VerificationToken;
import com.fuxuras.patisoru.repositories.UserRepository;
import com.fuxuras.patisoru.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DtoMapper mapper;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;


    public ResponseMessage createUser(RegisterRequest registerRequest) {
        Optional<User> existingUser =  userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            ResponseMessage responseMessage = new ResponseMessage();
            if (existingUser.get().getStatus().equals(Status.PENDING)){
                responseMessage.setMessage("Email doğrulama bekliyor");
            }else {
                responseMessage.setMessage("Email başka bir kullanıcı tarafından kullanılıyor");
            }
            responseMessage.setCode(-1);
            return responseMessage;
        }
        User user = mapper.RegisterRequestToUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setStatus(Status.PENDING);
        user = userRepository.save(user);
        emailService.sendAuthMail(user);

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Başarıyla kayıt oldunuz. Doğrulama için emailinizi kontrol ediniz.");
        responseMessage.setCode(1);
        return responseMessage;
    }

    protected Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
        UserResponse userResponse = mapper.userToUserResponse(user);
        return userResponse;
    }

    public ResponseMessage editUser(UserEditRequest userEditRequest, String oldEmail) {
        User user = userRepository.findByEmail(oldEmail).orElseThrow(() -> new RuntimeException("user not found"));
        mapper.updateUserFromRequest(userEditRequest, user);
        userRepository.save(user);
        ResponseMessage responseMessage = new ResponseMessage("Hesap Bilgileri Güncellendi",1);
        return responseMessage;
    }

    public ResponseMessage verifyUser(EmailVerification emailVerification) {
        Optional<VerificationToken> token = verificationTokenRepository.findByUserEmail(emailVerification.getEmail());
        if (token.isPresent()) {
            if (token.get().getToken().equals(emailVerification.getToken())) {
                User user = userRepository.findByEmail(emailVerification.getEmail()).orElseThrow(() -> new RuntimeException("user not found"));
                user.setStatus(Status.ACTIVE);
                userRepository.save(user);
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setCode(1);
                responseMessage.setMessage("Başarıyla Doğrulandı giriş yapabilirsiniz");
                return responseMessage;
            }
        }
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(-1);
        responseMessage.setMessage("Doğrulama kodu yanlış");
        return responseMessage;
    }
}
