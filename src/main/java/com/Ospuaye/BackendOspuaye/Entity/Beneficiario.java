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
public class Beneficiario extends Persona {

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    private Boolean afiliadoSindical;
    private Boolean esJubilado;

    @OneToOne(mappedBy = "titular")
    @JsonBackReference
    private GrupoFamiliar grupoFamiliar;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    @JsonManagedReference
    private Empresa empresa;
}
