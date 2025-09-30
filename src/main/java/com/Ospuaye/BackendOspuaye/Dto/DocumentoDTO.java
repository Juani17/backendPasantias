package com.Ospuaye.BackendOspuaye.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoDTO {
    private Long id;
    private String nombreArchivo; // antes tipo/url
    private String path;
    private String observacion;
}
