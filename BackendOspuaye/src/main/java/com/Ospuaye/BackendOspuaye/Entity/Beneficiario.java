package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "beneficiarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Beneficiario extends Base {

    private String nombre;
    private String apellido;

    @NotNull(message = "El DNI es obligatorio")
    @Min(value = 1000000, message = "El DNI debe tener al menos 7 dígitos")
    @Max(value = 99999999, message = "El DNI no puede tener más de 8 dígitos")
    private Integer dni;
    private Integer cuil;
    private Integer telefono;
}
