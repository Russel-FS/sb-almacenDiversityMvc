package com.api.diversity.domain.enums;

public enum TipoRol {
    ADMINISTRADOR("Administrador", "Acceso total al sistema"),
    SUPERVISOR("Supervisor", "Puede aprobar entradas y salidas"),
    OPERADOR("Operador", "Puede registrar entradas y salidas");

    private final String nombre;
    private final String descripcion;

    TipoRol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}