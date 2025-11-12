package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Provincia;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProvinciaRepository extends BaseNombrableRepository<Provincia, Long> {

    Optional<Provincia> findByNombre(String nombre);
    Optional<Provincia> findById(Long id);

}
