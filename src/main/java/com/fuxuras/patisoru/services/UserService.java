package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.configuration.DtoMapper;
import com.fuxuras.patisoru.dto.RegisterRequest;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.dto.UserEditRequest;
import com.fuxuras.patisoru.dto.UserResponse;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.repositories.UserRepository;
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

    public ResponseMessage createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("Email başka bir kulannıcı tarafından kullanılıyor");
            responseMessage.setCode(-1);
            return responseMessage;
        }
        User user = mapper.RegisterRequestToUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Başarıyla kayıt oldunuz");
        responseMessage.setCode(1);
        return responseMessage;
    }

    public Optional<User> findByEmail(String email) {
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
}
