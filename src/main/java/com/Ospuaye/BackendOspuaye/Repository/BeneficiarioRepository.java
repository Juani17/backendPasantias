package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Empresa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiarioRepository extends BaseRepository<Beneficiario, Long> {
    Optional<Beneficiario> findByPersona_Id(Long id);

    List<Beneficiario> findByEmpresa(Empresa empresa);

    Optional<Beneficiario> findByPersona_Dni(Integer dni);

    List<Beneficiario> findByAfiliadoSindicalTrue();

    boolean existsByUsuario_Id(Long usuarioId);

    boolean existsByPersona_Id(Long personaId);

    Optional<Beneficiario> findByUsuario_Id(Long usuarioId);
}
