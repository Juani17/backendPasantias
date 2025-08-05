package com.Ospuaye.BackendOspuaye.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "beneficiarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Beneficiario extends Base {

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    private String nombre;
    private String apellido;

    @NotNull(message = "El DNI es obligatorio")
    @Min(value = 1000000, message = "El DNI debe tener al menos 7 dígitos")
    @Max(value = 99999999, message = "El DNI no puede tener más de 8 dígitos")
    private Integer dni;
    private Long cuil;
    private Long telefono;

    @OneToOne(mappedBy = "titular")
    @JsonBackReference
    private GrupoFamiliar grupoFamiliar;

}
