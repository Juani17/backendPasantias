package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.MovimientoObraSocial;
import com.Ospuaye.BackendOspuaye.Entity.Persona;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoObraSocialRepository extends BaseRepository<MovimientoObraSocial, Long> {

    List<MovimientoObraSocial> findByPersona(Persona persona);

    List<MovimientoObraSocial> findByFechaDesdeBetween(Date inicio, Date fin);

    List<MovimientoObraSocial> findByFechaHastaBetween(Date inicio, Date fin);
}
