package com.enviarmail.modelo.entidad;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "materias_primas")
public class MateriaPrima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 30)
    private String unidadMedida; // kg, g, Litros, Unidades

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal stockActual;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal stockMinimo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal costoUnitario;

    public MateriaPrima() {
    }

    public MateriaPrima(String codigo, String nombre, String unidadMedida, BigDecimal stockActual, BigDecimal stockMinimo, BigDecimal costoUnitario) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.costoUnitario = costoUnitario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getStockActual() {
        return stockActual;
    }

    public void setStockActual(BigDecimal stockActual) {
        this.stockActual = stockActual;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public boolean isBajoStock() {
        return stockActual != null && stockMinimo != null && stockActual.compareTo(stockMinimo) <= 0;
    }
}

