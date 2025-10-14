package com.Ospuaye.BackendOspuaye.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Base implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(nullable = false)
    private Boolean activo = true;

    @PrePersist
    public void prePersist() {
        // Esto se ejecuta antes de que Hibernate haga el INSERT
        if (this.activo == null) {
            this.activo = true;
        }
    }
}
