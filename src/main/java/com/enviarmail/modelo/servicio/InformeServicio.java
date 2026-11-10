package com.enviarmail.modelo.servicio;

import com.enviarmail.modelo.entidad.Pedido;
import com.enviarmail.modelo.enums.EstadoPedido;
import com.enviarmail.modelo.enums.TipoVenta;
import com.enviarmail.modelo.repositorio.PedidoDetalleRepositorio;
import com.enviarmail.modelo.repositorio.PedidoRepositorio;
import com.enviarmail.modelo.repositorio.ProduccionRepositorio;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InformeServicio {

    private final PedidoRepositorio pedidoRepositorio;
    private final PedidoDetalleRepositorio pedidoDetalleRepositorio;
    private final ProduccionRepositorio produccionRepositorio;

    public InformeServicio(PedidoRepositorio pedidoRepositorio,
                           PedidoDetalleRepositorio pedidoDetalleRepositorio,
                           ProduccionRepositorio produccionRepositorio) {
        this.pedidoRepositorio = pedidoRepositorio;
        this.pedidoDetalleRepositorio = pedidoDetalleRepositorio;
        this.produccionRepositorio = produccionRepositorio;
    }

    public Map<String, Object> obtenerEstadisticasDashboard() {
        Map<String, Object> stats = new HashMap<>();

        List<Pedido> pedidos = pedidoRepositorio.findAll();

        BigDecimal ingresosTotales = BigDecimal.ZERO;
        long totalEntregados = 0;
        long totalPendientes = 0;
        BigDecimal ventasMinoristas = BigDecimal.ZERO;
        BigDecimal ventasMayoristas = BigDecimal.ZERO;

        for (Pedido p : pedidos) {
            if (p.getEstado() != EstadoPedido.CANCELADO) {
                if (p.getTotal() != null) {
                    ingresosTotales = ingresosTotales.add(p.getTotal());
                    if (p.getTipoVenta() == TipoVenta.MAYORISTA) {
                        ventasMayoristas = ventasMayoristas.add(p.getTotal());
                    } else {
                        ventasMinoristas = ventasMinoristas.add(p.getTotal());
                    }
                }
            }
            if (p.getEstado() == EstadoPedido.ENTREGADO) totalEntregados++;
            if (p.getEstado() == EstadoPedido.PENDIENTE) totalPendientes++;
        }

        stats.put("totalPedidos", pedidos.size());
        stats.put("ingresosTotales", ingresosTotales);
        stats.put("pedidosEntregados", totalEntregados);
        stats.put("pedidosPendientes", totalPendientes);
        stats.put("ventasMinoristas", ventasMinoristas);
        stats.put("ventasMayoristas", ventasMayoristas);
        stats.put("topProductos", pedidoDetalleRepositorio.findTopProductosVendidos());

        return stats;
    }

    public List<Pedido> filtrarVentas(LocalDate fechaInicio, LocalDate fechaFin, TipoVenta tipoVenta) {
        LocalDateTime inicio = (fechaInicio != null) ? fechaInicio.atStartOfDay() : LocalDate.now().minusMonths(3).atStartOfDay();
        LocalDateTime fin = (fechaFin != null) ? fechaFin.atTime(LocalTime.MAX) : LocalDateTime.now();

        List<Pedido> lista = pedidoRepositorio.findByRangoFechas(inicio, fin);
        if (tipoVenta != null) {
            lista.removeIf(p -> p.getTipoVenta() != tipoVenta);
        }
        return lista;
    }
}

