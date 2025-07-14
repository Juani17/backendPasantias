package com.Ospuaye.BackendOspuaye.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "documentos")
public class Documento extends Base {

    private String nombreArchivo;
    private String path;
    private String observacion;
    private LocalDateTime fechaSubida;

    @ManyToOne
    @JoinColumn(name = "subido_por")
    private Usuario subidoPor;
}