package com.enviarmail.modelo.enums;

public enum EstadoPedido {
    PENDIENTE("Pendiente"),
    EN_PREPARACION("En Preparación"),
    ENTREGADO("Entregado / Pagado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

