    package com.Ospuaye.BackendOspuaye.Entity;

    import jakarta.persistence.*;
    import lombok.*;
    import lombok.experimental.SuperBuilder;

    @Entity
    @Table(name = "provincias")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public class Provincia extends Base {

        private String nombre;

        @ManyToOne
        @JoinColumn(name = "pais_id")
        private Pais pais;
    }
