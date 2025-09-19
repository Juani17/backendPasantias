package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.EstadoPersona;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Sexo;
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

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private Long dni;

    @Column(unique = true)
    private Long cuil;

    private Long telefono;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @ManyToOne
    @JoinColumn(name = "nacionalidad_id")
    private Nacionalidad nacionalidad;

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
