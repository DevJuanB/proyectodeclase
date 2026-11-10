package com.enviarmail.modelo.servicio;

import com.enviarmail.modelo.entidad.Usuario;
import com.enviarmail.modelo.repositorio.UsuarioRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Optional<Usuario> autenticar(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario u = usuarioOpt.get();
            if (u.isActivo() && u.getPassword().equals(password)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public Usuario registrar(Usuario usuario) {
        if (usuarioRepositorio.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario '" + usuario.getUsername() + "' ya existe.");
        }
        usuario.setActivo(true);
        return usuarioRepositorio.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }

    public boolean existeUsername(String username) {
        return usuarioRepositorio.existsByUsername(username);
    }
}

