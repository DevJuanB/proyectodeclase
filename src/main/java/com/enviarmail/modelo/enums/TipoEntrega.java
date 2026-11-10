package com.enviarmail.modelo.enums;

public enum TipoEntrega {
    VENTA_DIRECTA("Venta Directa en Mostrador"),
    POR_ENCARGO("Pedido por Encargo / Evento");

    private final String descripcion;

    TipoEntrega(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

