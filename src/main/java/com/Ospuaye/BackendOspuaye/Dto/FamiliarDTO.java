package com.Ospuaye.BackendOspuaye.Dto;


import com.Ospuaye.BackendOspuaye.Entity.Enum.Sexo;
import com.Ospuaye.BackendOspuaye.Entity.Enum.TipoParentesco;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FamiliarDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Long dni;
    private String correoElectronico;
    private Sexo sexo;
    private TipoParentesco tipoParentesco;
    private BeneficiarioDTO beneficiario;
}
