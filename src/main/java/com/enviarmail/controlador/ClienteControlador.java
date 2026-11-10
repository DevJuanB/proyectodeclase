package com.enviarmail.controlador;

import com.enviarmail.modelo.entidad.Cliente;
import com.enviarmail.modelo.entidad.Pedido;
import com.enviarmail.modelo.enums.TipoCliente;
import com.enviarmail.modelo.servicio.ClienteServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteControlador {

    private final ClienteServicio clienteServicio;

    public ClienteControlador(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar,
                         @RequestParam(required = false) TipoCliente tipo,
                         HttpSession session,
                         Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        List<Cliente> clientes;
        if (tipo != null) {
            clientes = clienteServicio.listarPorTipo(tipo);
        } else if (buscar != null && !buscar.trim().isEmpty()) {
            clientes = clienteServicio.buscar(buscar.trim());
        } else {
            clientes = clienteServicio.listarTodos();
        }

        model.addAttribute("clientes", clientes);
        model.addAttribute("tipos", TipoCliente.values());
        model.addAttribute("buscar", buscar);
        model.addAttribute("tipoSeleccionado", tipo);
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("tipos", TipoCliente.values());
        return "clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        Cliente cli = clienteServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        model.addAttribute("cliente", cli);
        model.addAttribute("tipos", TipoCliente.values());
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente, RedirectAttributes ra) {
        clienteServicio.guardar(cliente);
        ra.addFlashAttribute("mensaje", "Cliente '" + cliente.getNombreCompleto() + "' guardado exitosamente.");
        return "redirect:/clientes";
    }

    @GetMapping("/historial/{id}")
    public String historialCompras(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        Cliente cli = clienteServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        List<Pedido> historial = clienteServicio.consultarHistorialCompras(id);

        model.addAttribute("cliente", cli);
        model.addAttribute("historial", historial);
        return "clientes/historial";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            clienteServicio.eliminar(id);
            ra.addFlashAttribute("mensaje", "Cliente eliminado con éxito.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se puede eliminar el cliente porque tiene ventas/pedidos asociados.");
        }
        return "redirect:/clientes";
    }
}

