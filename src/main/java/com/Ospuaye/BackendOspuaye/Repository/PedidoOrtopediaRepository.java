package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoOrtopediaRepository extends BaseRepository<PedidoOrtopedia, Long>{
    List<PedidoOrtopedia> findByBeneficiario_Id(Long idBeneficiario);
    List<PedidoOrtopedia> findByMedico_Id(Long idMedico);


}
