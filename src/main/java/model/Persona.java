package model;

import java.time.LocalDate;

public abstract class Persona {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String rut;

    public Persona(String nombre, LocalDate fechaNacimiento, String rut) {
        setNombre(nombre);
        setFechaNacimiento(fechaNacimiento);
        this.rut = rut;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            this.nombre = nombre;
        }
    }


    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento != null && fechaNacimiento.isBefore(LocalDate.now())) {
            this.fechaNacimiento = fechaNacimiento;
        }
    }

    public void setRut(String rut) {
        if (rut != null && !rut.isEmpty()) {
            this.rut = rut;
        }
    }
    public String getNombre() {
        return nombre;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public String getRut() {
        return rut;
    }

    @Override
    public String toString() {
        return  "Nombre:'" + nombre + '\'' +
                ", Apellido:'" + apellido + '\'' +
                ", FechaNacimiento:" + fechaNacimiento +
                ", Rut:'" + rut + '\'';
    }
}
