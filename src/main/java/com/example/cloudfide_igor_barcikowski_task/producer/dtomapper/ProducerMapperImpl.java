package com.example.cloudfide_igor_barcikowski_task.producer.dtomapper;

import org.springframework.stereotype.Component;
import com.example.cloudfide_igor_barcikowski_task.producer.Producer;
import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerResponse;

import java.time.Instant;

@Component
public class ProducerMapperImpl implements ProducerMapper {
    @Override
    public ProducerResponse producerToResponseDto(Producer request) {
        if  (request == null) {
            return null;
        }

        return new ProducerResponse(
                request.getId(),
                request.getName(),
                request.getCountry(),
                request.getDescription(),
                Instant.now()
        );
    }
}
