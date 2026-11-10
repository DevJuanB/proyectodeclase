package com.enviarmail.modelo.repositorio;

import com.enviarmail.modelo.entidad.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepositorio extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findTop50ByOrderByFechaDesc();
    List<MovimientoInventario> findByMateriaPrimaIdOrderByFechaDesc(Long materiaPrimaId);
}

