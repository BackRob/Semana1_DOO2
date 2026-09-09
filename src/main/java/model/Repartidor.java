package model;

import java.time.LocalDate;

import static Services.ControladorDeEnvios.getControladorEnvios;


public class Repartidor extends Persona {
    private boolean mochilaTermica;
    private boolean asignado;

    public Repartidor(String nombre, LocalDate fechaNacimiento, String rut,Boolean mochilaTermica) {
        super(nombre, fechaNacimiento, rut);
        setMochilaTermica(mochilaTermica);
        asignado = false;
    }

    public void setMochilaTermica(boolean mochilaTermica) {
        this.mochilaTermica = mochilaTermica;
    }
    public boolean getMochilaTermica() {
        return mochilaTermica;
    }

    public void setAsignado(boolean asignado) {this.asignado = asignado;}
    public boolean getAsignado() {return asignado;}


    //AutoAsignacion
    public void agregarControlador(){
        getControladorEnvios().agregarRepartidor(this);
    }


    @Override
    public String toString() {
        if (mochilaTermica) {
            return "Repartidor{" + super.toString() +
                    "mochila Termica= SI";

        }else{
            return "Repartidor{" + super.toString() +
                    "mochila Termica= NO";
        }
    }


}
