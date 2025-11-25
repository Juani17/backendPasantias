package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PedidoOftalmologiaRepository extends JpaRepository<PedidoOftalmologia, Long> {
    List<PedidoOftalmologia> findByBeneficiario(Beneficiario beneficiario);
    List<PedidoOftalmologia> findByMedico(Medico medico);
    Page<PedidoOftalmologia> findByBeneficiario_NombreContainingIgnoreCaseOrGrupoFamiliar_NombreGrupoContainingIgnoreCaseOrEmpresaContainingIgnoreCaseOrDelegacionContainingIgnoreCaseOrPaciente_NombreContainingIgnoreCaseOrMedico_NombreContainingIgnoreCaseOrMotivoConsultaContainingIgnoreCase(
            String beneficiario,
            String grupoFamiliar,
            String empresa,
            String delegacion,
            String paciente,
            String medico,
            String motivoConsulta,
            Pageable pageable
    );
    List<PedidoOftalmologia> findByMedicoIsNull();


}
