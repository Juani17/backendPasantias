package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "medicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Medico extends Base {

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String matricula;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;
}
