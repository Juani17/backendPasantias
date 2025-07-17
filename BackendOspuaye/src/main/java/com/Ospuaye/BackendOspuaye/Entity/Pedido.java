package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Pedido extends Base {

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "beneficiario_id")
    private Beneficiario beneficiario;

    @ManyToOne
    @JoinColumn(name = "grupo_familiar_id")
    private GrupoFamiliar grupoFamiliar;

    private Integer dni;
    private Integer telefono;
    private String empresa;
    private String delegacion;

    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;

    @OneToMany
    private List<Documento> documentos;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Usuario paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @Temporal(TemporalType.DATE)
    private Date fechaRevision;

    private String observacionMedico;
}
