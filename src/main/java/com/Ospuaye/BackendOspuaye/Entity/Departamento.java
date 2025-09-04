package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "departamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Departamento extends Base {

    private String nombre;
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;
}
