package com.Ospuaye.BackendOspuaye.Repository;


import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamiliarRepository extends BaseRepository<Familiar, Long>{
    List<Familiar> findByBeneficiarioId(Long beneficiarioId);
    Optional<Familiar> findByDni(Long dni);

}
