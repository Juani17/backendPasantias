package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "beneficiarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Beneficiario extends Base {

    private String nombre;
    private String apellido;
    private Integer dni;
    private Integer cuil;
    private Integer telefono;
}
