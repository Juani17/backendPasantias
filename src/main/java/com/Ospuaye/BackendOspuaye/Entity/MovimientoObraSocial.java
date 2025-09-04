package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Date;

@Entity
@Table(name = "movimientos_obra_social")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MovimientoObraSocial extends Base {

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_desde")
    private Date fechaDesde;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_hasta")
    private Date fechaHasta;

    private String observacion;

    @ManyToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;
}
