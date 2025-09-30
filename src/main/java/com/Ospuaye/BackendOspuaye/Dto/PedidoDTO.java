package com.Ospuaye.BackendOspuaye.Dto;

import lombok.*;

import java.util.Date;
import java.util.List;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PedidoDTO {
    private Long id;
    private String nombre;
    private Long dni;
    private Long telefono;
    // PedidoDTO.java

    private EmpresaDTO empresa; // cambia de String a EmpresaDTO

    private String delegacion;
    private Date fechaIngreso;
    private Estado estado;
    private Date fechaRevision;
    private String observacionMedico;

    private BeneficiarioDTO beneficiario;
    private GrupoFamiliarDTO grupoFamiliar;
    private FamiliarDTO paciente;
    private MedicoDTO medico;
    private UsuarioDTO usuario;
    private List<DocumentoDTO> documentos;
}
