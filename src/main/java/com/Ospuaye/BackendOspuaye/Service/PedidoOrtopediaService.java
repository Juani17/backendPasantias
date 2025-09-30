package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Dto.PedidoDTO;
import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOrtopediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOrtopediaService extends PedidoService<PedidoOrtopedia> {

    private final PedidoOrtopediaRepository repo;

    public PedidoOrtopediaService(PedidoOrtopediaRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Transactional
    public PedidoOrtopedia crearPedido(PedidoOrtopedia pedido, List<Documento> documentos) throws Exception {
        validarPedidoComun(pedido);

        if (pedido.getMotivoConsulta() == null || pedido.getMotivoConsulta().isBlank())
            throw new Exception("El motivo de consulta es obligatorio");
        if (pedido.getRecetaMedica() == null)
            throw new Exception("Debe indicar si adjunta receta");

        pedido.setEstado(Estado.Pendiente);
        pedido.setFechaIngreso(new Date());

        PedidoOrtopedia guardado = baseRepository.save(pedido);
        Usuario usuario = guardado.getBeneficiario().getUsuario();

        if (documentos != null && !documentos.isEmpty()) {
            agregarDocumentos(guardado, documentos, usuario);
        }

        // --- Grupo Familiar: setTitular por beneficiario ---
        if (guardado.getGrupoFamiliar() != null) {
            GrupoFamiliar grupo = guardado.getGrupoFamiliar();
            grupo.setTitular(guardado.getBeneficiario());
        }

        registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido creado");
        return guardado;
    }

    @Override
    public PedidoDTO mapToDTO(PedidoOrtopedia pedido) {
        return null; // placeholder
    }
}

