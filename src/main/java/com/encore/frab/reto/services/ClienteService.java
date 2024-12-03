package com.encore.frab.reto.services;

import com.encore.frab.reto.dto.ClienteRequest;
import com.encore.frab.reto.dto.ClienteResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ClienteService {

    Mono<ClienteResponse> create(ClienteRequest clienteRequest,
                                 Map<String, String> headers);

    Flux<ClienteResponse> listAll();

    Mono<ClienteResponse> getById(String id);

    Flux<ClienteResponse> findByNombre(String nombre);

    Mono<ClienteResponse> update(ClienteRequest clienteRequest, Map<String, String> headers, String id);


}
