package com.Ospuaye.BackendOspuaye.Dto;

import lombok.Data;

@Data
public class RegisterMedicoRequest {
    private String email;
    private String contrasena;
    private String matricula;
    private Long areaId;
}