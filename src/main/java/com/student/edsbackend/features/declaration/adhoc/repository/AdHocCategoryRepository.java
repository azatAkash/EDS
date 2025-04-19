package com.student.edsbackend.features.declaration.adhoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.student.edsbackend.features.declaration.adhoc.AdHocCategory;

/**
 * Repository for accessing AdHocCategory entities
 */
@Repository
public interface AdHocCategoryRepository extends JpaRepository<AdHocCategory, Integer> {
    // Add custom query methods if needed
}