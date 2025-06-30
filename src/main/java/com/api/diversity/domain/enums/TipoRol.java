package com.api.diversity.domain.enums;

public enum TipoRol {
    // Roles Generales
    ADMINISTRADOR("Administrador", "Acceso total al sistema"),
    SUPERVISOR_GENERAL("Supervisor General", "Supervisa todos los rubros"),

    // roles de supervisores por rubro
    SUPERVISOR_PINATERIA("Supervisor Piñatería", "Supervisa operaciones de piñatería"),
    SUPERVISOR_LIBRERIA("Supervisor Librería", "Supervisa operaciones de librería"),
    SUPERVISOR_CAMARAS("Supervisor Cámaras", "Supervisa operaciones de cámaras"),

    // roles de operadores por rubro
    OPERADOR_PINATERIA("Operador Piñatería", "Operaciones específicas de piñatería"),
    OPERADOR_LIBRERIA("Operador Librería", "Operaciones específicas de librería"),
    OPERADOR_CAMARAS("Operador Cámaras", "Operaciones específicas de cámaras"),

    // roles especificos
    VENDEDOR("Vendedor", "Solo puede registrar ventas"),
    ALMACENERO("Almacenero", "Solo puede registrar entradas"),
    CONTADOR("Contador", "Acceso a reportes y contabilidad");

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