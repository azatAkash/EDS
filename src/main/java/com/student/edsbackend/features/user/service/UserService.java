package com.student.edsbackend.features.user.service;

import java.util.List;
import java.util.Optional;

import com.student.edsbackend.features.user.dal.UserDTO;

public interface UserService {
    Optional<UserDTO> findUser(Integer id);
    List<UserDTO> getAllUsers();
    String editUser(UserDTO dto, Integer userId);
    void deleteUser(Integer id);
}
