package com.Ospuaye.BackendOspuaye.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

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

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "persona_id", nullable = false, unique = true)
    private Persona persona;

    private Boolean afiliadoSindical;
    private Boolean esJubilado;

    @OneToOne(mappedBy = "titular")
    @JsonBackReference
    private GrupoFamiliar grupoFamiliar;

    // 🔗 Relación con Empresa (muchos beneficiarios pueden pertenecer a una empresa)
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    @JsonManagedReference
    private Empresa empresa;
}
