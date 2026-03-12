package com.example.cloudfide_igor_barcikowski_task.product.dto;


import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductPatchRequest(

        @Size(max = 255, message = "product name cannot be longer than 255 characters")
        String productName,

        @Size(max = 1000, message = "description cannot be longer than 1000 characters")
        String description,

        @PositiveOrZero(message = "quantity must be positive or zero")
        Integer quantity,

        BigDecimal price
) {
}
