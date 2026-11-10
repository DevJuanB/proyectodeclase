package com.enviarmail.modelo.entidad;

import com.enviarmail.modelo.enums.EstadoProduccion;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "producciones")
public class Produccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String codigoLote;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidadPlanificada;

    private Integer cantidadProducida = 0;

    private Integer mermas = 0;

    @Column(length = 200)
    private String motivoMerma;

    private LocalDate fechaPlanificada = LocalDate.now();

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoProduccion estado = EstadoProduccion.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Empleado responsable; // Maestro pastelero o panadero

    @Column(length = 500)
    private String observaciones;

    public Produccion() {
    }

    public Produccion(String codigoLote, Producto producto, Integer cantidadPlanificada, Empleado responsable, String observaciones) {
        this.codigoLote = codigoLote;
        this.producto = producto;
        this.cantidadPlanificada = cantidadPlanificada;
        this.cantidadProducida = 0;
        this.mermas = 0;
        this.fechaPlanificada = LocalDate.now();
        this.estado = EstadoProduccion.PENDIENTE;
        this.responsable = responsable;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(String codigoLote) {
        this.codigoLote = codigoLote;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidadPlanificada() {
        return cantidadPlanificada;
    }

    public void setCantidadPlanificada(Integer cantidadPlanificada) {
        this.cantidadPlanificada = cantidadPlanificada;
    }

    public Integer getCantidadProducida() {
        return cantidadProducida;
    }

    public void setCantidadProducida(Integer cantidadProducida) {
        this.cantidadProducida = cantidadProducida;
    }

    public Integer getMermas() {
        return mermas;
    }

    public void setMermas(Integer mermas) {
        this.mermas = mermas;
    }

    public String getMotivoMerma() {
        return motivoMerma;
    }

    public void setMotivoMerma(String motivoMerma) {
        this.motivoMerma = motivoMerma;
    }

    public LocalDate getFechaPlanificada() {
        return fechaPlanificada;
    }

    public void setFechaPlanificada(LocalDate fechaPlanificada) {
        this.fechaPlanificada = fechaPlanificada;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoProduccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoProduccion estado) {
        this.estado = estado;
    }

    public Empleado getResponsable() {
        return responsable;
    }

    public void setResponsable(Empleado responsable) {
        this.responsable = responsable;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}

