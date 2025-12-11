package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Service.AreaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/areas")
public class AreaController extends BaseController<Area, Long> {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        super(areaService);
        this.areaService = areaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Area area) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(areaService.crear(area));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @GetMapping("/buscar")
    public ResponseEntity<Page<Area>> buscar(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Area> result = areaService.buscar(query, page, size);
        return ResponseEntity.ok(result);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @GetMapping("/buscar-inactivos")
    public ResponseEntity<Page<Area>> buscarInactivos(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Area> result = areaService.buscarInactivos(query, page, size);
        return ResponseEntity.ok(result);
    }


}
