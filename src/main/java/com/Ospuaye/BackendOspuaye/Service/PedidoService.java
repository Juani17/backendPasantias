package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Entity.Enum.PedidoTipo;
import com.Ospuaye.BackendOspuaye.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional(readOnly = true)
    public Page<Pedido> buscar(String query, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), size <= 0 ? 5 : size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginar(page, size);
        }

        String q = query.trim();
        return pedidoRepository.findByBeneficiario_NombreContainingIgnoreCaseAndActivoTrueOrGrupoFamiliar_NombreGrupoContainingIgnoreCaseAndActivoTrueOrEmpresaContainingIgnoreCaseAndActivoTrueOrDelegacionContainingIgnoreCaseAndActivoTrueOrPaciente_NombreContainingIgnoreCaseAndActivoTrueOrMedico_NombreContainingIgnoreCaseAndActivoTrue(
                q, q, q, q, q, q, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Pedido> buscarInactivos(String query, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), size <= 0 ? 5 : size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginarInactivos(page, size);
        }

        String q = query.trim();
        return pedidoRepository.findByBeneficiario_NombreContainingIgnoreCaseAndActivoFalseOrGrupoFamiliar_NombreGrupoContainingIgnoreCaseAndActivoFalseOrEmpresaContainingIgnoreCaseAndActivoFalseOrDelegacionContainingIgnoreCaseAndActivoFalseOrPaciente_NombreContainingIgnoreCaseAndActivoFalseOrMedico_NombreContainingIgnoreCaseAndActivoFalse(
                q, q, q, q, q, q, pageable);
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
        if (p.getMedico() != null && p.getMedico().getId() != null) {
            Medico medicoExistente = medicoRepository.findById(p.getMedico().getId())
                    .orElse(null);
            p.setMedico(medicoExistente);
        } else {
            p.setMedico(null);
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
        pedido.setPedidoTipo(PedidoTipo.Genérico);
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
        return pedidoRepository.findAll(); // Trae todos los tipos
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosSinMedico() {
        return pedidoRepository.findByMedicoIsNull();
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

    @Transactional
    public Pedido actualizarEstadoGeneral(Long id, Estado nuevoEstado) throws Exception {
        if (id == null) throw new Exception("El ID del pedido no puede ser nulo");
        if (nuevoEstado == null) throw new Exception("El nuevo estado no puede ser nulo");

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el pedido con ID: " + id));

        if (pedido.getEstado() == Estado.Aceptado || pedido.getEstado() == Estado.Rechazado)
            throw new Exception("No se puede cambiar el estado de un pedido finalizado");

        if (pedido.getEstado() == nuevoEstado)
            throw new Exception("El pedido ya tiene el estado " + nuevoEstado);

        pedido.setEstado(nuevoEstado);
        registrarMovimiento(pedido, nuevoEstado, pedido.getBeneficiario().getUsuario(),
                "Cambio de estado a " + nuevoEstado);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarPedido(Long id, Pedido datosActualizados) throws Exception {
        if (id == null) throw new Exception("El ID del pedido no puede ser nulo");
        if (datosActualizados == null) throw new Exception("Debe enviar los datos del pedido a actualizar");

        Pedido existente = pedidoRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el pedido con ID: " + id));

        // 🔄 Actualiza solo los campos enviados (sin pisar todo el objeto)
        if (datosActualizados.getNombre() != null)
            existente.setNombre(datosActualizados.getNombre());

        if (datosActualizados.getDni() != null)
            existente.setDni(datosActualizados.getDni());

        if (datosActualizados.getBeneficiario() != null && datosActualizados.getBeneficiario().getId() != null) {
            Beneficiario beneficiario = beneficiarioRepository.findById(datosActualizados.getBeneficiario().getId())
                    .orElseThrow(() -> new Exception("El beneficiario especificado no existe"));
            existente.setBeneficiario(beneficiario);
        }

        if (datosActualizados.getMedico() != null && datosActualizados.getMedico().getId() != null) {
            Medico medico = medicoRepository.findById(datosActualizados.getMedico().getId())
                    .orElseThrow(() -> new Exception("El médico especificado no existe"));
            existente.setMedico(medico);
        }

        if (datosActualizados.getPaciente() != null && datosActualizados.getPaciente().getId() != null) {
            Familiar paciente = familiarRepository.findById(datosActualizados.getPaciente().getId())
                    .orElseThrow(() -> new Exception("El paciente especificado no existe"));
            existente.setPaciente(paciente);
        }

        if (datosActualizados.getGrupoFamiliar() != null && datosActualizados.getGrupoFamiliar().getId() != null) {
            GrupoFamiliar grupo = grupoFamiliarRepository.findById(datosActualizados.getGrupoFamiliar().getId())
                    .orElseThrow(() -> new Exception("El grupo familiar especificado no existe"));
            existente.setGrupoFamiliar(grupo);
        }
        // Si se envió un médico, buscarlo
        if (datosActualizados.getMedico() != null && datosActualizados.getMedico().getId() != null) {
            Medico medicoExistente = medicoRepository.findById(datosActualizados.getMedico().getId())
                    .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
            existente.setMedico(medicoExistente);
        } else {
            existente.setMedico(null); // permitir quitar el médico
        }

        if (datosActualizados.getPedidoTipo() != null)
            existente.setPedidoTipo(datosActualizados.getPedidoTipo());

        if (datosActualizados.getEstado() != null)
            existente.setEstado(datosActualizados.getEstado());

        if (datosActualizados.getObservacionMedico() != null)
            existente.setObservacionMedico(datosActualizados.getObservacionMedico());

        // 🔧 Validar coherencia general (reutiliza tu validación)
        validarPedidoComun(existente);

        // 💾 Guardar cambios
        Pedido actualizado = pedidoRepository.save(existente);

        // 🕐 Registrar movimiento
        registrarMovimiento(actualizado, actualizado.getEstado(), actualizado.getBeneficiario().getUsuario(),
                "Pedido actualizado");

        return actualizado;
    }

    @Transactional
    public Pedido actualizarPedidoGeneral(Long id, Pedido datosActualizados, List<Documento> documentos) throws Exception {
        if (id == null) throw new Exception("El ID del pedido no puede ser nulo");
        if (datosActualizados == null) throw new Exception("Debe enviar los datos del pedido a actualizar");

        Pedido existente = pedidoRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el pedido con ID: " + id));

        // 🔄 Actualiza solo los campos enviados (sin pisar todo el objeto)
        if (datosActualizados.getNombre() != null)
            existente.setNombre(datosActualizados.getNombre());

        if (datosActualizados.getDni() != null)
            existente.setDni(datosActualizados.getDni());

        if (datosActualizados.getBeneficiario() != null && datosActualizados.getBeneficiario().getId() != null) {
            Beneficiario beneficiario = beneficiarioRepository.findById(datosActualizados.getBeneficiario().getId())
                    .orElseThrow(() -> new Exception("El beneficiario especificado no existe"));
            existente.setBeneficiario(beneficiario);
        }

        if (datosActualizados.getMedico() != null && datosActualizados.getMedico().getId() != null) {
            Medico medico = medicoRepository.findById(datosActualizados.getMedico().getId())
                    .orElseThrow(() -> new Exception("El médico especificado no existe"));
            existente.setMedico(medico);
        }

        if (datosActualizados.getPaciente() != null && datosActualizados.getPaciente().getId() != null) {
            Familiar paciente = familiarRepository.findById(datosActualizados.getPaciente().getId())
                    .orElseThrow(() -> new Exception("El paciente especificado no existe"));
            existente.setPaciente(paciente);
        }

        if (datosActualizados.getGrupoFamiliar() != null && datosActualizados.getGrupoFamiliar().getId() != null) {
            GrupoFamiliar grupo = grupoFamiliarRepository.findById(datosActualizados.getGrupoFamiliar().getId())
                    .orElseThrow(() -> new Exception("El grupo familiar especificado no existe"));
            existente.setGrupoFamiliar(grupo);
        }
        // Si se envió un médico, buscarlo
        if (datosActualizados.getMedico() != null && datosActualizados.getMedico().getId() != null) {
            Medico medicoExistente = medicoRepository.findById(datosActualizados.getMedico().getId())
                    .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
            existente.setMedico(medicoExistente);
        } else {
            existente.setMedico(null); // permitir quitar el médico
        }

        if (datosActualizados.getPedidoTipo() != null)
            existente.setPedidoTipo(datosActualizados.getPedidoTipo());

        if (datosActualizados.getEstado() != null)
            existente.setEstado(datosActualizados.getEstado());

        if (datosActualizados.getObservacionMedico() != null)
            existente.setObservacionMedico(datosActualizados.getObservacionMedico());

        // 🔧 Validar coherencia general (reutiliza tu validación)
        validarPedidoComun(existente);

        // 💾 Guardar cambios
        Pedido actualizado = pedidoRepository.save(existente);

        if (documentos != null && !documentos.isEmpty()) {
            Usuario usuario = actualizado.getBeneficiario().getUsuario();
            agregarDocumentos(actualizado, documentos, usuario);
        }

        // 🕐 Registrar movimiento
        registrarMovimiento(actualizado, actualizado.getEstado(), actualizado.getBeneficiario().getUsuario(),
                "Pedido actualizado");

        return actualizado;
    }

    @Transactional
    public Pedido tomarPedidoGlobal(Long idPedido, Long medicoId) throws Exception {
        if (idPedido == null) throw new Exception("El ID del pedido es obligatorio.");
        if (medicoId == null) throw new Exception("El ID del médico es obligatorio.");

        // Buscar pedido base
        Pedido pedidoBase = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new Exception("No se encontró el pedido con ID: " + idPedido));

        // Verificar que no esté ya tomado
        if (pedidoBase.getMedico() != null && pedidoBase.getMedico().getId() != null) {
            throw new Exception("El pedido ya fue tomado por otro médico.");
        }

        // Buscar médico
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new Exception("No se encontró el médico con ID: " + medicoId));

        // Asignar datos base
        pedidoBase.setMedico(medico);
        pedidoBase.setEstado(Estado.Pendiente);
        pedidoBase.setFechaRevision(new Date());

        // Usuario asociado al beneficiario (para registrar movimiento)
        Usuario usuarioRegistro = null;
        if (pedidoBase.getBeneficiario() != null) {
            usuarioRegistro = pedidoBase.getBeneficiario().getUsuario();
        }

        // Guardar según tipo de pedido
        switch (pedidoBase.getPedidoTipo()) {
            case Oftalmologia -> {
                PedidoOftalmologia oft = pedidoOftalmologiaRepository.findById(idPedido)
                        .orElseThrow(() -> new Exception("No se encontró el pedido oftalmológico con ID: " + idPedido));

                oft.setMedico(medico);
                oft.setEstado(Estado.Pendiente);
                oft.setFechaRevision(new Date());
                PedidoOftalmologia guardado = pedidoOftalmologiaRepository.save(oft);

                if (usuarioRegistro != null) {
                    registrarMovimiento(guardado, Estado.Pendiente, usuarioRegistro,
                            "Pedido tomado por el médico (Oftalmología), ID médico=" + medico.getId());
                }

                return guardado;
            }
            case Ortopedia -> {
                PedidoOrtopedia ort = pedidoOrtopediaRepository.findById(idPedido)
                        .orElseThrow(() -> new Exception("No se encontró el pedido ortopédico con ID: " + idPedido));

                ort.setMedico(medico);
                ort.setEstado(Estado.Pendiente);
                ort.setFechaRevision(new Date());
                PedidoOrtopedia guardado = pedidoOrtopediaRepository.save(ort);

                if (usuarioRegistro != null) {
                    registrarMovimiento(guardado, Estado.Pendiente, usuarioRegistro,
                            "Pedido tomado por el médico (Ortopedia), ID médico=" + medico.getId());
                }

                return guardado;
            }
            case Genérico, Sin_Información-> {
                Pedido guardado = pedidoRepository.save(pedidoBase);

                if (usuarioRegistro != null) {
                    registrarMovimiento(guardado, Estado.Pendiente, usuarioRegistro,
                            "Pedido tomado por el médico (General), ID médico=" + medico.getId());
                }

                return guardado;
            }
            default -> {
                Pedido guardado = pedidoRepository.save(pedidoBase);

                if (usuarioRegistro != null) {
                    registrarMovimiento(guardado, Estado.Pendiente, usuarioRegistro,
                            "Pedido tomado por el médico (Tipo desconocido), ID médico=" + medico.getId());
                }

                return guardado;
            }
        }
    }

}
