package com.student.edsbackend.features.declaration.agreement.repository;

import com.student.edsbackend.features.declaration.agreement.DecAgreementStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DecAgreementStatement entities
 */
@Repository
public interface DecAgreementStatementRepository extends JpaRepository<DecAgreementStatement, Integer> {
    
    /**
     * Find a declaration agreement statement by ID that is not deleted
     * @param id the ID of the declaration agreement statement
     * @return an Optional containing the declaration agreement statement if found
     */
    Optional<DecAgreementStatement> findByIdAndIsDeletedFalse(Integer id);
    
    /**
     * Find all declaration agreement statements that are not deleted
     * @return a list of all declaration agreement statements that are not deleted
     */
    List<DecAgreementStatement> findByIsDeletedFalse();

    @Query("SELECT d.id, d.description FROM DecAgreementStatement d WHERE d.isDeleted = false")
    List<Object[]> findAllDescriptionsAndIds();

}