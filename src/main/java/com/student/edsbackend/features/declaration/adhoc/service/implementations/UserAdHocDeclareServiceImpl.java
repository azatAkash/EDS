package com.student.edsbackend.features.declaration.adhoc.service.implementations;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareUpdateDTO;
import com.student.edsbackend.features.declaration.adhoc.repository.UserAdHocDeclareRepository;
import com.student.edsbackend.features.declaration.adhoc.service.UserAdHocDeclareService;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the UserAdHocDeclareService interface.
 */
@Service
@RequiredArgsConstructor
public class UserAdHocDeclareServiceImpl implements UserAdHocDeclareService {

    private final UserAdHocDeclareRepository adHocDeclareRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<UserAdHocDeclareDTO> findById(Integer id) {
        return adHocDeclareRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    public List<UserAdHocDeclareDTO> getAllAdHocDeclarations() {
        return adHocDeclareRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAdHocDeclareDTO> getCurrentUserAdHocDeclarations() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return adHocDeclareRepository.findByUserAndIsDeleted(currentUser, false).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAdHocDeclareDTO> getUserAdHocDeclarations(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return adHocDeclareRepository.findByUserAndIsDeleted(user, false).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAdHocDeclareDTO> getCurrentUserAdHocDeclarationsByStatus(UserDeclarationStatus status) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return adHocDeclareRepository.findByUserAndStatus(currentUser, status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserAdHocDeclareDTO createAdHocDeclaration(UserAdHocDeclareRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get current user from security context for createdBy field
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        if (currentUser.getRole() != null && currentUser.getRole().name().equals("ROLE_USER")) {
            throw new RuntimeException("Only admin can create ad hoc declaration");
        }

        UserAdHocDeclare adHocDeclare = UserAdHocDeclare.builder()
                .user(user)
                .createAt(LocalDateTime.now())
                .isDeleted(false)
                .status(UserDeclarationStatus.CREATED)
                .responsible(currentUser)
                .createAt(LocalDateTime.now())
                .createdBy(currentUser)
                .build();

        UserAdHocDeclare savedAdHocDeclare = adHocDeclareRepository.save(adHocDeclare);
        return mapToDTO(savedAdHocDeclare);
    }

    @Override
    @Transactional
    public UserAdHocDeclareDTO updateAdHocDeclarationStatus(Integer id, UserAdHocDeclareUpdateDTO updateDTO) {
        UserAdHocDeclare adHocDeclare = adHocDeclareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad hoc declaration not found"));


        if (updateDTO.getStatus()!= null && updateDTO.getStatus() != UserDeclarationStatus.CREATED) {
            adHocDeclare.setStatus(updateDTO.getStatus());
        }
        
        UserAdHocDeclare updatedAdHocDeclare = adHocDeclareRepository.save(adHocDeclare);
        return mapToDTO(updatedAdHocDeclare);
    }

    @Override
    @Transactional
    public void deleteAdHocDeclaration(Integer id) {
        UserAdHocDeclare adHocDeclare = adHocDeclareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad hoc declaration not found"));

        adHocDeclare.setIsDeleted(true);
        adHocDeclareRepository.save(adHocDeclare);
    }

    /**
     * Maps a UserAdHocDeclare entity to a UserAdHocDeclareDTO.
     *
     * @param adHocDeclare the entity to map
     * @return the mapped DTO
     */
    private UserAdHocDeclareDTO mapToDTO(UserAdHocDeclare adHocDeclare) {
        UserDTO userDTO = null;
        if (adHocDeclare.getUser() != null) {
            User user = adHocDeclare.getUser();
            userDTO = UserDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .build();
        }

        // Map responsible user to DTO
        UserDTO responsibleDTO = null;
        if (adHocDeclare.getResponsible() != null) {
            User responsible = adHocDeclare.getResponsible();
            responsibleDTO = UserDTO.builder()
                    .id(responsible.getId())
                    .email(responsible.getEmail())
                    .firstname(responsible.getFirstname())
                    .lastname(responsible.getLastname())
                    .build();
        }

        // Map createdBy user to DTO
        UserDTO createdByDTO = null;
        if (adHocDeclare.getCreatedBy() != null) {
            User createdBy = adHocDeclare.getCreatedBy();
            createdByDTO = UserDTO.builder()
                    .id(createdBy.getId())
                    .email(createdBy.getEmail())
                    .firstname(createdBy.getFirstname())
                    .lastname(createdBy.getLastname())
                    .build();
        }

        List<Integer> answerIds = adHocDeclare.getAnswers() != null ?
                adHocDeclare.getAnswers().stream().map(answer -> answer.getId()).collect(Collectors.toList()) :
                null;

        List<Integer> managementPlanIds = adHocDeclare.getManagementPlans() != null ?
                adHocDeclare.getManagementPlans().stream().map(plan -> plan.getId()).collect(Collectors.toList()) :
                null;

        List<Integer> adHocExcludeIds = adHocDeclare.getAdHocExcludes() != null ?
                adHocDeclare.getAdHocExcludes().stream().map(exclude -> exclude.getId()).collect(Collectors.toList()) :
                null;

        return UserAdHocDeclareDTO.builder()
                .id(adHocDeclare.getId())
                .user(userDTO)
                .createAt(adHocDeclare.getCreateAt())
                .isDeleted(adHocDeclare.getIsDeleted())
                .responsible(responsibleDTO)
                .createdBy(createdByDTO)
                .status(adHocDeclare.getStatus())
                .answerIds(answerIds)
                .managementPlanIds(managementPlanIds)
                .adHocExcludeIds(adHocExcludeIds)
                .build();
    }
}