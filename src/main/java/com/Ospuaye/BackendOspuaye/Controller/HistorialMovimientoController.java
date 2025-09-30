package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.HistorialMovimiento;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Repository.PedidoRepository;
import com.Ospuaye.BackendOspuaye.Service.HistorialMovimientoService;
import com.Ospuaye.BackendOspuaye.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historial-movimientos")
public class HistorialMovimientoController extends BaseController<HistorialMovimiento, Long> {

    private final HistorialMovimientoService historialService;
    private final UsuarioService usuarioService;
    private final PedidoRepository pedidoRepository;

    public HistorialMovimientoController(HistorialMovimientoService historialService,
                                         UsuarioService usuarioService,
                                         PedidoRepository pedidoRepository) {
        super(historialService);
        this.historialService = historialService;
        this.usuarioService = usuarioService;
        this.pedidoRepository = pedidoRepository;
    }

    private Usuario obtenerUsuario(Long id) {
        try {
            Optional<Usuario> uOpt = usuarioService.buscarPorId(id);
            if (!uOpt.isPresent()) throw new IllegalArgumentException("Usuario no encontrado");
            return uOpt.get();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }

    private Pedido obtenerPedido(Long id) {
        try {
            Optional<Pedido> pOpt = pedidoRepository.findById(id);
            if (!pOpt.isPresent()) throw new IllegalArgumentException("Pedido no encontrado");
            return pOpt.get();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al buscar pedido: " + e.getMessage(), e);
        }
    }


    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody HistorialMovimiento entity) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(historialService.crear(entity));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody HistorialMovimiento entity) {
        try {
            entity.setId(id);
            return ResponseEntity.ok(historialService.actualizar(entity));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario u = obtenerUsuario(usuarioId);
            return ResponseEntity.ok(historialService.listarPorUsuario(u));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> listarPorPedido(@PathVariable Long pedidoId) {
        try {
            Pedido p = obtenerPedido(pedidoId);
            return ResponseEntity.ok(historialService.listarPorPedido(p));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> listarPorEstado(@PathVariable Estado estado) {
        try {
            return ResponseEntity.ok(historialService.listarPorEstado(estado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/rango-fecha")
    public ResponseEntity<?> listarPorRangoFecha(@RequestParam Date inicio, @RequestParam Date fin) {
        try {
            return ResponseEntity.ok(historialService.listarPorRangoFecha(inicio, fin));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
