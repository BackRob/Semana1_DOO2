package model;

import Services.ControladorDeEnvios;
import interfaces.Cancelable;
import interfaces.Despachable;
import interfaces.Rastreable;

import java.util.Objects;

import static Services.ControladorDeEnvios.getControladorEnvios;

public abstract class Pedido implements Cancelable, Rastreable, Despachable {
    private int idPedido;
    private String direccionEntrega;
    private double distanciaKm;
    private EstadoPedido estadoPedido;
    private Repartidor repartidor;

    //constructor
    public Pedido(int idPedido, String direccionEntrega, double distanciaKm) {
        setIdPedido(idPedido);
        setDireccionEntrega(direccionEntrega);
        setDistanciaKm(distanciaKm);
        estadoPedido = EstadoPedido.INICIADO;
        repartidor = null;
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
    public void setEstado(EstadoPedido estadoPedido) {
                this.estadoPedido = estadoPedido;
    }
    public void setRepartidor(Repartidor repartidor) {this.repartidor = repartidor;}

    //gets
    public int getIdPedido() {
        return idPedido;
    }
    public String getDireccionEntrega() {
        return direccionEntrega;
    }
    public double getDistanciaKm() {return distanciaKm;}
    public String getEstado() {return estadoPedido.toString();}
    public Repartidor getRepartidor() {return repartidor;}

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


    //metodo para asignar, dependiendo el tipo de pedido
    public abstract void asignarRepartidor();



    //Agregar utilizando poliformismo
    public void agregarGestor() {
        if (estadoPedido!=EstadoPedido.INICIADO) {
            System.out.println("Pedido ya gestionado");
            return;
        }
        getControladorEnvios().agregarYAsignarPedido(this);
    }
    public void agregarGestor(String repartidor) {
        if (estadoPedido!=EstadoPedido.INICIADO) {
            System.out.println("Pedido ya gestionado");
            return;
        }
        getControladorEnvios().agregarYAsignarPedido(this, repartidor);
    }



    //metodo para asignar repartidor automaticamente
    public void asignarRepartidorAutomatico(boolean requiereMochilaTermica){
        Repartidor repartidorAsignado = getControladorEnvios().buscarRepartidorLibre(requiereMochilaTermica);
        if(repartidorAsignado == null){
            System.out.println("No se encontro repartidor disponible");
            return;
        }
        this.repartidor = repartidorAsignado;
        this.setEstado(EstadoPedido.ASIGNADO);
        System.out.println("Repartidor Asignado con exito!");
        System.out.println(repartidorAsignado);
        repartidorAsignado.setAsignado(true);
        System.out.println(this);
    }
    //metodo para asignar repartidor Manualmente
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Repartidor esta siendo asignado...");
    }


    //metodos solicitados
    public void mostrarResumen(){

        System.out.println("Direccion: " + getDireccionEntrega());
        System.out.println("Distancia: " + getDistanciaKm());
    }
    public abstract double calcularTiempoEntrega();

    //METODO ToString
    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", distanciaKm=" + distanciaKm +
                ", estado='" + estadoPedido+ '\'' +
                ", repartidor=" + (repartidor != null ? repartidor.getNombre() : "Sin asignar") +
                '}';
    }

    //Implementacion Interfases
    @Override
    public void cancelar() {
        this.setEstado(EstadoPedido.CANCELADO);
        this.getRepartidor().setAsignado(false);
        this.setRepartidor(null);
        System.out.println("Pedido Cancelado");
    }

    @Override
    public void despachar() {
        System.out.println("Despachando Pedido #" +idPedido+ "...");
        if(this.getRepartidor() != null){
            System.out.println("Tiempo estimado de entrega: "+calcularTiempoEntrega()+" minutos");
            System.out.println("Pedido #" +idPedido+ " despachado!\n");
            this.setEstado(EstadoPedido.DESPACHADO);
        }else{
            System.out.println("Pedido no asignado");
            System.out.println("Asignando...");
            getControladorEnvios().agregarYAsignarPedido(this);
            despachar();
        }

    }

    @Override
    public void verHistorial() {
        getControladorEnvios();
        for (Despachable pedido : getControladorEnvios().getListaDespachable()) {
            if (pedido.getClass() == this.getClass()) {
                System.out.println(pedido);
            }
        }
    }

}
