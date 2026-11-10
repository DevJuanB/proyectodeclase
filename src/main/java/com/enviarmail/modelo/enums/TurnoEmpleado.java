package com.enviarmail.modelo.enums;

public enum TurnoEmpleado {
    MANANA("Mañana (6:00 AM - 2:00 PM)"),
    TARDE("Tarde (2:00 PM - 10:00 PM)"),
    NOCHE("Noche (10:00 PM - 6:00 AM)"),
    COMPLETO("Tiempo Completo");

    private final String descripcion;

    TurnoEmpleado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

