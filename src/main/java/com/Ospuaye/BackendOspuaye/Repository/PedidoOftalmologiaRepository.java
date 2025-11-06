package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoOftalmologiaRepository extends JpaRepository<PedidoOftalmologia, Long> {
    List<PedidoOftalmologia> findByBeneficiario(Beneficiario beneficiario);
    List<PedidoOftalmologia> findByMedico(Medico medico);
}
