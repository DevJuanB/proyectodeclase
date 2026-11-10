package com.enviarmail.modelo.enums;

public enum TipoProducto {
    TORTAS("Tortas y Pasteles"),
    CUPCAKES("Cupcakes y Muffins"),
    GALLETAS("Galletas Artesanales"),
    POSTRES("Postres Individuales"),
    PANADERIA_FINA("Panadería Fina");

    private final String descripcion;

    TipoProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

