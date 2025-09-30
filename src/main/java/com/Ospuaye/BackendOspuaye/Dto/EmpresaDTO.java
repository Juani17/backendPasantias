package com.Ospuaye.BackendOspuaye.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    private Long id;
    private String cuit;
    private String razonSocial;
    private Boolean activo;
}
