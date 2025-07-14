package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.FormularioPodologia;
import com.Ospuaye.BackendOspuaye.Repository.FormularioPodologiaRepository;
import org.springframework.stereotype.Service;

@Service
public class FormularioPodologiaService extends BaseService<FormularioPodologia, Long> {
    public FormularioPodologiaService(FormularioPodologiaRepository repository) {
        super(repository);
    }
}
