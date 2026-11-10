package com.enviarmail.modelo.entidad;

import com.enviarmail.modelo.enums.CargoEmpleado;
import com.enviarmail.modelo.enums.TurnoEmpleado;
import jakarta.persistence.*;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String identificacion;

    @Column(nullable = false, length = 120)
    private String nombreCompleto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CargoEmpleado cargo;

    @Column(length = 30)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TurnoEmpleado turno;

    @Column(length = 255)
    private String funciones;

    private boolean activo = true;

    public Empleado() {
    }

    public Empleado(String identificacion, String nombreCompleto, CargoEmpleado cargo, String telefono, String email, TurnoEmpleado turno, String funciones) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.cargo = cargo;
        this.telefono = telefono;
        this.email = email;
        this.turno = turno;
        this.funciones = funciones;
        this.activo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public CargoEmpleado getCargo() {
        return cargo;
    }

    public void setCargo(CargoEmpleado cargo) {
        this.cargo = cargo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TurnoEmpleado getTurno() {
        return turno;
    }

    public void setTurno(TurnoEmpleado turno) {
        this.turno = turno;
    }

    public String getFunciones() {
        return funciones;
    }

    public void setFunciones(String funciones) {
        this.funciones = funciones;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}

