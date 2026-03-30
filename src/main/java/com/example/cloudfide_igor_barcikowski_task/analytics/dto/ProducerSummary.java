package com.example.cloudfide_igor_barcikowski_task.analytics.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProducerSummary {
    private Long producerId;
    private String producerName;
    private BigDecimal totalRevenue;
    private int totalProducts;
    private Date lastProductDate;
    private Date producerSince;
}
