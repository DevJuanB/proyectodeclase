package com.enviarmail.modelo.repositorio;

import com.enviarmail.modelo.entidad.Empleado;
import com.enviarmail.modelo.enums.CargoEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepositorio extends JpaRepository<Empleado, Long> {
    List<Empleado> findByActivoTrue();
    List<Empleado> findByCargo(CargoEmpleado cargo);
    List<Empleado> findByNombreCompletoContainingIgnoreCaseOrIdentificacionContaining(String nombre, String identificacion);
}

