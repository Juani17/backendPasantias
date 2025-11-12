package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaRepository extends BaseNombrableRepository<Area, Long> {
    Optional<Area> findByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}
