package com.enviarmail.controlador;

import com.enviarmail.modelo.entidad.Usuario;
import com.enviarmail.modelo.servicio.InformeServicio;
import com.enviarmail.modelo.servicio.InventarioServicio;
import com.enviarmail.modelo.servicio.PedidoServicio;
import com.enviarmail.modelo.servicio.ProductoServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class DashboardController {

    private final InformeServicio informeServicio;
    private final PedidoServicio pedidoServicio;
    private final ProductoServicio productoServicio;
    private final InventarioServicio inventarioServicio;

    public DashboardController(InformeServicio informeServicio,
                               PedidoServicio pedidoServicio,
                               ProductoServicio productoServicio,
                               InventarioServicio inventarioServicio) {
        this.informeServicio = informeServicio;
        this.pedidoServicio = pedidoServicio;
        this.productoServicio = productoServicio;
        this.inventarioServicio = inventarioServicio;
    }

    @GetMapping("/")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        Map<String, Object> stats = informeServicio.obtenerEstadisticasDashboard();
        model.addAllAttributes(stats);

        model.addAttribute("pedidosRecientes", pedidoServicio.listarTodos().stream().limit(8).toList());
        model.addAttribute("productosBajoStock", productoServicio.listarBajoStock());
        model.addAttribute("materiasBajoStock", inventarioServicio.listarBajoStock());

        return "dashboard";
    }
}

