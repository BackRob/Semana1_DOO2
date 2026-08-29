package model;

import java.time.LocalDate;
import java.util.Objects;

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
                ", Fecha Nacimiento: " + fechaNacimiento +
                ", Rut:'" + rut + '\''+ ", ";
    }


    //sobreescritura hashcode y equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Persona persona)) return false;
        return Objects.equals(rut, persona.rut);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rut);
    }
}
