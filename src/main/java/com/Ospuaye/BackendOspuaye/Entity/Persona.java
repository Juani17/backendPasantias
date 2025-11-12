package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.EstadoPersona;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Sexo;
import com.Ospuaye.BackendOspuaye.Entity.Enum.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Persona extends BaseNombrable {

    private String nombre;
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @Column
    private Long dni;

    @Column
    private Long cuil;

    private Long telefono;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_persona")
    private EstadoPersona estado;

    @ManyToOne
    @JoinColumn(name = "nacionalidad_id")
    private Nacionalidad nacionalidad;

    @ManyToOne
    @JoinColumn(name = "domicilio_id")
    private Domicilio domicilio;
}
