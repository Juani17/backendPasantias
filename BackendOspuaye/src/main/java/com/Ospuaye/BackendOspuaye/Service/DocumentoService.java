package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Repository.DocumentoRepository;
import org.springframework.stereotype.Service;

@Service
public class DocumentoService extends BaseService<Documento, Long> {

    public DocumentoService(DocumentoRepository repository) {
        super(repository);
    }
}
