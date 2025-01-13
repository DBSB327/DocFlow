package com.neoskat.docflow.model;

import com.neoskat.docflow.enums.Role;
import lombok.Data;

@Data
public class ChangeRoleRequest {
    private Long userId;
    private Role newRole;
}
