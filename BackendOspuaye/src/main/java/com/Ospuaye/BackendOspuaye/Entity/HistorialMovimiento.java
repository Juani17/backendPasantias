package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Estado;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "historial_movimiento")
public class HistorialMovimiento extends Base {

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private Estado tipoMovimiento;

    private String comentario;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "formulario_id")
    private FormularioPodologia formulario; // Asumiendo solo uno, podrías extenderlo luego para ambos formularios
}