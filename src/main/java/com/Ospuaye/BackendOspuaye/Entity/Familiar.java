package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.TipoParentesco;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "familiares")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Familiar extends Base {

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "grupo_familiar_id")
    private GrupoFamiliar grupoFamiliar;

    @ManyToOne
    @JoinColumn(name = "beneficiario_id")
    private Beneficiario beneficiario;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "persona_id", nullable = false, unique = true)
    private Persona persona;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_parentesco")
    private TipoParentesco tipoParentesco;
}
