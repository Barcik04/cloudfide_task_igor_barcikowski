package com.example.cloudfide_igor_barcikowski_task.analytics.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProducerAnalyticsDTO {
    private List<ProducerSummary> highValueProducers;
    private List<ProducerSummary> inactiveProducers;
    private List<ProducerSegment> producerSegments;
}
