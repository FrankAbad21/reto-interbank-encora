package com.encore.frab.reto.services;

import com.encore.frab.reto.Estado;
import com.encore.frab.reto.dto.ClienteRequest;
import com.encore.frab.reto.dto.ClienteResponse;
import com.encore.frab.reto.exception.CustomException;
import com.encore.frab.reto.modelos.documentos.Cliente;
import com.encore.frab.reto.modelos.repository.ClienteRepository;
import com.encore.frab.reto.util.GeneradorId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private static final String DOCUMENT_MESSAGE = "Tipo de documento y numero de documento ya existe";
    private static final String  NF_MESSAGE = "No se encuentra el cliente";
    private static final Logger LOG = LoggerFactory.getLogger(ClienteServiceImpl.class);

    @Override
    public Mono<ClienteResponse> create(ClienteRequest clienteRequest) {
        LOG.info("Ingreso a service {}", clienteRequest.toString());
        Mono<Boolean> existDocumento = clienteRepository
                        .findByTipoDocumentoAndNumeroDocumento(
                                clienteRequest.getTipoDocumento(),
                                clienteRequest.getNumeroDocumento()
                                ).hasElement();
        return existDocumento.flatMap(
            exist -> exist ?
            Mono.error(new CustomException(HttpStatus.BAD_REQUEST,
                DOCUMENT_MESSAGE))
                : clienteRepository.save(Cliente.builder()
                    .id(GeneradorId.generarIdDiezDigitosUnicos())
                    .tipoDocumento(clienteRequest.getTipoDocumento())
                    .numeroDocumento(clienteRequest.getNumeroDocumento())
                    .nombre(clienteRequest.getNombre())
                    .apellidoPaterno(clienteRequest.getApellidoPaterno())
                    .apellidoMaterno(clienteRequest.getApellidoMaterno())
                    .fechaCreacion(LocalDateTime.now())
                    .estado(Estado.ACTIVO.toString())
                    .build()
                ).map(cl -> ClienteResponse.builder()
                    .id(cl.getId())
                    .tipoDocumento(cl.getTipoDocumento())
                    .numeroDocumento(cl.getNumeroDocumento())
                    .nombre(cl.getNombre())
                    .apellidoPaterno(cl.getApellidoPaterno())
                    .apellidoMaterno(cl.getApellidoMaterno())
                    .fechaCreacion(cl.getFechaCreacion())
                    .estado(cl.getEstado())
                    .build()
                )
        );

    }

    @Override
    public Flux<ClienteResponse> listAll() {
        return clienteRepository.findAll()
                .map(cl -> ClienteResponse.builder()
                        .id(cl.getId())
                        .tipoDocumento(cl.getTipoDocumento())
                        .numeroDocumento(cl.getNumeroDocumento())
                        .nombre(cl.getNombre())
                        .apellidoPaterno(cl.getApellidoPaterno())
                        .apellidoMaterno(cl.getApellidoMaterno())
                        .fechaCreacion(cl.getFechaCreacion())
                        .estado(cl.getEstado())
                        .build()
                );
    }


    @Override
    public Mono<ClienteResponse> getById(String id) {
        return clienteRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)))
                .map(cl -> ClienteResponse.builder()
                    .id(cl.getId())
                    .tipoDocumento(cl.getTipoDocumento())
                    .numeroDocumento(cl.getNumeroDocumento())
                    .nombre(cl.getNombre())
                    .apellidoPaterno(cl.getApellidoPaterno())
                    .apellidoMaterno(cl.getApellidoMaterno())
                    .fechaCreacion(cl.getFechaCreacion())
                    .estado(cl.getEstado())
                    .build()
                );
    }

    @Override
    public Flux<ClienteResponse> findByNombre(String nombre) {
        return clienteRepository.findByNombre(nombre)
                .switchIfEmpty(Flux.error(
                        new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)))
                .map(cl -> ClienteResponse.builder()
                        .id(cl.getId())
                        .tipoDocumento(cl.getTipoDocumento())
                        .numeroDocumento(cl.getNumeroDocumento())
                        .nombre(cl.getNombre())
                        .apellidoPaterno(cl.getApellidoPaterno())
                        .apellidoMaterno(cl.getApellidoMaterno())
                        .fechaCreacion(cl.getFechaCreacion())
                        .estado(cl.getEstado())
                        .build()
                );
    }

    @Override
    public Mono<ClienteResponse> update(ClienteRequest clienteRequest, String id) {
        LOG.info("Ingreso a service update {}", clienteRequest.toString());
        Mono<Cliente> clienteMono = clienteRepository.findById(id);
        Mono<Boolean> clientExistId = clienteMono.hasElement();

        Mono<Boolean> existDocumento = clienteRepository
                .findByTipoDocumentoAndNumeroDocumento(
                        clienteRequest.getTipoDocumento(),
                        clienteRequest.getNumeroDocumento()
                ).hasElement();
        return clientExistId.flatMap(
                existsId -> existsId ?
                    existDocumento.flatMap(
                        exist -> exist ?
                            Mono.error(new CustomException(HttpStatus.BAD_REQUEST,
                                    DOCUMENT_MESSAGE))
                            : clienteMono.flatMap(
                                cli -> clienteRepository.save(
                                    Cliente.builder()
                                    .id(id)
                                    .tipoDocumento(clienteRequest.getTipoDocumento())
                                    .numeroDocumento(clienteRequest.getNumeroDocumento())
                                    .nombre(clienteRequest.getNombre())
                                    .apellidoPaterno(clienteRequest.getApellidoPaterno())
                                    .apellidoMaterno(clienteRequest.getApellidoMaterno())
                                    .estado(cli.getEstado())
                                    .fechaCreacion(cli.getFechaCreacion())
                                    .build()
                                )
                        ).map(cl -> ClienteResponse.builder()
                            .id(cl.getId())
                            .tipoDocumento(cl.getTipoDocumento())
                            .numeroDocumento(cl.getNumeroDocumento())
                            .nombre(cl.getNombre())
                            .apellidoPaterno(cl.getApellidoPaterno())
                            .apellidoMaterno(cl.getApellidoMaterno())
                            .fechaCreacion(cl.getFechaCreacion())
                            .estado(cl.getEstado())
                            .build()
                        )
                    )
                : Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE))
          );
    }

}
