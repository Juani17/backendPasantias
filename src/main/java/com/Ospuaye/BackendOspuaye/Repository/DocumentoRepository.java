package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends BaseRepository<Documento, Long> {
    List<Documento> findByPedidoId(Long pedidoId);
}
