package com.Ospuaye.BackendOspuaye.Repository;


import com.Ospuaye.BackendOspuaye.Entity.Base;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;


import java.util.Optional;

public interface BeneficiarioRepository extends BaseRepository<Beneficiario, Long> {
    Optional<Beneficiario> findByDni(Integer dni);

}
