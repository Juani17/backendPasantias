package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.BeneficiarioDTO;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import com.Ospuaye.BackendOspuaye.Service.BeneficiarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/beneficiarios")
public class BeneficiarioController extends BaseController<Beneficiario, Long> {

    private final BeneficiarioService beneficiarioService;
    private final BeneficiarioRepository beneficiarioRepository;

    public BeneficiarioController(BeneficiarioService beneficiarioService, BeneficiarioRepository beneficiarioRepository) {
        super(beneficiarioService); //
        this.beneficiarioService = beneficiarioService;
        this.beneficiarioRepository = beneficiarioRepository;
    }




    @GetMapping("/buscarPorDni")
    public ResponseEntity<?> buscarPorDni(@RequestParam String dni) {
        Optional<Beneficiario> beneficiario = beneficiarioService.buscarPorDni(dni);
        return beneficiario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    @GetMapping("/dto")
    public ResponseEntity<List<BeneficiarioDTO>> listarBeneficiarios() {
        List<BeneficiarioDTO> lista = beneficiarioService.listarDTOs();
        return ResponseEntity.ok(lista);
    }



}
