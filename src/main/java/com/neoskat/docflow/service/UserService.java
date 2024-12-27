package com.neoskat.docflow.service;

import com.neoskat.docflow.controller.model.LoginBody;
import com.neoskat.docflow.controller.model.RegisterBody;
import com.neoskat.docflow.model.User;
import com.neoskat.docflow.repository.UserRepository;
import com.neoskat.docflow.security.EncryptionService;
import com.neoskat.docflow.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;

    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    public User register(RegisterBody registerBody) throws Exception {
        if (userRepository.findByEmailIgnoreCase(registerBody.getEmail()).isPresent()) {
            throw new Exception("Email already exists");
        }
        User user = new User();
        user.setFirstname(registerBody.getFirstname());
        user.setLastname(registerBody.getLastname());
        user.setEmail(registerBody.getEmail());
        user.setPhoneNumber(registerBody.getPhoneNumber());
        user.setPassword(encryptionService.encryptPassword(registerBody.getPassword()));
        return userRepository.save(user);
    }

    public String loginEmail(LoginBody loginBody) {
        Optional<User> opUser = userRepository.findByEmailIgnoreCase(loginBody.getEmail());
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }
}

