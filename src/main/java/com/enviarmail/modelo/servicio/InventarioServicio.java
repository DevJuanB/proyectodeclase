package com.enviarmail.modelo.servicio;

import com.enviarmail.modelo.entidad.MateriaPrima;
import com.enviarmail.modelo.entidad.MovimientoInventario;
import com.enviarmail.modelo.enums.TipoMovimiento;
import com.enviarmail.modelo.repositorio.MateriaPrimaRepositorio;
import com.enviarmail.modelo.repositorio.MovimientoInventarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class InventarioServicio {

    private final MateriaPrimaRepositorio materiaPrimaRepositorio;
    private final MovimientoInventarioRepositorio movimientoRepositorio;

    public InventarioServicio(MateriaPrimaRepositorio materiaPrimaRepositorio,
                             MovimientoInventarioRepositorio movimientoRepositorio) {
        this.materiaPrimaRepositorio = materiaPrimaRepositorio;
        this.movimientoRepositorio = movimientoRepositorio;
    }

    public List<MateriaPrima> listarMateriasPrimas() {
        return materiaPrimaRepositorio.findAll();
    }

    public Optional<MateriaPrima> buscarPorId(Long id) {
        return materiaPrimaRepositorio.findById(id);
    }

    public MateriaPrima guardarMateriaPrima(MateriaPrima materiaPrima) {
        return materiaPrimaRepositorio.save(materiaPrima);
    }

    public void eliminarMateriaPrima(Long id) {
        materiaPrimaRepositorio.deleteById(id);
    }

    public List<MateriaPrima> buscarMaterias(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return listarMateriasPrimas();
        }
        return materiaPrimaRepositorio.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(criterio, criterio);
    }

    public List<MateriaPrima> listarBajoStock() {
        return materiaPrimaRepositorio.findMateriasConBajoStock();
    }

    public List<MovimientoInventario> listarMovimientosRecientes() {
        return movimientoRepositorio.findTop50ByOrderByFechaDesc();
    }

    @Transactional
    public MovimientoInventario registrarMovimiento(Long materiaPrimaId, TipoMovimiento tipo, BigDecimal cantidad, String motivo, String responsable) {
        MateriaPrima mp = materiaPrimaRepositorio.findById(materiaPrimaId)
                .orElseThrow(() -> new IllegalArgumentException("Materia prima no encontrada ID: " + materiaPrimaId));

        if (tipo == TipoMovimiento.ENTRADA) {
            mp.setStockActual(mp.getStockActual().add(cantidad));
        } else {
            BigDecimal nuevoStock = mp.getStockActual().subtract(cantidad);
            if (nuevoStock.compareTo(BigDecimal.ZERO) < 0) {
                nuevoStock = BigDecimal.ZERO;
            }
            mp.setStockActual(nuevoStock);
        }
        materiaPrimaRepositorio.save(mp);

        MovimientoInventario mov = new MovimientoInventario(mp, tipo, cantidad, motivo, responsable);
        return movimientoRepositorio.save(mov);
    }
}

