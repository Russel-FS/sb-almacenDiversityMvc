package com.api.diversity.domain.enums;

public enum TipoRubro {

    PIÑATERIA("PIÑAT", "Piñatería", "Venta de piñatas y artículos para fiestas"),
    LIBRERIA("LIBR", "Librería", "Venta de libros y artículos de papelería"),
    CAMARA_SEGURIDAD("CSEG", "Cámara de Seguridad",
            "Venta de cámaras y sistemas de seguridad"),
    SIN_RUBRO("SINRUB", "Sin Rubro", "No aplica a ningún rubro específico");

    private final String code;
    private final String nombre;
    private final String descripcion;

    TipoRubro(String code, String nombre, String descripcion) {
        this.code = code;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getCode() {
        return code;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
