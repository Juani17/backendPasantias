package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "personas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Persona extends Base {

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private Integer dni;

    @Column(unique = true)
    private String cuil;

    private String telefono;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    private Boolean activo;

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
