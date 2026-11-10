package com.enviarmail.modelo.enums;

public enum TipoMovimiento {
    ENTRADA("Entrada / Compra de Insumos"),
    SALIDA("Salida / Uso en Producción o Ajuste");

    private final String descripcion;

    TipoMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

