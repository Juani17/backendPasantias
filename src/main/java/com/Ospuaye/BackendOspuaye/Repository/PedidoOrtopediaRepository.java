package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PedidoOrtopediaRepository extends PedidoRepository<PedidoOrtopedia> {

    List<PedidoOrtopedia> findByBeneficiario(Beneficiario b);

    List<PedidoOrtopedia> findByEstado(Estado estado);

    List<PedidoOrtopedia> findByFechaIngresoBetween(Date inicio, Date fin);
}
