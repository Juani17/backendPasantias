package com.Ospuaye.BackendOspuaye.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BeneficiarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Long dni;
    private String correoElectronico;
    private Boolean afiliadoSindical;
    private Boolean esJubilado;
    private UsuarioDTO usuario;
    private String empresa; // r
}
