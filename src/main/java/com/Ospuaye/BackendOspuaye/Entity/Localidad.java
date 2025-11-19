package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "localidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Localidad extends Base {

    private String nombre;

    @Column(name = "codigo_postal")
    private String codigoPostal;


    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
}
