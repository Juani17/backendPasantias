package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PedidoRepository extends BaseNombrableRepository<Pedido, Long> {

    List<Pedido> findByBeneficiario(Beneficiario beneficiario);
    List<Pedido> findByGrupoFamiliar(GrupoFamiliar grupoFamiliar);
    List<Pedido> findByUsuario(Usuario usuario);
    List<Pedido> findByMedico(Medico medico);
    List<Pedido> findByDni(Integer dni);
    List<Pedido> findByMedicoIsNull();



    // 🔍 Métodos adicionales solicitados
    List<Pedido> findByEstado(Estado estado);

    @Query("SELECT p FROM Pedido p WHERE p.fechaIngreso BETWEEN :desde AND :hasta")
    List<Pedido> findByFechaIngresoBetween(@Param("desde") Date desde, @Param("hasta") Date hasta);

    @Query("SELECT p FROM Pedido p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Pedido> searchByNombre(@Param("nombre") String nombre);
}
