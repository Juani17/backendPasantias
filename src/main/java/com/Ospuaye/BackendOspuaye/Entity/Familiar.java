package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.Sexo;
import com.Ospuaye.BackendOspuaye.Entity.Enum.TipoParentesco;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "familiares")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Familiar extends Persona {

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "grupo_familiar_id")
    private GrupoFamiliar grupoFamiliar;

    @ManyToOne
    @JoinColumn(name = "beneficiario_id")
    private Beneficiario beneficiario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_parentesco")
    private TipoParentesco tipoParentesco;
}
