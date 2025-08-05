package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Rol;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends BaseRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);

}
