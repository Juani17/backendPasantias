package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Localidad;
import com.Ospuaye.BackendOspuaye.Entity.Departamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalidadRepository extends BaseNombrableRepository<Localidad, Long> {
    List<Localidad> findByDepartamento(Departamento departamento);
    List<Localidad> findByActivoTrue();
    Optional<Localidad> findByNombre(String nombre);
    boolean existsByNombreAndDepartamento_Id(String nombre, Long departamentoId);
    Page<Localidad> findByNombreContainingIgnoreCaseOrCodigoPostalContainingIgnoreCase(
            String nombre,
            String codigoPostal,
            Pageable pageable
    );
}
