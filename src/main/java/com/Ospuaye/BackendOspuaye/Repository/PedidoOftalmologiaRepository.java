package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PedidoOftalmologiaRepository extends PedidoRepository<PedidoOftalmologia> {

    List<PedidoOftalmologia> findByBeneficiario(Beneficiario b);

    List<PedidoOftalmologia> findByEstado(Estado estado);

    List<PedidoOftalmologia> findByFechaIngresoBetween(Date inicio, Date fin);
}
