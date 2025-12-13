package com.Ospuaye.BackendOspuaye.Dto;

import lombok.Data;
import java.util.Date;

@Data
public class DocumentoDTO {
    private Long id;
    private String nombreArchivo;
    private String url;
    private String path;
    private String observacion;
    private Date fechaSubida;
    private String subidoPor; // Email del usuario
}