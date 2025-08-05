package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "pedidos_ortopedia")
@DiscriminatorValue("ORTOPEDIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PedidoOrtopedia extends Pedido {

    @Column(nullable = false)
    private String motivoConsulta;
    private Boolean recetaMedica;

}