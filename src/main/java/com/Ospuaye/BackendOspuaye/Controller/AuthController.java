package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.*;
import com.Ospuaye.BackendOspuaye.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/register/beneficiario")
    public ResponseEntity<AuthResponse> registerBeneficiario(@RequestBody RegisterBeneficiarioRequest request) {
        return ResponseEntity.ok(authService.registerBeneficiario(request));
    }

    @PostMapping("/register/medico")
    public ResponseEntity<AuthResponse> registerMedico(@RequestBody RegisterMedicoRequest request) {
        return ResponseEntity.ok(authService.registerMedico(request));
    }
}
