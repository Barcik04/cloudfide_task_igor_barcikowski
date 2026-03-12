package com.example.cloudfide_igor_barcikowski_task.producer.dtomapper;

import com.example.cloudfide_igor_barcikowski_task.producer.Producer;
import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerResponse;

public interface ProducerMapper {
    ProducerResponse producerToResponseDto(Producer producer);
}
