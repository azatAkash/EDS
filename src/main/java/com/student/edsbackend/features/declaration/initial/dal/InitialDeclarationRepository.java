package com.student.edsbackend.features.declaration.initial.dal;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InitialDeclarationRepository extends JpaRepository<InitialDeclaration, Integer> {
}
