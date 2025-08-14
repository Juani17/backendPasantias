package com.Ospuaye.BackendOspuaye.Dto;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import lombok.Data;

import java.util.List;

@Data
public class PedidoRequest<T extends Pedido> {
    private T pedido;
    private List<Documento> documentos;
    private Usuario usuario;
}