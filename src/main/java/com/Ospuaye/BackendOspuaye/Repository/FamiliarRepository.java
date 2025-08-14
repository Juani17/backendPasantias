package com.Ospuaye.BackendOspuaye.Repository;


import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamiliarRepository extends BaseRepository<Familiar, Long>{
    List<Familiar> findByBeneficiarioId(Long beneficiarioId);

}
