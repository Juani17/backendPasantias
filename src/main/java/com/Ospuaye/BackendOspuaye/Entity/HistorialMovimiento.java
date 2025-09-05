package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Date;

@Entity
@Table(name = "historial_movimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HistorialMovimiento extends Base {

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado; // <-- usa Estado del diagrama

    private String comentario;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
