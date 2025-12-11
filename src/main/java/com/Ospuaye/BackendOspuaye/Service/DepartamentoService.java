package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Departamento;
import com.Ospuaye.BackendOspuaye.Repository.DepartamentoRepository;
import com.Ospuaye.BackendOspuaye.Repository.ProvinciaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService extends BaseService<Departamento, Long> {

    private final DepartamentoRepository departamentoRepository;
    private final ProvinciaRepository provinciaRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository,
                               ProvinciaRepository provinciaRepository) {
        super(departamentoRepository);
        this.departamentoRepository = departamentoRepository;
        this.provinciaRepository = provinciaRepository;
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Departamento> buscar(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginar(page, size); // ✅ activos por defecto
        }

        String q = query.trim();
        return departamentoRepository.findByNombreContainingIgnoreCaseAndActivoTrueOrProvincia_NombreContainingIgnoreCaseAndActivoTrue(
                q, q, pageable);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Departamento> buscarInactivos(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginarInactivos(page, size); // ✅ inactivos por defecto
        }

        String q = query.trim();
        return departamentoRepository.findByNombreContainingIgnoreCaseAndActivoFalseOrProvincia_NombreContainingIgnoreCaseAndActivoFalse(
                q, q, pageable);
    }




    @Override
    public Departamento crear(Departamento entity) throws Exception {
        if (entity.getNombre() == null || entity.getNombre().isBlank()) {
            throw new Exception("El nombre del departamento es obligatorio");
        }
        if (entity.getProvincia() == null || entity.getProvincia().getId() == null ||
                !provinciaRepository.existsById(entity.getProvincia().getId())) {
            throw new Exception("La provincia asociada no existe");
        }
        return super.crear(entity);
    }

    public List<Departamento> listarPorProvincia(Long provinciaId) throws Exception {
        if (!provinciaRepository.existsById(provinciaId)) {
            throw new Exception("La provincia con id " + provinciaId + " no existe");
        }
        return departamentoRepository.findByProvincia_Id(provinciaId);
    }

    public List<Departamento> listarActivos() {
        return departamentoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Departamento ListarPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del Departamento no puede ser nulo o vacio");
        }
        Optional<Departamento> dep = departamentoRepository.findByNombre(nombre);

        if (dep.isEmpty() || dep.get() == null) {
            throw new IllegalArgumentException("No se encontro un departamento con ese nombre");
        }
        return dep.get();
    }

    @Transactional(readOnly = true)
    public List<Departamento> buscarSimplePorNombre(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of(); // retorna vacío si no hay texto
        }

        return departamentoRepository.findByNombreContainingIgnoreCase(nombre.trim());
    }

}
