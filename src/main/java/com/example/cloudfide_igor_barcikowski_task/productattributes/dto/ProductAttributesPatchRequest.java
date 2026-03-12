package com.example.cloudfide_igor_barcikowski_task.productattributes.dto;

import jakarta.validation.constraints.Size;

public record ProductAttributesPatchRequest(
        @Size(max = 255, message = "attribute name cannot be larger that 255 characters")
        String name,

        @Size(max = 255, message = "attribute value cannot be larger than 255 characters")
        String value
) {

}
