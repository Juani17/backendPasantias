package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Provincia;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProvinciaRepository extends BaseRepository<Provincia, Long> {

    Optional<Provincia> findByNombre(String nombre);
    Optional<Provincia> findById(Long id);
    // Activos
    Page<Provincia> findByNombreContainingIgnoreCaseAndActivoTrueOrPais_NombreContainingIgnoreCaseAndActivoTrue(
            String nombre, String paisNombre, Pageable pageable);

    // Inactivos
    Page<Provincia> findByNombreContainingIgnoreCaseAndActivoFalseOrPais_NombreContainingIgnoreCaseAndActivoFalse(
            String nombre, String paisNombre, Pageable pageable);

    List<Provincia> findByNombreContainingIgnoreCase(String nombre);


}
