package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.MovimientoObraSocial;
import com.Ospuaye.BackendOspuaye.Service.MovimientoObraSocialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos-obra-social")
public class MovimientoObraSocialController extends BaseController<MovimientoObraSocial, Long> {

    private final MovimientoObraSocialService movimientoService;

    public MovimientoObraSocialController(MovimientoObraSocialService movimientoService) {
        super(movimientoService);
        this.movimientoService = movimientoService;
    }

    @GetMapping("/persona/{personaId}")
    public ResponseEntity<?> listarPorPersona(@PathVariable Long personaId) {
        try {
            List<MovimientoObraSocial> lista = movimientoService.listarPorPersona(personaId);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/rango")
    public ResponseEntity<?> listarPorRango(@RequestParam Date inicio, @RequestParam Date fin) {
        try {
            List<MovimientoObraSocial> lista = movimientoService.listarPorRangoFecha(inicio, fin);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
