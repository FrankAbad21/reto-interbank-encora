package com.encore.frab.reto.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Analitica {

    private String analyticsTraceSource;
    private String applicationId;
    private String channelOperationNumber;
    private String currentDate;
    private String customerId;
    private String region;
    private String statusCode;
    private LocalDateTime timestamp;
    private String traceId;
    private String inbound;
    private String outbound;
    private String transactionCode;

}
