package com.neoskat.docflow.repository;

import com.neoskat.docflow.enums.Role;
import com.neoskat.docflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> getUserById(Long id);
    int countByRole(Role role);
}
