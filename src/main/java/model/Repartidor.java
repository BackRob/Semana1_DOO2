package model;

import java.time.LocalDate;



public class Repartidor extends Persona {
    private boolean mochilaTermica;

    public Repartidor(String nombre, LocalDate fechaNacimiento, String rut,Boolean mochilaTermica) {
        super(nombre, fechaNacimiento, rut);
        setMochilaTermica(mochilaTermica);
    }

    public void setMochilaTermica(boolean mochilaTermica) {
        this.mochilaTermica = mochilaTermica;
    }
    public boolean getMochilaTermica() {
        return mochilaTermica;
    }


    @Override
    public String toString() {
        return "Repartidor{" +super.toString() +
                "mochilaTermica=" + mochilaTermica;
    }
}
