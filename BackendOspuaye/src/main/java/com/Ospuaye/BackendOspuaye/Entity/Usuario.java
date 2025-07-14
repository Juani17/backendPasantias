package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "usuarios")
public class Usuario extends Base {

    private String nombre;
    private String apellido;
    private Integer dni;
    private String email;
    private String contrasenia;
    private Integer telefono;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private InterfaceRol rol;

    @OneToMany(mappedBy = "usuario")
    private List<FormularioPodologia> formulariosPodologia;

    @OneToMany(mappedBy = "usuario")
    private List<FormularioOftalmologia> formulariosOftalmologia;

    @OneToMany(mappedBy = "usuario")
    private List<HistorialMovimiento> historialMovimientos;

    @OneToMany(mappedBy = "subidoPor")
    private List<Documento> documentosSubidos;
}