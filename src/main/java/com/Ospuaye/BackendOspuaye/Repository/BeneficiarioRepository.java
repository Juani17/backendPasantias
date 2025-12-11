package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Empresa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiarioRepository extends BaseRepository<Beneficiario, Long> {

    List<Beneficiario> findByEmpresa(Empresa empresa);

    Optional<Beneficiario> findByDni(Long dni);

    List<Beneficiario> findByAfiliadoSindicalTrue();

    boolean existsByUsuario_Id(Long usuarioId);

    Optional<Beneficiario> findByUsuario_Id(Long usuarioId);

    Optional<Beneficiario> findByCuil(Long cuil);

    Page<Beneficiario> findByActivoTrue(Pageable pageable);

    Page<Beneficiario> findByActivoFalse(Pageable pageable);

    // 🔍 Buscar por nombre o apellido

    // Activos
    Page<Beneficiario> findByDniAndActivoTrue(Long dni, Pageable pageable);
    Page<Beneficiario> findByNombreContainingIgnoreCaseAndActivoTrueOrApellidoContainingIgnoreCaseAndActivoTrue(
            String nombre, String apellido, Pageable pageable);

    // Inactivos
    Page<Beneficiario> findByDniAndActivoFalse(Long dni, Pageable pageable);
    Page<Beneficiario> findByNombreContainingIgnoreCaseAndActivoFalseOrApellidoContainingIgnoreCaseAndActivoFalse(
            String nombre, String apellido, Pageable pageable);

    List<Beneficiario> findTop20ByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrDniEqualsOrCuilEquals(
            String nombre,
            String apellido,
            Long dni,
            Long cuil
    );

}

