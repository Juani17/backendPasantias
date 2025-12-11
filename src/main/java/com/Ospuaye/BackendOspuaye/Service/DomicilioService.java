package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Domicilio;
import com.Ospuaye.BackendOspuaye.Entity.Localidad;
import com.Ospuaye.BackendOspuaye.Repository.DomicilioRepository;
import com.Ospuaye.BackendOspuaye.Repository.LocalidadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DomicilioService extends BaseService<Domicilio, Long> {

    private final DomicilioRepository domicilioRepository;
    private final LocalidadRepository localidadRepository;

    public DomicilioService(DomicilioRepository domicilioRepository,
                            LocalidadRepository localidadRepository) {
        super(domicilioRepository);
        this.domicilioRepository = domicilioRepository;
        this.localidadRepository = localidadRepository;
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Domicilio> buscar(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginar(page, size); // ✅ activos por defecto
        }

        String q = query.trim();
        return domicilioRepository.findByCalleContainingIgnoreCaseAndActivoTrueOrNumeracionContainingIgnoreCaseAndActivoTrueOrBarrioContainingIgnoreCaseAndActivoTrueOrManzanaPisoContainingIgnoreCaseAndActivoTrueOrCasaDepartamentoContainingIgnoreCaseAndActivoTrueOrEmpresa_RazonSocialContainingIgnoreCaseAndActivoTrue(
                q, q, q, q, q, q, pageable);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Domicilio> buscarInactivos(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginarInactivos(page, size); // ✅ inactivos por defecto
        }

        String q = query.trim();
        return domicilioRepository.findByCalleContainingIgnoreCaseAndActivoFalseOrNumeracionContainingIgnoreCaseAndActivoFalseOrBarrioContainingIgnoreCaseAndActivoFalseOrManzanaPisoContainingIgnoreCaseAndActivoFalseOrCasaDepartamentoContainingIgnoreCaseAndActivoFalseOrEmpresa_RazonSocialContainingIgnoreCaseAndActivoFalse(
                q, q, q, q, q, q, pageable);
    }


@Override
    @Transactional
    public Domicilio crear(Domicilio entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("El domicilio no puede ser nulo");

        if (entity.getCalle() == null || entity.getCalle().isBlank())
            throw new IllegalArgumentException("La calle es obligatoria");

        if (entity.getLocalidad() != null) {
            Long locId = entity.getLocalidad().getId();
            if (locId == null) throw new IllegalArgumentException("La localidad debe tener ID");
            if (!localidadRepository.existsById(locId))
                throw new IllegalArgumentException("La localidad indicada no existe");
        }
        // 🔹 Si piso o departamento vienen vacíos o nulos → poner "Indefinido"
        if (entity.getManzanaPiso() == null || entity.getManzanaPiso().isBlank()) {
            entity.setManzanaPiso("Indefinido");
        }
        if (entity.getCasaDepartamento() == null || entity.getCasaDepartamento().isBlank()) {
            entity.setCasaDepartamento("Indefinido");
        }

        if (entity.getActivo() == null) entity.setActivo(true);

        return domicilioRepository.save(entity);
    }

    @Override
    @Transactional
    public Domicilio actualizar(Domicilio entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("El domicilio o su ID no pueden ser nulos");

        Domicilio existente = domicilioRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Domicilio no encontrado"));

        if (entity.getCalle() != null && !entity.getCalle().isBlank())
            existente.setCalle(entity.getCalle());
        existente.setNumeracion(entity.getNumeracion());
        existente.setBarrio(entity.getBarrio());
        existente.setManzanaPiso(entity.getManzanaPiso());
        existente.setCasaDepartamento(entity.getCasaDepartamento());
        existente.setReferencia(entity.getReferencia());
        if (entity.getActivo() != null) existente.setActivo(entity.getActivo());

        if (entity.getLocalidad() != null) {
            Long locId = entity.getLocalidad().getId();
            if (locId == null || !localidadRepository.existsById(locId))
                throw new IllegalArgumentException("La localidad indicada no existe");
            existente.setLocalidad(entity.getLocalidad());
        }

        return domicilioRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<Domicilio> listarPorLocalidad(Long localidadId) throws Exception {
        if (localidadId == null) throw new IllegalArgumentException("El ID de localidad es obligatorio");
        Localidad loc = localidadRepository.findById(localidadId)
                .orElseThrow(() -> new IllegalArgumentException("Localidad no encontrada"));
        return domicilioRepository.findByLocalidad(loc);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Optional<Domicilio> listarPorCalleYNumeracionYLocalidad(String calle, String numeracion, Long localidadId) {
        try {
            // Validar parámetros básicos
            if (calle == null || calle.isBlank() || numeracion == null || numeracion.isBlank()) {
                return Optional.empty(); // No buscamos si faltan datos esenciales
            }

            // Buscar según disponibilidad de localidad
            if (localidadId == null) {
                return domicilioRepository.findByCalleAndNumeracion(calle, numeracion);
            }

            return domicilioRepository.findByCalleAndNumeracionAndLocalidad_Id(calle, numeracion, localidadId);

        } catch (Exception e) {
            // Cualquier error inesperado devuelve vacío, sin romper el flujo del importador
            return Optional.empty();
        }
    }


    @Transactional(readOnly = true)
    public List<Domicilio> listarActivos() {
        return domicilioRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Domicilio> listarActivosPorLocalidad(Long localidadId) throws Exception {
        if (localidadId == null) throw new IllegalArgumentException("El ID de localidad es obligatorio");
        if (!localidadRepository.existsById(localidadId))
            throw new IllegalArgumentException("Localidad no encontrada");
        return domicilioRepository.findByLocalidadIdAndActivoTrue(localidadId);
    }

    @Transactional(readOnly = true)
    public List<Domicilio> buscarSimple(String filtro) {

        if (filtro == null || filtro.trim().isEmpty()) {
            return List.of(); // No devolvemos todo para evitar carga masiva
        }

        String f = filtro.trim();

        return domicilioRepository
                .findTop20ByCalleContainingIgnoreCaseOrNumeracionContainingIgnoreCaseOrBarrioContainingIgnoreCase(
                        f, f, f
                );
    }

}
