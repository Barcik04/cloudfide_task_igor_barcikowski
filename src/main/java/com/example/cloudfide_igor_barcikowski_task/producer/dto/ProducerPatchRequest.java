package com.example.cloudfide_igor_barcikowski_task.producer.dto;

import jakarta.validation.constraints.Size;

public record ProducerPatchRequest(
        @Size(max = 255, message = "name cannot be longer than 255 characters")
        String name,

        @Size(max = 100, message = "country cant be longer than 100 characters")
        String country,

        @Size(max = 1000, message = "description cant be longer than 1000 characters")
        String description
) {
}
