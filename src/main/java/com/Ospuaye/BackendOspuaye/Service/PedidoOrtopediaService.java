package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Dto.DocumentoDTO;
import com.Ospuaye.BackendOspuaye.Dto.PedidoOftalmologiaDTO;
import com.Ospuaye.BackendOspuaye.Dto.PedidoOrtopediaDTO;
import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Entity.Enum.PedidoTipo;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOrtopediaRepository;
import com.Ospuaye.BackendOspuaye.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOrtopediaService extends PedidoService {

    @Autowired
    private PedidoOrtopediaRepository pedidoOrtopediaRepository;

    public PedidoOrtopediaService(PedidoRepository pedidoRepository) {
        super(pedidoRepository);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Pedido> buscar(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return pedidoOrtopediaRepository.findAll(pageable).map(p -> (Pedido) p);
        }

        String q = query.trim();
        return pedidoOrtopediaRepository
                .findByBeneficiario_NombreContainingIgnoreCaseOrGrupoFamiliar_NombreGrupoContainingIgnoreCaseOrEmpresaContainingIgnoreCaseOrDelegacionContainingIgnoreCaseOrPaciente_NombreContainingIgnoreCaseOrMedico_NombreContainingIgnoreCaseOrMotivoConsultaContainingIgnoreCase(
                        q, q, q, q, q, q, q, pageable
                ).map(p -> (Pedido) p);
    }




    // 🧾 CREAR PEDIDO ORTOPEDIA (con validaciones completas)
    @Transactional
    public PedidoOrtopedia crearPedidoOrtopedia(PedidoOrtopedia pedido, List<Documento> documentos) throws Exception {
        // validaciones comunes (del padre)
        validarPedidoComun(pedido);

        // validaciones específicas de ortopedia
        if (pedido.getMotivoConsulta() == null || pedido.getMotivoConsulta().isBlank())
            throw new Exception("El motivo de consulta es obligatorio");
        if (pedido.getRecetaMedica() == null)
            throw new Exception("Debe indicar si adjunta receta médica");

        // opcionales: longitud, formatos, etc.
        if (pedido.getMotivoConsulta().length() < 5)
            throw new Exception("El motivo de consulta es demasiado corto (mínimo 5 caracteres)");

        // set iniciales
        pedido.setEstado(Estado.Pendiente);
        pedido.setFechaIngreso(new Date());
        pedido.setPedidoTipo(PedidoTipo.Ortopedia);

        // guardado
        PedidoOrtopedia guardado = pedidoOrtopediaRepository.save(pedido);

        // usuario desde beneficiario
        Usuario usuario = guardado.getBeneficiario().getUsuario();

        // documentos (validación y guardado)
        if (documentos != null && !documentos.isEmpty()) {
            for (Documento doc : documentos) {
                if (doc.getNombreArchivo() == null || doc.getNombreArchivo().isBlank())
                    throw new Exception("Cada documento debe tener un nombre de archivo");
            }
            agregarDocumentos(guardado, documentos, usuario);
        }

        // registrar movimiento
        registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido de ortopedia creado");

        return guardado;
    }

    @Transactional
    public PedidoOrtopedia actualizarPedidoOrtopedia(Long id, PedidoOrtopedia datosActualizados) throws Exception {
        // ✅ Reutiliza la lógica de actualización general del padre
        Pedido pedidoActualizado = super.actualizarPedido(id, datosActualizados);

        // 🔍 Validaciones y actualizaciones específicas de ortopedia
        PedidoOrtopedia existente = pedidoOrtopediaRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el pedido de ortopedia con ID: " + id));

        if (datosActualizados.getMotivoConsulta() != null && !datosActualizados.getMotivoConsulta().isBlank()) {
            existente.setMotivoConsulta(datosActualizados.getMotivoConsulta());
        }

        if (datosActualizados.getRecetaMedica() != null) {
            existente.setRecetaMedica(datosActualizados.getRecetaMedica());
        }

        // 💾 Guardar cambios finales
        PedidoOrtopedia actualizado = pedidoOrtopediaRepository.save(existente);

        // 🕐 Registrar movimiento (opcional si querés registrar distinto al genérico)
        registrarMovimiento(actualizado, actualizado.getEstado(),
                actualizado.getBeneficiario().getUsuario(), "Pedido de ortopedia actualizado");

        return actualizado;
    }



    @Transactional(readOnly = true)
    public List<PedidoOrtopedia> listarTodos() throws Exception {
        return pedidoOrtopediaRepository.findAll();
    }

    // Métodos específicos del hijo — NOMBRES distintos para evitar choque con padre
    @Transactional(readOnly = true)
    public List<PedidoOrtopedia> listarPorBeneficiario(Long idBeneficiario) throws Exception {
        if (idBeneficiario == null) throw new Exception("El ID del beneficiario no puede ser nulo");
        Beneficiario b = beneficiarioRepository.findById(idBeneficiario)
                .orElseThrow(() -> new Exception("No se encontró beneficiario con ID: " + idBeneficiario));
        return pedidoOrtopediaRepository.findByBeneficiario(b);
    }

    @Transactional(readOnly = true)
    public List<PedidoOrtopedia> listarPorMedico(Long idMedico) throws Exception {
        if (idMedico == null) throw new Exception("El ID del médico no puede ser nulo");
        Medico m = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new Exception("No se encontró médico con ID: " + idMedico));
        return pedidoOrtopediaRepository.findByMedico(m);
    }

    @Transactional
    public PedidoOrtopedia actualizarEstadoOrtopedia(Long id, Estado nuevoEstado) throws Exception {
        if (id == null) throw new Exception("El ID del pedido no puede ser nulo");
        if (nuevoEstado == null) throw new Exception("El nuevo estado no puede ser nulo");

        PedidoOrtopedia pedido = pedidoOrtopediaRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el pedido con ID: " + id));

        if (pedido.getEstado() == Estado.Aceptado || pedido.getEstado() == Estado.Rechazado)
            throw new Exception("No se puede cambiar el estado de un pedido finalizado");

        if (pedido.getEstado() == nuevoEstado)
            throw new Exception("El pedido ya tiene el estado " + nuevoEstado);

        pedido.setEstado(nuevoEstado);
        registrarMovimiento(pedido, nuevoEstado, pedido.getBeneficiario().getUsuario(),
                "Cambio de estado a " + nuevoEstado);

        return pedidoOrtopediaRepository.save(pedido);
    }

    public PedidoOrtopediaDTO obtenerDto(Long id) {
        PedidoOrtopedia pedido = pedidoOrtopediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PedidoOrtopediaDTO dto = new PedidoOrtopediaDTO();
        dto.setId(pedido.getId());
        dto.setNombre(pedido.getNombre());
        dto.setMotivoConsulta(pedido.getMotivoConsulta());
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
    public List<PedidoOrtopedia> listarPedidosOrtopediaSinMedico() {
        return pedidoOrtopediaRepository.findByMedicoIsNull();
    }
}
