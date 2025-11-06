package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoOrtopediaRepository extends JpaRepository<PedidoOrtopedia, Long> {
    List<PedidoOrtopedia> findByBeneficiario(Beneficiario beneficiario);
    List<PedidoOrtopedia> findByMedico(Medico medico);
}
