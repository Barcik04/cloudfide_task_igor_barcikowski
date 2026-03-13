package com.example.cloudfide_igor_barcikowski_task.utils.dynamicfilter;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductFilter(
        String q,

        @PositiveOrZero
        @DecimalMax("100000")
        BigDecimal minPrice,

        @Positive
        @DecimalMax("100000")
        BigDecimal maxPrice,

        String producerName

) {
}
