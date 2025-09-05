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

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody HistorialMovimiento entity) {
        try {
            HistorialMovimiento creado = historialService.crear(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
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
            HistorialMovimiento actualizado = historialService.actualizar(entity);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario u = usuarioService.buscarPorId(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            List<HistorialMovimiento> lista = historialService.listarPorUsuario(u);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> listarPorPedido(@PathVariable Long pedidoId) {
        try {
            Pedido p = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
            List<HistorialMovimiento> lista = historialService.listarPorPedido(p);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Cambio aquí: se usa Estado en lugar de TipoMovimiento
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> listarPorEstado(@PathVariable Estado estado) {
        try {
            List<HistorialMovimiento> lista = historialService.listarPorEstado(estado);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/rango-fecha")
    public ResponseEntity<?> listarPorRangoFecha(@RequestParam Date inicio, @RequestParam Date fin) {
        try {
            List<HistorialMovimiento> lista = historialService.listarPorRangoFecha(inicio, fin);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
