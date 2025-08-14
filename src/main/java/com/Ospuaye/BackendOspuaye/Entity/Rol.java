package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Rol extends Base {

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;
}
