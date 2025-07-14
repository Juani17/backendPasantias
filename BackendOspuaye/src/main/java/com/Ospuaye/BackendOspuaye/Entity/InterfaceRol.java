package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roles")
public class InterfaceRol extends Base {

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;
}