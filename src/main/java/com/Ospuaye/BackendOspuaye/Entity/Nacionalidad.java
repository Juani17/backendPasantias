package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "nacionalidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Nacionalidad extends BaseNombrable {
    private String nombre;
}
