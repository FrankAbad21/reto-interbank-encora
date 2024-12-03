package com.encore.frab.reto.handler;

import com.encore.frab.reto.dto.ClienteRequest;
import com.encore.frab.reto.dto.ClienteResponse;
import com.encore.frab.reto.services.ClienteService;
import com.frab.spring.webflux.dto.ProductDto;
import com.frab.spring.webflux.entity.Product;
import com.frab.spring.webflux.service.ProductService;
import com.frab.spring.webflux.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Mono<ServerResponse> listAll(ServerRequest request) {
        Flux<ClienteResponse> products = clienteService.listAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(products, ClienteResponse.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        int id = Integer.valueOf(request.pathVariable("id"));
        Mono<ClienteResponse> cliente = clienteService.getById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(cliente, ClienteResponse.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<ClienteRequest> productDto = request.bodyToMono(ClienteRequest.class).doOnNext(objectValidator::validate);
        return productDto.flatMap((prod -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clienteService.create(prod, ), ClienteResponse.class)));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        int id = Integer.valueOf(request.pathVariable("id"));
        Mono<ClienteRequest> clienteRequest = request.bodyToMono(ClienteRequest.class).doOnNext(objectValidator::validate);
        return clienteRequest.flatMap((prod -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.update(prod, id), ClienteResponse.class)));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        int id = Integer.valueOf(request.pathVariable("id"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.delete(id), Product.class);
    }

}
