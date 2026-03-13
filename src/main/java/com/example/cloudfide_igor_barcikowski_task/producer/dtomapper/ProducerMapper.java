package com.example.cloudfide_igor_barcikowski_task.producer.dtomapper;

import com.example.cloudfide_igor_barcikowski_task.producer.Producer;
import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerResponse;
import org.springframework.data.domain.Page;

public interface ProducerMapper {
    ProducerResponse producerToResponseDto(Producer producer);
}
