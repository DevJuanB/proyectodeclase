package com.enviarmail.controlador;

import com.enviarmail.modelo.entidad.Pedido;
import com.enviarmail.modelo.enums.TipoVenta;
import com.enviarmail.modelo.servicio.EmailServicio;
import com.enviarmail.modelo.servicio.InformeServicio;
import com.enviarmail.modelo.servicio.PdfServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/informes")
public class InformeControlador {

    private final InformeServicio informeServicio;
    private final PdfServicio pdfServicio;
    private final EmailServicio emailServicio;

    public InformeControlador(InformeServicio informeServicio, PdfServicio pdfServicio, EmailServicio emailServicio) {
        this.informeServicio = informeServicio;
        this.pdfServicio = pdfServicio;
        this.emailServicio = emailServicio;
    }

    @GetMapping
    public String informes(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                           @RequestParam(required = false) TipoVenta tipoVenta,
                           HttpSession session,
                           Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        List<Pedido> pedidosFiltrados = informeServicio.filtrarVentas(fechaInicio, fechaFin, tipoVenta);

        model.addAttribute("pedidos", pedidosFiltrados);
        model.addAttribute("tiposVenta", TipoVenta.values());
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("tipoVenta", tipoVenta);
        model.addAllAttributes(informeServicio.obtenerEstadisticasDashboard());

        return "informes/reportes";
    }

    /**
     * DESCARGA DEL REPORTE DE VENTAS EN FORMATO PDF
     */
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> descargarReportePdf(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                                                      @RequestParam(required = false) TipoVenta tipoVenta) {
        List<Pedido> pedidos = informeServicio.filtrarVentas(fechaInicio, fechaFin, tipoVenta);
        String periodo = (fechaInicio != null ? fechaInicio.toString() : "Inicio") + " al " + (fechaFin != null ? fechaFin.toString() : "Hoy");
        byte[] pdfBytes = pdfServicio.generarReporteVentasPdf(pedidos, periodo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte-Ventas-YujuReposteria.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    /**
     * ENVIAR EL REPORTE PDF POR CORREO ELECTRÓNICO
     */
    @PostMapping("/enviar-correo")
    public String enviarReportePorCorreo(@RequestParam String correoDestino,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                                         @RequestParam(required = false) TipoVenta tipoVenta,
                                         RedirectAttributes ra) {
        try {
            List<Pedido> pedidos = informeServicio.filtrarVentas(fechaInicio, fechaFin, tipoVenta);
            String periodo = (fechaInicio != null ? fechaInicio.toString() : "Inicio") + " al " + (fechaFin != null ? fechaFin.toString() : "Hoy");
            byte[] pdfBytes = pdfServicio.generarReporteVentasPdf(pedidos, periodo);

            emailServicio.enviarReportePdf(correoDestino,
                    "Reporte Ejecutivo de Ventas - Yuju Repostería (" + periodo + ")",
                    "Adjunto a este correo encontrará el informe consolidado de ventas y pedidos en formato PDF correspondiente al período solicitado.",
                    pdfBytes,
                    "Reporte-Ventas.pdf");

            ra.addFlashAttribute("mensaje", "Reporte PDF enviado exitosamente a: " + correoDestino);
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Error al enviar el reporte: " + e.getMessage());
        }

        return "redirect:/informes";
    }
}

