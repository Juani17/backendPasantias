package com.Ospuaye.BackendOspuaye.Entity.Enum;

public enum Sexo {
    MASCULINO("Masculino"),
    FEMENINO("Femenino"),
    SIN_INFORMACION("Sin Informacion"),
    AMBOS_SEXOS("Ambos Sexos");

    private String texto;
    Sexo(String texto) {
        this.texto = texto;
    }

    public String toString() {
        return texto;
    }
}
