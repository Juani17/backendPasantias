package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Dto.PedidoDTO;
import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    // ================= Validaciones Comunes =================
    protected void validarPedidoComun(Pedido p) throws Exception {
        if (p == null) throw new Exception("El pedido no puede ser nulo");
        if (p.getNombre() == null || p.getNombre().isBlank())
            throw new Exception("El nombre del pedido es obligatorio");

        // Beneficiario obligatorio y existente
        if (p.getBeneficiario() == null || p.getBeneficiario().getId() == null)
            throw new Exception("El pedido debe tener un beneficiario válido");
        Beneficiario b = beneficiarioRepository.findById(p.getBeneficiario().getId())
                .orElseThrow(() -> new Exception("El beneficiario no existe"));
        p.setBeneficiario(b);

        // Usuario asociado al beneficiario
        if (b.getUsuario() != null) p.setUsuario(b.getUsuario());
        else throw new Exception("El beneficiario no tiene usuario asociado");

        // Grupo familiar (opcional)
        if (p.getGrupoFamiliar() != null && p.getGrupoFamiliar().getId() != null) {
            GrupoFamiliar gf = grupoFamiliarRepository.findById(p.getGrupoFamiliar().getId())
                    .orElseThrow(() -> new Exception("El grupo familiar no existe"));
            p.setGrupoFamiliar(gf);
        }

        // Médico (opcional)
        if (p.getMedico() != null && p.getMedico().getId() != null) {
            Medico m = medicoRepository.findById(p.getMedico().getId())
                    .orElseThrow(() -> new Exception("El médico no existe"));
            p.setMedico(m);
        }

        // DNI opcional pero válido
        if (p.getDni() != null && (p.getDni() < 1_000_000 || p.getDni() > 99_999_999))
            throw new Exception("El DNI debe tener entre 7 y 8 dígitos");

        // Paciente (opcional)
        if (p.getPaciente() != null && p.getPaciente().getId() != null) {
            Familiar f = familiarRepository.findById(p.getPaciente().getId())
                    .orElseThrow(() -> new Exception("El paciente no existe"));
            p.setPaciente(f);
        }

        // Documentos
        if (p.getDocumentos() != null) {
            for (Documento doc : p.getDocumentos()) {
                if (doc.getNombreArchivo() == null || doc.getNombreArchivo().isBlank())
                    throw new Exception("Todos los documentos deben tener un nombre de archivo");
            }
        }
    }

    // ================= Métodos Comunes =================
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
            doc.setFechaSubida(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            documentoRepository.save(doc);
        }
    }

    @Transactional
    public void registrarMovimiento(E pedido, Estado estado, Usuario usuario, String comentario) throws Exception {
        if (pedido == null) throw new Exception("Pedido es obligatorio");
        if (estado == null) throw new Exception("Estado es obligatorio");
        if (usuario == null || usuario.getId() == null)
            throw new Exception("Usuario es obligatorio");

        Usuario u = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        HistorialMovimiento historial = HistorialMovimiento.builder()
                .pedido(pedido)
                .fecha(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .estado(estado)
                .usuario(u)
                .comentario(comentario)
                .build();
        historialRepository.save(historial);
    }

    // ================= Métodos de Búsqueda =================
    @Transactional(readOnly = true)
    public List<E> buscarPorBeneficiario(Long beneficiarioId) throws Exception {
        Beneficiario b = beneficiarioRepository.findById(beneficiarioId)
                .orElseThrow(() -> new Exception("Beneficiario no encontrado"));
        return ((PedidoRepository<E>) baseRepository).findByBeneficiario(b);
    }

    @Transactional(readOnly = true)
    public List<E> buscarPorGrupoFamiliar(Long grupoId) throws Exception {
        GrupoFamiliar g = grupoFamiliarRepository.findById(grupoId)
                .orElseThrow(() -> new Exception("Grupo familiar no encontrado"));
        return ((PedidoRepository<E>) baseRepository).findByGrupoFamiliar(g);
    }

    @Transactional(readOnly = true)
    public List<E> buscarPorPaciente(Long pacienteId) throws Exception {
        Familiar f = familiarRepository.findById(pacienteId)
                .orElseThrow(() -> new Exception("Paciente no encontrado"));
        return baseRepository.findAll().stream()
                .filter(p -> p.getPaciente() != null && p.getPaciente().equals(f))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<E> buscarPorMedico(Long medicoId) throws Exception {
        Medico m = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new Exception("Médico no encontrado"));
        return ((PedidoRepository<E>) baseRepository).findByMedico(m);
    }

    @Transactional(readOnly = true)
    public List<E> buscarPorUsuario(Long usuarioId) throws Exception {
        Usuario u = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
        return ((PedidoRepository<E>) baseRepository).findByUsuario(u);
    }

    @Transactional(readOnly = true)
    public List<E> listar() {
        return baseRepository.findAll();
    }

    public abstract PedidoDTO mapToDTO(E pedido);
}
