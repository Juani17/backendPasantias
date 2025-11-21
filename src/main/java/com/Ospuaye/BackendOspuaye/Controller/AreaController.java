package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Service.AreaService;
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

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        try {
            return ResponseEntity.ok(areaService.buscar(query, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
