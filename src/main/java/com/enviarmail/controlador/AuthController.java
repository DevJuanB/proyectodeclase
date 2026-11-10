package com.enviarmail.controlador;

import com.enviarmail.modelo.entidad.Usuario;
import com.enviarmail.modelo.servicio.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final UsuarioServicio usuarioServicio;

    public AuthController(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales incorrectas o usuario inactivo.");
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        Optional<Usuario> userOpt = usuarioServicio.autenticar(username, password);
        if (userOpt.isPresent()) {
            Usuario u = userOpt.get();
            session.setAttribute("usuarioLogueado", u);
            session.setAttribute("nombreUsuario", u.getNombreCompleto());
            session.setAttribute("rolUsuario", u.getRol().name());
            return "redirect:/";
        } else {
            model.addAttribute("error", "Usuario o contraseña inválidos.");
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Usuario usuario, Model model) {
        try {
            usuarioServicio.registrar(usuario);
            model.addAttribute("mensajeExito", "Usuario del personal registrado exitosamente. Ya puedes iniciar sesión.");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "auth/registro";
        }
    }
}

