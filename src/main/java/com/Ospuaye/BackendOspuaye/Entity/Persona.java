package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.EstadoPersona;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Sexo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Persona extends Base {

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private Long dni;

    @Column(unique = true)
    private Long cuil;

    private Long telefono;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_persona")
    private EstadoPersona estado;

    @ManyToOne
    @JoinColumn(name = "nacionalidad_id")
    private Nacionalidad nacionalidad;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "domicilio_id")
    private Domicilio domicilio;
}
