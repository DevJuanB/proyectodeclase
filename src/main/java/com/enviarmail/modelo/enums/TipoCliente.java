package com.enviarmail.modelo.enums;

public enum TipoCliente {
    MINORISTA("Cliente Minorista (Al detal)"),
    MAYORISTA("Cliente Mayorista (Eventos / Empresas)");

    private final String descripcion;

    TipoCliente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

