package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.BaseNombrable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseNombrableRepository<E extends BaseNombrable, ID extends Serializable> extends BaseRepository<E, ID> {

    List<E> findByNombreContainingIgnoreCase(String nombre);

    List<E> findByActivoTrueAndNombreContainingIgnoreCase(String nombre);
}
