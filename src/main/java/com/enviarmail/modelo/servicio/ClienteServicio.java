package com.enviarmail.modelo.servicio;

import com.enviarmail.modelo.entidad.Cliente;
import com.enviarmail.modelo.entidad.Pedido;
import com.enviarmail.modelo.enums.TipoCliente;
import com.enviarmail.modelo.repositorio.ClienteRepositorio;
import com.enviarmail.modelo.repositorio.PedidoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServicio {

    private final ClienteRepositorio clienteRepositorio;
    private final PedidoRepositorio pedidoRepositorio;

    public ClienteServicio(ClienteRepositorio clienteRepositorio, PedidoRepositorio pedidoRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
        this.pedidoRepositorio = pedidoRepositorio;
    }

    public List<Cliente> listarTodos() {
        return clienteRepositorio.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepositorio.findById(id);
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }

    public void eliminar(Long id) {
        clienteRepositorio.deleteById(id);
    }

    public List<Cliente> buscar(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return listarTodos();
        }
        return clienteRepositorio.findByNombreCompletoContainingIgnoreCaseOrDocumentoContaining(criterio, criterio);
    }

    public List<Cliente> listarPorTipo(TipoCliente tipo) {
        return clienteRepositorio.findByTipoCliente(tipo);
    }

    public List<Pedido> consultarHistorialCompras(Long clienteId) {
        return pedidoRepositorio.findByClienteIdOrderByFechaPedidoDesc(clienteId);
    }
}

