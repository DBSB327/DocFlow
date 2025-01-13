package com.neoskat.docflow.config;

import com.neoskat.docflow.enums.Role;
import com.neoskat.docflow.model.User;
import com.neoskat.docflow.repository.UserRepository;
import com.neoskat.docflow.security.EncryptionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialSetup {
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    @PostConstruct
    public void init() {
        String superAdminEmail = "superadmin@gmail.com";
        if(userRepository.findByEmailIgnoreCase(superAdminEmail).isEmpty()) {
            User superAdmin = new User();
            superAdmin.setFirstname("Super");
            superAdmin.setLastname("Admin");
            superAdmin.setEmail(superAdminEmail);
            superAdmin.setPassword(encryptionService.encryptPassword("superadmin"));
            superAdmin.setRole(Role.SUPER_ADMIN);
            userRepository.save(superAdmin);
            System.out.println("SuperAdmin added: " + superAdminEmail);
        }
    }
}
