package com.student.edsbackend.features.declaration.answers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;

/**
 * Repository for accessing UserAdHocDeclareAnswer entities
 */
@Repository
public interface UserAdHocDeclareAnswerRepository extends JpaRepository<UserAdHocDeclareAnswer, Integer> {
    
    /**
     * Find all answers for a specific ad hoc declaration
     * 
     * @param userAdHocDeclareId the ID of the ad hoc declaration
     * @return list of answers for the specified ad hoc declaration
     */
    @Query("SELECT a FROM UserAdHocDeclareAnswer a WHERE a.userAdHocDeclare.id = :userAdHocDeclareId")
    List<UserAdHocDeclareAnswer> findByUserAdHocDeclareId(@Param("userAdHocDeclareId") Integer userAdHocDeclareId);
    
    /**
     * Find all answers for a specific user's ad hoc declarations
     * 
     * @param userId the ID of the user
     * @return list of answers for the specified user's ad hoc declarations
     */
    @Query("SELECT a FROM UserAdHocDeclareAnswer a WHERE a.userAdHocDeclare.user.id = :userId")
    List<UserAdHocDeclareAnswer> findByUserId(@Param("userId") Integer userId);
    
    /**
     * Find all answers by category
     * 
     * @param categoryId the ID of the category
     * @return list of answers for the specified category
     */
    List<UserAdHocDeclareAnswer> findByCategoryId(Integer categoryId);
}