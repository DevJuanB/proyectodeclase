package com.enviarmail.modelo.servicio;

import com.enviarmail.modelo.entidad.Empleado;
import com.enviarmail.modelo.entidad.Pedido;
import com.enviarmail.modelo.entidad.Produccion;
import com.enviarmail.modelo.enums.CargoEmpleado;
import com.enviarmail.modelo.repositorio.EmpleadoRepositorio;
import com.enviarmail.modelo.repositorio.PedidoRepositorio;
import com.enviarmail.modelo.repositorio.ProduccionRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServicio {

    private final EmpleadoRepositorio empleadoRepositorio;
    private final PedidoRepositorio pedidoRepositorio;
    private final ProduccionRepositorio produccionRepositorio;

    public EmpleadoServicio(EmpleadoRepositorio empleadoRepositorio,
                            PedidoRepositorio pedidoRepositorio,
                            ProduccionRepositorio produccionRepositorio) {
        this.empleadoRepositorio = empleadoRepositorio;
        this.pedidoRepositorio = pedidoRepositorio;
        this.produccionRepositorio = produccionRepositorio;
    }

    public List<Empleado> listarTodos() {
        return empleadoRepositorio.findAll();
    }

    public List<Empleado> listarActivos() {
        return empleadoRepositorio.findByActivoTrue();
    }

    public List<Empleado> listarPorCargo(CargoEmpleado cargo) {
        return empleadoRepositorio.findByCargo(cargo);
    }

    public Optional<Empleado> buscarPorId(Long id) {
        return empleadoRepositorio.findById(id);
    }

    public Empleado guardar(Empleado empleado) {
        return empleadoRepositorio.save(empleado);
    }

    public void eliminar(Long id) {
        empleadoRepositorio.deleteById(id);
    }

    public List<Empleado> buscar(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return listarTodos();
        }
        return empleadoRepositorio.findByNombreCompletoContainingIgnoreCaseOrIdentificacionContaining(criterio, criterio);
    }

    public List<Pedido> consultarVentasPorVendedor(Long vendedorId) {
        return pedidoRepositorio.findByVendedorIdOrderByFechaPedidoDesc(vendedorId);
    }

    public List<Produccion> consultarProduccionesPorEmpleado(Long empleadoId) {
        return produccionRepositorio.findByResponsableId(empleadoId);
    }
}

