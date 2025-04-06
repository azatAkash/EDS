package com.student.edsbackend.web.service.implementations;

import com.student.edsbackend.features.user.dal.Role;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;
import com.student.edsbackend.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findUser(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(UserDTO dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .middlename(dto.getMiddlename())
                .role(dto.getRole())

                .build();
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UserDTO dto) {
        // Assuming that UserDTO contains an id field to identify the user
        Optional<User> optionalUser = userRepository.findById(dto.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            user.setFirstname(dto.getFirstname());
            user.setLastname(dto.getLastname());
            user.setMiddlename(dto.getMiddlename());
            user.setRole(dto.getRole());
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + dto.getId());
        }
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
