package com.Ospuaye.BackendOspuaye.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioDTO {
    private Long id;
    private String email;
    private String rol; // eliminé username que no existía
}
