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
public class PedidoInputDTO {
    private String nombre;
    private Long dni;
    private Long telefono;
    private Long beneficiarioId;
    private Long grupoFamiliarId;
    private Long pacienteId;
    private Long medicoId;
    private String delegacion;
    private String observacionMedico;
    private List<Long> documentoIds; // se pasan IDs de documentos existentes o nuevos
    private Boolean recetaMedica; // para Ortopedia/Oftalmología
    private Boolean usaLentes; // solo para Oftalmología
    private String motivoConsulta; // obligatorio
}
