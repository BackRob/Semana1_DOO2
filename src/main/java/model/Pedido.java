package model;

import interfaces.Cancelable;
import interfaces.Despachable;
import interfaces.Rastreable;

import java.util.Objects;

public abstract class Pedido implements Cancelable, Rastreable, Despachable {
    private int idPedido;
    private String direccionEntrega;
    private double distanciaKm;
    private String estado;

    //constructor
    public Pedido(int idPedido, String direccionEntrega, double distanciaKm) {
        setIdPedido(idPedido);
        setDireccionEntrega(direccionEntrega);
        setDistanciaKm(distanciaKm);
        estado = "Iniciado";
    }

    //sets
    public void setDireccionEntrega(String direccionEntrega) {
        if (direccionEntrega != null && !direccionEntrega.isEmpty()) {
            this.direccionEntrega = direccionEntrega;
        }
    }
    public void setIdPedido(int idPedido) {
        if (idPedido > 0) {
            this.idPedido = idPedido;
        }
    }
    public void setDistanciaKm(double distanciaKm) {
        if (distanciaKm > 0) {
            this.distanciaKm = distanciaKm;
        }
    }

    //gets
    public int getIdPedido() {
        return idPedido;
    }
    public String getDireccionEntrega() {
        return direccionEntrega;
    }
    public double getDistanciaKm() {return distanciaKm;}

    //Reescritura del HashCode y equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pedido pedido)) return false;
        return idPedido == pedido.idPedido;
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(idPedido);
    }


    //metodo solicitado y sobrecarga semana 1
    public abstract void asignarRepartidor();
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Repartidor esta siendo asignado...");
    }


    //metodos solicitados
    public void mostrarResumen(){

        System.out.println("Direccion: " + getDireccionEntrega());
        System.out.println("Distancia: " + getDistanciaKm());
    }
    public abstract void calcularTiempoEntrega();

    //METODO ToString
    @Override
    public String toString() {
        return  "idPedido:" + idPedido +
                ", Direccion de Entrega:'" + direccionEntrega + '\''+"\n";
    }

    //Implementacion Interfases
    @Override
    public void cancelar() {}

    @Override
    public void despachar() {
    }

    @Override
    public void verHistorial(){}

}
