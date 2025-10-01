package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOftalmologiaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOftalmologiaService extends PedidoService<PedidoOftalmologia> {

    private final PedidoOftalmologiaRepository repo;

    public PedidoOftalmologiaService(PedidoOftalmologiaRepository pedidoOftalmologiaRepository) {
        super(pedidoOftalmologiaRepository);
        this.repo = pedidoOftalmologiaRepository;
    }

    @Transactional
    public PedidoOftalmologia crearPedido(PedidoOftalmologia pedido,
                                          List<Documento> documentos) throws Exception {

        // Validaciones generales
        validarPedidoComun(pedido);

        // Validaciones específicas
        if (pedido.getMotivoConsulta() == null || pedido.getMotivoConsulta().isBlank())
            throw new Exception("El motivo de consulta es obligatorio");
        if (pedido.getUsaLentes() == null)
            throw new Exception("Debe indicar si usa lentes");
        if (pedido.getRecetaMedica() == null)
            throw new Exception("Debe indicar si adjunta receta");

        // Estado y fecha
        pedido.setEstado(Estado.Pendiente);
        pedido.setFechaIngreso(new Date());

        // Guardar pedido
        PedidoOftalmologia guardado = baseRepository.save(pedido);

        // Tomamos el usuario del beneficiario
        Usuario usuario = guardado.getBeneficiario().getUsuario();

        // Validar y agregar documentos
        if (documentos != null && !documentos.isEmpty()) {
            for (Documento doc : documentos) {
                if (doc.getNombreArchivo() == null || doc.getNombreArchivo().isBlank())
                    throw new Exception("Cada documento debe tener un nombre de archivo");
            }
            agregarDocumentos(guardado, documentos, usuario);
        }

        // Registrar movimiento
        registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido creado");

        return guardado;
    }

    @Transactional(readOnly = true)
    public List<PedidoOftalmologia> findByBeneficiarioId(Long idBeneficiario) throws Exception {
        if (idBeneficiario == null) throw new Exception("El ID del beneficiario no puede ser nulo");
        return repo.findByBeneficiario_Id(idBeneficiario);
    }

    @Transactional(readOnly = true)
    public List<PedidoOftalmologia> findByMedicoId(Long idMedico) throws Exception {
        if (idMedico == null) throw new Exception("El ID del médico no puede ser nulo");
        return repo.findByMedico_Id(idMedico);
    }

    @Transactional
    public PedidoOftalmologia actualizarEstado(Long id, Estado nuevoEstado) throws Exception {
        if (id == null) {
            throw new Exception("El ID del pedido no puede ser nulo");
        }
        if (nuevoEstado == null) {
            throw new Exception("El nuevo estado no puede ser nulo");
        }

        PedidoOftalmologia pedido = repo.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el pedido con ID: " + id));

        // Validar transición lógica de estados
        if (pedido.getEstado() == Estado.Aceptado || pedido.getEstado() == Estado.Rechazado) {
            throw new Exception("No se puede cambiar el estado de un pedido ya finalizado");
        }

        if (pedido.getEstado() == nuevoEstado) {
            throw new Exception("El pedido ya tiene el estado " + nuevoEstado);
        }

        // Actualizar estado
        pedido.setEstado(nuevoEstado);

        // Registrar movimiento
        registrarMovimiento(pedido, nuevoEstado, pedido.getBeneficiario().getUsuario(),
                "Cambio de estado a " + nuevoEstado);

        return repo.save(pedido);
    }


}
