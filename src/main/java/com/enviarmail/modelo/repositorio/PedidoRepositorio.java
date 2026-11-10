package com.enviarmail.modelo.repositorio;

import com.enviarmail.modelo.entidad.Pedido;
import com.enviarmail.modelo.enums.EstadoPedido;
import com.enviarmail.modelo.enums.TipoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByOrderByFechaPedidoDesc();
    List<Pedido> findByClienteIdOrderByFechaPedidoDesc(Long clienteId);
    List<Pedido> findByVendedorIdOrderByFechaPedidoDesc(Long vendedorId);
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByTipoVenta(TipoVenta tipoVenta);

    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido >= :inicio AND p.fechaPedido <= :fin ORDER BY p.fechaPedido DESC")
    List<Pedido> findByRangoFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = 'PENDIENTE'")
    long countPedidosPendientes();
}

