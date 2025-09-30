package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository<E extends Pedido> extends BaseRepository<E, Long> {

    List<E> findByBeneficiario(Beneficiario beneficiario);

    List<E> findByGrupoFamiliar(GrupoFamiliar grupoFamiliar);

    List<E> findByUsuario(Usuario usuario);

    List<E> findByMedico(Medico medico);

    List<E> findByDni(Integer dni);
}

