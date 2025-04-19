package com.student.edsbackend.features.declaration.adhoc.service;

import java.util.List;
import java.util.Optional;

import com.student.edsbackend.features.declaration.adhoc.dto.AdHocCategoryDTO;

/**
 * Service interface for managing ad-hoc categories
 */
public interface AdHocCategoryService {
    
    /**
     * Get all ad-hoc categories
     * 
     * @return List of all ad-hoc categories
     */
    List<AdHocCategoryDTO> getAllCategories();
    
    /**
     * Get an ad-hoc category by ID
     * 
     * @param id The ID of the category to retrieve
     * @return The category if found
     */
    Optional<AdHocCategoryDTO> getCategoryById(Integer id);
    
    /**
     * Create a new ad-hoc category
     * 
     * @param categoryDTO The category data to create
     * @return The created category
     */
    AdHocCategoryDTO createCategory(AdHocCategoryDTO categoryDTO);
    
    /**
     * Update an existing ad-hoc category
     * 
     * @param id The ID of the category to update
     * @param categoryDTO The updated category data
     * @return The updated category
     */
    AdHocCategoryDTO updateCategory(Integer id, AdHocCategoryDTO categoryDTO);
    
    /**
     * Delete an ad-hoc category
     * 
     * @param id The ID of the category to delete
     */
    void deleteCategory(Integer id);
}