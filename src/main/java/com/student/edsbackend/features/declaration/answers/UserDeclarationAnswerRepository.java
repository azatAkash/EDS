package com.student.edsbackend.features.declaration.answers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing UserDeclarationAnswer entities
 */
@Repository
public interface UserDeclarationAnswerRepository extends JpaRepository<UserDeclarationAnswer, Integer> {
    
    /**
     * Check if any answers exist for a specific question (through its options)
     * 
     * @param questionId the ID of the question to check
     * @return true if any answers exist for the question, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM UserDeclarationAnswer a " +
           "JOIN a.option o WHERE o.question.id = :questionId AND a.isDeleted = false")
    boolean existsAnswersForQuestion(@Param("questionId") Integer questionId);
}