package com.Ospuaye.BackendOspuaye.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DomicilioDTO {
    private Long id;
    private String calle;
    private String numeracion; // antes era numero
    private String barrio;
    private String manzanaPiso;
    private String casaDepartamento;
    private String referencia;
    private String ciudad;       // desde Localidad
    private String provincia;    // desde Departamento
    private String pais;         // si tenés entidad Pais, o dejar null
    private String tipo;         // URBANO o RURAL
}
