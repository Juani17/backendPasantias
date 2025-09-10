package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Dto.*;
import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    private final EmpresaRepository empresaRepository;

    public AuthResponse register(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank())
            throw new RuntimeException("Email es obligatorio");
        if (request.getContrasena() == null || request.getContrasena().length() < 6)
            throw new RuntimeException("Contraseña mínimo 6 caracteres");

        var rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso.");
        }

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

        return AuthResponse.builder()
                .token(jwtToken)
                .rol(rolUser.getNombre())
                .build();
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

        return AuthResponse.builder()
                .token(jwtToken)
                .rol(usuario.getRol().getNombre())
                .build();
    }

    @Transactional
    public AuthResponse registerBeneficiario(RegisterBeneficiarioRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank())
            throw new RuntimeException("Email es obligatorio");
        if (usuarioRepository.existsByEmail(request.getEmail())) {
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
                .dni(Long.valueOf(request.getDni()))
                .cuil(Long.valueOf(request.getCuil()))
                .telefono(Long.valueOf(request.getTelefono()))
                .sexo(request.getSexo())
                .estado(request.getEstado())
                .afiliadoSindical(request.getAfiliadoSindical() != null && request.getAfiliadoSindical())
                .esJubilado(request.getEsJubilado() != null && request.getEsJubilado())
                .build();

        // Empresa opcional
        if (request.getEmpresaId() != null) {
            var empresa = empresaRepository.findById(request.getEmpresaId())
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
            beneficiario.setEmpresa(empresa);
        }

        beneficiarioRepository.save(beneficiario);

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getContrasena())
                        .authorities("ROLE_" + rolUser.getNombre().toUpperCase())
                        .build()
        );

        return AuthResponse.builder()
                .token(token)
                .rol(rolUser.getNombre())
                .build();
    }


    @Transactional
    public AuthResponse registerMedico(RegisterMedicoRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank())
            throw new RuntimeException("Email es obligatorio");
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso.");
        }

        var rolMedico = rolRepository.findByNombre("MEDICO")
                .or(() -> rolRepository.findByNombre("MEDICO OFTALMOLOGO"))
                .or(() -> rolRepository.findByNombre("MEDICO ORTOPEDIA"))
                .orElseThrow(() -> new RuntimeException("Ninguno de los roles buscados fue encontrado"));

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
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .dni(request.getDni())
                .cuil(request.getCuil())
                .telefono(request.getTelefono())
                .sexo(request.getSexo())
                .estado(request.getEstado())
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

        return AuthResponse.builder()
                .token(token)
                .rol(rolMedico.getNombre())
                .build();
    }

}