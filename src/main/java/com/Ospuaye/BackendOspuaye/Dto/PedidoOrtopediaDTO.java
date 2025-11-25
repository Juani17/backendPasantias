package com.Ospuaye.BackendOspuaye.Dto;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Medico;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PedidoOrtopediaDTO {
    private Long id;
    private String nombre;
    private String motivoConsulta;
    private Boolean recetaMedica;
    private Date fechaRevision;
    private String observacionMedico;
    private Beneficiario beneficiario;
    private Long dni;
    private Long telefono;
    private String empresa;
    private String delegacion;
    private Medico medico;

    private List<DocumentoDTO> documentos;
}


