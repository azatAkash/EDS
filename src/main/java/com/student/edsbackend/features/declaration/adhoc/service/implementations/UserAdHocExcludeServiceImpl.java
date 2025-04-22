package com.student.edsbackend.features.declaration.adhoc.service.implementations;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.declaration.adhoc.UserAdHocExclude;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.repository.UserAdHocDeclareRepository;
import com.student.edsbackend.features.declaration.adhoc.repository.UserAdHocExcludeRepository;
import com.student.edsbackend.features.declaration.adhoc.service.UserAdHocExcludeService;
import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswer;
import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswerRepository;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAnswer;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAnswerRepository;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclaration;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationRepository;
import com.student.edsbackend.features.user.dal.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the UserAdHocExcludeService interface
 */
@Service
@RequiredArgsConstructor
public class UserAdHocExcludeServiceImpl implements UserAdHocExcludeService {

    private final UserAdHocExcludeRepository userAdHocExcludeRepository;
    private final UserRepository userRepository;
    private final UserInitialDeclarationRepository userInitialDeclarationRepository;
    private final UserAdHocDeclareRepository userAdHocDeclareRepository;
    private final UserAdHocDeclareAnswerRepository userAdHocDeclareAnswerRepository;
    private final UserDeclarationAnswerRepository userDeclarationAnswerRepository;

    @Override
    @Transactional
    public UserAdHocExcludeDTO createExclusion(UserAdHocExcludeRequestDTO requestDTO) {
        // Get current user from security context if userId is not provided
        User user;
        if (requestDTO.getUserId() != null) {
            user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            
            // Check if current user has permission to create for another user
            checkAdminPermission();
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();
            user = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));
        }

        // Validate that at least one of initialDeclarationId or userAdHocDeclareId is provided
        if (requestDTO.getInitialDeclarationId() == null && requestDTO.getUserAdHocDeclareId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Either initialDeclarationId or userAdHocDeclareId must be provided");
        }

        // Validate that the exclude reason is provided
        if (requestDTO.getExcludeReason() == null || requestDTO.getExcludeReason().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exclude reason is required");
        }

        // Set up the UserAdHocExclude entity
        UserAdHocExclude exclude = UserAdHocExclude.builder()
                .user(user)
                .excludeReason(requestDTO.getExcludeReason())
                .createdAt(LocalDateTime.now())
                .status(requestDTO.getStatus() != null ? requestDTO.getStatus() : UserDeclarationStatus.CREATED)
                .isDeleted(false)
                .isConfirmed(requestDTO.getIsConfirmed() != null ? requestDTO.getIsConfirmed() : false)
                .confirmedAgreements(requestDTO.getConfirmedAgreements() != null ? requestDTO.getConfirmedAgreements() : false)
                .agreements_details(requestDTO.getAgreementsDetails())
                .build();

        // Set the initial declaration if provided
        if (requestDTO.getInitialDeclarationId() != null) {
            UserInitialDeclaration initialDeclaration = userInitialDeclarationRepository.findById(requestDTO.getInitialDeclarationId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Initial declaration not found"));
            
            // Verify that a conflict actually exists for this initial declaration
            if (!conflictExistsForInitialDeclaration(user.getId(), initialDeclaration.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "No conflict exists for this user and initial declaration");
            }
            
            exclude.setUserInitialDeclaration(initialDeclaration);
        }

        // Set the ad hoc declaration if provided
        if (requestDTO.getUserAdHocDeclareId() != null) {
            UserAdHocDeclare adHocDeclare = userAdHocDeclareRepository.findById(requestDTO.getUserAdHocDeclareId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad hoc declaration not found"));
            
            // Verify that a conflict actually exists for this ad hoc declaration
            if (!conflictExistsForAdHocDeclare(user.getId(), adHocDeclare.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "No conflict exists for this user and ad hoc declaration");
            }
            
            exclude.setUserAdHocDeclare(adHocDeclare);
        }

        // Save and return the exclude
        exclude = userAdHocExcludeRepository.save(exclude);
        return convertToDTO(exclude);
    }

    @Override
    public UserAdHocExcludeDTO getExclusionById(Integer id) {
        UserAdHocExclude exclude = userAdHocExcludeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exclusion not found"));

        // Check if user has permission to view this exclusion
        checkPermission(exclude.getUser().getId());

        return convertToDTO(exclude);
    }

    @Override
    public List<UserAdHocExcludeDTO> getCurrentUserExclusions() {
        // Get current user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        return userAdHocExcludeRepository.findByUserIdAndIsDeletedFalse(currentUser.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAdHocExcludeDTO> getUserExclusions(Integer userId) {
        // Check if user has permission to view exclusions for this user
        checkPermission(userId);

        return userAdHocExcludeRepository.findByUserIdAndIsDeletedFalse(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAdHocExcludeDTO> getUserExclusionsByStatus(Integer userId, UserDeclarationStatus status) {
        // Check if user has permission to view exclusions for this user
        checkPermission(userId);

        return userAdHocExcludeRepository.findByUserIdAndStatusAndIsDeletedFalse(userId, status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAdHocExcludeDTO> getExclusionsByInitialDeclarationId(Integer initialDeclarationId) {
        // Find the initial declaration to check ownership
        UserInitialDeclaration declaration = userInitialDeclarationRepository.findById(initialDeclarationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Initial declaration not found"));

        // Check if user has permission to view exclusions for this declaration
        checkPermission(declaration.getUser().getId());

        return userAdHocExcludeRepository.findByUserInitialDeclarationIdAndIsDeletedFalse(initialDeclarationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAdHocExcludeDTO> getExclusionsByAdHocDeclareId(Integer adHocDeclareId) {
        // Find the ad hoc declaration to check ownership
        UserAdHocDeclare adHocDeclare = userAdHocDeclareRepository.findById(adHocDeclareId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad hoc declaration not found"));

        // Check if user has permission to view exclusions for this ad hoc declaration
        checkPermission(adHocDeclare.getUser().getId());

        return userAdHocExcludeRepository.findByUserAdHocDeclareIdAndIsDeletedFalse(adHocDeclareId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserAdHocExcludeDTO updateExclusion(Integer id, UserAdHocExcludeRequestDTO requestDTO) {
        UserAdHocExclude exclude = userAdHocExcludeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exclusion not found"));

        // Check if user has permission to update this exclusion
        checkPermission(exclude.getUser().getId());

        // Update fields if provided
        if (requestDTO.getExcludeReason() != null && !requestDTO.getExcludeReason().trim().isEmpty()) {
            exclude.setExcludeReason(requestDTO.getExcludeReason());
        }

        if (requestDTO.getStatus() != null) {
            exclude.setStatus(requestDTO.getStatus());
        }

        if (requestDTO.getIsConfirmed() != null) {
            exclude.setIsConfirmed(requestDTO.getIsConfirmed());
        }

        if (requestDTO.getConfirmedAgreements() != null) {
            exclude.setConfirmedAgreements(requestDTO.getConfirmedAgreements());
        }

        if (requestDTO.getAgreementsDetails() != null) {
            exclude.setAgreements_details(requestDTO.getAgreementsDetails());
        }

        // Save and return the updated exclusion
        exclude = userAdHocExcludeRepository.save(exclude);
        return convertToDTO(exclude);
    }

    @Override
    @Transactional
    public UserAdHocExcludeDTO confirmExclusion(Integer id) {
        UserAdHocExclude exclude = userAdHocExcludeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exclusion not found"));

        // Check if user has permission to confirm this exclusion
        checkAdminPermission(); // Only admins can confirm exclusions

        // Update the confirmation status
        exclude.setIsConfirmed(true);
        exclude.setStatus(UserDeclarationStatus.APPROVED);

        // Save and return the updated exclusion
        exclude = userAdHocExcludeRepository.save(exclude);
        return convertToDTO(exclude);
    }

    @Override
    @Transactional
    public boolean deleteExclusion(Integer id) {
        UserAdHocExclude exclude = userAdHocExcludeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exclusion not found"));

        // Check if user has permission to delete this exclusion
        checkPermission(exclude.getUser().getId());

        // Soft delete the exclusion
        exclude.setIsDeleted(true);
        userAdHocExcludeRepository.save(exclude);
        return true;
    }

    @Override
    public boolean conflictExistsForAdHocDeclare(Integer userId, Integer adHocDeclareId) {
        // Check if the user has any ad hoc declare answers for this ad hoc declaration
        UserAdHocDeclare adHocDeclare = userAdHocDeclareRepository.findById(adHocDeclareId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad hoc declaration not found"));
        
        // Get answers for this ad hoc declaration
        List<UserAdHocDeclareAnswer> answers = userAdHocDeclareAnswerRepository.findByUserAdHocDeclareId(adHocDeclareId);
        
        // If there are answers, a conflict exists
        return !answers.isEmpty();
    }

    @Override
    public boolean conflictExistsForInitialDeclaration(Integer userId, Integer initialDeclarationId) {
        // Check if the user has any declaration answers for this initial declaration
        UserInitialDeclaration initialDeclaration = userInitialDeclarationRepository.findById(initialDeclarationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Initial declaration not found"));
        
        // Get answers for this initial declaration
        List<UserDeclarationAnswer> answers = userDeclarationAnswerRepository.findByUserDeclarationId(initialDeclarationId);
        
        // Check if any answers have conflicts
        return answers.stream().anyMatch(UserDeclarationAnswer::getHasConflict);
    }

    /**
     * Convert a UserAdHocExclude entity to a DTO
     */
    private UserAdHocExcludeDTO convertToDTO(UserAdHocExclude exclude) {
        String userFullName = exclude.getUser().getFirstname() + " " + exclude.getUser().getLastname();
        
        return UserAdHocExcludeDTO.builder()
                .id(exclude.getId())
                .userId(exclude.getUser().getId())
                .userFullName(userFullName)
                .initialDeclarationId(exclude.getUserInitialDeclaration() != null ? exclude.getUserInitialDeclaration().getId() : null)
                .userAdHocDeclareId(exclude.getUserAdHocDeclare() != null ? exclude.getUserAdHocDeclare().getId() : null)
                .excludeReason(exclude.getExcludeReason())
                .createdAt(exclude.getCreatedAt())
                .status(exclude.getStatus())
                .isDeleted(exclude.getIsDeleted())
                .isConfirmed(exclude.getIsConfirmed())
                .confirmedAgreements(exclude.getConfirmedAgreements())
                .agreementsDetails(exclude.getAgreements_details())
                .build();
    }

    /**
     * Check if the current user has permission to access/modify the data for a specific user
     */
    private void checkPermission(Integer ownerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        // Allow access if the user is the owner or has admin/manager role
        if (!currentUser.getId().equals(ownerId) && 
            !currentUser.getRole().name().equals("SUPER_ADMIN") && 
            !currentUser.getRole().name().equals("ADMIN") && 
            !currentUser.getRole().name().equals("MANAGER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this data");
        }
    }

    /**
     * Check if the current user has admin permissions
     */
    private void checkAdminPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        // Allow access if the user has admin/manager role
        if (!currentUser.getRole().name().equals("SUPER_ADMIN") && 
            !currentUser.getRole().name().equals("ADMIN") && 
            !currentUser.getRole().name().equals("MANAGER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to perform this action");
        }
    }
}