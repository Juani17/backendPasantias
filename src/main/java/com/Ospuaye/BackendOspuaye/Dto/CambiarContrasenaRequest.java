package com.Ospuaye.BackendOspuaye.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CambiarContrasenaRequest {
    // Getters y setters
    private String email;
    private String actual;
    private String nueva;

}