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
    private static final Logger LOG = LoggerFactory.getLogger(ClienteServiceImpl.class);

    @Override
    public Mono<ClienteResponse> create(Mono<ClienteRequest> clienteRequest,
                                        Map<String, String> headers) {
        LOG.info("Ingreso a service {}", clienteRequest.toString());
        Mono<Boolean> existDocumento = clienteRequest
                .flatMap(cli -> clienteRepository
                        .findByTipoDocumentoAndNumeroDocumento(
                                cli.getTipoDocumento(),
                                cli.getNumeroDocumento()
                                ).hasElement()
        );
        Mono<Cliente> cliente = clienteRequest
                .map(cli -> Cliente.builder()
                        .id(GeneradorId.generarIdDiezDigitosUnicos())
                        .tipoDocumento(cli.getTipoDocumento())
                        .numeroDocumento(cli.getNumeroDocumento())
                        .nombre(cli.getNombre())
                        .apellidoPaterno(cli.getApellidoPaterno())
                        .apellidoMaterno(cli.getApellidoMaterno())
                        .fechaCreacion(LocalDateTime.now())
                        .estado(Estado.ACTIVO.toString())
                        .build());
        return existDocumento.flatMap(exist -> exist ?
                Mono.error(new Exception(
                        DOCUMENT_MESSAGE))
                : cliente.flatMap(cli -> clienteRepository.save(cli)
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
        return null;
    }


    @Override
    public Mono<ClienteResponse> getById(int id) {
        return null;
    }

    @Override
    public Flux<ClienteResponse> findByNombre(String nombre) {
        return null;
    }

    @Override
    public Mono<ClienteResponse> update(ClienteRequest clienteRequest,
                                        Map<String, String> headers, int id) {
        return null;
    }

}
