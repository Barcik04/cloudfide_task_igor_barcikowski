package com.example.cloudfide_igor_barcikowski_task.analytics.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProducerSegment {
    private String segmentName;
    private List<ProducerSummary> producers;
    private int producerCount;
}
