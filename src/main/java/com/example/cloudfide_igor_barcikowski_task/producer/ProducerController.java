package com.example.cloudfide_igor_barcikowski_task.producer;

import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerPatchRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerCreateRequest;
import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerResponse;

@RestController
@Validated
@RequestMapping("/api/v1/producers")
public class ProducerController {
    private final ProducerService producerService;


    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Operation(
            summary = "Creates producer")
    @PostMapping
    public ResponseEntity<ProducerResponse> createProducer(@RequestBody @Valid ProducerCreateRequest producerCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(producerService.createProducer(producerCreateRequest));
    }


    @Operation(
            summary = "Retrieving all producers from the system paged")
    @GetMapping
    public Page<ProducerResponse> getAllProducers(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "name")
            Pageable pageable
    ) {
        return producerService.getAllProducers(pageable);
    }



    @Operation(
            summary = "deletes producer by given producerId")
    @DeleteMapping("/{producerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProducer(
            @PositiveOrZero(message = "id must be positive")
            @PathVariable Long producerId
    ) {
        producerService.deleteProducer(producerId);
    }




    @Operation(
            summary = "Patches producer by given producerId")
    @PatchMapping("/{producerId}")
    public ResponseEntity<ProducerResponse> patchProducer(
            @PathVariable @Positive Long producerId,
            @RequestBody @Valid ProducerPatchRequest producerPatchRequest
    ) {
        return ResponseEntity.ok(producerService.patchProducer(producerPatchRequest, producerId));
    }
}
