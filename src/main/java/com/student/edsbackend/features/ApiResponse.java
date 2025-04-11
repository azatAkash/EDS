package com.student.edsbackend.features;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    @JsonProperty("message")
    private String message;
}
