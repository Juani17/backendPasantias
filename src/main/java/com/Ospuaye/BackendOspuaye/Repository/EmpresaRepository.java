package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Empresa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface EmpresaRepository extends BaseRepository<Empresa, Long> {

    Optional<Empresa> findByCuit(String cuit);

    List<Empresa> findByActivo(Boolean activo);

    boolean existsByCuit(String cuit);

    // Activos
    Page<Empresa> findByCuitContainingIgnoreCaseAndActivoTrueOrRazonSocialContainingIgnoreCaseAndActivoTrueOrDomicilio_CalleContainingIgnoreCaseAndActivoTrue(
            String cuit, String razonSocial, String calle, Pageable pageable);

    // Inactivos
    Page<Empresa> findByCuitContainingIgnoreCaseAndActivoFalseOrRazonSocialContainingIgnoreCaseAndActivoFalseOrDomicilio_CalleContainingIgnoreCaseAndActivoFalse(
            String cuit, String razonSocial, String calle, Pageable pageable);


}
