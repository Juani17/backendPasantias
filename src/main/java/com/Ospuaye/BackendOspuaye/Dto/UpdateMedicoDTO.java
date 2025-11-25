package com.Ospuaye.BackendOspuaye.Dto;

import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Entity.Enum.EstadoPersona;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Sexo;
import lombok.Data;

@Data
public class UpdateMedicoDTO {
    private String nombre;
    private String apellido;
    private Long dni;
    private Long cuil;
    private Long telefono;
    private Sexo sexo;
    private EstadoPersona estado;
    private String matricula;
    private Long areaId;

    private String email;
    private String contrasena; // opcional: actualizar solo si viene
}