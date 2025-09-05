package com.Ospuaye.BackendOspuaye.Dto;

import com.Ospuaye.BackendOspuaye.Entity.Enum.EstadoPersona;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Sexo;
import lombok.Data;

@Data
public class RegisterBeneficiarioRequest {
    private String email;
    private String contrasena;

    private String nombre;
    private String apellido;
    private Integer dni;
    private String cuil;
    private String telefono;
    private Sexo sexo;
    private EstadoPersona estado;

    private Boolean afiliadoSindical;
    private Boolean esJubilado;
    private Long empresaId; // opcional
}
