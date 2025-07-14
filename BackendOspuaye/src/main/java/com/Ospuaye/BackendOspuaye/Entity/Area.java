package com.Ospuaye.BackendOspuaye.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "areas")
public class Area extends Base {

    private String nombre;
}