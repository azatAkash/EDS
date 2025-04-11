package com.student.edsbackend.features.declaration.initial.dal.option;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InitialDeclarationOptionRepository extends JpaRepository<InitialDeclarationOption, Integer> {
}
