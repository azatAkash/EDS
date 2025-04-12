package com.student.edsbackend.features.declaration.answers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for accessing UserDeclarationAdditionalAnswer entities
 */
@Repository
public interface UserDeclarationAdditionalAnswerRepository extends JpaRepository<UserDeclarationAdditionalAnswer, Integer> {
    
    /**
     * Find all additional answers by user declaration answer ID
     * 
     * @param userDeclarationAnswerId the ID of the user declaration answer
     * @return a list of additional answers for the given user declaration answer
     */
    List<UserDeclarationAdditionalAnswer> findByUserDeclarationAnswerIdAndIsDeletedFalse(Integer userDeclarationAnswerId);
}