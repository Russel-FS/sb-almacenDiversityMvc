package com.api.diversity.domain.enums;

public enum TipoCliente {
    Persona_Natural("Persona Natural"),
    Empresa("Empresa");

    private final String descripcion;

    TipoCliente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}