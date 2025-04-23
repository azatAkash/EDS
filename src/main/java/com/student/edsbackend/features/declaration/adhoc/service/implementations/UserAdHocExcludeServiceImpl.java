package com.student.edsbackend.features.declaration.adhoc.service.implementations;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.declaration.adhoc.UserAdHocExclude;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeUpdateDTO;
import com.student.edsbackend.features.declaration.adhoc.repository.UserAdHocDeclareRepository;
import com.student.edsbackend.features.declaration.adhoc.repository.UserAdHocExcludeRepository;
import com.student.edsbackend.features.declaration.adhoc.service.UserAdHocDeclareService;
import com.student.edsbackend.features.declaration.adhoc.service.UserAdHocExcludeService;
import com.student.edsbackend.features.declaration.agreement.repository.DecAgreementStatementRepository;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclaration;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationRepository;
import com.student.edsbackend.features.user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the UserAdHocExcludeService interface
 */
@Service
@RequiredArgsConstructor
public class UserAdHocExcludeServiceImpl implements UserAdHocExcludeService {

    private static final Logger log = LoggerFactory.getLogger(UserAdHocExcludeServiceImpl.class);
    private final DecAgreementStatementRepository decAgreementStatementRepository;
    private final UserAdHocExcludeRepository userAdHocExcludeRepository;
    private final UserAdHocDeclareRepository userAdHocDeclareRepository;
    private final UserInitialDeclarationRepository userInitialDeclarationRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserAdHocDeclareService userAdHocDeclareService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserAdHocExcludeDTO createAdHocExclusion(Integer userId, UserAdHocExcludeRequestDTO requestDTO) {
       // Get current user from security context
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String currentUserEmail = authentication.getName();

       // Find the user by email
       User currentUser = userRepository.findByEmail(currentUserEmail)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

       
       // Validate that the declaration belongs to the current user or user has appropriate permissions
       if (userId != currentUser.getId() && 
           currentUser.getRole().name() != ("SUPER_ADMIN")) {
           throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to save answers for this declaration");
       }
       if (requestDTO.getHasAgreedWithStatements() != true) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must agree with all statements before submitting the exclusion");
       }

       
       // Get the initial declaration if provided
        UserInitialDeclaration initialDeclaration = null;
        if (requestDTO.getInitialDeclarationId() != null) {
            initialDeclaration = userInitialDeclarationRepository.findById(requestDTO.getInitialDeclarationId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Initial declaration not found"));
        }
        
        // Get the ad hoc declaration if provided
        UserAdHocDeclare adHocDeclare = null;
        if (requestDTO.getUserAdHocDeclareId() != null) {
            adHocDeclare = userAdHocDeclareRepository.findById(requestDTO.getUserAdHocDeclareId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad hoc declaration not found"));
        }

        if (requestDTO.getInitialDeclarationId()!= null && requestDTO.getUserAdHocDeclareId()!= null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't submit an exclusion for both initial and ad hoc declarations");
        }


        List<Map<String, String>> agreedStatements = decAgreementStatementRepository.findAllDescriptionsAndIsDeletedFalse();
        // Create the new exclusion
        UserAdHocExclude exclusion = UserAdHocExclude.builder()
                .user(userRepository.findById(userId).orElseThrow(() -> 
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")))
                .createdAt(LocalDateTime.now())
                .excludeReason(requestDTO.getExcludeReason())
                .userInitialDeclaration(initialDeclaration)
                .userAdHocDeclare(adHocDeclare)
                .status(UserDeclarationStatus.SENT_FOR_APPROVAL)
                .isDeleted(false)
                .isConfirmed(false)
                .hasAgreedWithStatements(requestDTO.getHasAgreedWithStatements())
                .agreedStatements(agreedStatements)
                .build();
        
        UserAdHocExclude savedExclusion = userAdHocExcludeRepository.save(exclusion);
        return convertToDTO(savedExclusion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserAdHocExcludeDTO> getAdHocExclusionById(Integer id) {
        return userAdHocExcludeRepository.findByIdAndIsDeletedFalse(id)
                .map(this::convertToDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserAdHocExcludeDTO> getAdHocExclusionsByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        return userAdHocExcludeRepository.findByUserAndIsDeletedFalse(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserAdHocExcludeDTO> getAdHocExclusionsByStatus(UserDeclarationStatus status) {
        return userAdHocExcludeRepository.findByStatusAndIsDeletedFalse(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserAdHocExcludeDTO updateAdHocExclusion(Integer id, UserAdHocExcludeUpdateDTO updateDTO) {
        UserAdHocExclude exclusion = userAdHocExcludeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad hoc exclusion not found"));
        
        // Update only the fields that are provided (PATCH behavior)
        if (updateDTO.getIsConfirmed() != null) {
            exclusion.setIsConfirmed(updateDTO.getIsConfirmed());
        }
        
        if (updateDTO.getStatus() != null) {
            exclusion.setStatus(updateDTO.getStatus());
        }
        
        UserAdHocExclude updatedExclusion = userAdHocExcludeRepository.save(exclusion);
        return convertToDTO(updatedExclusion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteAdHocExclusion(Integer id) {
        UserAdHocExclude exclusion = userAdHocExcludeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad hoc exclusion not found"));
        
        // Soft delete
        exclusion.setIsDeleted(true);
        userAdHocExcludeRepository.save(exclusion);
    }
    
    /**
     * Converts a User entity to a UserDTO. Handles null input.
     */
    private UserDTO convertUserToDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .department(user.getDepartment())
                .isActive(user.getIsActive())
                .role(user.getRole())
                .position(user.getPosition())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserAdHocExcludeDTO convertToDTO(UserAdHocExclude exclusion) {
        if (exclusion == null) {
            return null;
        }
        
        // Convert related entities to DTOs
        UserDTO userDTO = convertUserToDTO(exclusion.getUser());
        UserDTO createdByDTO = null; // This would need to be set if you track who created it
        UserDTO responsibleDTO = null; // This would need to be set if you track who is responsible
        
        // Convert initial declaration if present
        UserInitialDeclarationDTO initialDeclarationDTO = null;
        if (exclusion.getUserInitialDeclaration() != null) {
            UserInitialDeclaration declaration = exclusion.getUserInitialDeclaration();
            initialDeclarationDTO = UserInitialDeclarationDTO.builder()
                .id(declaration.getId())
                .user(convertUserToDTO(declaration.getUser()))
                .declarationId(declaration.getId())
                .creationDate(declaration.getCreationDate())
                .status(declaration.getStatus())
                .responsible(convertUserToDTO(declaration.getResponsible()))
                .createdBy(convertUserToDTO(declaration.getCreatedBy()))
                .build();
        }
        
        // Convert ad hoc declaration if present
        UserAdHocDeclareDTO adHocDeclareDTO = null;
        if (exclusion.getUserAdHocDeclare() != null) {
            adHocDeclareDTO = userAdHocDeclareService.getAdHocDeclarationById(exclusion.getUserAdHocDeclare().getId()).orElse(null);
        }
        
        return UserAdHocExcludeDTO.builder()
                .id(exclusion.getId())
                .user(userDTO)
                .createdBy(createdByDTO)
                .responsible(responsibleDTO)
                .userInitialDeclaration(initialDeclarationDTO)
                .userAdHocDeclare(adHocDeclareDTO)
                .excludeReason(exclusion.getExcludeReason())
                .createdAt(exclusion.getCreatedAt())
                .status(exclusion.getStatus())
                .isDeleted(exclusion.getIsDeleted())
                .isConfirmed(exclusion.getIsConfirmed())
                .hasAgreedWithStatements(exclusion.getHasAgreedWithStatements())
                .agreedStatements(exclusion.getAgreedStatements())
                .build();
    }
}