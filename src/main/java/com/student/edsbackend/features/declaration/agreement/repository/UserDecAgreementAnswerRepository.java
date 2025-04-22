package com.student.edsbackend.features.declaration.agreement.repository;

import com.student.edsbackend.features.declaration.agreement.UserDecAgreementAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserDecAgreementAnswer entities
 */
@Repository
public interface UserDecAgreementAnswerRepository extends JpaRepository<UserDecAgreementAnswer, Integer> {
    
    /**
     * Find a user declaration agreement answer by ID that is not deleted
     * @param id the ID of the user declaration agreement answer
     * @return an Optional containing the user declaration agreement answer if found
     */
    Optional<UserDecAgreementAnswer> findByIdAndIsDeletedFalse(Integer id);
    
    /**
     * Find all user declaration agreement answers for a specific user that are not deleted
     * @param userId the ID of the user
     * @return a list of all user declaration agreement answers for the user that are not deleted
     */
    List<UserDecAgreementAnswer> findByUserIdAndIsDeletedFalse(Integer userId);
    
       /**
     * Find all user declaration agreement answers for a specific ad hoc declare answer that are not deleted
     * @param adHocDeclareAnswerId the ID of the ad hoc declare answer
     * @return a list of all user declaration agreement answers for the ad hoc declare answer that are not deleted
     */
    List<UserDecAgreementAnswer> findByUserAdHocDeclareAnswerIdAndIsDeletedFalse(Integer UseradHocDeclareAnswerId);
    
    /**
     * Find all user declaration agreement answers for a specific ad hoc exclude that are not deleted
     * @param adHocExcludeId the ID of the ad hoc exclude
     * @return a list of all user declaration agreement answers for the ad hoc exclude that are not deleted
     */
    List<UserDecAgreementAnswer> findByUserAdHocExcludeIdAndIsDeletedFalse(Integer UserAdHocExcludeId);
     
    /**
     * Find all user declaration agreement answers for a specific agreement statement that are not deleted
     * @param agreementStatementId the ID of the agreement statement
     * @return a list of all user declaration agreement answers for the agreement statement that are not deleted
     */
    List<UserDecAgreementAnswer> findByAgreementStatementIdAndIsDeletedFalse(Integer agreementStatementId);
}