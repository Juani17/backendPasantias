package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public abstract class PedidoService<E extends Pedido> extends BaseService<E, Long> {

    @Autowired protected DocumentoRepository documentoRepository;
    @Autowired protected HistorialMovimientoRepository historialRepository;

    @Autowired protected BeneficiarioRepository beneficiarioRepository;
    @Autowired protected GrupoFamiliarRepository grupoFamiliarRepository;
    @Autowired protected UsuarioRepository usuarioRepository;
    @Autowired protected MedicoRepository medicoRepository;

    public PedidoService(BaseRepository<E, Long> baseRepository) {
        super(baseRepository);
    }

    // Validaciones comunes para crear/actualizar pedido
    protected void validarPedidoComun(Pedido p) throws Exception {
        if (p.getNombre() == null || p.getNombre().isBlank())
            throw new Exception("El nombre del pedido es obligatorio");

        if (p.getUsuario() == null || p.getUsuario().getId() == null
                || !usuarioRepository.existsById(p.getUsuario().getId()))
            throw new Exception("El usuario que crea el pedido no existe");

        if (p.getBeneficiario() != null) {
            if (p.getBeneficiario().getId() == null ||
                    !beneficiarioRepository.existsById(p.getBeneficiario().getId()))
                throw new Exception("El beneficiario no existe");
        }

        if (p.getGrupoFamiliar() != null) {
            if (p.getGrupoFamiliar().getId() == null ||
                    !grupoFamiliarRepository.existsById(p.getGrupoFamiliar().getId()))
                throw new Exception("El grupo familiar no existe");
        }

        if (p.getMedico() != null) {
            if (p.getMedico().getId() == null ||
                    !medicoRepository.existsById(p.getMedico().getId()))
                throw new Exception("El médico no existe");
        }

        if (p.getDni() != null && (p.getDni() < 1_000_000 || p.getDni() > 99_999_999))
            throw new Exception("El DNI del pedido debe tener entre 7 y 8 dígitos");
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
