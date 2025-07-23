package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Service.BeneficiarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/beneficiarios")
public class BeneficiarioController extends BaseController<Beneficiario, Long> {

    private final BeneficiarioService beneficiarioService;

    public BeneficiarioController(BeneficiarioService beneficiarioService) {
        super(beneficiarioService); // ✅ llamar al constructor del padre
        this.beneficiarioService = beneficiarioService;
    }


    @GetMapping("/buscarPorDni")
    public ResponseEntity<?> buscarPorDni(@RequestParam String dni) {
        Optional<Beneficiario> beneficiario = beneficiarioService.buscarPorDni(dni);
        return beneficiario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearBeneficiario(@Valid @RequestBody Beneficiario beneficiario) {
        if (beneficiarioService.dniExiste(String.valueOf(beneficiario.getDni()))) {
            return ResponseEntity.badRequest().body("El DNI ya está registrado");
        }
        return ResponseEntity.ok(beneficiarioService.crear(beneficiario));
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarBeneficiario(@RequestBody Beneficiario beneficiario) {
        Optional<Beneficiario> existente = beneficiarioService.buscarPorId(beneficiario.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!existente.get().getDni().equals(beneficiario.getDni()) &&
                beneficiarioService.dniExiste(String.valueOf(beneficiario.getDni()))) {
            return ResponseEntity.badRequest().body("El nuevo DNI ya está en uso");
        }

        return ResponseEntity.ok(beneficiarioService.actualizar(beneficiario));
    }

}
