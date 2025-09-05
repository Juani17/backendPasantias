package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOftalmologiaRepository;
import jakarta.transaction.Transactional;
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
                                          List<Documento> documentos,
                                          Usuario usuario) throws Exception {

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

        // Validar y agregar documentos
        if (documentos != null) {
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
}
