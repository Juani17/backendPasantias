package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.TipoDeDomicilio;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "domicilios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Domicilio extends Base {

    private String calle;
    private String numeracion;
    private String barrio;

    @Column(name = "manzana_piso")
    private String manzanaPiso;

    @Column(name = "casa_departamento")
    private String casaDepartamento;

    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_domicilio")
    private TipoDeDomicilio tipoDomicilio;


    @ManyToOne
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @ManyToOne
    @JsonBackReference(value = "empresa-domicilio")
    private Empresa empresa;
}
