package com.enviarmail.modelo.repositorio;

import com.enviarmail.modelo.entidad.Cliente;
import com.enviarmail.modelo.enums.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNombreCompletoContainingIgnoreCaseOrDocumentoContaining(String nombre, String documento);
    List<Cliente> findByTipoCliente(TipoCliente tipoCliente);
}

