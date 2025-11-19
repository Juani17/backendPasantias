package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Departamento;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends BaseRepository<Departamento, Long> {
    List<Departamento> findByProvincia_Id(Long provinciaId);
    List<Departamento> findByActivoTrue();
    Optional<Departamento> findByNombre(String nombre);
}
