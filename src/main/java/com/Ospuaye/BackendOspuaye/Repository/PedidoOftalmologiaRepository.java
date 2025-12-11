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
    // Activos
    Page<PedidoOftalmologia> findByBeneficiario_NombreContainingIgnoreCaseAndActivoTrueOrGrupoFamiliar_NombreGrupoContainingIgnoreCaseAndActivoTrueOrEmpresaContainingIgnoreCaseAndActivoTrueOrDelegacionContainingIgnoreCaseAndActivoTrueOrPaciente_NombreContainingIgnoreCaseAndActivoTrueOrMedico_NombreContainingIgnoreCaseAndActivoTrueOrMotivoConsultaContainingIgnoreCaseAndActivoTrue(
            String beneficiario, String grupoFamiliar, String empresa, String delegacion, String paciente, String medico, String motivoConsulta, Pageable pageable);

    // Inactivos
    Page<PedidoOftalmologia> findByBeneficiario_NombreContainingIgnoreCaseAndActivoFalseOrGrupoFamiliar_NombreGrupoContainingIgnoreCaseAndActivoFalseOrEmpresaContainingIgnoreCaseAndActivoFalseOrDelegacionContainingIgnoreCaseAndActivoFalseOrPaciente_NombreContainingIgnoreCaseAndActivoFalseOrMedico_NombreContainingIgnoreCaseAndActivoFalseOrMotivoConsultaContainingIgnoreCaseAndActivoFalse(
            String beneficiario, String grupoFamiliar, String empresa, String delegacion, String paciente, String medico, String motivoConsulta, Pageable pageable);


List<PedidoOftalmologia> findByMedicoIsNull();


}
