package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PedidoService extends BaseService<Pedido, Long> {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private PedidoOftalmologiaRepository pedidoOftalmologiaRepository;
    @Autowired private PedidoOrtopediaRepository pedidoOrtopediaRepository;

    @Autowired protected DocumentoRepository documentoRepository;
    @Autowired protected HistorialMovimientoRepository historialRepository;
    @Autowired protected BeneficiarioRepository beneficiarioRepository;
    @Autowired protected GrupoFamiliarRepository grupoFamiliarRepository;
    @Autowired protected UsuarioRepository usuarioRepository;
    @Autowired protected MedicoRepository medicoRepository;
    @Autowired protected FamiliarRepository familiarRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        super(pedidoRepository);
        this.pedidoRepository = pedidoRepository;
    }
    // 🔍 VALIDACIONES COMUNES
    protected void validarPedidoComun(Pedido p) throws Exception {
        if (p == null) throw new Exception("El pedido no puede ser nulo");
        if (p.getNombre() == null || p.getNombre().isBlank())
            throw new Exception("El nombre del pedido es obligatorio");

        if (p.getBeneficiario() == null)
            throw new Exception("El pedido debe tener un beneficiario");

        Beneficiario b = beneficiarioRepository.findById(p.getBeneficiario().getId())
                .orElseThrow(() -> new Exception("El beneficiario no existe"));
        p.setBeneficiario(b);

        if (b.getUsuario() == null)
            throw new Exception("El beneficiario no tiene usuario asociado");
        p.setUsuario(b.getUsuario());

        if (p.getGrupoFamiliar() != null) {
            GrupoFamiliar gf = grupoFamiliarRepository.findById(p.getGrupoFamiliar().getId())
                    .orElseThrow(() -> new Exception("El grupo familiar no existe"));
            p.setGrupoFamiliar(gf);
        }

        if (p.getMedico() != null) {
            Medico m = medicoRepository.findById(p.getMedico().getId())
                    .orElseThrow(() -> new Exception("El médico no existe"));
            p.setMedico(m);
        }

        if (p.getDni() != null && (p.getDni() < 1_000_000 || p.getDni() > 99_999_999))
            throw new Exception("El DNI debe tener entre 7 y 8 dígitos");

        if (p.getPaciente() != null) {
            Familiar f = familiarRepository.findById(p.getPaciente().getId())
                    .orElseThrow(() -> new Exception("El paciente no existe"));
            p.setPaciente(f);
        }

        if (p.getDocumentos() != null) {
            for (Documento doc : p.getDocumentos()) {
                if (doc.getNombreArchivo() == null || doc.getNombreArchivo().isBlank())
                    throw new Exception("Todos los documentos deben tener un nombre de archivo");
            }
        }
    }

    // 🧾 CREAR PEDIDO GENÉRICO
    @Transactional
    public Pedido crearPedido(Pedido pedido, List<Documento> documentos) throws Exception {
        validarPedidoComun(pedido);
        pedido.setEstado(Estado.Pendiente);
        pedido.setFechaIngreso(new Date());

        Pedido guardado = pedidoRepository.save(pedido);
        Usuario usuario = guardado.getBeneficiario().getUsuario();

        if (documentos != null && !documentos.isEmpty())
            agregarDocumentos(guardado, documentos, usuario);

        registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido genérico creado");
        return guardado;
    }

    // 📎 AGREGAR DOCUMENTOS
    @Transactional
    public void agregarDocumentos(Pedido pedido, List<Documento> documentos, Usuario usuario) throws Exception {
        if (pedido == null) throw new Exception("Pedido es obligatorio");
        if (usuario == null || usuario.getId() == null)
            throw new Exception("Usuario que sube los documentos es obligatorio");

        Usuario u = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        for (Documento doc : documentos) {
            if (doc.getNombreArchivo() == null || doc.getNombreArchivo().isBlank())
                throw new Exception("Cada documento debe tener un nombre de archivo");
            doc.setPedido(pedido);
            doc.setSubidoPor(u);
            doc.setFechaSubida(new Date());
            documentoRepository.save(doc);
        }
    }

    // 🕐 REGISTRAR MOVIMIENTO
    @Transactional
    public void registrarMovimiento(Pedido pedido, Estado estado, Usuario usuario, String comentario) throws Exception {
        if (pedido == null) throw new Exception("Pedido es obligatorio");
        if (estado == null) throw new Exception("Estado es obligatorio");
        if (usuario == null || usuario.getId() == null)
            throw new Exception("Usuario es obligatorio");

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

    // 📋 LISTADOS
    @Transactional(readOnly = true)
    public List<Pedido> listarTodosLosPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.addAll(pedidoRepository.findAll());
        pedidos.addAll(pedidoOftalmologiaRepository.findAll());
        pedidos.addAll(pedidoOrtopediaRepository.findAll());
        return pedidos;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosGenericos() {
        return pedidoRepository.findAll();
    }

    // 🔍 FILTROS COMUNES
    @Transactional(readOnly = true)
    public List<Pedido> findByBeneficiarioId(Long idBeneficiario) throws Exception {
        Beneficiario b = beneficiarioRepository.findById(idBeneficiario)
                .orElseThrow(() -> new Exception("No se encontró beneficiario con ID: " + idBeneficiario));
        return pedidoRepository.findByBeneficiario(b);
    }

    @Transactional(readOnly = true)
    public List<Pedido> findByMedicoId(Long idMedico) throws Exception {
        Medico m = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new Exception("No se encontró médico con ID: " + idMedico));
        return pedidoRepository.findByMedico(m);
    }
}
