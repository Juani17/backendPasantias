package com.Ospuaye.BackendOspuaye.Dto;

import lombok.Data;

@Data
public class RegisterBeneficiarioRequest {
    private String email;
    private String contrasena;

    private String nombre;
    private String apellido;
    private Integer dni;
    private Long cuil;
    private Long telefono;
}
