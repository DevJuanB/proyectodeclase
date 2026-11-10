package com.enviarmail.modelo.enums;

public enum RolUsuario {
    ADMIN("Administrador General"),
    VENDEDOR("Asesor de Ventas"),
    PASTELERO("Pastelero / Producción");

    private final String descripcion;

    RolUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

