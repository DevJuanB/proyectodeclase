package com.enviarmail.modelo.repositorio;

import com.enviarmail.modelo.entidad.Producto;
import com.enviarmail.modelo.enums.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    List<Producto> findByTipo(TipoProducto tipo);
    List<Producto> findByDisponibleTrue();
    List<Producto> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo);

    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockMinimo")
    List<Producto> findProductosConBajoStock();
}

