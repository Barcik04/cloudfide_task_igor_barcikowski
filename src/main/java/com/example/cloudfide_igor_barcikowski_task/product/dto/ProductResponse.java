package com.example.cloudfide_igor_barcikowski_task.product.dto;

import com.example.cloudfide_igor_barcikowski_task.productattributes.dto.ProductAttributeResponse;

import java.math.BigDecimal;
import java.util.Set;

public record ProductResponse(
        Long id,
        Long producerId,
        String productName,
        String description,
        int quantity,
        Set<ProductAttributeResponse> productAttributes,
        BigDecimal price
) {
}
