package com.student.edsbackend.web.service.implementations;

import java.sql.Time;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.edsbackend.dal.declaration.Declaration;
import com.student.edsbackend.dal.declaration.Declaration.DeclarationBuilder;
import com.student.edsbackend.dal.declaration.DeclarationDTO;
import com.student.edsbackend.dal.declaration.DeclarationRepository;
import com.student.edsbackend.dal.enums.DeclarationStatus;
import com.student.edsbackend.dal.user.User;
import com.student.edsbackend.web.service.DeclarationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeclarationServiceImpl implements DeclarationService {

    private final DeclarationRepository declarationRepository;
    private final ObjectMapper objectMapper; // injected by Spring

    /**
     * For a regular user, obtain the user id from the SecurityContext (JWT) and
     * return that user's declaration. Since we've enforced a unique constraint,
     * the repository returns at most one declaration.
     */
    @Override
    public DeclarationDTO findDeclarationForUser() {
        Integer userId = getCurrentUserId();
        return declarationRepository.findByUserId(userId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No declaration found for user with id: " + userId));
    }

    /**
     * For admin users, return all declarations.
     */
    @Override
    public List<DeclarationDTO> findAllForAdmin() {
        List<Declaration> declarations = declarationRepository.findAll();
        return declarations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Create a new Declaration. Uses the current authenticated user and the
     * provided JSON values.
     */
    @Override
    public void createDeclaration(DeclarationDTO dto) {
        User currentUser = getCurrentUser();
        Declaration.DeclarationBuilder builder = (DeclarationBuilder) Declaration.builder();
        Declaration declaration = builder
                .c1(nonNull(dto.getC1()))
                .c2(nonNull(dto.getC2()))
                .c3(nonNull(dto.getC3()))
                .c4(nonNull(dto.getC4()))
                .c5(nonNull(dto.getC5()))
                .c6(nonNull(dto.getC6()))
                .c7(nonNull(dto.getC7()))
                .c8(nonNull(dto.getC8()))
                .c9(nonNull(dto.getC9()))
                .c10(nonNull(dto.getC10()))
                .createdAt(LocalDateTime.now())
                .managerId(currentUser.getId())
                .status(DeclarationStatus.CREATED)
                .user(currentUser)
                .build();
        declarationRepository.save(declaration);
    }

    /**
     * Update an existing Declaration with new JSON values. The user association
     * remains unchanged.
     */
    @Override
    public void updateDeclaration(DeclarationDTO dto) {
        Optional<Declaration> optional = declarationRepository.findById(dto.getId());
        if (optional.isPresent()) {
            Declaration declaration = optional.get();
            declaration.setC1(nonNull(dto.getC1()));
            declaration.setC2(nonNull(dto.getC2()));
            declaration.setC3(nonNull(dto.getC3()));
            declaration.setC4(nonNull(dto.getC4()));
            declaration.setC5(nonNull(dto.getC5()));
            declaration.setC6(nonNull(dto.getC6()));
            declaration.setC7(nonNull(dto.getC7()));
            declaration.setC8(nonNull(dto.getC8()));
            declaration.setC9(nonNull(dto.getC9()));
            declaration.setC10(nonNull(dto.getC10()));
            declarationRepository.save(declaration);
        } else {
            throw new RuntimeException("Declaration not found with id: " + dto.getId());
        }
    }

    @Override
    public void deleteDeclaration(Integer id) {
        if (declarationRepository.existsById(id)) {
            declarationRepository.deleteById(id);
        } else {
            throw new RuntimeException("Declaration not found with id: " + id);
        }
    }

    // Helper method: if the given JsonNode is not null, return it; otherwise, return an empty JSON object.
    private JsonNode nonNull(JsonNode node) {
        return node != null ? node : objectMapper.createObjectNode();
    }

    // Helper method to convert a Declaration entity to a DeclarationDTO.
    private DeclarationDTO convertToDto(Declaration d) {
        return DeclarationDTO.builder()
                .id(d.getId())
                .c1(d.getC1())
                .c2(d.getC2())
                .c3(d.getC3())
                .c4(d.getC4())
                .c5(d.getC5())
                .c6(d.getC6())
                .c7(d.getC7())
                .c8(d.getC8())
                .c9(d.getC9())
                .c10(d.getC10())
                .build();
    }

    // Helper method to get the current user's id from the security context.
    private Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return ((User) auth.getPrincipal()).getId();
        }
        throw new RuntimeException("User is not authenticated or invalid principal");
    }

    // Helper method to get the current User object from the security context.
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        throw new RuntimeException("User is not authenticated or invalid principal");
    }
}
