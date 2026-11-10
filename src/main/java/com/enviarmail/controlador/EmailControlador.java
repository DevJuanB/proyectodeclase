package com.enviarmail.controlador;

import com.enviarmail.modelo.servicio.EmailServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EmailControlador {

    private final EmailServicio emailServ;

    public EmailControlador(EmailServicio emailServ) {
        this.emailServ = emailServ;
    }

    // Formulario libre para envío directo de correos con plantilla y adjuntos
    @GetMapping("/correo")
    public String form() {
        return "emailform";
    }

    @PostMapping("/send")
    public String sendEmail(@RequestParam String destino,
                            @RequestParam String asunto,
                            @RequestParam(required = false) String nombreUsuario,
                            @RequestParam(required = false) MultipartFile archivo,
                            Model model) {
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("nombre", nombreUsuario != null && !nombreUsuario.isEmpty() ? nombreUsuario : "Estimado/a");
            vars.put("mensaje", "Este correo fue enviado desde el sistema de gestión de repostería <strong>Yuju Repostería</strong>.");
            emailServ.enviarConPlantillaYAdjunto(destino, asunto, vars, archivo);
            model.addAttribute("resultado", "Correo enviado correctamente a " + destino);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("resultado", "Error al enviar: " + e.getMessage());
        }
        return "emailform";
    }
}
