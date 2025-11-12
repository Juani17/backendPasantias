package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "paises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Pais extends BaseNombrable {

    private String nombre;
}
