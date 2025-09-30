package com.Ospuaye.BackendOspuaye.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "grupos_familiares")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class GrupoFamiliar extends Base {

    @Column(nullable = false)
    private String nombreGrupo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "titular_id")
    @JsonIgnoreProperties({"grupoFamiliar"}) // evita loop con Beneficiario
    private Beneficiario titular;

    private Date fechaAlta;
    private Boolean activo;

    @OneToMany(mappedBy = "grupoFamiliar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Familiar> familiares = new ArrayList<>();

    // ---------------- Getters y Setters ----------------

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public void setTitular(Beneficiario titular) {
        this.titular = titular;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setFamiliares(List<Familiar> familiares) {
        this.familiares = familiares;
    }
}
