package com.student.edsbackend.features.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenDTO {
    @Schema(readOnly = true)
    private Integer id;
    private Integer userId;
    private String token;
}
