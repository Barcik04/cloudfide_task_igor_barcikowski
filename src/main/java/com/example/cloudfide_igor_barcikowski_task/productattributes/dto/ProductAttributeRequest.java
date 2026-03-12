package com.example.cloudfide_igor_barcikowski_task.productattributes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductAttributeRequest(
        @Schema(example = "color")
        @NotBlank(message = "attribute name cannot be blank")
        @Size(max = 255, message = "attribute name cannot be larger that 255 characters")
        String name,

        @Schema(example = "black")
        @NotBlank(message = "attribute value cannot be blank")
        @Size(max = 255, message = "attribute value cannot be larger than 255 characters")
        String value
) {
}
