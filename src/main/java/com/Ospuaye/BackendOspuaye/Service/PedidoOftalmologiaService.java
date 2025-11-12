package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Enum.PedidoTipo;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOftalmologiaRepository;
import com.Ospuaye.BackendOspuaye.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOftalmologiaService extends PedidoService {

    @Autowired
    private PedidoOftalmologiaRepository pedidoOftalmologiaRepository;

    public PedidoOftalmologiaService(PedidoRepository pedidoRepository) {
        super(pedidoRepository);
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
    public PedidoOftalmologia actualizarPedidoOftalmologia(Long id, PedidoOftalmologia datosActualizados) throws Exception {
        // ✅ Reutiliza la lógica común de PedidoService
        super.actualizarPedido(id, datosActualizados);

        // 🔍 Recupera el pedido específico de oftalmología
        PedidoOftalmologia existente = pedidoOftalmologiaRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el pedido de oftalmología con ID: " + id));

        // ⚙️ Actualiza campos específicos del tipo Oftalmología
        if (datosActualizados.getMotivoConsulta() != null && !datosActualizados.getMotivoConsulta().isBlank()) {
            existente.setMotivoConsulta(datosActualizados.getMotivoConsulta());
        }

        if (datosActualizados.getUsaLentes() != null) {
            existente.setUsaLentes(datosActualizados.getUsaLentes());
        }

        if (datosActualizados.getRecetaMedica() != null) {
            existente.setRecetaMedica(datosActualizados.getRecetaMedica());
        }

        // 💾 Guardar cambios finales
        PedidoOftalmologia actualizado = pedidoOftalmologiaRepository.save(existente);

        // 🕐 Registrar movimiento (puede ser distinto del genérico)
        registrarMovimiento(actualizado, actualizado.getEstado(),
                actualizado.getBeneficiario().getUsuario(), "Pedido de oftalmología actualizado");

        return actualizado;
    }
}
