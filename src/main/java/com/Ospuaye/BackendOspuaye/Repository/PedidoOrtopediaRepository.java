package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PedidoOrtopediaRepository extends JpaRepository<PedidoOrtopedia, Long> {
    List<PedidoOrtopedia> findByBeneficiario(Beneficiario beneficiario);
    List<PedidoOrtopedia> findByMedico(Medico medico);
    // Activos
    Page<PedidoOrtopedia> findByBeneficiario_NombreContainingIgnoreCaseAndActivoTrueOrGrupoFamiliar_NombreGrupoContainingIgnoreCaseAndActivoTrueOrEmpresaContainingIgnoreCaseAndActivoTrueOrDelegacionContainingIgnoreCaseAndActivoTrueOrPaciente_NombreContainingIgnoreCaseAndActivoTrueOrMedico_NombreContainingIgnoreCaseAndActivoTrueOrMotivoConsultaContainingIgnoreCaseAndActivoTrue(
            String beneficiario, String grupoFamiliar, String empresa, String delegacion, String paciente, String medico, String motivoConsulta, Pageable pageable);

    // Inactivos
    Page<PedidoOrtopedia> findByBeneficiario_NombreContainingIgnoreCaseAndActivoFalseOrGrupoFamiliar_NombreGrupoContainingIgnoreCaseAndActivoFalseOrEmpresaContainingIgnoreCaseAndActivoFalseOrDelegacionContainingIgnoreCaseAndActivoFalseOrPaciente_NombreContainingIgnoreCaseAndActivoFalseOrMedico_NombreContainingIgnoreCaseAndActivoFalseOrMotivoConsultaContainingIgnoreCaseAndActivoFalse(
            String beneficiario, String grupoFamiliar, String empresa, String delegacion, String paciente, String medico, String motivoConsulta, Pageable pageable);
    List<PedidoOrtopedia> findByMedicoIsNull();


}
