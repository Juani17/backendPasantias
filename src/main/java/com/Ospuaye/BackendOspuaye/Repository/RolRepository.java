package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends BaseRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
    // Activos
    Page<Rol> findByNombreContainingIgnoreCaseAndActivoTrueOrArea_NombreContainingIgnoreCaseAndActivoTrue(
            String nombre, String areaNombre, Pageable pageable);

    // Inactivos
    Page<Rol> findByNombreContainingIgnoreCaseAndActivoFalseOrArea_NombreContainingIgnoreCaseAndActivoFalse(
            String nombre, String areaNombre, Pageable pageable);



}
