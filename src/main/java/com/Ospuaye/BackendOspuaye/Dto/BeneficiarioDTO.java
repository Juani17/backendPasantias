package com.Ospuaye.BackendOspuaye.Dto;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import lombok.Getter;

@Getter
public class BeneficiarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer dni;
    private Long cuil;
    private Long telefono;
    private Long usuarioId;
    private String email;
    private Long grupoFamiliarId;
    private String nombreGrupo;

    public BeneficiarioDTO(Beneficiario b) {
        this.id = b.getId();
        this.nombre = b.getNombre();
        this.apellido = b.getApellido();
        this.dni = b.getDni();
        this.cuil = b.getCuil();
        this.telefono = b.getTelefono();
        this.usuarioId = b.getUsuario().getId();
        this.email = b.getUsuario().getEmail();

        if (b.getGrupoFamiliar() != null) {
            this.grupoFamiliarId = b.getGrupoFamiliar().getId();
            this.nombreGrupo = b.getGrupoFamiliar().getNombreGrupo();
        }
    }
}

