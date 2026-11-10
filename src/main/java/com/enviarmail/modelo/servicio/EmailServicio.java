package com.enviarmail.modelo.servicio;

import com.enviarmail.modelo.entidad.Pedido;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailServicio {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username:yuju.sas.empresa@gmail.com}")
    private String remitente;

    public EmailServicio(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Envía la factura PDF generada directamente al correo del cliente
     */
    public void enviarFacturaCliente(Pedido pedido, byte[] pdfBytes) throws MessagingException {
        if (pedido.getCliente() == null || pedido.getCliente().getEmail() == null || pedido.getCliente().getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El cliente no tiene un correo electrónico registrado.");
        }

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, StandardCharsets.UTF_8.name());

        if (remitente != null && !remitente.trim().isEmpty()) {
            helper.setFrom(remitente);
        }
        helper.setTo(pedido.getCliente().getEmail());
        helper.setSubject("Comprobante de Venta #" + pedido.getNumeroFactura() + " - Yuju Repostería");

        // Construir contenido HTML enriquecido para el correo
        Context context = new Context();
        context.setVariable("nombre", pedido.getCliente().getNombreCompleto());
        context.setVariable("numeroFactura", pedido.getNumeroFactura());
        context.setVariable("total", pedido.getTotal());
        context.setVariable("saldo", pedido.getSaldoPendiente());
        context.setVariable("estado", pedido.getEstado().getDescripcion());
        context.setVariable("mensaje", "Estimado/a cliente, adjunto a este mensaje encontrará su factura / comprobante oficial en formato PDF correspondiente a su compra en <strong>Yuju Repostería</strong>.<br><br>Agradecemos su preferencia.");

        String contenidoHtml = templateEngine.process("correo", context);
        helper.setText(contenidoHtml, true);

        // Adjuntar el PDF generado
        if (pdfBytes != null && pdfBytes.length > 0) {
            String nombreArchivo = "Factura-" + pedido.getNumeroFactura() + ".pdf";
            helper.addAttachment(nombreArchivo, new ByteArrayResource(pdfBytes), "application/pdf");
        }

        mailSender.send(mensaje);
    }

    /**
     * Envía un correo con archivo adjunto genérico (ej. reporte, catálogo o formulario libre)
     */
    public void enviarConPlantillaYAdjunto(String destino, String asunto, Map<String, Object> vars, MultipartFile archivo) throws MessagingException {
        Context context = new Context();
        if (vars != null) {
            context.setVariables(vars);
        }

        String contenidoHtml = templateEngine.process("correo", context);

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, StandardCharsets.UTF_8.name());

        if (remitente != null && !remitente.trim().isEmpty()) {
            helper.setFrom(remitente);
        }
        helper.setTo(destino);
        helper.setSubject(asunto);
        helper.setText(contenidoHtml, true);

        if (archivo != null && !archivo.isEmpty()) {
            String nombreArchivo = archivo.getOriginalFilename();
            if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                nombreArchivo = "archivo_adjunto";
            }
            helper.addAttachment(nombreArchivo, archivo);
        }

        mailSender.send(mensaje);
    }

    /**
     * Envía reportes o catálogos PDF por correo a cualquier destinatario
     */
    public void enviarReportePdf(String destino, String asunto, String mensajeCuerpo, byte[] pdfBytes, String nombreArchivo) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, StandardCharsets.UTF_8.name());

        if (remitente != null && !remitente.trim().isEmpty()) {
            helper.setFrom(remitente);
        }
        helper.setTo(destino);
        helper.setSubject(asunto);

        Context context = new Context();
        context.setVariable("nombre", "Equipo Directivo / Cliente");
        context.setVariable("mensaje", mensajeCuerpo);
        String contenidoHtml = templateEngine.process("correo", context);
        helper.setText(contenidoHtml, true);

        if (pdfBytes != null && pdfBytes.length > 0) {
            helper.addAttachment(nombreArchivo, new ByteArrayResource(pdfBytes), "application/pdf");
        }

        mailSender.send(mensaje);
    }
}
