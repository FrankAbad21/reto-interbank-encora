package com.encore.frab.reto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ClienteRequest {

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
