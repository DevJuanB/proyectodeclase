package com.enviarmail.modelo.servicio;

import com.enviarmail.modelo.entidad.Pedido;
import com.enviarmail.modelo.entidad.PedidoDetalle;
import com.enviarmail.modelo.entidad.Producto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PdfServicio {

    private final Color COLOR_PRIMARIO = new Color(74, 21, 37);      // Borgoña / Chocolate elegante
    private final Color COLOR_SECUNDARIO = new Color(212, 175, 55);  // Dorado repostería
    private final Color COLOR_FONDO_CLARO = new Color(253, 248, 245); // Crema suave
    private final Color COLOR_GRIS = new Color(240, 240, 240);

    private final Font FONT_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, COLOR_PRIMARIO);
    private final Font FONT_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, COLOR_PRIMARIO);
    private final Font FONT_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
    private final Font FONT_NORMAL_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
    private final Font FONT_CABECERA_TABLA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
    private final Font FONT_PIE = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, Color.GRAY);

    private final NumberFormat monedaFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    private final DateTimeFormatter fechaHoraFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter fechaFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Genera la factura o comprobante de venta en formato PDF
     */
    public byte[] generarFacturaPdf(Pedido pedido) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. Cabecera con datos de la empresa
            PdfPTable tablaCabecera = new PdfPTable(2);
            tablaCabecera.setWidthPercentage(100);
            tablaCabecera.setWidths(new float[]{60, 40});

            PdfPCell celdaEmpresa = new PdfPCell();
            celdaEmpresa.setBorder(Rectangle.NO_BORDER);
            celdaEmpresa.addElement(new Paragraph("🍰 YUJU REPOSTERÍA", FONT_TITULO));
            celdaEmpresa.addElement(new Paragraph("Repostería Artesanal & Gourmet", FONT_SUBTITULO));
            celdaEmpresa.addElement(new Paragraph("NIT: 901.234.567-8 | Tel: +57 (601) 555-0199", FONT_NORMAL));
            celdaEmpresa.addElement(new Paragraph("Calle 10 #24-15, Bogotá - Colombia", FONT_NORMAL));
            celdaEmpresa.addElement(new Paragraph("Email: yuju.sas.empresa@gmail.com", FONT_NORMAL));
            tablaCabecera.addCell(celdaEmpresa);

            PdfPCell celdaFactura = new PdfPCell();
            celdaFactura.setBorder(Rectangle.BOX);
            celdaFactura.setBorderColor(COLOR_PRIMARIO);
            celdaFactura.setBackgroundColor(COLOR_FONDO_CLARO);
            celdaFactura.setPadding(10);
            Paragraph pFac = new Paragraph("FACTURA DE VENTA", FONT_SUBTITULO);
            pFac.setAlignment(Element.ALIGN_CENTER);
            celdaFactura.addElement(pFac);

            Paragraph pNum = new Paragraph(pedido.getNumeroFactura(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.RED));
            pNum.setAlignment(Element.ALIGN_CENTER);
            celdaFactura.addElement(pNum);

            celdaFactura.addElement(new Paragraph("Fecha: " + pedido.getFechaPedido().format(fechaHoraFormat), FONT_NORMAL));
            celdaFactura.addElement(new Paragraph("Tipo Venta: " + pedido.getTipoVenta().getDescripcion(), FONT_NORMAL));
            celdaFactura.addElement(new Paragraph("Modalidad: " + pedido.getTipoEntrega().getDescripcion(), FONT_NORMAL));
            tablaCabecera.addCell(celdaFactura);

            document.add(tablaCabecera);
            document.add(new Paragraph(" "));

            // 2. Información del Cliente y Vendedor
            PdfPTable tablaCliente = new PdfPTable(2);
            tablaCliente.setWidthPercentage(100);
            tablaCliente.setWidths(new float[]{50, 50});

            PdfPCell cCliente = new PdfPCell();
            cCliente.setBackgroundColor(COLOR_GRIS);
            cCliente.setPadding(8);
            cCliente.addElement(new Paragraph("DATOS DEL CLIENTE", FONT_SUBTITULO));
            cCliente.addElement(new Paragraph("Nombre: " + pedido.getCliente().getNombreCompleto(), FONT_NORMAL_BOLD));
            cCliente.addElement(new Paragraph("Doc / NIT: " + pedido.getCliente().getDocumento(), FONT_NORMAL));
            cCliente.addElement(new Paragraph("Teléfono: " + (pedido.getCliente().getTelefono() != null ? pedido.getCliente().getTelefono() : "N/A"), FONT_NORMAL));
            cCliente.addElement(new Paragraph("Correo: " + (pedido.getCliente().getEmail() != null ? pedido.getCliente().getEmail() : "N/A"), FONT_NORMAL));
            cCliente.addElement(new Paragraph("Dirección: " + (pedido.getCliente().getDireccion() != null ? pedido.getCliente().getDireccion() : "N/A"), FONT_NORMAL));
            tablaCliente.addCell(cCliente);

            PdfPCell cDetalles = new PdfPCell();
            cDetalles.setBackgroundColor(COLOR_GRIS);
            cDetalles.setPadding(8);
            cDetalles.addElement(new Paragraph("DATOS DE LA ENTREGA", FONT_SUBTITULO));
            String fEntrega = pedido.getFechaEntrega() != null ? pedido.getFechaEntrega().format(fechaFormat) : "Entrega Inmediata";
            cDetalles.addElement(new Paragraph("Fecha de Entrega: " + fEntrega, FONT_NORMAL_BOLD));
            String vendedorNom = pedido.getVendedor() != null ? pedido.getVendedor().getNombreCompleto() : "Vendedor de Mostrador";
            cDetalles.addElement(new Paragraph("Atendido por: " + vendedorNom, FONT_NORMAL));
            cDetalles.addElement(new Paragraph("Método de Pago: " + pedido.getMetodoPago().getDescripcion(), FONT_NORMAL));
            cDetalles.addElement(new Paragraph("Estado del Pedido: " + pedido.getEstado().getDescripcion(), FONT_NORMAL));
            tablaCliente.addCell(cDetalles);

            document.add(tablaCliente);
            document.add(new Paragraph(" "));

            // 3. Tabla de Productos
            PdfPTable tablaItems = new PdfPTable(5);
            tablaItems.setWidthPercentage(100);
            tablaItems.setWidths(new float[]{15, 45, 12, 14, 14});

            agregarCeldaCabecera(tablaItems, "Código");
            agregarCeldaCabecera(tablaItems, "Descripción del Producto");
            agregarCeldaCabecera(tablaItems, "Cant.");
            agregarCeldaCabecera(tablaItems, "P. Unitario");
            agregarCeldaCabecera(tablaItems, "Subtotal");

            if (pedido.getDetalles() != null) {
                for (PedidoDetalle det : pedido.getDetalles()) {
                    tablaItems.addCell(crearCeldaDato(det.getProducto().getCodigo(), Element.ALIGN_CENTER));
                    tablaItems.addCell(crearCeldaDato(det.getProducto().getNombre() + " (" + det.getProducto().getPresentacion() + ")", Element.ALIGN_LEFT));
                    tablaItems.addCell(crearCeldaDato(String.valueOf(det.getCantidad()), Element.ALIGN_CENTER));
                    tablaItems.addCell(crearCeldaDato(formatoMoneda(det.getPrecioUnitario()), Element.ALIGN_RIGHT));
                    tablaItems.addCell(crearCeldaDato(formatoMoneda(det.getSubtotal()), Element.ALIGN_RIGHT));
                }
            }

            document.add(tablaItems);

            // 4. Totales y Saldo
            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(100);
            tablaTotales.setWidths(new float[]{65, 35});

            PdfPCell cObs = new PdfPCell();
            cObs.setBorder(Rectangle.NO_BORDER);
            cObs.addElement(new Paragraph("Observaciones:", FONT_NORMAL_BOLD));
            String obs = pedido.getObservaciones() != null && !pedido.getObservaciones().isEmpty() ? pedido.getObservaciones() : "Sin observaciones adicionales.";
            cObs.addElement(new Paragraph(obs, FONT_NORMAL));
            tablaTotales.addCell(cObs);

            PdfPTable subTablaTot = new PdfPTable(2);
            subTablaTot.setWidthPercentage(100);
            subTablaTot.setWidths(new float[]{50, 50});

            agregarFilaTotal(subTablaTot, "TOTAL:", formatoMoneda(pedido.getTotal()), true);
            agregarFilaTotal(subTablaTot, "Abono / Pagado:", formatoMoneda(pedido.getAbono()), false);
            agregarFilaTotal(subTablaTot, "SALDO PENDIENTE:", formatoMoneda(pedido.getSaldoPendiente()), true);

            PdfPCell cTotales = new PdfPCell(subTablaTot);
            cTotales.setBorder(Rectangle.BOX);
            cTotales.setPadding(4);
            tablaTotales.addCell(cTotales);

            document.add(tablaTotales);
            document.add(new Paragraph(" "));

            // 5. Pie de página
            Paragraph pie = new Paragraph("¡Gracias por confiar en Yuju Repostería! Endulzando tus mejores momentos.", FONT_PIE);
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    /**
     * Genera catálogo de productos en PDF
     */
    public byte[] generarCatalogoProductosPdf(List<Producto> productos) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph pTitulo = new Paragraph("CATÁLOGO OFICIAL DE REPOSTERÍA GOURMET", FONT_TITULO);
            pTitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(pTitulo);

            Paragraph pSub = new Paragraph("Yuju Repostería - Precios Minoristas y Mayoristas", FONT_SUBTITULO);
            pSub.setAlignment(Element.ALIGN_CENTER);
            document.add(pSub);
            document.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{12, 28, 15, 15, 15, 15});

            agregarCeldaCabecera(tabla, "Código");
            agregarCeldaCabecera(tabla, "Producto");
            agregarCeldaCabecera(tabla, "Categoría");
            agregarCeldaCabecera(tabla, "P. Detal");
            agregarCeldaCabecera(tabla, "P. Mayorista");
            agregarCeldaCabecera(tabla, "Stock");

            for (Producto p : productos) {
                tabla.addCell(crearCeldaDato(p.getCodigo(), Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaDato(p.getNombre(), Element.ALIGN_LEFT));
                tabla.addCell(crearCeldaDato(p.getTipo().getDescripcion(), Element.ALIGN_LEFT));
                tabla.addCell(crearCeldaDato(formatoMoneda(p.getPrecioMinorista()), Element.ALIGN_RIGHT));
                tabla.addCell(crearCeldaDato(formatoMoneda(p.getPrecioMayorista()), Element.ALIGN_RIGHT));
                tabla.addCell(crearCeldaDato(String.valueOf(p.getStock()), Element.ALIGN_CENTER));
            }

            document.add(tabla);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    /**
     * Genera reporte de ventas en PDF
     */
    public byte[] generarReporteVentasPdf(List<Pedido> pedidos, String tituloPeriodo) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36); // Horizontal para más columnas

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph pTitulo = new Paragraph("INFORME EJECUTIVO DE VENTAS Y PEDIDOS", FONT_TITULO);
            pTitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(pTitulo);

            Paragraph pSub = new Paragraph("Período: " + tituloPeriodo + " | Yuju Repostería", FONT_SUBTITULO);
            pSub.setAlignment(Element.ALIGN_CENTER);
            document.add(pSub);
            document.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(7);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{14, 16, 25, 15, 15, 15, 15});

            agregarCeldaCabecera(tabla, "N° Factura");
            agregarCeldaCabecera(tabla, "Fecha");
            agregarCeldaCabecera(tabla, "Cliente");
            agregarCeldaCabecera(tabla, "Tipo Venta");
            agregarCeldaCabecera(tabla, "Estado");
            agregarCeldaCabecera(tabla, "Método Pago");
            agregarCeldaCabecera(tabla, "Total Venta");

            BigDecimal sumaTotal = BigDecimal.ZERO;

            for (Pedido p : pedidos) {
                tabla.addCell(crearCeldaDato(p.getNumeroFactura(), Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaDato(p.getFechaPedido().format(fechaHoraFormat), Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaDato(p.getCliente().getNombreCompleto(), Element.ALIGN_LEFT));
                tabla.addCell(crearCeldaDato(p.getTipoVenta().getDescripcion(), Element.ALIGN_LEFT));
                tabla.addCell(crearCeldaDato(p.getEstado().getDescripcion(), Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaDato(p.getMetodoPago().getDescripcion(), Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaDato(formatoMoneda(p.getTotal()), Element.ALIGN_RIGHT));

                if (p.getTotal() != null) {
                    sumaTotal = sumaTotal.add(p.getTotal());
                }
            }

            document.add(tabla);
            document.add(new Paragraph(" "));

            Paragraph pTot = new Paragraph("VENTAS TOTALES REGISTRADAS: " + formatoMoneda(sumaTotal), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, COLOR_PRIMARIO));
            pTot.setAlignment(Element.ALIGN_RIGHT);
            document.add(pTot);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private void agregarCeldaCabecera(PdfPTable tabla, String texto) {
        PdfPCell c = new PdfPCell(new Phrase(texto, FONT_CABECERA_TABLA));
        c.setBackgroundColor(COLOR_PRIMARIO);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setPadding(6);
        tabla.addCell(c);
    }

    private PdfPCell crearCeldaDato(String texto, int alineacion) {
        PdfPCell c = new PdfPCell(new Phrase(texto != null ? texto : "", FONT_NORMAL));
        c.setHorizontalAlignment(alineacion);
        c.setPadding(5);
        return c;
    }

    private void agregarFilaTotal(PdfPTable tabla, String label, String valor, boolean resaltar) {
        Font f = resaltar ? FONT_NORMAL_BOLD : FONT_NORMAL;
        PdfPCell c1 = new PdfPCell(new Phrase(label, f));
        c1.setBorder(Rectangle.NO_BORDER);
        PdfPCell c2 = new PdfPCell(new Phrase(valor, f));
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.addCell(c1);
        tabla.addCell(c2);
    }

    private String formatoMoneda(BigDecimal valor) {
        if (valor == null) return "$0";
        return monedaFormat.format(valor);
    }
}

