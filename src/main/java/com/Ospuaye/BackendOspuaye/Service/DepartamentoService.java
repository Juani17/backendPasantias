package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Departamento;
import com.Ospuaye.BackendOspuaye.Repository.DepartamentoRepository;
import com.Ospuaye.BackendOspuaye.Repository.ProvinciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService extends BaseNombrableService<Departamento, Long> {

    private final DepartamentoRepository departamentoRepository;
    private final ProvinciaRepository provinciaRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository,
                               ProvinciaRepository provinciaRepository) {
        super(departamentoRepository);
        this.departamentoRepository = departamentoRepository;
        this.provinciaRepository = provinciaRepository;
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
}
