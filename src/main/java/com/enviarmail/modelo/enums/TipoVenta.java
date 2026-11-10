package com.enviarmail.modelo.enums;

public enum TipoVenta {
    MINORISTA("Minorista"),
    MAYORISTA("Mayorista");

    private final String descripcion;

    TipoVenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

