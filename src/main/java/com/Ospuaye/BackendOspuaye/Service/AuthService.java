package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Dto.*;
import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BeneficiarioRepository beneficiarioRepository;
    private final MedicoRepository medicoRepository;
    private final AreaRepository areaRepository;

    public AuthResponse register(RegisterRequest request) {
        // Buscar el rol por nombre (USER)
        var rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        var usuario = Usuario.builder()
                .email(request.getEmail())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .rol(rolUser)
                .build();

        usuarioRepository.save(usuario);

        var jwtToken = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getContrasena())
                        .authorities("ROLE_" + rolUser.getNombre().toUpperCase())
                        .build()
        );

        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getContrasena()
                )
        );

        var usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var jwtToken = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getContrasena())
                        .authorities("ROLE_" + usuario.getRol().getNombre().toUpperCase())
                        .build()
        );

        return new AuthResponse(jwtToken);
    }

    @Transactional
    public AuthResponse registerBeneficiario(RegisterBeneficiarioRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso.");
        }

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está en uso.");
        }

        var rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .rol(rolUser)
                .build();

        usuarioRepository.save(usuario);

        Beneficiario beneficiario = Beneficiario.builder()
                .usuario(usuario)
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .dni(request.getDni())
                .cuil(request.getCuil())
                .telefono(request.getTelefono())
                .build();

        beneficiarioRepository.save(beneficiario);

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getContrasena())
                        .authorities("ROLE_" + rolUser.getNombre().toUpperCase())
                        .build()
        );

        return new AuthResponse(token);
    }

    @Transactional
    public AuthResponse registerMedico(RegisterMedicoRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso.");
        }

        var rolMedico = rolRepository.findByNombre("MEDICO")
                .orElseThrow(() -> new RuntimeException("Rol MEDICO no encontrado"));

        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .rol(rolMedico)
                .build();

        usuarioRepository.save(usuario);

        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        Medico medico = Medico.builder()
                .usuario(usuario)
                .matricula(request.getMatricula())
                .area(area)
                .build();

        medicoRepository.save(medico);

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getContrasena())
                        .authorities("ROLE_" + rolMedico.getNombre().toUpperCase())
                        .build()
        );

        return new AuthResponse(token);
    }
}
