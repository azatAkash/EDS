package com.student.edsbackend.features.declaration.adhoc.service.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.features.declaration.adhoc.AdHocCategory;
import com.student.edsbackend.features.declaration.adhoc.dto.AdHocCategoryDTO;
import com.student.edsbackend.features.declaration.adhoc.repository.AdHocCategoryRepository;
import com.student.edsbackend.features.declaration.adhoc.service.AdHocCategoryService;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of the AdHocCategoryService interface
 */
@Service
@RequiredArgsConstructor
public class AdHocCategoryServiceImpl implements AdHocCategoryService {

    private final AdHocCategoryRepository adHocCategoryRepository;

    @Override
    public List<AdHocCategoryDTO> getAllCategories() {
        return adHocCategoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AdHocCategoryDTO> getCategoryById(Integer id) {
        return adHocCategoryRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    public AdHocCategoryDTO createCategory(AdHocCategoryDTO categoryDTO) {
        AdHocCategory category = mapToEntity(categoryDTO);
        AdHocCategory savedCategory = adHocCategoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    @Override
    public AdHocCategoryDTO updateCategory(Integer id, AdHocCategoryDTO categoryDTO) {
        AdHocCategory category = adHocCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Ad-hoc category not found with id: " + id));
        
        // Update the category fields
        if (categoryDTO.getDescription() != null) {
            category.setDescription(categoryDTO.getDescription());
        }
        
        AdHocCategory updatedCategory = adHocCategoryRepository.save(category);
        return mapToDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        AdHocCategory category = adHocCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Ad-hoc category not found with id: " + id));
        
        adHocCategoryRepository.delete(category);
    }
    
    /**
     * Maps an AdHocCategory entity to a DTO
     * 
     * @param category The entity to map
     * @return The mapped DTO
     */
    private AdHocCategoryDTO mapToDTO(AdHocCategory category) {
        return AdHocCategoryDTO.builder()
                .id(category.getId())
                .description(category.getDescription())
                .build();
    }
    
    /**
     * Maps an AdHocCategoryDTO to an entity
     * 
     * @param categoryDTO The DTO to map
     * @return The mapped entity
     */
    private AdHocCategory mapToEntity(AdHocCategoryDTO categoryDTO) {
        return AdHocCategory.builder()
                .id(categoryDTO.getId())
                .description(categoryDTO.getDescription())
                .build();
    }
}