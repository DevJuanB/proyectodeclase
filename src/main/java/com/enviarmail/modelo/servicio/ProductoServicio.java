package com.enviarmail.modelo.servicio;

import com.enviarmail.modelo.entidad.Producto;
import com.enviarmail.modelo.enums.TipoProducto;
import com.enviarmail.modelo.repositorio.ProductoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServicio {

    private final ProductoRepositorio productoRepositorio;

    public ProductoServicio(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public List<Producto> listarTodos() {
        return productoRepositorio.findAll();
    }

    public List<Producto> listarDisponibles() {
        return productoRepositorio.findByDisponibleTrue();
    }

    public List<Producto> listarPorTipo(TipoProducto tipo) {
        return productoRepositorio.findByTipo(tipo);
    }

    public List<Producto> listarBajoStock() {
        return productoRepositorio.findProductosConBajoStock();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepositorio.findById(id);
    }

    public Producto guardar(Producto producto) {
        return productoRepositorio.save(producto);
    }

    public void eliminar(Long id) {
        productoRepositorio.deleteById(id);
    }

    public List<Producto> buscar(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return listarTodos();
        }
        return productoRepositorio.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(criterio, criterio);
    }

    public void actualizarStock(Long productoId, int cantidadDelta) {
        Optional<Producto> opt = productoRepositorio.findById(productoId);
        if (opt.isPresent()) {
            Producto p = opt.get();
            int nuevoStock = (p.getStock() != null ? p.getStock() : 0) + cantidadDelta;
            if (nuevoStock < 0) nuevoStock = 0;
            p.setStock(nuevoStock);
            productoRepositorio.save(p);
        }
    }
}

