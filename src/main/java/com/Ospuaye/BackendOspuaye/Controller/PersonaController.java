package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Persona;
import com.Ospuaye.BackendOspuaye.Service.PersonaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public abstract class PersonaController extends BaseController<Persona, Long> {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        super(personaService);
        this.personaService = personaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Persona entity) {
        try {
            Persona creado = personaService.crear(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear persona: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Persona entity) {
        try {
            entity.setId(id);
            Persona actualizado = personaService.actualizar(entity);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar persona: " + e.getMessage());
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Persona>> listarActivos() {
        return ResponseEntity.ok(personaService.listarActivos());
    }
}
