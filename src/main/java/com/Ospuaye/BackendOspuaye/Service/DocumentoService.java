package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Repository.DocumentoRepository;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class DocumentoService extends BaseService<Documento, Long> {

    public DocumentoService(DocumentoRepository repository) {
        super(repository);
    }

    public String handleFileUpload(MultipartFile file) throws Exception {
        try{
            String fileName = UUID.randomUUID().toString();
            byte[] bytes = file.getBytes();
            String fileOriginalName = file.getOriginalFilename();

            //tamaño maximo de archivos
            long size = file.getSize();
            long maxSize = 5 * 1024 * 1024;

            if (size > maxSize) {
                return("El tamaño del archivo debe ser de 5MB o menor");
            }

        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


}
