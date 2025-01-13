package com.neoskat.docflow.controller.model;

import com.neoskat.docflow.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class RegisterBody {
    @NotNull
    @NotBlank
    private String firstname;
    @NotNull
    @NotBlank
    private String lastname;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String phoneNumber;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    @Size(min = 8, max = 20)
    private String password;
    @NotNull
    private Role role;
}
