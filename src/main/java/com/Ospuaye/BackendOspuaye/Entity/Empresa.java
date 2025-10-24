package com.Ospuaye.BackendOspuaye.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference(value = "empresa-beneficiarios")
    private Set<Beneficiario> beneficiarios = new HashSet<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "domicilio_id", referencedColumnName = "id")
    @JsonManagedReference(value = "empresa-domicilio")
    private Domicilio domicilio;
}
