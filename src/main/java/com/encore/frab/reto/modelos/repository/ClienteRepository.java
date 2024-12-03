package com.encore.frab.reto.modelos.repository;

import com.encore.frab.reto.modelos.documentos.Cliente;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ClienteRepository extends
        ReactiveMongoRepository<Cliente, String> {

    Mono<Cliente> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento,
                                                     String numeroDocumento);

    Flux<Cliente> findByNombre(String nombre);

    @Query("{ 'appId' : ?0, 'status' : ?1, 'id' : ?2}")
    Mono<Cliente> repeatedTipoDocumentoAndNumeroDocumento(String tipoDocumento,
                                                          String numeroDocumento,
                                                            String id);

}
