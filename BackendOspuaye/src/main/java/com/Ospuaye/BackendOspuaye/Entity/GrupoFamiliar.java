package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Table(name = "grupos_familiares")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GrupoFamiliar extends Base {

    @Column(nullable = false)
    private String nombreGrupo;

    @ManyToOne
    @JoinColumn(name = "titular_id")
    private Beneficiario titular;

    private Date fechaAlta;
    private Integer dni;
    private Integer cuil;

    @Enumerated(EnumType.STRING)
    private TipoParentesco tipoParentesco;

    private Boolean activo;
}
