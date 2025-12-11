package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaRepository extends BaseRepository<Area, Long> {
    Optional<Area> findByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);

    // Activos
    Page<Area> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre, Pageable pageable);

    // Inactivos
    Page<Area> findByNombreContainingIgnoreCaseAndActivoFalse(String nombre, Pageable pageable);

}
