package com.Ospuaye.BackendOspuaye.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "areas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Area extends Base {

    @Column(nullable = false)
    private String nombre;
}
