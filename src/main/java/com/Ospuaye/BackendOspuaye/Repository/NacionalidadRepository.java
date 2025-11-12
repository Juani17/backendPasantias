package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Nacionalidad;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NacionalidadRepository extends BaseNombrableRepository<Nacionalidad, Long> {
    Optional<Nacionalidad> findById(Long id);
    Optional<Nacionalidad> findByNombre(String nombre);
}
