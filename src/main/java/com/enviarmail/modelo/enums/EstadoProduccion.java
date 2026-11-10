package com.enviarmail.modelo.enums;

public enum EstadoProduccion {
    PENDIENTE("Pendiente de Iniciar"),
    EN_PROCESO("En Preparación / Horneado"),
    TERMINADO("Terminado e Ingresado a Stock");

    private final String descripcion;

    EstadoProduccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

