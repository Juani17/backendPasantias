package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Service.FamiliarService;
import com.Ospuaye.BackendOspuaye.Service.BeneficiarioService;
import com.Ospuaye.BackendOspuaye.Service.GrupoFamiliarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/familiares")
public class FamiliarController extends BaseController<Familiar, Long> {

    private final FamiliarService familiarService;
    private final BeneficiarioService beneficiarioService;
    private final GrupoFamiliarService grupoFamiliarService;

    public FamiliarController(FamiliarService familiarService,
                              BeneficiarioService beneficiarioService,
                              GrupoFamiliarService grupoFamiliarService) {
        super(familiarService);
        this.familiarService = familiarService;
        this.beneficiarioService = beneficiarioService;
        this.grupoFamiliarService = grupoFamiliarService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Familiar familiar) {
        try {
            Familiar creado = familiarService.crear(familiar);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Familiar familiar) {
        try {
            familiar.setId(id);
            Familiar actualizado = familiarService.actualizar(familiar);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/beneficiario/{beneficiarioId}")
    public ResponseEntity<?> listarPorBeneficiario(@PathVariable Long beneficiarioId) {
        try {
            // Pasamos solo el ID al service
            List<Familiar> lista = familiarService.listarPorBeneficiario(beneficiarioId);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/grupo-familiar/{grupoId}")
    public ResponseEntity<?> listarPorGrupoFamiliar(@PathVariable Long grupoId) {
        try {
            // Pasamos solo el ID al service
            List<Familiar> lista = familiarService.listarPorGrupoFamiliar(grupoId);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
