package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrupoFamiliarRepository extends BaseRepository<GrupoFamiliar, Long> {
    Optional<GrupoFamiliar> findByTitularId(Long titularId);
    Optional<GrupoFamiliar> findByTitularIdAndActivoTrue(Long titularId);
    boolean existsByNombreGrupoAndTitularId(String nombreGrupo, Long titularId);
}
