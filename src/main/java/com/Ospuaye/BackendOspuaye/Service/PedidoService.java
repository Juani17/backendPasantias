package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Repository.BaseRepository;
import com.Ospuaye.BackendOspuaye.Repository.DocumentoRepository;
import com.Ospuaye.BackendOspuaye.Repository.HistorialMovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public abstract class PedidoService<E extends Pedido> extends BaseService<E, Long> {

    @Autowired
    protected DocumentoRepository documentoRepository;

    @Autowired
    protected HistorialMovimientoRepository historialRepository;

    public PedidoService(BaseRepository<E, Long> baseRepository) {
        super(baseRepository);
    }

    @Transactional
    public void agregarDocumentos(E pedido, List<Documento> documentos, Usuario usuario) {
        for (Documento doc : documentos) {
            doc.setPedido(pedido);
            doc.setSubidoPor(usuario);
            doc.setFechaSubida(new Date());
            documentoRepository.save(doc);
        }
    }

    @Transactional
    public void registrarMovimiento(E pedido, Estado estado, Usuario usuario, String comentario) {
        HistorialMovimiento historial = HistorialMovimiento.builder()
                .pedido(pedido)
                .fecha(new Date())
                .tipoMovimiento(estado)
                .usuario(usuario)
                .comentario(comentario)
                .build();
        historialRepository.save(historial);
    }
    public List<E> findAll() {
        return baseRepository.findAll();
    }
}
