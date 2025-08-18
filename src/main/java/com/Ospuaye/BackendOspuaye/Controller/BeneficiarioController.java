package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.BeneficiarioDTO;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import com.Ospuaye.BackendOspuaye.Service.BeneficiarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/beneficiarios")
public class BeneficiarioController extends BaseController<Beneficiario, Long> {

    private final BeneficiarioService beneficiarioService;

    public BeneficiarioController(BeneficiarioService beneficiarioService,
                                  BeneficiarioRepository beneficiarioRepository) {
        super(beneficiarioService);
        this.beneficiarioService = beneficiarioService;
    }

    @GetMapping("/buscarPorDni")
    public ResponseEntity<?> buscarPorDni(@RequestParam String dni) {
        Optional<Beneficiario> beneficiario = beneficiarioService.buscarPorDni(dni);
        return beneficiario.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró beneficiario"));
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Beneficiario beneficiario) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(beneficiarioService.crear(beneficiario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Beneficiario beneficiario) {
        try {
            return ResponseEntity.ok(beneficiarioService.actualizar(beneficiario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/dto")
    public ResponseEntity<List<BeneficiarioDTO>> listarDTO() {
        return ResponseEntity.ok(beneficiarioService.listarDTOs());
    }
}
