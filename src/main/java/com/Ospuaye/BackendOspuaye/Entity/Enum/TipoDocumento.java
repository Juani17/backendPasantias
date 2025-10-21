package com.Ospuaye.BackendOspuaye.Entity.Enum;

public enum TipoDocumento {
    DU("DOCUMENTO UNICO"),
    LE("LIBRETA DE ENROLAMIENTO"),
    LC("LIBRETA CIVICA"),
    PA("PASAPORTE"),
    CM("CERTIFICADO MIGRATORIO"),
    ET("EN TRAMITE (recién nacidos)");

    private final String descripcion;

    TipoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
