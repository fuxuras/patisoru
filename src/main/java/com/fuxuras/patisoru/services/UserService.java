package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.configuration.DtoMapper;
import com.fuxuras.patisoru.dto.RegisterRequest;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseMessage createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("Email başka bir kulannıcı tarafından kullanılıyor");
            responseMessage.setCode(-1);
            return responseMessage;
        }
        User user = DtoMapper.INSTANCE.RegisterRequestToUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Başarıyla kayıt oldunuz");
        responseMessage.setCode(1);
        return responseMessage;
    }

}
