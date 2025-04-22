package com.student.edsbackend.features.declaration.agreement.service.implementation;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocExclude;
import com.student.edsbackend.features.declaration.adhoc.repository.UserAdHocExcludeRepository;
import com.student.edsbackend.features.declaration.agreement.DecAgreementStatement;
import com.student.edsbackend.features.declaration.agreement.UserDecAgreementAnswer;
import com.student.edsbackend.features.declaration.agreement.dto.UserDecAgreementAnswerDeclareDTO;
import com.student.edsbackend.features.declaration.agreement.dto.UserDecAgreementAnswerExcludeDTO;
import com.student.edsbackend.features.declaration.agreement.dto.UserDecAgreementAnswerRequestDTO;
import com.student.edsbackend.features.declaration.agreement.repository.DecAgreementStatementRepository;
import com.student.edsbackend.features.declaration.agreement.repository.UserDecAgreementAnswerRepository;
import com.student.edsbackend.features.declaration.agreement.service.UserDecAgreementAnswerService;
import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswer;
import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswerRepository;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDecAgreementAnswerServiceImpl implements UserDecAgreementAnswerService {

    private final UserDecAgreementAnswerRepository answerRepo;
    private final UserRepository userRepo;
    private final UserAdHocDeclareAnswerRepository adHocDeclareRepo;
    private final UserAdHocExcludeRepository adHocExcludeRepo;
    private final DecAgreementStatementRepository stmtRepo;

    @Override
    @Transactional
    public UserDecAgreementAnswerDeclareDTO create(UserDecAgreementAnswerRequestDTO request) {
        User user = resolveUser(request.getUserId());
        validateRequest(request);

        UserAdHocDeclareAnswer declareAns = request.getAdHocDeclareAnswerId() != null
            ? adHocDeclareRepo.findById(request.getAdHocDeclareAnswerId())
                              .orElseThrow(() -> new ResponseStatusException(
                                      HttpStatus.NOT_FOUND, "Ad hoc declare answer not found"))
            : null;

        UserAdHocExclude exclude = request.getAdHocExcludeId() != null
            ? adHocExcludeRepo.findById(request.getAdHocExcludeId())
                              .orElseThrow(() -> new ResponseStatusException(
                                      HttpStatus.NOT_FOUND, "Ad hoc exclude not found"))
            : null;

        if (declareAns != null && exclude != null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "You can't provide both adHocDeclareAnswerId and adHocExcludeId"
            );
        }

        Integer firstStmtId = request.getAgreementStatementIds().get(0);
        UserDecAgreementAnswer firstSaved = null;

        for (Integer stmtId : request.getAgreementStatementIds()) {
            DecAgreementStatement stmt = stmtRepo.findByIdAndIsDeletedFalse(stmtId)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Agreement statement not found: " + stmtId));

            UserDecAgreementAnswer ans = UserDecAgreementAnswer.builder()
                .user(user)
                .agreementStatement(stmt)
                .isAgreed(request.getIsAgreed())
                .isDeleted(false)
                .build();

            if (declareAns != null) ans.setUserAdHocDeclareAnswer(declareAns);
            if (exclude    != null) ans.setUserAdHocExclude(exclude);

            ans = answerRepo.save(ans);
            if (stmtId.equals(firstStmtId)) {
                firstSaved = ans;
            }
        }

        return convertToDeclareDTO(firstSaved);
    }

    @Override
    public List<UserDecAgreementAnswerDeclareDTO> getAllDeclare() {
        checkAdminPermission();
        return answerRepo.findAll().stream()
                .filter(a -> !a.getIsDeleted() && a.getUserAdHocDeclareAnswer() != null)
                .map(this::convertToDeclareDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDecAgreementAnswerExcludeDTO> getAllExclude() {
        checkAdminPermission();
        return answerRepo.findAll().stream()
                .filter(a -> !a.getIsDeleted() && a.getUserAdHocExclude() != null)
                .map(this::convertToExcludeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDecAgreementAnswerDeclareDTO getById(Integer id) {
        UserDecAgreementAnswer ans = answerRepo.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Agreement answer not found"));
        checkPermission(ans.getUser().getId());
        return convertToDeclareDTO(ans);
    }

    @Override
    public List<UserDecAgreementAnswerDeclareDTO> getByUserId(Integer userId) {
        checkPermission(userId);
        return answerRepo.findByUserIdAndIsDeletedFalse(userId).stream()
                .map(this::convertToDeclareDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDecAgreementAnswerDeclareDTO> getAdHocDeclareAnswerByUserId(Integer adHocDeclareAnswerId) {
        UserAdHocDeclareAnswer declareAns = adHocDeclareRepo.findById(adHocDeclareAnswerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Ad hoc declare answer not found"));
        checkPermission(declareAns.getUserAdHocDeclare().getUser().getId());
        return answerRepo.findByUserAdHocDeclareAnswerIdAndIsDeletedFalse(adHocDeclareAnswerId).stream()
                .map(this::convertToDeclareDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDecAgreementAnswerExcludeDTO> getAdHocExcludeAnswerByUserId(Integer adHocExcludeId) {
        UserAdHocExclude exclude = adHocExcludeRepo.findById(adHocExcludeId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Ad hoc exclude not found"));
        checkPermission(exclude.getUser().getId());
        return answerRepo.findByUserAdHocExcludeIdAndIsDeletedFalse(adHocExcludeId).stream()
                .map(this::convertToExcludeDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        UserDecAgreementAnswer ans = answerRepo.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Agreement answer not found"));
        checkPermission(ans.getUser().getId());
        ans.setIsDeleted(true);
        answerRepo.save(ans);
        return true;
    }

    // ——— Converters —————————————————————————————————————————————————————

    private UserDecAgreementAnswerDeclareDTO convertToDeclareDTO(UserDecAgreementAnswer ans) {
        return UserDecAgreementAnswerDeclareDTO.builder()
            .id(ans.getId())
            .userId(ans.getUser().getId())
            .adHocDeclareAnswerId(
                ans.getUserAdHocDeclareAnswer() != null
                    ? ans.getUserAdHocDeclareAnswer().getId()
                    : null)
            .agreeIdandDescription(Map.of(
                ans.getAgreementStatement().getId(),
                ans.getAgreementStatement().getDescription().get("en")))
            .isAgreed(ans.getIsAgreed())
            .build();
    }

    private UserDecAgreementAnswerExcludeDTO convertToExcludeDTO(UserDecAgreementAnswer ans) {
        return UserDecAgreementAnswerExcludeDTO.builder()
            .id(ans.getId())
            .userId(ans.getUser().getId())
            .adHocExcludeId(
                ans.getUserAdHocExclude() != null
                    ? ans.getUserAdHocExclude().getId()
                    : null)
            .agreeIdandDescription(Map.of(
                ans.getAgreementStatement().getId(),
                ans.getAgreementStatement().getDescription().get("en")))
            .isAgreed(ans.getIsAgreed())
            .build();
    }

    // ——— Helpers —————————————————————————————————————————————————————————

    private User resolveUser(Integer maybeUserId) {
        if (maybeUserId != null) {
            checkAdminPermission();
            return userRepo.findById(maybeUserId)
                    .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Current user not found"));
    }

    private void validateRequest(UserDecAgreementAnswerRequestDTO req) {
        if (req.getAgreementStatementIds() == null || req.getAgreementStatementIds().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "At least one agreement statement ID is required"
            );
        }
    }

    private void checkPermission(Integer ownerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Current user not found"));

        if (!current.getId().equals(ownerId) && !isAdminOrManager(current)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You don't have permission to access this data"
            );
        }
    }

    private void checkAdminPermission() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Current user not found"));

        if (!isAdminOrManager(current)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You don't have permission to perform this action"
            );
        }
    }

    private boolean isAdminOrManager(User user) {
        String role = user.getRole().name();
        return role.equals("SUPER_ADMIN")
            || role.equals("ADMIN")
            || role.equals("MANAGER");
    }
}
