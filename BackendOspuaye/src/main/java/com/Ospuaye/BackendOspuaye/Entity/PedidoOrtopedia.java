package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "pedidos_ortopedia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PedidoOrtopedia extends Base {

    @Column(nullable = false)
    private String tipoEstudio;

    private Boolean usaLentes;

    private String motivoConsulta;
}