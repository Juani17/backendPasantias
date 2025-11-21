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

    Page<Area> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
