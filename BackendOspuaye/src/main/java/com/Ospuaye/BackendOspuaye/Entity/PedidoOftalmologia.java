package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pedidos_oftalmologia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PedidoOftalmologia extends Base {

    private Boolean usaLentes;

    private Boolean recetaMedica;
}