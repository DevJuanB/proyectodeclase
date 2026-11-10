package com.enviarmail.modelo.repositorio;

import com.enviarmail.modelo.entidad.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoDetalleRepositorio extends JpaRepository<PedidoDetalle, Long> {
    List<PedidoDetalle> findByPedidoId(Long pedidoId);
    List<PedidoDetalle> findByProductoId(Long productoId);

    @Query("SELECT d.producto.nombre, SUM(d.cantidad), SUM(d.subtotal) FROM PedidoDetalle d GROUP BY d.producto.id, d.producto.nombre ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> findTopProductosVendidos();
}

