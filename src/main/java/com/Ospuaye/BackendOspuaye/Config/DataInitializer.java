package com.Ospuaye.BackendOspuaye.Config;
import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Entity.Rol;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import com.Ospuaye.BackendOspuaye.Repository.RolRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final AreaRepository areaRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // 1) Crear área ADMIN si no existe
            Area areaAdmin = areaRepository.findByNombre("ADMIN")
                    .orElseGet(() -> {
                        Area nuevaArea = Area.builder()
                                .nombre("ADMIN")
                                .build();
                        return areaRepository.save(nuevaArea);
                    });

            // 2) Crear rol ADMIN si no existe
            Rol rolAdmin = rolRepository.findByNombre("ADMIN")
                    .orElseGet(() -> {
                        Rol nuevoRol = Rol.builder()
                                .nombre("ADMIN")
                                .area(areaAdmin)
                                .build();
                        return rolRepository.save(nuevoRol);
                    });

            // 3) Crear área USER si no existe
            Area areaUser = areaRepository.findByNombre("USER")
                    .orElseGet(() -> {
                        Area nuevaArea = Area.builder()
                                .nombre("USER")
                                .build();
                        return areaRepository.save(nuevaArea);
                    });

            // 4) Crear rol USER si no existe
            Rol rolUser = rolRepository.findByNombre("USER")
                    .orElseGet(() -> {
                        Rol nuevoRol = Rol.builder()
                                .nombre("USER")
                                .area(areaUser)
                                .build();
                        return rolRepository.save(nuevoRol);
                    });

            // 5) Crear área MEDICO OFTALMOLOGO si no existe
            Area areaMedicoOft = areaRepository.findByNombre("MEDICO OFTALMOLOGO")
                    .orElseGet(() -> {
                        Area nuevaArea = Area.builder()
                                .nombre("MEDICO OFTALMOLOGO")
                                .build();
                        return areaRepository.save(nuevaArea);
                    });

            // 6) Crear rol MEDICO OFTALMOLOGO si no existe
            Rol rolMedicoOft = rolRepository.findByNombre("MEDICO OFTALMOLOGO")
                    .orElseGet(() -> {
                        Rol nuevoRol = Rol.builder()
                                .nombre("MEDICO OFTALMOLOGO")
                                .area(areaMedicoOft)
                                .build();
                        return rolRepository.save(nuevoRol);
                    });

            // 7) Crear área MEDICO ORTOPEDIA si no existe
            Area areaMedicoOrt = areaRepository.findByNombre("MEDICO ORTOPEDIA")
                    .orElseGet(() -> {
                        Area nuevaArea = Area.builder()
                                .nombre("MEDICO ORTOPEDIA")
                                .build();
                        return areaRepository.save(nuevaArea);
                    });

            // 8) Crear rol MEDICO ORTOPEDIA si no existe
            Rol rolMedicoOrt = rolRepository.findByNombre("MEDICO ORTOPEDIA")
                    .orElseGet(() -> {
                        Rol nuevoRol = Rol.builder()
                                .nombre("MEDICO ORTOPEDIA")
                                .area(areaMedicoOrt)
                                .build();
                        return rolRepository.save(nuevoRol);
                    });

            // 9) Crear usuario ADMIN si no existe
            if (usuarioRepository.findByEmail("admin@admin.com").isEmpty()) {
                Usuario admin = Usuario.builder()
                        .email("admin@admin.com")
                        .contrasena(passwordEncoder.encode("admin1234"))
                        .rol(rolAdmin)
                        .build();

                usuarioRepository.save(admin);
                System.out.println("✅ Usuario ADMIN creado: admin@admin.com / admin1234");
            } else {
                System.out.println("ℹ️ Usuario ADMIN ya existe, no se creó otro.");
            }
        };
    }
}