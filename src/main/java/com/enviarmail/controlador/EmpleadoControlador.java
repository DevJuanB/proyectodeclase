package com.enviarmail.controlador;

import com.enviarmail.modelo.entidad.Empleado;
import com.enviarmail.modelo.entidad.Pedido;
import com.enviarmail.modelo.entidad.Produccion;
import com.enviarmail.modelo.enums.CargoEmpleado;
import com.enviarmail.modelo.enums.TurnoEmpleado;
import com.enviarmail.modelo.servicio.EmpleadoServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/empleados")
public class EmpleadoControlador {

    private final EmpleadoServicio empleadoServicio;

    public EmpleadoControlador(EmpleadoServicio empleadoServicio) {
        this.empleadoServicio = empleadoServicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar,
                         @RequestParam(required = false) CargoEmpleado cargo,
                         HttpSession session,
                         Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        List<Empleado> empleados;
        if (cargo != null) {
            empleados = empleadoServicio.listarPorCargo(cargo);
        } else if (buscar != null && !buscar.trim().isEmpty()) {
            empleados = empleadoServicio.buscar(buscar.trim());
        } else {
            empleados = empleadoServicio.listarTodos();
        }

        model.addAttribute("empleados", empleados);
        model.addAttribute("cargos", CargoEmpleado.values());
        model.addAttribute("buscar", buscar);
        model.addAttribute("cargoSeleccionado", cargo);
        return "empleados/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        model.addAttribute("empleado", new Empleado());
        model.addAttribute("cargos", CargoEmpleado.values());
        model.addAttribute("turnos", TurnoEmpleado.values());
        return "empleados/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        Empleado emp = empleadoServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        model.addAttribute("empleado", emp);
        model.addAttribute("cargos", CargoEmpleado.values());
        model.addAttribute("turnos", TurnoEmpleado.values());
        return "empleados/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Empleado empleado, RedirectAttributes ra) {
        empleadoServicio.guardar(empleado);
        ra.addFlashAttribute("mensaje", "Empleado '" + empleado.getNombreCompleto() + "' guardado exitosamente.");
        return "redirect:/empleados";
    }

    @GetMapping("/ventas/{id}")
    public String ventasVendedor(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        Empleado emp = empleadoServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        List<Pedido> ventas = empleadoServicio.consultarVentasPorVendedor(id);

        model.addAttribute("empleado", emp);
        model.addAttribute("ventas", ventas);
        return "empleados/ventas";
    }

    @GetMapping("/produccion/{id}")
    public String produccionEmpleado(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        Empleado emp = empleadoServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        List<Produccion> producciones = empleadoServicio.consultarProduccionesPorEmpleado(id);

        model.addAttribute("empleado", emp);
        model.addAttribute("producciones", producciones);
        return "empleados/produccion";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            empleadoServicio.eliminar(id);
            ra.addFlashAttribute("mensaje", "Empleado eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se puede eliminar el empleado porque tiene registros de ventas o producción.");
        }
        return "redirect:/empleados";
    }
}

