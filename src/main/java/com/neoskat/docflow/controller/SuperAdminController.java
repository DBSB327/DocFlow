    package com.neoskat.docflow.controller;

    import com.neoskat.docflow.model.ChangeRoleRequest;
    import com.neoskat.docflow.service.UserService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/super-admin")
    @RequiredArgsConstructor
    public class SuperAdminController {

        private final UserService userService;

        @DeleteMapping("/users/{id}")
        public ResponseEntity<String> deleteUser(@PathVariable Long id) {
            try {
                System.out.println("Attempting to delete user with ID: " + id);
                userService.deleteUserById(id);
                return ResponseEntity.ok("User deleted successfully");
            } catch (Exception e) {
                System.out.println("Error occurred while deleting user with ID: " + id + ", error: " + e.getMessage());
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        @PutMapping("/users/role")
        public ResponseEntity<String> changeUserRole(@RequestBody ChangeRoleRequest roleRequest) {
            try {
                System.out.println("Changing role for user with ID: " + roleRequest.getUserId() + " to role: " + roleRequest.getNewRole());
                userService.changeUserRole(roleRequest.getUserId(), roleRequest.getNewRole());
                return ResponseEntity.ok("User role updated successfully");
            } catch (Exception e) {
                System.out.println("Error occurred while changing role: " + e.getMessage());
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }
