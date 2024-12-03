package com.encore.frab.reto.handler;

import com.encore.frab.reto.dto.ClienteRequest;
import com.encore.frab.reto.dto.ClienteResponse;
import com.encore.frab.reto.services.ClienteService;
import com.encore.frab.reto.services.ClienteServiceImpl;
import com.encore.frab.reto.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return clientRequest.flatMap((cli -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(clienteService.create(cli,
                        request.headers().asHttpHeaders().asSingleValueMap()
                    ), ClienteResponse.class)
            )
        );
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<ClienteRequest> clienteRequest = request.bodyToMono(ClienteRequest.class)
                                        .doOnNext(objectValidator::validate);
        return clienteRequest.flatMap((cli -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clienteService.update(cli, request.headers()
                        .asHttpHeaders().asSingleValueMap(), id),
                        ClienteResponse.class)
            )
        );
    }


}
