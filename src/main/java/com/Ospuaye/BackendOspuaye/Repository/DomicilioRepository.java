package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Domicilio;
import com.Ospuaye.BackendOspuaye.Entity.Localidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomicilioRepository extends BaseRepository<Domicilio, Long> {
    List<Domicilio> findByLocalidad(Localidad localidad);
    List<Domicilio> findByActivoTrue();
    List<Domicilio> findByLocalidadIdAndActivoTrue(Long localidadId);
    Optional<Domicilio> findByCalleAndNumeracion(String calle, String numeracion);
    Optional<Domicilio> findByCalleAndNumeracionAndLocalidad_Id(String calle, String numeracion, Long localidad );
    Page<Domicilio> findByCalleContainingIgnoreCaseOrNumeracionContainingIgnoreCaseOrBarrioContainingIgnoreCaseOrManzanaPisoContainingIgnoreCaseOrCasaDepartamentoContainingIgnoreCaseOrEmpresa_RazonSocialContainingIgnoreCase(
            String calle,
            String numeracion,
            String barrio,
            String manzanaPiso,
            String casaDepartamento,
            String razonSocial,
            Pageable pageable
    );

}
