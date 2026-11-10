package com.enviarmail.controlador;

import com.enviarmail.modelo.entidad.MateriaPrima;
import com.enviarmail.modelo.enums.TipoMovimiento;
import com.enviarmail.modelo.servicio.InventarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/inventario")
public class InventarioControlador {

    private final InventarioServicio inventarioServicio;

    public InventarioControlador(InventarioServicio inventarioServicio) {
        this.inventarioServicio = inventarioServicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar,
                         HttpSession session,
                         Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        model.addAttribute("materiasPrimas", buscar != null && !buscar.trim().isEmpty()
                ? inventarioServicio.buscarMaterias(buscar.trim())
                : inventarioServicio.listarMateriasPrimas());
        model.addAttribute("buscar", buscar);
        model.addAttribute("bajoStockCount", inventarioServicio.listarBajoStock().size());
        return "inventario/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        model.addAttribute("materiaPrima", new MateriaPrima());
        return "inventario/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        MateriaPrima mp = inventarioServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Materia prima no encontrada"));
        model.addAttribute("materiaPrima", mp);
        return "inventario/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute MateriaPrima materiaPrima, RedirectAttributes ra) {
        if (materiaPrima.getCodigo() == null || materiaPrima.getCodigo().trim().isEmpty()) {
            materiaPrima.setCodigo("MP-" + String.format("%03d", (int)(Math.random() * 900 + 100)));
        }
        inventarioServicio.guardarMateriaPrima(materiaPrima);
        ra.addFlashAttribute("mensaje", "Insumo '" + materiaPrima.getNombre() + "' guardado exitosamente.");
        return "redirect:/inventario";
    }

    @PostMapping("/movimiento")
    public String registrarMovimiento(@RequestParam Long materiaPrimaId,
                                      @RequestParam TipoMovimiento tipo,
                                      @RequestParam BigDecimal cantidad,
                                      @RequestParam String motivo,
                                      HttpSession session,
                                      RedirectAttributes ra) {
        String responsable = (String) session.getAttribute("nombreUsuario");
        if (responsable == null) responsable = "Encargado de Inventario";

        inventarioServicio.registrarMovimiento(materiaPrimaId, tipo, cantidad, motivo, responsable);
        ra.addFlashAttribute("mensaje", "Movimiento de inventario registrado con éxito.");
        return "redirect:/inventario";
    }

    @GetMapping("/kardex")
    public String kardex(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        model.addAttribute("movimientos", inventarioServicio.listarMovimientosRecientes());
        return "inventario/kardex";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            inventarioServicio.eliminarMateriaPrima(id);
            ra.addFlashAttribute("mensaje", "Insumo eliminado del inventario.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se puede eliminar porque tiene movimientos asociados.");
        }
        return "redirect:/inventario";
    }
}

