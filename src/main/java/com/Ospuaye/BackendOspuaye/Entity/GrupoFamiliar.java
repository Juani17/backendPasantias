package com.Ospuaye.BackendOspuaye.Entity;

import com.Ospuaye.BackendOspuaye.Entity.Enum.TipoDeBeneficiarioTitular;
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
public class GrupoFamiliar extends Base {

    @Column(nullable = false)
    private String nombreGrupo;

    @EqualsAndHashCode.Exclude
    @OneToOne
    @JoinColumn(name = "titular_id")
    private Beneficiario titular;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_beneficiario_titular")
    private TipoDeBeneficiarioTitular tipoBeneficiarioTitular;

    private Date fechaAlta;

    @OneToMany(mappedBy = "grupoFamiliar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Familiar> familiares = new ArrayList<>();
}
