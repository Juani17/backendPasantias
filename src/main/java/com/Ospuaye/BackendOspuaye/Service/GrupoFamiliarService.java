package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import com.Ospuaye.BackendOspuaye.Repository.GrupoFamiliarRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class GrupoFamiliarService extends BaseService<GrupoFamiliar, Long> {

    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final BeneficiarioRepository beneficiarioRepository;

    public GrupoFamiliarService(GrupoFamiliarRepository repository,
                                BeneficiarioRepository beneficiarioRepository) {
        super(repository);
        this.grupoFamiliarRepository = repository;
        this.beneficiarioRepository = beneficiarioRepository;
    }

    @Override
    public GrupoFamiliar crear(GrupoFamiliar gf) throws Exception {
        validar(gf, null);
        if (gf.getFechaAlta() == null) {
            gf.setFechaAlta(new Date());
        }
        if (gf.getActivo() == null) gf.setActivo(true);
        return grupoFamiliarRepository.save(gf);
    }

    @Override
    public GrupoFamiliar actualizar(GrupoFamiliar gf) throws Exception {
        if (gf.getId() == null || !grupoFamiliarRepository.existsById(gf.getId())) {
            throw new Exception("Grupo familiar no encontrado");
        }
        validar(gf, gf.getId());
        return grupoFamiliarRepository.save(gf);
    }

    private void validar(GrupoFamiliar gf, Long idActual) throws Exception {
        if (gf.getNombreGrupo() == null || gf.getNombreGrupo().isBlank()) {
            throw new Exception("El nombre del grupo es obligatorio");
        }

        Beneficiario titular = gf.getTitular();
        if (titular == null || titular.getId() == null || !beneficiarioRepository.existsById(titular.getId())) {
            throw new Exception("El titular del grupo no existe");
        }

        // Un titular no debería tener dos grupos activos
        var existente = grupoFamiliarRepository.findByTitularId(titular.getId());
        if (existente.isPresent() && (idActual == null || !existente.get().getId().equals(idActual))) {
            throw new Exception("El titular ya posee un grupo familiar activo");
        }

        // Nombre único por titular
        if (grupoFamiliarRepository.existsByNombreGrupoAndTitularId(gf.getNombreGrupo(), titular.getId())) {
            throw new Exception("El titular ya tiene un grupo con ese nombre");
        }
    }

    public Optional<GrupoFamiliar> buscarPorTitularActivo(Long titularId) throws Exception {
        if (titularId == null) {
            throw new IllegalArgumentException("El Id no puede ser nulo");
        }

        Optional<GrupoFamiliar> grupoFamiliar = grupoFamiliarRepository.findByTitularIdAndActivoTrue(titularId);

        if (grupoFamiliar.isEmpty()) {
            throw new IllegalArgumentException("No se encontró un Titular con el id especificado");
        }

        return grupoFamiliar;
    }
}
