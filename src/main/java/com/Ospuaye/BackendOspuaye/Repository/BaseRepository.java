package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Base;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<E extends Base, ID extends Serializable> extends JpaRepository<E, ID> {
    List<E> findByActivoTrue();

    // 🔹 Paginado dinámico con búsqueda genérica
    @Query("SELECT e FROM #{#entityName} e WHERE " +
            "(LOWER(CAST(e AS string)) LIKE LOWER(CONCAT('%', :filtro, '%')) OR :filtro IS NULL)")
    Page<E> buscarConPaginado(String filtro, Pageable pageable);
}
