package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Dto.DocumentoDTO;
import com.Ospuaye.BackendOspuaye.Dto.PedidoOftalmologiaDTO;
import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.PedidoTipo;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOftalmologiaRepository;
import com.Ospuaye.BackendOspuaye.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOftalmologiaService extends PedidoService {

    @Autowired
    private PedidoOftalmologiaRepository pedidoOftalmologiaRepository;
    private DocumentoService documentoService;

    public PedidoOftalmologiaService(PedidoRepository pedidoRepository) {
        super(pedidoRepository);
    }


    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Pedido> buscar(String query, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), size <= 0 ? 5 : size);

        if (query == null || query.trim().isEmpty()) {
            // map() convierte cada PedidoOftalmologia en Pedido
            return pedidoOftalmologiaRepository.findAll(pageable).map(p -> (Pedido) p);
        }

        String q = query.trim();
        return pedidoOftalmologiaRepository
                .findByBeneficiario_NombreContainingIgnoreCaseAndActivoTrueOrGrupoFamiliar_NombreGrupoContainingIgnoreCaseAndActivoTrueOrEmpresaContainingIgnoreCaseAndActivoTrueOrDelegacionContainingIgnoreCaseAndActivoTrueOrPaciente_NombreContainingIgnoreCaseAndActivoTrueOrMedico_NombreContainingIgnoreCaseAndActivoTrueOrMotivoConsultaContainingIgnoreCaseAndActivoTrue(
                        q, q, q, q, q, q, q, pageable)
                .map(p -> (Pedido) p);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Pedido> buscarInactivos(String query, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), size <= 0 ? 5 : size);

        if (query == null || query.trim().isEmpty()) {
            return pedidoOftalmologiaRepository.findAll(pageable).map(p -> (Pedido) p);
        }

        String q = query.trim();
        return pedidoOftalmologiaRepository
                .findByBeneficiario_NombreContainingIgnoreCaseAndActivoFalseOrGrupoFamiliar_NombreGrupoContainingIgnoreCaseAndActivoFalseOrEmpresaContainingIgnoreCaseAndActivoFalseOrDelegacionContainingIgnoreCaseAndActivoFalseOrPaciente_NombreContainingIgnoreCaseAndActivoFalseOrMedico_NombreContainingIgnoreCaseAndActivoFalseOrMotivoConsultaContainingIgnoreCaseAndActivoFalse(
                        q, q, q, q, q, q, q, pageable)
                .map(p -> (Pedido) p);
    }


    // NOTE: NO definir constructor que haga super(pedidoOftalmologiaRepository)

    @Transactional
    public PedidoOftalmologia crearPedidoOftalmologia(PedidoOftalmologia pedido, List<Documento> documentos) throws Exception {
        // validaciones comunes
        validarPedidoComun(pedido);

        // validaciones específicas
        if (pedido.getMotivoConsulta() == null || pedido.getMotivoConsulta().isBlank())
            throw new Exception("El motivo de consulta es obligatorio");
        if (pedido.getUsaLentes() == null)
            throw new Exception("Debe indicar si usa lentes");
        if (pedido.getRecetaMedica() == null)
            throw new Exception("Debe indicar si adjunta receta médica");

        if (pedido.getMotivoConsulta().length() < 5)
            throw new Exception("El motivo de consulta es demasiado corto (mínimo 5 caracteres)");

        // set iniciales
        pedido.setEstado(Estado.Pendiente);
        pedido.setFechaIngreso(new Date());
        pedido.setPedidoTipo(PedidoTipo.Oftalmologia);

        PedidoOftalmologia guardado = pedidoOftalmologiaRepository.save(pedido);
        Usuario usuario = guardado.getBeneficiario().getUsuario();

        // documentos
        if (documentos != null && !documentos.isEmpty()) {
            for (Documento doc : documentos) {
                if (doc.getNombreArchivo() == null || doc.getNombreArchivo().isBlank())
                    throw new Exception("Cada documento debe tener un nombre de archivo");
            }
            agregarDocumentos(guardado, documentos, usuario);
        }

        registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido de oftalmología creado");
        return guardado;
    }

    @Transactional(readOnly = true)
    public List<PedidoOftalmologia> listarTodos() throws Exception {
        return pedidoOftalmologiaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PedidoOftalmologia> listarPorBeneficiario(Long idBeneficiario) throws Exception {
        if (idBeneficiario == null) throw new Exception("El ID del beneficiario no puede ser nulo");
        Beneficiario b = beneficiarioRepository.findById(idBeneficiario)
                .orElseThrow(() -> new Exception("No se encontró beneficiario con ID: " + idBeneficiario));
        return pedidoOftalmologiaRepository.findByBeneficiario(b);
    }

    @Transactional(readOnly = true)
    public List<PedidoOftalmologia> listarPorMedico(Long idMedico) throws Exception {
        if (idMedico == null) throw new Exception("El ID del médico no puede ser nulo");
        Medico m = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new Exception("No se encontró médico con ID: " + idMedico));
        return pedidoOftalmologiaRepository.findByMedico(m);
    }

    @Transactional
    public PedidoOftalmologia actualizarEstadoOftalmologia(Long id, Estado nuevoEstado) throws Exception {
        if (id == null) throw new Exception("El ID del pedido no puede ser nulo");
        if (nuevoEstado == null) throw new Exception("El nuevo estado no puede ser nulo");

        PedidoOftalmologia pedido = pedidoOftalmologiaRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el pedido con ID: " + id));

        if (pedido.getEstado() == Estado.Aceptado || pedido.getEstado() == Estado.Rechazado)
            throw new Exception("No se puede cambiar el estado de un pedido finalizado");

        if (pedido.getEstado() == nuevoEstado)
            throw new Exception("El pedido ya tiene el estado " + nuevoEstado);

        pedido.setEstado(nuevoEstado);
        registrarMovimiento(pedido, nuevoEstado, pedido.getBeneficiario().getUsuario(),
                "Cambio de estado a " + nuevoEstado);

        return pedidoOftalmologiaRepository.save(pedido);
    }

    @Transactional
    public PedidoOftalmologia actualizarPedidoOftalmologia(
            Long id,
            PedidoOftalmologia datosActualizados,
            List<MultipartFile> documentos
    ) throws Exception {

        // Reutiliza lógica de Pedido base
        super.actualizarPedido(id, datosActualizados);

        // Pedido existente
        PedidoOftalmologia existente = pedidoOftalmologiaRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el pedido de oftalmología con ID: " + id));

        // Campos específicos
        if (datosActualizados.getMotivoConsulta() != null && !datosActualizados.getMotivoConsulta().isBlank()) {
            existente.setMotivoConsulta(datosActualizados.getMotivoConsulta());
        }

        if (datosActualizados.getUsaLentes() != null) {
            existente.setUsaLentes(datosActualizados.getUsaLentes());
        }

        if (datosActualizados.getRecetaMedica() != null) {
            existente.setRecetaMedica(datosActualizados.getRecetaMedica());
        }

        /* ======================================================
         *   PROCESAR DOCUMENTOS ADJUNTADOS (SI VIENEN)
         * ====================================================== */
        if (documentos != null && !documentos.isEmpty()) {

            for (MultipartFile archivo : documentos) {

                // Guardar archivo físico
                String path = documentoService.handleFileUpload(archivo);

                // Crear entidad Documento
                Documento doc = new Documento();
                doc.setNombreArchivo(archivo.getOriginalFilename());
                doc.setPath(path);
                doc.setFechaSubida(new Date());
                doc.setPedido(existente); // RELACIÓN
                doc.setSubidoPor(existente.getBeneficiario().getUsuario()); // opcional, según tu lógica

                // Agregar al pedido
                existente.getDocumentos().add(doc);
            }
        }

        // Guardar cambios totales
        PedidoOftalmologia actualizado = pedidoOftalmologiaRepository.save(existente);

        // Registrar movimiento
        registrarMovimiento(actualizado, actualizado.getEstado(),
                actualizado.getBeneficiario().getUsuario(),
                "Pedido de oftalmología actualizado");

        return actualizado;
    }


    public PedidoOftalmologia obtenerPorId(Long id) {
        return pedidoOftalmologiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public PedidoOftalmologiaDTO obtenerDto(Long id) {
        PedidoOftalmologia pedido = pedidoOftalmologiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PedidoOftalmologiaDTO dto = new PedidoOftalmologiaDTO();
        dto.setId(pedido.getId());
        dto.setNombre(pedido.getNombre());
        dto.setMotivoConsulta(pedido.getMotivoConsulta());
        dto.setUsaLentes(pedido.getUsaLentes());
        dto.setRecetaMedica(pedido.getRecetaMedica());
        dto.setFechaRevision(pedido.getFechaRevision());
        dto.setObservacionMedico(pedido.getObservacionMedico());
        dto.setBeneficiario(pedido.getBeneficiario());
        dto.setDni(pedido.getDni());
        dto.setTelefono(pedido.getTelefono());
        dto.setEmpresa(pedido.getEmpresa());
        dto.setDelegacion(pedido.getDelegacion());
        dto.setMedico(pedido.getMedico());

        // convertir documentos a DTO
        List<DocumentoDTO> documentos = pedido.getDocumentos().stream()
                .map(doc -> {
                    DocumentoDTO d = new DocumentoDTO();
                    d.setId(doc.getId());
                    d.setNombreArchivo(doc.getNombreArchivo());
                    d.setUrl("/api/documentos/" + doc.getId()); // <--- URL de descarga
                    return d;
                })
                .toList();

        dto.setDocumentos(documentos);

        return dto;
    }
    @Transactional(readOnly = true)
    public List<PedidoOftalmologia> listarPedidosOftalmologiaSinMedico() {
        return pedidoOftalmologiaRepository.findByMedicoIsNull();
    }

}
