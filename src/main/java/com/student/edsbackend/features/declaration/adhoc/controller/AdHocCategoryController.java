package com.student.edsbackend.features.declaration.adhoc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.adhoc.dto.AdHocCategoryDTO;
import com.student.edsbackend.features.declaration.adhoc.service.AdHocCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing ad-hoc categories
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ad-hoc-categories")
@Tag(name = "Ad-Hoc Categories", description = "Endpoints for managing ad-hoc categories")
public class AdHocCategoryController {

    private final AdHocCategoryService adHocCategoryService;

    @GetMapping
    @Operation(summary = "Get all ad-hoc categories", 
            description = "Retrieves all ad-hoc categories")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'MANAGER')")
    public ResponseEntity<List<AdHocCategoryDTO>> getAllCategories() {
        List<AdHocCategoryDTO> categories = adHocCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ad-hoc category by ID", 
            description = "Retrieves an ad-hoc category by its ID")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'MANAGER')")
    public ResponseEntity<AdHocCategoryDTO> getCategoryById(@PathVariable Integer id) {
        AdHocCategoryDTO category = adHocCategoryService.getCategoryById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Ad-hoc category not found with id: " + id));
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @Operation(summary = "Create a new ad-hoc category", 
            description = "Creates a new ad-hoc category with multilingual description")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<AdHocCategoryDTO> createCategory(@RequestBody AdHocCategoryDTO categoryDTO) {
        AdHocCategoryDTO createdCategory = adHocCategoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing ad-hoc category", 
            description = "Updates an existing ad-hoc category by its ID")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<AdHocCategoryDTO> updateCategory(
            @PathVariable Integer id, 
            @RequestBody AdHocCategoryDTO categoryDTO) {
        AdHocCategoryDTO updatedCategory = adHocCategoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an ad-hoc category", 
            description = "Deletes an ad-hoc category by its ID")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer id) {
        adHocCategoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse("Ad-hoc category successfully deleted"));
    }
}