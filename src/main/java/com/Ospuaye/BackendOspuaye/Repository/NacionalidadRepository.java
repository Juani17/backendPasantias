package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Nacionalidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NacionalidadRepository extends BaseRepository<Nacionalidad, Long> {
    Optional<Nacionalidad> findById(Long id);
    Optional<Nacionalidad> findByNombre(String nombre);
    Page<Nacionalidad> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

}
