package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "medicos")
public class Medico extends Base {

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String matricula;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;
}