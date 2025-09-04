package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends BaseRepository<Pedido, Long> {

    List<Pedido> findByBeneficiario(Beneficiario beneficiario);

    List<Pedido> findByGrupoFamiliar(GrupoFamiliar grupoFamiliar);

    List<Pedido> findByUsuario(Usuario usuario);

    List<Pedido> findByMedico(Medico medico);

    List<Pedido> findByDni(Integer dni);
}
