package com.encore.frab.reto.util;

import com.encore.frab.reto.dto.Analitica;
import com.encore.frab.reto.dto.ClienteRequest;
import com.encore.frab.reto.dto.ClienteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class AnaliticaUtil {

    private static final String APPLICATION = "application-";
    private static final String CONSUMERID = "consumerId";
    private static final String TRACEPARENT = "traceparent";

    private static final Logger LOG = LoggerFactory.getLogger(AnaliticaUtil.class);

    public Analitica addDatosAnalitica(Analitica analitica,
                           Map<String, String> headers,
                           ClienteResponse clienteResponse,
                           String region,
                           String transaccion) {
        if (headers != null) {
            analitica.setApplicationId(APPLICATION.concat(headers.get(CONSUMERID)));
            analitica.setAnalyticsTraceSource(headers.get(CONSUMERID));
            analitica.setChannelOperationNumber(LocalDateTime.now().toString());
            analitica.setTraceId(headers.get(TRACEPARENT));
        }

        if (clienteResponse != null) {
            analitica.setCurrentDate(LocalDateToISO8601
                    .formatUsingDateTimeFormatter(clienteResponse.getFechaCreacion())
            );
            analitica.setCustomerId(clienteResponse.getId());
            analitica.setStatusCode(HttpStatus.OK.toString());
            analitica.setTimestamp(clienteResponse.getFechaCreacion());
            analitica.setOutbound(clienteResponse.toString());
            analitica.setRegion(region);
            analitica.setTransactionCode(transaccion);
        }
        return analitica;
    }

    public Analitica addDatosAnalitica(Analitica analitica,
                                       ClienteRequest clienteRequest) {
        LOG.info("clienteRequest {} ",clienteRequest);
        if (clienteRequest != null) {
            analitica.setInbound(clienteRequest.toString());
        }
        return analitica;
    }

}
