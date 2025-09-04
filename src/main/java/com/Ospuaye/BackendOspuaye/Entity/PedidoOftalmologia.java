package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "pedidos_oftalmologia")
@DiscriminatorValue("OFTALMOLOGIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PedidoOftalmologia extends Pedido {
    private String motivoConsulta;
    private Boolean usaLentes;
    private Boolean recetaMedica;
}
