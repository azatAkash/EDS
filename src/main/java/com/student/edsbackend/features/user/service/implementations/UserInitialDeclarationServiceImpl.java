package com.student.edsbackend.features.user.service.implementations;

import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRepository;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.Role;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclaration;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationRepository;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationRequestDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationUpdateDTO;
import com.student.edsbackend.features.user.service.UserInitialDeclarationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInitialDeclarationServiceImpl implements UserInitialDeclarationService {

        private final UserInitialDeclarationRepository userInitialDeclarationRepository;
        private final UserRepository userRepository;
        private final InitialDeclarationRepository initialDeclarationRepository;

        @Override
        public Optional<UserInitialDeclarationDTO> getUserInitialDeclarationById(Integer id) {
                return userInitialDeclarationRepository.findById(id)
                                .filter(declaration -> !declaration.getIsDeleted())
                                .map(this::mapToDTO);
        }

        @Override
        public List<UserInitialDeclarationDTO> getAllUserInitialDeclarations() {
                return userInitialDeclarationRepository.findByIsDeletedFalse()
                                .stream()
                                .map(this::mapToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public UserInitialDeclarationDTO createUserInitialDeclaration(UserInitialDeclarationRequestDTO requestDTO) {
                // Validate user exists
                User user = userRepository.findById(requestDTO.getUserId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "User not found with id: " + requestDTO.getUserId()));

                // Find the active declaration
                InitialDeclaration declaration = initialDeclarationRepository.findAll().stream()
                                .filter(d -> d.getIsActive() && !d.getIsDeleted())
                                .findFirst()
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "No active declaration found. Please ask Administrator to activate a declaration first."));

                // Check if a record already exists for this user and declaration
                Optional<UserInitialDeclaration> existingDeclaration = userInitialDeclarationRepository
                                .findByUserIdAndDeclarationIdAndIsDeletedFalse(
                                                requestDTO.getUserId(), declaration.getId());

                if (existingDeclaration.isPresent()) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                        "A declaration already exists for this user and declaration");
                }

                // Get current user from security context to set as responsible
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String currentUserEmail = authentication.getName();

                // Find the current user by email
                User currentUser = userRepository.findByEmail(currentUserEmail)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Current user not found"));
                if (currentUser == user && currentUser.getRole() != Role.SUPER_ADMIN
                                && currentUser.getRole() != Role.ADMIN) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "You cannot set yourself as responsible for a declaration");
                }
                // Create new user declaration
                UserInitialDeclaration userInitialDeclaration = UserInitialDeclaration.builder()
                                .user(user)
                                .declaration(declaration)
                                .creationDate(LocalDateTime.now())
                                .status(UserDeclarationStatus.CREATED)
                                .responsible(currentUser)
                                .createdBy(currentUser)
                                .isDeleted(false)
                                .build();

                // Save and return
                UserInitialDeclaration savedDeclaration = userInitialDeclarationRepository.save(userInitialDeclaration);
                return mapToDTO(savedDeclaration);
        }

        @Override
        public void deleteUserInitialDeclaration(Integer id) {
                // Find the declaration
                UserInitialDeclaration declaration = userInitialDeclarationRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "User declaration not found with id: " + id));

                // Soft delete
                declaration.setIsDeleted(true);
                userInitialDeclarationRepository.save(declaration);
        }

        @Override
        public UserInitialDeclarationDTO updateResponsible(Integer id, UserInitialDeclarationRequestDTO requestDTO) {
                // Find the declaration
                UserInitialDeclaration declaration = userInitialDeclarationRepository.findById(id)
                                .filter(d -> !d.getIsDeleted())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "User declaration not found with id: " + id));

                // Check if the declaration is in SENT_FOR_APPROVAL status
                if (declaration.getStatus() != UserDeclarationStatus.SENT_FOR_APPROVAL) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Only declarations in SENT_FOR_APPROVAL status can be verified");
                }

                // Update responsible if provided
                if (requestDTO.getUserId() != null) {
                        User responsible = userRepository.findById(requestDTO.getUserId())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                        "Responsible user not found with id: "
                                                                        + requestDTO.getUserId()));

                        // Check if the responsible user is a manager or admin
                        if (responsible.getRole() != Role.MANAGER && responsible.getRole() != Role.ADMIN
                                        && responsible.getRole() != Role.SUPER_ADMIN) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Only managers and admins can be responsible for a declaration");
                        }
                        declaration.setResponsible(responsible);
                } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Responsible user is required");
                }

                // Save and return

                UserInitialDeclaration updatedDeclaration = userInitialDeclarationRepository.save(declaration);
                return mapToDTO(updatedDeclaration);
        }

        @Override
        public UserInitialDeclarationDTO changeStatusById(Integer id, UserInitialDeclarationUpdateDTO updateDTO) {
                // Find the declaration
                UserInitialDeclaration declaration = userInitialDeclarationRepository.findById(id)
                                .filter(d -> !d.getIsDeleted())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "User declaration not found with id: " + id));
                // Update status if provided
                if (updateDTO.getStatus() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Status is required");
                }

                // Get current user from security context to set as responsible
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String currentUserEmail = authentication.getName();
                // Get current user from security context
                User currentUser = userRepository.findByEmail(currentUserEmail)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Current user not found"));

                if (currentUser.getRole()!= Role.SUPER_ADMIN && currentUser.getRole()!= Role.ADMIN
                                && currentUser.getRole()!= Role.MANAGER) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "You do not have permission to change the status of this declaration");
                }

                // Check if the declaration is already sent for approval
                if (declaration.getStatus() == updateDTO.getStatus()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Declaration is already in this status");
                }
                declaration.setStatus(updateDTO.getStatus());
                // Save and return
                UserInitialDeclaration updatedDeclaration = userInitialDeclarationRepository.save(declaration);
                return mapToDTO(updatedDeclaration);

        }

        private UserDTO mapUserToUserDTO(User user) {
                return UserDTO.builder()
                                .id(user.getId())
                                .firstname(user.getFirstname())
                                .lastname(user.getLastname())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .department(user.getDepartment())
                                .middlename(user.getMiddlename())
                                .isActive(user.getIsActive())
                                .isDeleted(user.getIsDeleted())
                                .position(user.getPosition())
                                .registrationDate(user.getRegistrationDate())
                                .build();
        }

        /**
         * Maps a UserInitialDeclaration entity to its DTO representation
         */
        private UserInitialDeclarationDTO mapToDTO(UserInitialDeclaration declaration) {
                return UserInitialDeclarationDTO.builder()
                                .id(declaration.getId())
                                .user(mapUserToUserDTO(declaration.getUser()))
                                .declarationId(declaration.getDeclaration().getId())
                                .creationDate(declaration.getCreationDate())
                                .createdBy(mapUserToUserDTO(declaration.getCreatedBy()))
                                .status(declaration.getStatus())
                                .responsible(mapUserToUserDTO(
                                                declaration.getResponsible() != null ? declaration.getResponsible()
                                                                : null))
                                .responsibleName(declaration.getResponsible() != null
                                                ? declaration.getResponsible().getFirstname() + " "
                                                                + declaration.getResponsible().getLastname()
                                                : null)
                                .isDeleted(declaration.getIsDeleted())
                                .build();
        }
}
