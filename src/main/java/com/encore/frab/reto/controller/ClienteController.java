package com.encore.frab.reto.controller;

import com.encore.frab.reto.dto.ClienteRequest;
import com.encore.frab.reto.dto.ClienteResponse;
import com.encore.frab.reto.services.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("api/v1/cliente")
@Slf4j
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    private static final Logger LOG = LoggerFactory.getLogger(ClienteController.class);
    @GetMapping
    public Flux<ClienteResponse> listAll() {
        return clienteService.listAll();
    }

    @GetMapping("/{id}")
    public Mono<ClienteResponse> getById(@PathVariable("id") int id) {

        return clienteService.getById(id);
    }

    @PostMapping
    public Mono<ClienteResponse> save(@Valid @RequestBody ClienteRequest cliente,
                                      @RequestHeader Map<String, String> headers) {
        LOG.info(cliente.toString());
        return clienteService.create(Mono.just(cliente), headers);
    }

    @PutMapping("/{id}")
    public Mono<ClienteResponse> update(@PathVariable("id") int id,
                                        @RequestBody ClienteRequest cliente,
                                        @RequestHeader Map<String, String> headers) {
        return clienteService.update(cliente, headers, id);
    }


}
