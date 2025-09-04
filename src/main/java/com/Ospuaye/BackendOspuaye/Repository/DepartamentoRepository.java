package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Departamento;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentoRepository extends BaseRepository<Departamento, Long> {
    List<Departamento> findByProvincia_Id(Long provinciaId);
    List<Departamento> findByActivoTrue();
}
