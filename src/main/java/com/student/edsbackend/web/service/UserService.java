package com.student.edsbackend.web.service;

import com.student.edsbackend.dal.user.User;
import com.student.edsbackend.dal.user.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findUser(Integer id);
    User createUser(UserDTO dto);
    List<User> getAllUsers();
    void updateUser(UserDTO dto);
    void deleteUser(Integer id);
}
