package com.Ospuaye.BackendOspuaye.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "documentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Documento extends Base {

    @Column(nullable = false)
    private String nombreArchivo;

    private String path;
    private String observacion;
    private Date fechaSubida;

    @ManyToOne
    @JoinColumn(name = "subido_por")
    private Usuario subidoPor;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
