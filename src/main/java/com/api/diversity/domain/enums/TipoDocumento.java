package com.api.diversity.domain.enums;

/**
 * Enum que define los tipos de documento para entradas y salidas
 */
public enum TipoDocumento {
    FACTURA("Factura"),
    BOLETA("Boleta"),
    NOTA_CREDITO("Nota de Crédito"),
    NOTA_DEBITO("Nota de Débito"),
    GUIA_REMISION("Guía de Remisión");

    private final String descripcion;

    TipoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}