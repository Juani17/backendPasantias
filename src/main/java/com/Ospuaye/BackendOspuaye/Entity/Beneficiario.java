package com.Ospuaye.BackendOspuaye.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "beneficiarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Beneficiario extends Persona {

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    private Boolean afiliadoSindical;
    private Boolean esJubilado;

    @OneToOne(mappedBy = "titular", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"titular", "familiares"}) // evita loop
    private GrupoFamiliar grupoFamiliar;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
