package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.EstadoPersona;
import com.Ospuaye.BackendOspuaye.Entity.Enum.TipoDocumento;
import com.Ospuaye.BackendOspuaye.Repository.PersonaRepository;
import com.Ospuaye.BackendOspuaye.Repository.NacionalidadRepository;
import com.Ospuaye.BackendOspuaye.Repository.DomicilioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public abstract class PersonaService extends BaseService<Persona, Long> {

    private final PersonaRepository personaRepository;
    private final NacionalidadRepository nacionalidadRepository;
    private final DomicilioRepository domicilioRepository;

    public PersonaService(PersonaRepository personaRepository,
                          NacionalidadRepository nacionalidadRepository,
                          DomicilioRepository domicilioRepository) {
        super(personaRepository);
        this.personaRepository = personaRepository;
        this.nacionalidadRepository = nacionalidadRepository;
        this.domicilioRepository = domicilioRepository;
    }

    @Override
    @Transactional
    public Persona crear(Persona persona) throws Exception {
        if (persona.getNombre() == null || persona.getNombre().isBlank())
            throw new Exception("El nombre es obligatorio");

        if (persona.getApellido() == null || persona.getApellido().isBlank())
            throw new Exception("El apellido es obligatorio");

        if (persona.getTipoDocumento() == null)
            throw new Exception("El tipo de documento es obligatorio");

        if (persona.getDni() == null)
            throw new Exception("El número de documento es obligatorio");

        // Validar combinación tipoDocumento + dni
        if (personaRepository.findByTipoDocumentoAndDni(persona.getTipoDocumento(), persona.getDni()).isPresent())
            throw new Exception("Ya existe una persona con ese tipo y número de documento");

        if (persona.getCuil() == null || persona.getCuil().describeConstable().isEmpty())
            throw new Exception("El CUIL es obligatorio");

        if (personaRepository.findByCuil(persona.getCuil()).isPresent())
            throw new Exception("Ya existe una persona con ese CUIL");

        validarNacionalidad(persona.getNacionalidad());
        validarDomicilio(persona.getDomicilio());

        if (persona.getActivo() == null) persona.setActivo(true);
        if (persona.getEstado() == null) persona.setEstado(EstadoPersona.SIN_DEFINIR);

        return personaRepository.save(persona);
    }

    @Override
    @Transactional
    public Persona actualizar(Persona persona) throws Exception {
        if (persona.getId() == null || !personaRepository.existsById(persona.getId()))
            throw new Exception("Persona no encontrada");

        Persona existente = personaRepository.findById(persona.getId()).get();

        if (persona.getNombre() != null) existente.setNombre(persona.getNombre());
        if (persona.getApellido() != null) existente.setApellido(persona.getApellido());

        if (persona.getTipoDocumento() != null) existente.setTipoDocumento(persona.getTipoDocumento());

        if (persona.getDni() != null && !persona.getDni().equals(existente.getDni())) {
            // Verifica si ya existe la combinación tipoDocumento + dni
            TipoDocumento tipo = persona.getTipoDocumento() != null ? persona.getTipoDocumento() : existente.getTipoDocumento();
            if (personaRepository.findByTipoDocumentoAndDni(tipo, persona.getDni()).isPresent())
                throw new Exception("Ya existe una persona con ese tipo y número de documento");
            existente.setDni(persona.getDni());
        }

        if (persona.getCuil() != null && !persona.getCuil().equals(existente.getCuil())) {
            if (personaRepository.findByCuil(persona.getCuil()).isPresent())
                throw new Exception("CUIL ya en uso");
            existente.setCuil(persona.getCuil());
        }

        if (persona.getTelefono() != null) existente.setTelefono(persona.getTelefono());
        if (persona.getCorreoElectronico() != null) existente.setCorreoElectronico(persona.getCorreoElectronico());
        if (persona.getActivo() != null) existente.setActivo(persona.getActivo());
        if (persona.getSexo() != null) existente.setSexo(persona.getSexo());
        if (persona.getEstado() != null) existente.setEstado(persona.getEstado());

        if (persona.getNacionalidad() != null) validarNacionalidad(persona.getNacionalidad());
        if (persona.getNacionalidad() != null) existente.setNacionalidad(persona.getNacionalidad());

        if (persona.getDomicilio() != null) validarDomicilio(persona.getDomicilio());
        if (persona.getDomicilio() != null) existente.setDomicilio(persona.getDomicilio());

        return personaRepository.save(existente);
    }

    private void validarNacionalidad(Nacionalidad n) throws Exception {
        if (n == null || n.getId() == null || !nacionalidadRepository.existsById(n.getId()))
            throw new Exception("La nacionalidad asociada no existe");
    }

    private void validarDomicilio(Domicilio d) throws Exception {
        if (d == null) throw new Exception("El domicilio es obligatorio");
        if (d.getId() != null && !domicilioRepository.existsById(d.getId()))
            throw new Exception("El domicilio no existe");
    }

    @Transactional(readOnly = true)
    public List<Persona> listarActivos() {
        return personaRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .toList();
    }
}
