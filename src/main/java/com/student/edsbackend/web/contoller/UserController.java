package com.student.edsbackend.web.contoller;

import com.student.edsbackend.configs.ApiResponse;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO dto) {
        User createdUser = userService.createUser(dto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Get a single user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> userOpt = userService.findUser(id);
        return userOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // // Update an existing user (general update - will be deprecated)
    // @PutMapping
    // public ResponseEntity<String> updateUser(@RequestBody UserDTO dto) {
    //     userService.updateUser(dto);
    //     return ResponseEntity.ok("User was successfully updated");
    // }

    // Edit user endpoint with role-based access control
    // Only allows SUPER_ADMIN to edit
    @PutMapping("/editUser/{id}")
    public ResponseEntity<?> editUser(@PathVariable Integer id, @RequestBody UserDTO dto) {
        String editResponse = userService.editUser(dto, id);
        return ResponseEntity.ok(new ApiResponse(editResponse));
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse("User was successfully deleted"));
    }
}
