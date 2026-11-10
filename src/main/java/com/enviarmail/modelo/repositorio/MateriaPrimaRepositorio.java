package com.enviarmail.modelo.repositorio;

import com.enviarmail.modelo.entidad.MateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaPrimaRepositorio extends JpaRepository<MateriaPrima, Long> {
    List<MateriaPrima> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo);

    @Query("SELECT m FROM MateriaPrima m WHERE m.stockActual <= m.stockMinimo")
    List<MateriaPrima> findMateriasConBajoStock();
}

