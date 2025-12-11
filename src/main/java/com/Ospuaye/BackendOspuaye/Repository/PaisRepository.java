package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Pais;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PaisRepository extends BaseRepository<Pais, Long> {

    Optional<Pais> findByNombre(String nombre);
    // Activos
    Page<Pais> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre, Pageable pageable);

    // Inactivos
    Page<Pais> findByNombreContainingIgnoreCaseAndActivoFalse(String nombre, Pageable pageable);

    List<Pais> findByNombreContainingIgnoreCase(String nombre);

}
