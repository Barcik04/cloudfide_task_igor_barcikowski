package com.example.cloudfide_igor_barcikowski_task.producer.dto;

import java.time.Instant;

public record ProducerResponse(
        Long id,
        String name,
        String country,
        String description,
        Instant createdAt
) {
}
