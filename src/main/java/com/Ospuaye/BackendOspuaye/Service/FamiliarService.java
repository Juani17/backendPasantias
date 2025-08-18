package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Entity.TipoParentesco;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import com.Ospuaye.BackendOspuaye.Repository.FamiliarRepository;
import com.Ospuaye.BackendOspuaye.Repository.GrupoFamiliarRepository;
import org.springframework.stereotype.Service;

@Service
public class FamiliarService extends BaseService<Familiar, Long> {

    private final FamiliarRepository familiarRepository;
    private final BeneficiarioRepository beneficiarioRepository;
    private final GrupoFamiliarRepository grupoFamiliarRepository;

    public FamiliarService(FamiliarRepository repository,
                           BeneficiarioRepository beneficiarioRepository,
                           GrupoFamiliarRepository grupoFamiliarRepository) {
        super(repository);
        this.familiarRepository = repository;
        this.beneficiarioRepository = beneficiarioRepository;
        this.grupoFamiliarRepository = grupoFamiliarRepository;
    }

    @Override
    public Familiar crear(Familiar familiar) throws Exception {
        validar(familiar, null);
        return familiarRepository.save(familiar);
    }

    @Override
    public Familiar actualizar(Familiar familiar) throws Exception {
        if (familiar.getId() == null || !familiarRepository.existsById(familiar.getId())) {
            throw new Exception("Familiar no encontrado");
        }
        validar(familiar, familiar.getId());
        return familiarRepository.save(familiar);
    }

    private void validar(Familiar f, Long idActual) throws Exception {
        if (f.getNombre() == null || f.getNombre().isBlank()) throw new Exception("El nombre es obligatorio");
        if (f.getApellido() == null || f.getApellido().isBlank()) throw new Exception("El apellido es obligatorio");
        if (f.getDni() == null) throw new Exception("El DNI es obligatorio");
        if (f.getDni() < 1_000_000 || f.getDni() > 99_999_999) throw new Exception("El DNI debe tener entre 7 y 8 dígitos");

        var existente = familiarRepository.findByDni(f.getDni());
        if (existente.isPresent() && (idActual == null || !existente.get().getId().equals(idActual))) {
            throw new Exception("Ya existe un familiar con ese DNI");
        }

        if (f.getTipoParentesco() == null || f.getTipoParentesco() == TipoParentesco.Solo_Parentescos
                || f.getTipoParentesco() == TipoParentesco.Sin_Informacion) {
            throw new Exception("El tipo de parentesco es obligatorio");
        }

        GrupoFamiliar gf = f.getGrupoFamiliar();
        if (gf == null || gf.getId() == null || !grupoFamiliarRepository.existsById(gf.getId())) {
            throw new Exception("El grupo familiar asociado no existe");
        }

        Beneficiario b = f.getBeneficiario();
        if (b == null || b.getId() == null || !beneficiarioRepository.existsById(b.getId())) {
            throw new Exception("El beneficiario asociado no existe");
        }
    }
}
