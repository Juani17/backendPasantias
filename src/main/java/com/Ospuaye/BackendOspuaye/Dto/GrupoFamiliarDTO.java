package com.Ospuaye.BackendOspuaye.Dto;

import lombok.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GrupoFamiliarDTO {
    private Long id;
    private String nombreGrupo;
    private Date fechaAlta;
    private Boolean activo;
    private BeneficiarioDTO titular;
    private List<FamiliarDTO> familiares;
}
