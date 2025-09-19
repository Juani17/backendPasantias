package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOrtopediaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOrtopediaService extends PedidoService<PedidoOrtopedia> {

    private final PedidoOrtopediaRepository repo;

    public PedidoOrtopediaService(PedidoOrtopediaRepository pedidoOrtopediaRepository) {
        super(pedidoOrtopediaRepository);
        this.repo = pedidoOrtopediaRepository;
    }

    @Transactional
    public PedidoOrtopedia crearPedido(PedidoOrtopedia pedido,
                                       List<Documento> documentos) throws Exception {

        // ✅ Validaciones generales (ya asigna usuario desde beneficiario)
        validarPedidoComun(pedido);

        // ✅ Validaciones específicas de Ortopedia
        if (pedido.getMotivoConsulta() == null || pedido.getMotivoConsulta().isBlank())
            throw new Exception("El motivo de consulta es obligatorio");
        if (pedido.getRecetaMedica() == null)
            throw new Exception("Debe indicar si adjunta receta");

        // Estado y fecha
        pedido.setEstado(Estado.Pendiente);
        pedido.setFechaIngreso(new Date());

        // Guardar pedido
        PedidoOrtopedia guardado = baseRepository.save(pedido);

        // ✅ Tomamos el usuario desde el beneficiario (ya lo resolvió validarPedidoComun)
        Usuario usuario = guardado.getBeneficiario().getUsuario();

        // Validar y agregar documentos
        if (documentos != null && !documentos.isEmpty()) {
            for (Documento doc : documentos) {
                if (doc.getNombreArchivo() == null || doc.getNombreArchivo().isBlank()) {
                    throw new Exception("Cada documento debe tener un nombre de archivo");
                }
            }
            agregarDocumentos(guardado, documentos, usuario);
        }

        // Registrar movimiento
        registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido creado");

        return guardado;
    }
}
