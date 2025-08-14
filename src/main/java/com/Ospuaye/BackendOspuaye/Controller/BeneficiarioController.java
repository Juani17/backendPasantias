package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.BeneficiarioDTO;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import com.Ospuaye.BackendOspuaye.Service.BeneficiarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/beneficiarios")
public class BeneficiarioController extends BaseController<Beneficiario, Long> {

    private final BeneficiarioService beneficiarioService;
    private final BeneficiarioRepository beneficiarioRepository;

    public BeneficiarioController(BeneficiarioService beneficiarioService, BeneficiarioRepository beneficiarioRepository) {
        super(beneficiarioService);
        this.beneficiarioService = beneficiarioService;
        this.beneficiarioRepository = beneficiarioRepository;
    }

    @GetMapping("/buscarPorDni")
    public ResponseEntity<?> buscarPorDni(@RequestParam String dni) {
        try {
            Optional<Beneficiario> beneficiario = beneficiarioService.buscarPorDni(dni);
            return beneficiario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarBeneficiario(@Valid @RequestBody Beneficiario beneficiario) {
        try {
            // El servicio se encarga de validar existencia, DNI, etc.
            Beneficiario actualizado = beneficiarioService.actualizar(beneficiario);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/dto")
    public ResponseEntity<?> listarBeneficiariosDTO() {
        try {
            List<BeneficiarioDTO> lista = beneficiarioService.listarDTOs();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
