package com.example.cloudfide_igor_barcikowski_task.producer.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProducerCreateRequest(
        @Schema(example = "Samsung")
        @NotBlank(message = "name cant be blank")
        @Size(max = 255, message = "name cannot be longer than 255 characters")
        String name,

        @Schema(example = "Poland")
        @Size(max = 100, message = "country cant be longer than 100 characters")
        String country,


        @Schema(example = "Phone manufacturer")
        @Size(max = 1000, message = "description cant be longer than 1000 characters")
        String description
) {
}
