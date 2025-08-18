package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaRepository extends BaseRepository<Area, Long> {
    Optional<Object> findByNombre(String nombre, Sort sort, Limit limit);

    Optional<Area> findByNombre(String nombre);

    Optional<Object> findByNombre(String nombre, Pageable pageable);
}
