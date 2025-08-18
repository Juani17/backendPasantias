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

    public AreaController(AreaService service) {
        super(service);
        this.areaService = service;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Area area) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(areaService.crear(area));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Area area) {
        try {
            return ResponseEntity.ok(areaService.actualizar(area));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
