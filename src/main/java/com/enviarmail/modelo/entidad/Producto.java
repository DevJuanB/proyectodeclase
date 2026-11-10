package com.enviarmail.modelo.entidad;

import com.enviarmail.modelo.enums.TipoProducto;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoProducto tipo;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 500)
    private String ingredientes;

    @Column(length = 255)
    private String alergenos;

    @Column(length = 255)
    private String condicionesAlmacenamiento;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioMinorista;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioMayorista;

    @Column(length = 60)
    private String presentacion; // Ej: Caja x 6, Unidad entera, Bandeja x 12

    @Column(length = 60)
    private String tamano; // Ej: 1 Libra, Mediano, 20 cm, Individual

    private Integer stock = 0;

    private Integer stockMinimo = 5;

    private boolean disponible = true;

    @Column(length = 255)
    private String imagenUrl;

    public Producto() {
    }

    public Producto(String codigo, String nombre, TipoProducto tipo, String descripcion, String ingredientes,
                    String alergenos, String condicionesAlmacenamiento, BigDecimal precioMinorista,
                    BigDecimal precioMayorista, String presentacion, String tamano, Integer stock,
                    Integer stockMinimo, boolean disponible, String imagenUrl) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
        this.alergenos = alergenos;
        this.condicionesAlmacenamiento = condicionesAlmacenamiento;
        this.precioMinorista = precioMinorista;
        this.precioMayorista = precioMayorista;
        this.presentacion = presentacion;
        this.tamano = tamano;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.disponible = disponible;
        this.imagenUrl = imagenUrl;
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

    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getAlergenos() {
        return alergenos;
    }

    public void setAlergenos(String alergenos) {
        this.alergenos = alergenos;
    }

    public String getCondicionesAlmacenamiento() {
        return condicionesAlmacenamiento;
    }

    public void setCondicionesAlmacenamiento(String condicionesAlmacenamiento) {
        this.condicionesAlmacenamiento = condicionesAlmacenamiento;
    }

    public BigDecimal getPrecioMinorista() {
        return precioMinorista;
    }

    public void setPrecioMinorista(BigDecimal precioMinorista) {
        this.precioMinorista = precioMinorista;
    }

    public BigDecimal getPrecioMayorista() {
        return precioMayorista;
    }

    public void setPrecioMayorista(BigDecimal precioMayorista) {
        this.precioMayorista = precioMayorista;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public boolean isBajoStock() {
        return stock != null && stockMinimo != null && stock <= stockMinimo;
    }
}

