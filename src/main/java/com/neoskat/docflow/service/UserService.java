package com.neoskat.docflow.service;

import com.neoskat.docflow.controller.model.LoginBody;
import com.neoskat.docflow.controller.model.RegisterBody;
import com.neoskat.docflow.enums.Role;
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
        user.setRole(registerBody.getRole());
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

    public void deleteUserById(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user");
        }
    }

    public void changeUserRole(Long id, Role newRole) throws Exception {
        User user = userRepository.findById(id).
                orElseThrow(() -> new Exception("User not found"));

        if (newRole == Role.SUPER_ADMIN) {
            int superAdminCount = userRepository.countByRole(Role.SUPER_ADMIN);
            if(superAdminCount > 0 && user.getRole() != Role.SUPER_ADMIN){
                throw new Exception("Super admin already exist");
            }
        }
        user.setRole(newRole);
        userRepository.save(user);
    }
}

