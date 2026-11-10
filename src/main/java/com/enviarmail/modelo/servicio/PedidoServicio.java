package com.enviarmail.modelo.servicio;

import com.enviarmail.modelo.entidad.Pedido;
import com.enviarmail.modelo.entidad.PedidoDetalle;
import com.enviarmail.modelo.entidad.Producto;
import com.enviarmail.modelo.enums.EstadoPedido;
import com.enviarmail.modelo.enums.TipoVenta;
import com.enviarmail.modelo.repositorio.PedidoDetalleRepositorio;
import com.enviarmail.modelo.repositorio.PedidoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServicio {

    private final PedidoRepositorio pedidoRepositorio;
    private final PedidoDetalleRepositorio pedidoDetalleRepositorio;
    private final ProductoServicio productoServicio;

    public PedidoServicio(PedidoRepositorio pedidoRepositorio,
                          PedidoDetalleRepositorio pedidoDetalleRepositorio,
                          ProductoServicio productoServicio) {
        this.pedidoRepositorio = pedidoRepositorio;
        this.pedidoDetalleRepositorio = pedidoDetalleRepositorio;
        this.productoServicio = productoServicio;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepositorio.findAllByOrderByFechaPedidoDesc();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepositorio.findById(id);
    }

    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        return pedidoRepositorio.findByEstado(estado);
    }

    public List<Pedido> listarPorTipoVenta(TipoVenta tipoVenta) {
        return pedidoRepositorio.findByTipoVenta(tipoVenta);
    }

    public List<Pedido> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepositorio.findByRangoFechas(inicio, fin);
    }

    @Transactional
    public Pedido crearPedido(Pedido pedido, List<Long> productoIds, List<Integer> cantidades) {
        if (pedido.getNumeroFactura() == null || pedido.getNumeroFactura().trim().isEmpty()) {
            pedido.setNumeroFactura("FAC-" + String.format("%05d", (int)(Math.random() * 90000 + 10000)));
        }

        pedido.setFechaPedido(LocalDateTime.now());
        pedido.getDetalles().clear();

        BigDecimal granTotal = BigDecimal.ZERO;

        if (productoIds != null && cantidades != null) {
            for (int i = 0; i < productoIds.size(); i++) {
                Long pId = productoIds.get(i);
                Integer cant = cantidades.get(i);
                if (pId != null && cant != null && cant > 0) {
                    Producto prod = productoServicio.buscarPorId(pId)
                            .orElseThrow(() -> new IllegalArgumentException("Producto ID: " + pId + " no encontrado"));

                    // Aplicar precio según tipo de venta: minorista o mayorista
                    BigDecimal precioUnitario = (pedido.getTipoVenta() == TipoVenta.MAYORISTA)
                            ? prod.getPrecioMayorista()
                            : prod.getPrecioMinorista();

                    PedidoDetalle detalle = new PedidoDetalle(pedido, prod, cant, precioUnitario);
                    pedido.getDetalles().add(detalle);
                    granTotal = granTotal.add(detalle.getSubtotal());
                }
            }
        }

        pedido.setTotal(granTotal);
        if (pedido.getAbono() == null) {
            pedido.setAbono(BigDecimal.ZERO);
        }
        pedido.setSaldoPendiente(granTotal.subtract(pedido.getAbono()));
        if (pedido.getSaldoPendiente().compareTo(BigDecimal.ZERO) < 0) {
            pedido.setSaldoPendiente(BigDecimal.ZERO);
        }

        return pedidoRepositorio.save(pedido);
    }

    @Transactional
    public Pedido cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido p = pedidoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        EstadoPedido estadoAnterior = p.getEstado();
        p.setEstado(nuevoEstado);

        // Si cambia a ENTREGADO y antes no lo estaba, descontar automáticamente de stock
        if (nuevoEstado == EstadoPedido.ENTREGADO && estadoAnterior != EstadoPedido.ENTREGADO) {
            for (PedidoDetalle d : p.getDetalles()) {
                productoServicio.actualizarStock(d.getProducto().getId(), -d.getCantidad());
            }
            // Si se entrega, se asume pago completo si no había abono completo
            if (p.getSaldoPendiente().compareTo(BigDecimal.ZERO) > 0) {
                p.setAbono(p.getTotal());
                p.setSaldoPendiente(BigDecimal.ZERO);
            }
        }

        return pedidoRepositorio.save(p);
    }

    @Transactional
    public Pedido registrarAbono(Long id, BigDecimal montoAbono) {
        Pedido p = pedidoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        BigDecimal nuevoAbono = (p.getAbono() != null ? p.getAbono() : BigDecimal.ZERO).add(montoAbono);
        p.setAbono(nuevoAbono);
        BigDecimal nuevoSaldo = p.getTotal().subtract(nuevoAbono);
        p.setSaldoPendiente(nuevoSaldo.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : nuevoSaldo);

        return pedidoRepositorio.save(p);
    }

    public void eliminar(Long id) {
        pedidoRepositorio.deleteById(id);
    }
}

