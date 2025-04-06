package com.student.edsbackend.web.service;

import java.util.List;
import java.util.Optional;

import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;

public interface UserService {
    Optional<User> findUser(Integer id);
    User createUser(UserDTO dto);
    List<User> getAllUsers();
    void updateUser(UserDTO dto);
    String editUser(UserDTO dto, Integer userId);
    void deleteUser(Integer id);
}
