package com.Ospuaye.BackendOspuaye.Dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String contrasena;
}