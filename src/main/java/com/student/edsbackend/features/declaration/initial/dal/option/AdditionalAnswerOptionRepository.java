package com.student.edsbackend.features.declaration.initial.dal.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdditionalAnswerOptionRepository extends JpaRepository<AdditionalAnswerOption, Integer> {
    List<AdditionalAnswerOption> findByOptionIdAndIsDeletedFalse(Integer optionId);
}
