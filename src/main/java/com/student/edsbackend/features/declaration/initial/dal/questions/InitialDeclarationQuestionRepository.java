package com.student.edsbackend.features.declaration.initial.dal.questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InitialDeclarationQuestionRepository extends JpaRepository<InitialDeclarationQuestion, Integer> {
    // Find all non-deleted questions
    List<InitialDeclarationQuestion> findByIsDeletedFalse();
    
    // Find all questions for a specific declaration that are not deleted
    List<InitialDeclarationQuestion> findByDeclarationIdAndIsDeletedFalse(Integer declarationId);
}