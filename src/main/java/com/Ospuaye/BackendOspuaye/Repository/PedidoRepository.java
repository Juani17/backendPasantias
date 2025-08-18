package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Pedido;

public interface PedidoRepository extends BaseRepository<Pedido, Long>{
    boolean existsById(Long id);
}
