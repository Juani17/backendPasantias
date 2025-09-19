package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
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
    @Autowired protected FamiliarRepository familiarRepository;

    public PedidoService(BaseRepository<E, Long> baseRepository) {
        super(baseRepository);
    }

    protected void validarPedidoComun(Pedido p) throws Exception {
        if (p == null) throw new Exception("El pedido no puede ser nulo");

        if (p.getNombre() == null || p.getNombre().isBlank())
            throw new Exception("El nombre del pedido es obligatorio");

        // ✅ Validar beneficiario y obtener usuario desde ahí
        if (p.getBeneficiario() != null) {
            if (p.getBeneficiario().getId() == null)
                throw new Exception("El beneficiario tiene ID inválido");

            Beneficiario b = beneficiarioRepository.findById(p.getBeneficiario().getId())
                    .orElseThrow(() -> new Exception("El beneficiario no existe"));
            p.setBeneficiario(b);

            if (b.getUsuario() != null) {
                p.setUsuario(b.getUsuario()); // usuario viene del beneficiario
            } else {
                throw new Exception("El beneficiario no tiene usuario asociado");
            }
        } else {
            throw new Exception("El pedido debe tener un beneficiario");
        }

        if (p.getGrupoFamiliar() != null) {
            if (p.getGrupoFamiliar().getId() == null)
                throw new Exception("El grupo familiar tiene ID inválido");
            GrupoFamiliar gf = grupoFamiliarRepository.findById(p.getGrupoFamiliar().getId())
                    .orElseThrow(() -> new Exception("El grupo familiar no existe"));
            p.setGrupoFamiliar(gf);
        }

        if (p.getMedico() != null) {
            if (p.getMedico().getId() == null)
                throw new Exception("El médico tiene ID inválido");
            Medico m = medicoRepository.findById(p.getMedico().getId())
                    .orElseThrow(() -> new Exception("El médico no existe"));
            p.setMedico(m);
        }

        if (p.getDni() != null && (p.getDni() < 1_000_000 || p.getDni() > 99_999_999))
            throw new Exception("El DNI del pedido debe tener entre 7 y 8 dígitos");

        if (p.getPaciente() != null) {
            if (p.getPaciente().getId() == null)
                throw new Exception("El paciente tiene ID inválido");
            Familiar f = familiarRepository.findById(p.getPaciente().getId())
                    .orElseThrow(() -> new Exception("El paciente no existe"));
            p.setPaciente(f);
        }

        // Validación de documentos
        if (p.getDocumentos() != null) {
            for (Documento doc : p.getDocumentos()) {
                if (doc.getNombreArchivo() == null || doc.getNombreArchivo().isBlank())
                    throw new Exception("Todos los documentos deben tener un nombre de archivo");
            }
        }
    }


    @Transactional
    public void agregarDocumentos(E pedido, List<Documento> documentos, Usuario usuario) throws Exception {
        if (pedido == null) throw new Exception("Pedido es obligatorio");
        if (usuario == null || usuario.getId() == null)
            throw new Exception("Usuario que sube los documentos es obligatorio");
        Usuario u = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        if (documentos == null || documentos.isEmpty())
            throw new Exception("Debe proporcionar al menos un documento");

        for (Documento doc : documentos) {
            if (doc.getNombreArchivo() == null || doc.getNombreArchivo().isBlank())
                throw new Exception("Cada documento debe tener un nombre de archivo");
            doc.setPedido(pedido);
            doc.setSubidoPor(u);
            doc.setFechaSubida(new Date());
            documentoRepository.save(doc);
        }
    }

    @Transactional
    public void registrarMovimiento(E pedido, Estado estado, Usuario usuario, String comentario) throws Exception {
        if (pedido == null) throw new Exception("Pedido es obligatorio para registrar movimiento");
        if (estado == null) throw new Exception("Estado es obligatorio para registrar movimiento");
        if (usuario == null || usuario.getId() == null)
            throw new Exception("Usuario es obligatorio para registrar movimiento");

        Usuario u = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        HistorialMovimiento historial = HistorialMovimiento.builder()
                .pedido(pedido)
                .fecha(new Date())
                .estado(estado)
                .usuario(u)
                .comentario(comentario)
                .build();
        historialRepository.save(historial);
    }

    @Transactional(readOnly = true)
    public List<E> findAll() {
        return baseRepository.findAll();
    }
}
