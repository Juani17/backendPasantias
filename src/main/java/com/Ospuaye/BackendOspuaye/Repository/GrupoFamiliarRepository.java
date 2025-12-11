package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrupoFamiliarRepository extends BaseRepository<GrupoFamiliar, Long> {
    Optional<GrupoFamiliar> findByTitularId(Long titularId);
    Optional<GrupoFamiliar> findByTitularIdAndActivoTrue(Long titularId);
    boolean existsByNombreGrupoAndTitularId(String nombreGrupo, Long titularId);
    // Activos
    Page<GrupoFamiliar> findByNombreGrupoContainingIgnoreCaseAndActivoTrueOrTitular_NombreContainingIgnoreCaseAndActivoTrueOrTitular_ApellidoContainingIgnoreCaseAndActivoTrue(
            String nombreGrupo, String titularNombre, String titularApellido, Pageable pageable);

    // Inactivos
    Page<GrupoFamiliar> findByNombreGrupoContainingIgnoreCaseAndActivoFalseOrTitular_NombreContainingIgnoreCaseAndActivoFalseOrTitular_ApellidoContainingIgnoreCaseAndActivoFalse(
            String nombreGrupo, String titularNombre, String titularApellido, Pageable pageable);

}
