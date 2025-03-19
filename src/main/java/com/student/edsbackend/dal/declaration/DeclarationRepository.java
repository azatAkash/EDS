package com.student.edsbackend.dal.declaration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Integer> {

    Optional<Declaration> findByUserId(Integer userId);
}
