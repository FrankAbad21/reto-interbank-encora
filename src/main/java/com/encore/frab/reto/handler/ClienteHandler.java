package com.encore.frab.reto.handler;

import com.encore.frab.reto.dto.Analitica;
import com.encore.frab.reto.dto.ClienteRequest;
import com.encore.frab.reto.dto.ClienteResponse;
import com.encore.frab.reto.services.ClienteService;
import com.encore.frab.reto.services.ClienteServiceImpl;
import com.encore.frab.reto.services.KafkaMessagePublisher;
import com.encore.frab.reto.util.AnaliticaUtil;
import com.encore.frab.reto.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClienteHandler {

    private final ClienteService clienteService;

    private final ObjectValidator objectValidator;

    private final KafkaMessagePublisher kafkaMessagePublisher;

    @Value("${app.region}")
    private String region;

    @Value("${app.transaccion}")
    private String transaccion;

    private static final Logger LOG = LoggerFactory.getLogger(ClienteHandler.class);

    public Mono<ServerResponse> listAll(ServerRequest request) {
        Flux<ClienteResponse> products = clienteService.listAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(products, ClienteResponse.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        String id = (String) request.pathVariable("id");
        LOG.info("informacion busqueda id: {}", id);
        Mono<ClienteResponse> client = clienteService.getById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(client, ClienteResponse.class);
    }

    public Mono<ServerResponse> getByNombre(ServerRequest request) {
        String nombre = (String) request.queryParam("nombre").orElse("");
        LOG.info("informacion busqued nombre: {}", nombre);
        Flux<ClienteResponse> client = clienteService.findByNombre(nombre);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(client, ClienteResponse.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<ClienteRequest> clientRequest = request.bodyToMono(ClienteRequest.class)
                .doOnNext(objectValidator::validate);
        Analitica analitica = new Analitica();
        clientRequest.map(cli -> AnaliticaUtil.addDatosAnalitica(analitica,
                request.headers().asHttpHeaders().asSingleValueMap(),
                cli,
                null,
                region,
                transaccion
                ));
        return clientRequest.flatMap((cli -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(clienteService.create(cli)
                    .doOnNext( next -> {
                            AnaliticaUtil.addDatosAnalitica(analitica,
                            request.headers().asHttpHeaders().asSingleValueMap(),
                            null,
                            next,
                            region,
                            transaccion
                            );
                        kafkaMessagePublisher.sendEventsToTopic(analitica);

                    }
                ), ClienteResponse.class)
            )
        );
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<ClienteRequest> clienteRequest = request.bodyToMono(ClienteRequest.class)
                                        .doOnNext(objectValidator::validate);
        Analitica analitica = new Analitica();
        clienteRequest.map(cli -> AnaliticaUtil.addDatosAnalitica(analitica,
                request.headers().asHttpHeaders().asSingleValueMap(),
                cli,
                null,
                region,
                transaccion
        ));
        return clienteRequest.flatMap((cli -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clienteService.update(cli, id)
                    .doOnNext( next -> {
                                AnaliticaUtil.addDatosAnalitica(analitica,
                                        request.headers().asHttpHeaders().asSingleValueMap(),
                                        null,
                                        next,
                                        region,
                                        transaccion
                                );
                                kafkaMessagePublisher.sendEventsToTopic(analitica);

                            }
                    ),
                        ClienteResponse.class)
            )
        );
    }


}
