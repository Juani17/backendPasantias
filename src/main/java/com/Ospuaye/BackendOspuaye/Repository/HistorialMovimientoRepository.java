package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.HistorialMovimiento;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface HistorialMovimientoRepository extends BaseRepository<HistorialMovimiento, Long> {
    List<HistorialMovimiento> findByUsuario(Usuario usuario);
    List<HistorialMovimiento> findByPedido(Pedido pedido);
    List<HistorialMovimiento> findByEstado(Estado estado);
    List<HistorialMovimiento> findByFechaBetween(Date inicio, Date fin);
}
