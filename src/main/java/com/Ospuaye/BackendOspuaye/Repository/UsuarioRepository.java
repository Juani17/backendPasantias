package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByRol_NombreIgnoreCase(String nombreRol);
    // Activos
    Page<Usuario> findByEmailContainingIgnoreCaseAndActivoTrue(String email, Pageable pageable);

    // Inactivos
    Page<Usuario> findByEmailContainingIgnoreCaseAndActivoFalse(String email, Pageable pageable);
}
