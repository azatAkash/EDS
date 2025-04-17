package com.student.edsbackend.features.management.service.implementations;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.enums.ManagementPlanStatus;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.management.ManagementPlanAction;
import com.student.edsbackend.features.management.UserManagementPlan;
import com.student.edsbackend.features.management.dto.UserManagementPlanDTO;
import com.student.edsbackend.features.management.dto.UserManagementPlanRequestDTO;
import com.student.edsbackend.features.management.repository.ManagementPlanActionRepository;
import com.student.edsbackend.features.management.repository.UserManagementPlanRepository;
import com.student.edsbackend.features.management.service.UserManagementPlanService;
import com.student.edsbackend.features.user.dal.Role;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclaration;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationRepository;
import com.student.edsbackend.features.user.dal.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ObjectInputFilter.Status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManagementPlanServiceImpl implements UserManagementPlanService {

    private final UserManagementPlanRepository managementPlanRepository;
    private final UserRepository userRepository;
    private final UserInitialDeclarationRepository userDeclarationRepository;
    private final ManagementPlanActionRepository actionRepository; // ✅ Added

    @Override
    public Optional<UserManagementPlanDTO> getManagementPlanById(Integer id) {
        Optional<UserManagementPlan> managementPlan = managementPlanRepository.findByIdAndIsDeletedFalse(id);
        
        if (managementPlan.isPresent() && !hasAccessToManagementPlan(managementPlan.get())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this management plan");
        }
        
        return managementPlan.map(this::mapToDTO);
    }

    @Override
    public List<UserManagementPlanDTO> getAllManagementPlans() {
        User currentUser = getCurrentUser();
        
        // Super admin, admin, and manager can see all management plans
        if (currentUser.getRole() == Role.SUPER_ADMIN || 
            currentUser.getRole() == Role.ADMIN || 
            currentUser.getRole() == Role.MANAGER) {
            return managementPlanRepository.findByIsDeletedFalse().stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }
        
        // Regular users can only see their own management plans
        return getCurrentUserManagementPlans();
    }

    @Override
    public List<UserManagementPlanDTO> getCurrentUserManagementPlans() {
        User currentUser = getCurrentUser();
        
        return managementPlanRepository.findByUserDeclaration_UserIdOrAdHoc_UserIdAndIsDeletedFalse(
                currentUser.getId(), currentUser.getId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserManagementPlanDTO createManagementPlan(UserManagementPlanRequestDTO requestDTO) {
        // Validate that either userDeclarationId or adHocId is provided
        if ((requestDTO.getUserDeclarationId() == null && requestDTO.getAdHocId() == null) ||
            (requestDTO.getUserDeclarationId() != null && requestDTO.getAdHocId() != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Either userDeclarationId or adHocId must be provided, but not both");
        }
        
        User currentUser = getCurrentUser();
        UserManagementPlan managementPlan = new UserManagementPlan();
        if(requestDTO.getUserDeclarationId() != null && requestDTO.getAdHocId()!= null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Either userDeclarationId or adHocId must be provided, but not both");
        }
        // Set user declaration if provided
        if (requestDTO.getUserDeclarationId() != null) {
            UserInitialDeclaration userDeclaration = userDeclarationRepository.findById(requestDTO.getUserDeclarationId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                            "User declaration not found with id: " + requestDTO.getUserDeclarationId()));
            
            
            if (userDeclaration.getStatus() != UserDeclarationStatus.SENT_FOR_APPROVAL){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "User declaration must be in SENT_FOR_APPROVAL status");
            }
            managementPlan.setUserDeclaration(userDeclaration);
        }
        
        // Set ad hoc if provided
        if (requestDTO.getAdHocId() != null) {
            // Assuming there's a repository for UserAdHocDeclare
            // This would need to be injected as a dependency
            // UserAdHocDeclare adHoc = adHocRepository.findById(requestDTO.getAdHocId())
            //         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            //                 "Ad hoc declaration not found with id: " + requestDTO.getAdHocId()));
            // managementPlan.setAdHoc(adHoc);
            
            // For now, we'll just set the ID and let JPA handle the relationship
            UserAdHocDeclare adHoc = new UserAdHocDeclare();
            adHoc.setId(requestDTO.getAdHocId());
            managementPlan.setAdHoc(adHoc);
        }


        if (requestDTO.getActionRequired() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Action required must be provided");
        }

        if (requestDTO.getActionRequired()){
            if (requestDTO.getActionId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Action ID must be provided if action is required");
            }
        } 
        // Set other fields
        managementPlan.setCreationDate(LocalDateTime.now());
        managementPlan.setCreatedBy(currentUser);
        managementPlan.setResponsible(currentUser);
        managementPlan.setStatus(ManagementPlanStatus.SENT_FOR_CONFIRMATION);
        managementPlan.setIsDeleted(false);
        managementPlan.setIsAmended(false);
        managementPlan.setActionRequired(requestDTO.getActionRequired());
        
        if (requestDTO.getActionId() != null) {
            ManagementPlanAction action = actionRepository.findByIdAndIsDeletedFalse(requestDTO.getActionId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Active action not found with id: " + requestDTO.getActionId()));
        }
        
        managementPlan.setActionDetails(requestDTO.getActionDetails());
        managementPlan.setExecutionDate(requestDTO.getExecutionDate());
        managementPlan.setNotificationDate(LocalDateTime.now());
        managementPlan.setEnsuredByManager(true);
        // Save and return
        UserManagementPlan savedPlan = managementPlanRepository.save(managementPlan);
        return mapToDTO(savedPlan);
    }

    @Override
    public boolean deleteManagementPlan(Integer id) {
        Optional<UserManagementPlan> managementPlanOpt = managementPlanRepository.findByIdAndIsDeletedFalse(id);
        
        if (managementPlanOpt.isEmpty()) {
            return false;
        }
        
        UserManagementPlan managementPlan = managementPlanOpt.get();
        
        // Check if user has access to delete
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != Role.SUPER_ADMIN && 
            currentUser.getRole() != Role.ADMIN && currentUser.getRole()!= Role.MANAGER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to delete this management plan");
        }
        
        // Soft delete
        managementPlan.setIsDeleted(true);
        managementPlanRepository.save(managementPlan);
        return true;
    }

    @Override
    public boolean ammendManagementPlan(Integer id) {
        Optional<UserManagementPlan> managementPlanOpt = managementPlanRepository.findByIdAndIsDeletedFalse(id);
        
        if (managementPlanOpt.isEmpty()) {
            return false;
        }
        
        UserManagementPlan managementPlan = managementPlanOpt.get();
        
        // Check if user has access to delete
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != Role.SUPER_ADMIN && 
            currentUser.getRole() != Role.ADMIN && currentUser.getRole()!= Role.MANAGER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to delete this management plan");
        }
        
        // Soft delete
        managementPlan.setIsAmended(true);
        managementPlanRepository.save(managementPlan);
        return true;
    }

    @Override
    public boolean hasAccessToManagementPlan(UserManagementPlan managementPlan) {
        User currentUser = getCurrentUser();
        
        // Super admin, admin, and manager have access to all management plans
        if (currentUser.getRole() == Role.SUPER_ADMIN || 
            currentUser.getRole() == Role.ADMIN || 
            currentUser.getRole() == Role.MANAGER) {
            return true;
        }
    
        
        // Check if the current user is the user assigned to this management plan
        if (managementPlan.getUserDeclaration() != null && 
            managementPlan.getUserDeclaration().getUser().getId().equals(currentUser.getId())) {
            return true;
        }
        
        if (managementPlan.getAdHoc() != null && 
            managementPlan.getAdHoc().getUser().getId().equals(currentUser.getId())) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Get the current authenticated user
     * @return the current user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));
    }
    
    /**
     * Maps a UserManagementPlan entity to its DTO representation
     * @param managementPlan the entity to map
     * @return the mapped DTO
     */
    private UserManagementPlanDTO mapToDTO(UserManagementPlan managementPlan) {
        UserDTO createdByDTO = null;
        if (managementPlan.getCreatedBy() != null) {
            createdByDTO = UserDTO.builder()
                    .id(managementPlan.getCreatedBy().getId())
                    .email(managementPlan.getCreatedBy().getEmail())
                    .firstname(managementPlan.getCreatedBy().getFirstname())
                    .lastname(managementPlan.getCreatedBy().getLastname())
                    .middlename(managementPlan.getCreatedBy().getMiddlename())
                    .role(managementPlan.getCreatedBy().getRole())
                    .position(managementPlan.getCreatedBy().getPosition())
                    .department(managementPlan.getCreatedBy().getDepartment())
                    .build();
        }
        
        return UserManagementPlanDTO.builder()
                .id(managementPlan.getId())
                .userDeclarationId(managementPlan.getUserDeclaration() != null ? 
                        managementPlan.getUserDeclaration().getId() : null)
                .adHocId(managementPlan.getAdHoc() != null ? 
                        managementPlan.getAdHoc().getId() : null)
                .creationDate(managementPlan.getCreationDate())
                .createdBy(createdByDTO)
                .status(managementPlan.getStatus())
                .isDeleted(managementPlan.getIsDeleted())
                .isAmended(managementPlan.getIsAmended())
                .actionRequired(managementPlan.getActionRequired())
                .actionId(managementPlan.getAction() != null ? 
                        managementPlan.getAction().getId() : null)
                .actionDetails(managementPlan.getActionDetails())
                .executionDate(managementPlan.getExecutionDate())
                .notificationDate(managementPlan.getNotificationDate())
                .confirmationDate(managementPlan.getConfirmationDate())
                .reasonNonExecution(managementPlan.getReasonNonExecution())
                .acknowledgedByUser(managementPlan.getAcknowledgedByUser())
                .ensuredByManager(managementPlan.getEnsuredByManager())
                .userDisagreementReason(managementPlan.getUserDisagreementReason())
                .build();
    }
}