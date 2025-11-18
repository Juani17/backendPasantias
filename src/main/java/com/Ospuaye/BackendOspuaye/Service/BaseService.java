package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Base;
import com.Ospuaye.BackendOspuaye.Repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<E extends Base, ID extends Serializable> {

    protected BaseRepository<E, ID> baseRepository;

    public BaseService(BaseRepository<E, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Transactional(readOnly = true)
    public List<E> listar() throws Exception {
        return baseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<E> paginar(int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;
        Pageable pageable = PageRequest.of(page, size);
        return baseRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<E> buscarPorId(ID id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return baseRepository.findById(id);
    }

    @Transactional
    public E crear(E entity) throws Exception {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        // aseguramos que activo nunca sea null al crear
        if (entity.getActivo() == null) {
            entity.setActivo(true);
        }
        return baseRepository.save(entity);
    }

    @Transactional
    public E actualizar(E entity) throws Exception {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("La entidad o su ID no pueden ser nulos");
        }
        if (!baseRepository.existsById((ID) entity.getId())) {
            throw new IllegalArgumentException("No se encontró la entidad con el ID proporcionado");
        }
        return baseRepository.save(entity);
    }

    @Transactional
    public void eliminar(ID id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        if (!baseRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró la entidad con el ID proporcionado");
        }
        baseRepository.deleteById(id);
    }

    @Transactional
    public E alternarEstado(ID id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        E entity = baseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la entidad con el ID proporcionado"));

        // invertimos el estado, si es null lo ponemos en true
        Boolean activoActual = entity.getActivo();
        entity.setActivo(activoActual == null ? true : !activoActual);

        return baseRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<E> listarActivos() throws Exception {
        return baseRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<E> buscar(String filtro) throws Exception {
        if (filtro == null || filtro.trim().isEmpty()) {
            throw new IllegalArgumentException("El parámetro 'filtro' no puede estar vacío");
        }

        String filtroLower = filtro.toLowerCase();
        List<E> todas = baseRepository.findAll();
        List<E> resultado = new ArrayList<>();

        for (E entidad : todas) {
            for (Field campo : entidad.getClass().getDeclaredFields()) {
                campo.setAccessible(true);
                try {
                    Object valor = campo.get(entidad);

                    // Ignorar nulos y campos de tipo fecha o colecciones
                    if (valor == null) continue;
                    if (valor instanceof java.util.Date ||
                            valor instanceof java.time.LocalDate ||
                            valor instanceof java.time.LocalDateTime ||
                            valor instanceof java.util.List ||
                            valor instanceof java.util.Set) continue;

                    // Comparar según tipo
                    if (valor instanceof String) {
                        if (((String) valor).toLowerCase().contains(filtroLower)) {
                            resultado.add(entidad);
                            break;
                        }
                    } else if (valor instanceof Number) {
                        if (valor.toString().contains(filtro)) {
                            resultado.add(entidad);
                            break;
                        }
                    }
                } catch (IllegalAccessException ignored) {}
            }
        }

        return resultado;
    }

    @Transactional(readOnly = true)
    public Page<E> buscarConPaginado(String filtro, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);

        // 🔹 Si no hay filtro, devolver todo paginado directamente del repo
        if (filtro == null || filtro.trim().isEmpty()) {
            return baseRepository.findAll(pageable);
        }

        // 🔹 Buscar todos (en memoria) y filtrar por reflexión
        String filtroLower = filtro.toLowerCase();
        List<E> todas = baseRepository.findAll();
        List<E> filtradas = new ArrayList<>();

        for (E entidad : todas) {
            for (Field campo : entidad.getClass().getDeclaredFields()) {
                campo.setAccessible(true);
                try {
                    Object valor = campo.get(entidad);

                    // Ignorar nulos y campos de tipo fecha o colección
                    if (valor == null) continue;
                    if (valor instanceof java.util.Date ||
                            valor instanceof java.time.LocalDate ||
                            valor instanceof java.time.LocalDateTime ||
                            valor instanceof java.util.List ||
                            valor instanceof java.util.Set) continue;

                    if (valor instanceof String) {
                        if (((String) valor).toLowerCase().contains(filtroLower)) {
                            filtradas.add(entidad);
                            break;
                        }
                    } else if (valor instanceof Number) {
                        if (valor.toString().contains(filtro)) {
                            filtradas.add(entidad);
                            break;
                        }
                    }
                } catch (IllegalAccessException ignored) {}
            }
        }

        // 🔹 Paginado manual sobre la lista filtrada
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtradas.size());
        List<E> subList = filtradas.subList(start, end);

        System.out.println("🔍 [BUSCAR PAGINADO REFLEXIÓN] Filtro: '" + filtro + "' | Resultados: " + filtradas.size());

        return new PageImpl<>(subList, pageable, filtradas.size());
    }


}
