package com.Ospuaye.BackendOspuaye.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedicoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Long dni;
    private String matricula;
    private UsuarioDTO usuario;
    private String area; // nom
}
