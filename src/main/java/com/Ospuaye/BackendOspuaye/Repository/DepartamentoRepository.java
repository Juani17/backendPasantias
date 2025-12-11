package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Departamento;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface DepartamentoRepository extends BaseRepository<Departamento, Long> {
    List<Departamento> findByProvincia_Id(Long provinciaId);
    List<Departamento> findByActivoTrue();
    Optional<Departamento> findByNombre(String nombre);
    // Activos
    Page<Departamento> findByNombreContainingIgnoreCaseAndActivoTrueOrProvincia_NombreContainingIgnoreCaseAndActivoTrue(
            String nombre, String provinciaNombre, Pageable pageable);

    // Inactivos
    Page<Departamento> findByNombreContainingIgnoreCaseAndActivoFalseOrProvincia_NombreContainingIgnoreCaseAndActivoFalse(
            String nombre, String provinciaNombre, Pageable pageable);
    List<Departamento> findByNombreContainingIgnoreCase(String nombre);

}
