package com.Ospuaye.BackendOspuaye.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CambiarContrasenaRequest {
    private String actual;
    private String nueva;

}