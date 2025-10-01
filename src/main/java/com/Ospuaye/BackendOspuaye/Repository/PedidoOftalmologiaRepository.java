package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoOftalmologiaRepository extends BaseRepository<PedidoOftalmologia, Long> {
    List<PedidoOftalmologia> findByBeneficiario_Id(Long idBeneficiario);
    List<PedidoOftalmologia> findByMedico_Id(Long idMedico);

}

