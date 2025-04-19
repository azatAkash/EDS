package com.student.edsbackend.features.declaration.initial.dal.questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InitialDeclarationQuestionRepository extends JpaRepository<InitialDeclarationQuestion, Integer> {
    // Find all non-deleted questions
    List<InitialDeclarationQuestion> findByIsDeletedFalse();

    // Find all questions for a specific declaration that are not deleted
    List<InitialDeclarationQuestion> findByDeclarationIdAndIsDeletedFalse(Integer declarationId);

    List<InitialDeclarationQuestion> findByDeclarationIdAndIsDeletedFalseOrderByOrderNumberAsc(Integer declarationId);

    @Query("SELECT COALESCE(MAX(q.orderNumber), 0) FROM InitialDeclarationQuestion q WHERE q.declaration.id = :declarationId AND q.isDeleted = false")
    short findMaxOrderNumberByDeclarationId(@Param("declarationId") Integer declarationId);

    boolean existsByDeclarationIdAndOrderNumberAndIsDeletedFalse(Integer declarationId, Short orderNumber);
}