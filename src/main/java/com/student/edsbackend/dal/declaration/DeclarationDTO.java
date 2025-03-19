package com.student.edsbackend.dal.declaration;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.student.edsbackend.dal.enums.DeclarationStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeclarationDTO {

    private Integer id;
    private Integer userId;
    private JsonNode c1;
    private JsonNode c2;
    private JsonNode c3;
    private JsonNode c4;
    private JsonNode c5;
    private JsonNode c6;
    private JsonNode c7;
    private JsonNode c8;
    private JsonNode c9;
    private JsonNode c10;
    private DeclarationStatus status;
    private Integer managerId;
    private LocalDateTime createdAt;
}
