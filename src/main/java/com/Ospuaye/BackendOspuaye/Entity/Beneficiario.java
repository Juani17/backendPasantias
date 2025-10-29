package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.EstadoCivil;

import com.Ospuaye.BackendOspuaye.Entity.Enum.Incapacidad;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "beneficiarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Beneficiario extends Persona {

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    private Boolean afiliadoSindical;
    private Boolean esJubilado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil")
    private EstadoCivil estadoCivil;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "titular")
    @JsonBackReference
    private GrupoFamiliar grupoFamiliar;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    @JsonBackReference(value = "empresa-beneficiarios")
    private Empresa empresa;

    @Enumerated(EnumType.STRING)
    @Column(name = "incapacidad")
    private Incapacidad incapacidad;

}
