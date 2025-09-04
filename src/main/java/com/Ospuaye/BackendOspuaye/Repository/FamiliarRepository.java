package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamiliarRepository extends BaseRepository<Familiar, Long> {
    List<Familiar> findByGrupoFamiliar(GrupoFamiliar grupoFamiliar);
    List<Familiar> findByBeneficiario(Beneficiario beneficiario);
    Optional<Familiar> findByPersona_Id(Long personaId);
}
