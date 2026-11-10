package com.enviarmail.controlador;

import com.enviarmail.modelo.entidad.Producto;
import com.enviarmail.modelo.enums.TipoProducto;
import com.enviarmail.modelo.servicio.PdfServicio;
import com.enviarmail.modelo.servicio.ProductoServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoControlador {

    private final ProductoServicio productoServicio;
    private final PdfServicio pdfServicio;

    public ProductoControlador(ProductoServicio productoServicio, PdfServicio pdfServicio) {
        this.productoServicio = productoServicio;
        this.pdfServicio = pdfServicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar,
                         @RequestParam(required = false) TipoProducto tipo,
                         HttpSession session,
                         Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        List<Producto> productos;
        if (tipo != null) {
            productos = productoServicio.listarPorTipo(tipo);
        } else if (buscar != null && !buscar.trim().isEmpty()) {
            productos = productoServicio.buscar(buscar.trim());
        } else {
            productos = productoServicio.listarTodos();
        }

        model.addAttribute("productos", productos);
        model.addAttribute("tipos", TipoProducto.values());
        model.addAttribute("buscar", buscar);
        model.addAttribute("tipoSeleccionado", tipo);
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        model.addAttribute("producto", new Producto());
        model.addAttribute("tipos", TipoProducto.values());
        return "productos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        Producto prod = productoServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado ID: " + id));
        model.addAttribute("producto", prod);
        model.addAttribute("tipos", TipoProducto.values());
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, RedirectAttributes ra) {
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            producto.setCodigo("PRD-" + System.currentTimeMillis() % 100000);
        }
        productoServicio.guardar(producto);
        ra.addFlashAttribute("mensaje", "Producto '" + producto.getNombre() + "' guardado con éxito.");
        return "redirect:/productos";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        Producto prod = productoServicio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        model.addAttribute("producto", prod);
        return "productos/detalle";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            productoServicio.eliminar(id);
            ra.addFlashAttribute("mensaje", "Producto eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se puede eliminar el producto porque está asociado a pedidos o producciones.");
        }
        return "redirect:/productos";
    }

    /**
     * DESCARGA DE CATÁLOGO EN PDF
     */
    @GetMapping("/catalogo-pdf")
    public ResponseEntity<byte[]> descargarCatalogoPdf() {
        List<Producto> productos = productoServicio.listarDisponibles();
        byte[] pdfBytes = pdfServicio.generarCatalogoProductosPdf(productos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Catalogo-YujuReposteria.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}

