package com.encore.frab.reto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ClienteDto {

    @NotBlank
    private String id;
    @NotBlank
    private String tipoDocumento;
    @NotBlank
    private String numeroDocumento;
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellidoPaterno;
    @NotNull
    private String apellidoMaterno;

}
