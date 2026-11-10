package com.enviarmail.modelo.servicio;

import com.enviarmail.modelo.entidad.Empleado;
import com.enviarmail.modelo.entidad.Produccion;
import com.enviarmail.modelo.entidad.Producto;
import com.enviarmail.modelo.enums.EstadoProduccion;
import com.enviarmail.modelo.repositorio.ProduccionRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProduccionServicio {

    private final ProduccionRepositorio produccionRepositorio;
    private final ProductoServicio productoServicio;

    public ProduccionServicio(ProduccionRepositorio produccionRepositorio,
                              ProductoServicio productoServicio) {
        this.produccionRepositorio = produccionRepositorio;
        this.productoServicio = productoServicio;
    }

    public List<Produccion> listarTodas() {
        return produccionRepositorio.findAllByOrderByFechaPlanificadaDesc();
    }

    public List<Produccion> listarPorEstado(EstadoProduccion estado) {
        return produccionRepositorio.findByEstado(estado);
    }

    public Optional<Produccion> buscarPorId(Long id) {
        return produccionRepositorio.findById(id);
    }

    public Produccion crear(Produccion produccion) {
        if (produccion.getCodigoLote() == null || produccion.getCodigoLote().trim().isEmpty()) {
            produccion.setCodigoLote("LOTE-" + System.currentTimeMillis() % 1000000);
        }
        produccion.setEstado(EstadoProduccion.PENDIENTE);
        return produccionRepositorio.save(produccion);
    }

    public Produccion actualizar(Produccion produccion) {
        return produccionRepositorio.save(produccion);
    }

    @Transactional
    public Produccion iniciarProduccion(Long id) {
        Produccion p = produccionRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden de producción no encontrada"));
        p.setEstado(EstadoProduccion.EN_PROCESO);
        p.setFechaInicio(LocalDateTime.now());
        return produccionRepositorio.save(p);
    }

    @Transactional
    public Produccion terminarProduccion(Long id, int cantidadProducida, int mermas, String motivoMerma) {
        Produccion p = produccionRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden de producción no encontrada"));
        p.setEstado(EstadoProduccion.TERMINADO);
        p.setFechaFin(LocalDateTime.now());
        p.setCantidadProducida(cantidadProducida);
        p.setMermas(mermas);
        p.setMotivoMerma(motivoMerma);

        // Actualizar automáticamente el stock del producto terminado
        productoServicio.actualizarStock(p.getProducto().getId(), cantidadProducida);

        return produccionRepositorio.save(p);
    }

    public void eliminar(Long id) {
        produccionRepositorio.deleteById(id);
    }
}

