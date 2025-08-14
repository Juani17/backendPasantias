package com.Ospuaye.BackendOspuaye.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class GrupoFamiliar extends Base {

    @Column(nullable = false)
    private String nombreGrupo;

    @ManyToOne
    @JoinColumn(name = "titular_id")
    private Beneficiario titular;

    private Date fechaAlta;
    private Boolean activo;

    @OneToMany(mappedBy = "grupoFamiliar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Familiar> familiares = new ArrayList<>();

}
