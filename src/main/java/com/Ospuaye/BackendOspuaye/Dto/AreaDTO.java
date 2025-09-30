package com.Ospuaye.BackendOspuaye.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para la entidad Area
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaDTO {
    private Long id;       // Heredado de Base
    private String nombre; // Nombre del área
}
