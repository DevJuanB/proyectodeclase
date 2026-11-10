package com.enviarmail.modelo.repositorio;

import com.enviarmail.modelo.entidad.Produccion;
import com.enviarmail.modelo.enums.EstadoProduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduccionRepositorio extends JpaRepository<Produccion, Long> {
    List<Produccion> findAllByOrderByFechaPlanificadaDesc();
    List<Produccion> findByEstado(EstadoProduccion estado);
    List<Produccion> findByResponsableId(Long responsableId);
}

