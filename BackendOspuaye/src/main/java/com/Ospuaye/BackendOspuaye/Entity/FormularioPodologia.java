package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Estado;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "formularios_podologia")
public class FormularioPodologia extends Base {

    private String nombre;
    private Integer dni;

    @OneToMany
    private List<Documento> documentos;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    private Date fechaRevision;
    private String observacionMedico;
}