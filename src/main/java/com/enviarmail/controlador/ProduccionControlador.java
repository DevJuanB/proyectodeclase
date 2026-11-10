package com.enviarmail.controlador;

import com.enviarmail.modelo.entidad.Produccion;
import com.enviarmail.modelo.enums.CargoEmpleado;
import com.enviarmail.modelo.enums.EstadoProduccion;
import com.enviarmail.modelo.servicio.EmpleadoServicio;
import com.enviarmail.modelo.servicio.ProduccionServicio;
import com.enviarmail.modelo.servicio.ProductoServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produccion")
public class ProduccionControlador {

    private final ProduccionServicio produccionServicio;
    private final ProductoServicio productoServicio;
    private final EmpleadoServicio empleadoServicio;

    public ProduccionControlador(ProduccionServicio produccionServicio,
                                 ProductoServicio productoServicio,
                                 EmpleadoServicio empleadoServicio) {
        this.produccionServicio = produccionServicio;
        this.productoServicio = productoServicio;
        this.empleadoServicio = empleadoServicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) EstadoProduccion estado,
                         HttpSession session,
                         Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        model.addAttribute("producciones", estado != null ? produccionServicio.listarPorEstado(estado) : produccionServicio.listarTodas());
        model.addAttribute("estados", EstadoProduccion.values());
        model.addAttribute("estadoSeleccionado", estado);
        return "produccion/lista";
    }

    @GetMapping("/nueva")
    public String formularioNueva(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        model.addAttribute("produccion", new Produccion());
        model.addAttribute("productos", productoServicio.listarDisponibles());
        model.addAttribute("pasteleros", empleadoServicio.listarTodos().stream()
                .filter(e -> e.getCargo() == CargoEmpleado.PASTELERO || e.getCargo() == CargoEmpleado.PANADERO).toList());
        return "produccion/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Produccion produccion, RedirectAttributes ra) {
        produccionServicio.crear(produccion);
        ra.addFlashAttribute("mensaje", "Orden de producción planificada con lote: " + produccion.getCodigoLote());
        return "redirect:/produccion";
    }

    @GetMapping("/iniciar/{id}")
    public String iniciar(@PathVariable Long id, RedirectAttributes ra) {
        produccionServicio.iniciarProduccion(id);
        ra.addFlashAttribute("mensaje", "Producción iniciada exitosamente.");
        return "redirect:/produccion";
    }

    @PostMapping("/terminar/{id}")
    public String terminar(@PathVariable Long id,
                           @RequestParam int cantidadProducida,
                           @RequestParam(defaultValue = "0") int mermas,
                           @RequestParam(required = false) String motivoMerma,
                           RedirectAttributes ra) {
        produccionServicio.terminarProduccion(id, cantidadProducida, mermas, motivoMerma);
        ra.addFlashAttribute("mensaje", "Producción finalizada. ¡Se han ingresado " + cantidadProducida + " unidades al stock del producto!");
        return "redirect:/produccion";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        produccionServicio.eliminar(id);
        ra.addFlashAttribute("mensaje", "Registro de producción eliminado.");
        return "redirect:/produccion";
    }
}

