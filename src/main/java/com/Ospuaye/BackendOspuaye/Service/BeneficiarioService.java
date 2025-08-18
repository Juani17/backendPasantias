package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Dto.BeneficiarioDTO;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeneficiarioService extends BaseService<Beneficiario, Long> {

    private final BeneficiarioRepository beneficiarioRepository;
    private final UsuarioRepository usuarioRepository;

    public BeneficiarioService(BeneficiarioRepository beneficiarioRepository,
                               UsuarioRepository usuarioRepository) {
        super(beneficiarioRepository);
        this.beneficiarioRepository = beneficiarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Beneficiario crear(Beneficiario beneficiario) throws Exception {
        validarObligatorios(beneficiario);
        validarDni(beneficiario.getDni(), null);
        validarCuil(beneficiario.getCuil(), null);
        validarUsuario(beneficiario.getUsuario());
        return beneficiarioRepository.save(beneficiario);
    }

    @Override
    public Beneficiario actualizar(Beneficiario beneficiario) throws Exception {
        if (beneficiario.getId() == null || !beneficiarioRepository.existsById(beneficiario.getId())) {
            throw new Exception("Beneficiario no encontrado");
        }
        if (beneficiario.getDni() != null) validarDni(beneficiario.getDni(), beneficiario.getId());
        if (beneficiario.getCuil() != null) validarCuil(beneficiario.getCuil(), beneficiario.getId());
        if (beneficiario.getUsuario() != null) validarUsuario(beneficiario.getUsuario());
        return beneficiarioRepository.save(beneficiario);
    }

    public Optional<Beneficiario> buscarPorDni(String dni) {
        try {
            return beneficiarioRepository.findByDni(Integer.valueOf(dni));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public boolean dniExiste(String dni) {
        try {
            return beneficiarioRepository.findByDni(Integer.valueOf(dni)).isPresent();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<BeneficiarioDTO> listarDTOs() {
        return beneficiarioRepository.findAll().stream()
                .map(BeneficiarioDTO::new)
                .collect(Collectors.toList());
    }

    // Helpers
    private void validarObligatorios(Beneficiario b) throws Exception {
        if (b.getNombre() == null || b.getNombre().isBlank()) throw new Exception("El nombre es obligatorio");
        if (b.getApellido() == null || b.getApellido().isBlank()) throw new Exception("El apellido es obligatorio");
        if (b.getDni() == null) throw new Exception("El DNI es obligatorio");
        if (b.getDni() < 1_000_000 || b.getDni() > 99_999_999) throw new Exception("El DNI debe tener entre 7 y 8 dígitos");
        if (b.getCuil() == null) throw new Exception("El CUIL es obligatorio");
        if (b.getTelefono() == null) throw new Exception("El teléfono es obligatorio");
        if (b.getUsuario() == null) throw new Exception("El usuario asociado es obligatorio");
    }
    private void validarDni(Integer dni, Long idActual) throws Exception {
        var found = beneficiarioRepository.findByDni(dni);
        if (found.isPresent() && (idActual == null || !found.get().getId().equals(idActual))) {
            throw new Exception("Ya existe un beneficiario con ese DNI");
        }
    }
    private void validarCuil(Long cuil, Long idActual) throws Exception {
        var found = beneficiarioRepository.findByCuil(cuil);
        if (found.isPresent() && (idActual == null || !found.get().getId().equals(idActual))) {
            throw new Exception("Ya existe un beneficiario con ese CUIL");
        }
    }
    private void validarUsuario(Usuario u) throws Exception {
        if (u == null || u.getId() == null || !usuarioRepository.existsById(u.getId())) {
            throw new Exception("El usuario asociado no existe");
        }
        // que no esté ya tomado por otro beneficiario
        var existing = beneficiarioRepository.findByUsuarioId(u.getId());
        if (existing.isPresent()) {
            throw new Exception("El usuario ya está vinculado a otro beneficiario");
        }
    }
}
