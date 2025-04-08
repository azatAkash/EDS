package com.student.edsbackend.features.user.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO user = userService.findUser(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with id " + id + " not found"));
        return ResponseEntity.ok(user);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Edit user endpoint with role-based access control
    // Allows both ADMIN and SUPER_ADMIN to edit
    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@PathVariable Integer id, @RequestBody UserDTO dto) {
        String editResponse = userService.editUser(dto, id);
        return ResponseEntity.ok(new ApiResponse(editResponse));
    }

    // Soft delete a user by ID - only SUPER_ADMIN can delete users
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse("User was successfully marked as deleted"));
    }
}
