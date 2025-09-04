package com.Ospuaye.BackendOspuaye.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Empresa extends Base {

    @Column(nullable = false, unique = true)
    private String cuit;

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    private Boolean activo;

    // 🔗 Relación inversa con Beneficiarios
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonBackReference
    private Set<Beneficiario> beneficiarios = new HashSet<>();
}
