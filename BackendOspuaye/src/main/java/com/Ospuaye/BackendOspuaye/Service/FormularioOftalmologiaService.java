package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.FormularioOftalmologia;
import com.Ospuaye.BackendOspuaye.Repository.FormularioOftalmologiaRepository;
import org.springframework.stereotype.Service;

@Service
public class FormularioOftalmologiaService extends BaseService<FormularioOftalmologia, Long> {
    public FormularioOftalmologiaService(FormularioOftalmologiaRepository repository) {
        super(repository);
    }
}
