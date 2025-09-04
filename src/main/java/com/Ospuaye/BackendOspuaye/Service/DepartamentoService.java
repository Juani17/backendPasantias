package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Departamento;
import com.Ospuaye.BackendOspuaye.Repository.DepartamentoRepository;
import com.Ospuaye.BackendOspuaye.Repository.ProvinciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
