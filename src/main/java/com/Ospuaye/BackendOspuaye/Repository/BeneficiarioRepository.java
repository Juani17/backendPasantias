package com.Ospuaye.BackendOspuaye.Repository;


import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface BeneficiarioRepository extends BaseRepository<Beneficiario, Long> {
    Optional<Beneficiario> findByDni(Integer dni);

}
