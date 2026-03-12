package com.example.cloudfide_igor_barcikowski_task.product.dto;

import com.example.cloudfide_igor_barcikowski_task.productattributes.dto.ProductAttributeRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

public record ProductCreateRequest(
        @Schema(example = "TV")
        @NotBlank(message = "product name cant be blank")
        @Size(max = 255, message = "product name cannot be longer than 255 characters")
        String productName,

        @Schema(example = "Big TV")
        @Size(max = 1000, message = "description cannot be longer than 1000 characters")
        String description,

        @Schema(example = "12")
        @PositiveOrZero(message = "quantity must be positive or zero")
        int quantity,


        @Size(max = 100, message = "product cannot have more than 100 attributes")
        @Schema(
                description = "List of product attributes",
                example = """
                [
                 {
                  "name": "color",
                  "value": "black"
                 },
                 {
                  "name": "size",
                  "value": "55 inch"
                 }
                ]
                """
        )
        Set<ProductAttributeRequest> productAttributes,

        @Schema(example = "100")
        BigDecimal price
) {
}
