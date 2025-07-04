package com.api.diversity.domain.enums;

public enum TipoEntrada {
    COMPRA("Compra"),
    DEVOLUCION("Devolución");

    private final String descripcion;

    TipoEntrada(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
