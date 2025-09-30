package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Pedido extends Base {

    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beneficiario_id")
    private Beneficiario beneficiario;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "grupo_familiar_id", nullable = true)
    @JsonIgnoreProperties({"familiares", "titular"}) // evita loops
    private GrupoFamiliar grupoFamiliar;


    private Long dni;
    private Long telefono;
    private String empresa;
    private String delegacion;

    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Documento> documentos;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id")
    private Familiar paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date fechaRevision;

    private String observacionMedico;
}
