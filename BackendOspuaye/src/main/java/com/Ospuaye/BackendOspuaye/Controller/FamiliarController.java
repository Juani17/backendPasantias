package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import com.Ospuaye.BackendOspuaye.Service.FamiliarService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/familiares")
public class FamiliarController extends BaseController<Familiar, Long> {

    private final FamiliarService service;

    public FamiliarController(FamiliarService service) {
        super(service);
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Familiar f) {
        try {
            return ResponseEntity.ok(service.crear(f));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
