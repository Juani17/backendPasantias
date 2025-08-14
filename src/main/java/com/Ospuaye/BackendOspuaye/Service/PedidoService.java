package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Estado;
import com.Ospuaye.BackendOspuaye.Entity.HistorialMovimiento;
import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.BaseRepository;
import com.Ospuaye.BackendOspuaye.Repository.DocumentoRepository;
import com.Ospuaye.BackendOspuaye.Repository.HistorialMovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;

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

    protected void agregarDocumentos(E pedido, List<Documento> documentos, Usuario usuario) throws Exception {
        if (pedido == null) throw new Exception("Pedido nulo al agregar documentos");
        if (documentos == null || documentos.isEmpty()) return;
        for (Documento doc : documentos) {
            if (doc == null) continue;
            doc.setPedido(pedido);
            doc.setSubidoPor(usuario);
            doc.setFechaSubida(new Date());
            documentoRepository.save(doc);
        }
    }

    protected void registrarMovimiento(E pedido, Estado estado, Usuario usuario, String comentario) throws Exception {
        if (pedido == null) throw new Exception("Pedido nulo al registrar movimiento");
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
