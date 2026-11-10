package com.enviarmail.controlador;

import com.enviarmail.modelo.entidad.Pedido;
import com.enviarmail.modelo.enums.CargoEmpleado;
import com.enviarmail.modelo.enums.EstadoPedido;
import com.enviarmail.modelo.enums.MetodoPago;
import com.enviarmail.modelo.enums.TipoEntrega;
import com.enviarmail.modelo.enums.TipoVenta;
import com.enviarmail.modelo.servicio.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoControlador {

    private final PedidoServicio pedidoServicio;
    private final ClienteServicio clienteServicio;
    private final EmpleadoServicio empleadoServicio;
    private final ProductoServicio productoServicio;
    private final PdfServicio pdfServicio;
    private final EmailServicio emailServicio;

    public PedidoControlador(PedidoServicio pedidoServicio,
                            ClienteServicio clienteServicio,
                            EmpleadoServicio empleadoServicio,
                            ProductoServicio productoServicio,
                            PdfServicio pdfServicio,
                            EmailServicio emailServicio) {
        this.pedidoServicio = pedidoServicio;
        this.clienteServicio = clienteServicio;
        this.empleadoServicio = empleadoServicio;
        this.productoServicio = productoServicio;
        this.pdfServicio = pdfServicio;
        this.emailServicio = emailServicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) EstadoPedido estado,
                         @RequestParam(required = false) TipoVenta tipoVenta,
                         HttpSession session,
                         Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        List<Pedido> pedidos;
        if (estado != null) {
            pedidos = pedidoServicio.listarPorEstado(estado);
        } else if (tipoVenta != null) {
            pedidos = pedidoServicio.listarPorTipoVenta(tipoVenta);
        } else {
            pedidos = pedidoServicio.listarTodos();
        }

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("estados", EstadoPedido.values());
        model.addAttribute("tiposVenta", TipoVenta.values());
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("tipoSeleccionado", tipoVenta);
        return "pedidos/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        model.addAttribute("pedido", new Pedido());
        model.addAttribute("clientes", clienteServicio.listarTodos());
        model.addAttribute("vendedores", empleadoServicio.listarPorCargo(CargoEmpleado.VENDEDOR));
        model.addAttribute("productos", productoServicio.listarDisponibles());
        model.addAttribute("tiposVenta", TipoVenta.values());
        model.addAttribute("tiposEntrega", TipoEntrega.values());
        model.addAttribute("metodosPago", MetodoPago.values());
        return "pedidos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pedido pedido,
                          @RequestParam(name = "productoIds", required = false) List<Long> productoIds,
                          @RequestParam(name = "cantidades", required = false) List<Integer> cantidades,
                          RedirectAttributes ra) {
        if (productoIds == null || productoIds.isEmpty()) {
            ra.addFlashAttribute("error", "Debes seleccionar al menos un producto para registrar el pedido.");
            return "redirect:/pedidos/nuevo";
        }

        Pedido guardado = pedidoServicio.crearPedido(pedido, productoIds, cantidades);
        ra.addFlashAttribute("mensaje", "Pedido #" + guardado.getNumeroFactura() + " registrado con éxito por un total de $" + guardado.getTotal());
        return "redirect:/pedidos/detalle/" + guardado.getId();
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        Pedido p = pedidoServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        model.addAttribute("pedido", p);
        model.addAttribute("estados", EstadoPedido.values());
        return "pedidos/detalle";
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido nuevoEstado, RedirectAttributes ra) {
        pedidoServicio.cambiarEstado(id, nuevoEstado);
        ra.addFlashAttribute("mensaje", "Estado del pedido actualizado a " + nuevoEstado.getDescripcion());
        return "redirect:/pedidos/detalle/" + id;
    }

    @PostMapping("/abono/{id}")
    public String registrarAbono(@PathVariable Long id, @RequestParam BigDecimal monto, RedirectAttributes ra) {
        pedidoServicio.registrarAbono(id, monto);
        ra.addFlashAttribute("mensaje", "Abono de $" + monto + " registrado con éxito.");
        return "redirect:/pedidos/detalle/" + id;
    }

    /**
     * DESCARGAR FACTURA EN PDF
     */
    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> descargarFacturaPdf(@PathVariable Long id) {
        Pedido pedido = pedidoServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        byte[] pdfBytes = pdfServicio.generarFacturaPdf(pedido);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Factura-" + pedido.getNumeroFactura() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    /**
     * ENVIAR FACTURA POR CORREO ELECTRÓNICO AL CLIENTE
     */
    @GetMapping("/enviar-correo/{id}")
    public String enviarFacturaPorCorreo(@PathVariable Long id, RedirectAttributes ra) {
        Pedido pedido = pedidoServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        try {
            byte[] pdfBytes = pdfServicio.generarFacturaPdf(pedido);
            emailServicio.enviarFacturaCliente(pedido, pdfBytes);
            ra.addFlashAttribute("mensaje", "¡Factura PDF enviada exitosamente al correo: " + pedido.getCliente().getEmail() + "!");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "No se pudo enviar el correo: " + e.getMessage() + ". (Verifica las credenciales SMTP en application.properties)");
        }

        return "redirect:/pedidos/detalle/" + id;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        pedidoServicio.eliminar(id);
        ra.addFlashAttribute("mensaje", "Pedido eliminado.");
        return "redirect:/pedidos";
    }
}

