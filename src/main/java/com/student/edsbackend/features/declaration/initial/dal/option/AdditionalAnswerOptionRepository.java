package com.student.edsbackend.features.declaration.initial.dal.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalAnswerOptionRepository extends JpaRepository<AdditionalAnswerOption, Integer> {
}
