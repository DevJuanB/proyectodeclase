package com.enviarmail.modelo.enums;

public enum CargoEmpleado {
    ADMINISTRADOR("Administrador General"),
    PASTELERO("Maestro Pastelero"),
    PANADERO("Panadero Artesanal"),
    VENDEDOR("Asesor de Ventas y Atención");

    private final String descripcion;

    CargoEmpleado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

