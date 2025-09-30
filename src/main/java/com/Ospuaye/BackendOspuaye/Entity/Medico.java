package com.Ospuaye.BackendOspuaye.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "medicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Medico extends Persona {

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    private String matricula;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "area_id")
    private Area area;
}
