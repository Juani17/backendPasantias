package com.Ospuaye.BackendOspuaye.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "familiares")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Familiar extends Base{
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "grupo_familiar_id")
    private GrupoFamiliar grupoFamiliar;

    @ManyToOne
    @JoinColumn(name = "beneficiario_id")
    private Beneficiario beneficiario;


    private String nombre;
    private String apellido;
    private Long dni;
    private Long cuil;
    private Long telefono;
    @Enumerated(EnumType.STRING)
    private TipoParentesco tipoParentesco;
}
