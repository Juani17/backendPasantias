package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.Tipo;
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
    private Boolean activo;

    @Enumerated(EnumType.STRING)
    private Tipo tipo; // URBANO o RURAL

    @ManyToOne
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @OneToOne(mappedBy = "domicilio")
    @JsonBackReference(value = "empresa-domicilio")
    private Empresa empresa;
}
