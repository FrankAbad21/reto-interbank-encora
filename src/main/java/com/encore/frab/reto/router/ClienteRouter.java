package com.encore.frab.reto.router;

import com.encore.frab.reto.handler.ClienteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class ClienteRouter {

    private static final String PATH = "api/v1/cliente";

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    RouterFunction<ServerResponse> router(ClienteHandler handler) {
        return RouterFunctions.route()
                .GET(PATH, RequestPredicates.queryParam("nombre", t -> true), handler::getByNombre)
                .GET(PATH, handler::listAll)
                .GET(PATH + "/{id}", handler::getById)
                .POST(PATH, handler::create)
                .PUT(PATH + "/{id}", handler::update)
                .build();
    }

}
