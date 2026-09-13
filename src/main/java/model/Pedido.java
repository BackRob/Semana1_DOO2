package model;

import interfaces.Cancelable;
import interfaces.Despachable;
import interfaces.Rastreable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static Services.ControladorDeEnvios.getControladorEnvios;

public abstract class Pedido implements Cancelable, Rastreable, Despachable, Comparable<Pedido> {
    private static final AtomicInteger contadorGlobal = new AtomicInteger(1); // contador seguro para hilos
    private final int idPedido;
    private String direccionEntrega;
    private double distanciaKm;
    private EstadoPedido estadoPedido;
    private Repartidor repartidor;
    protected PrioridadPedido prioridadPedido;

    //constructor
    public Pedido(String direccionEntrega, double distanciaKm) {
        idPedido = contadorGlobal.getAndIncrement();
        setDireccionEntrega(direccionEntrega);
        setDistanciaKm(distanciaKm);
        estadoPedido = EstadoPedido.INICIADO;
        repartidor = null;
        this.agregarPedidoGestor();
    }

    //sets
    public void setDireccionEntrega(String direccionEntrega) {
        if (direccionEntrega != null && !direccionEntrega.isEmpty()) {
            this.direccionEntrega = direccionEntrega;
        }
    }

    public PrioridadPedido getPrioridadPedido() {
        return prioridadPedido;
    }

    public void setPrioridadPedido(PrioridadPedido prioridadPedido) {
        this.prioridadPedido = prioridadPedido;
    }

    public void setDistanciaKm(double distanciaKm) {
        if (distanciaKm > 0) {
            this.distanciaKm = distanciaKm;
        }
    }
    public synchronized void setEstado(EstadoPedido estadoPedido) {
                this.estadoPedido = estadoPedido;
    }
    public void setRepartidor(Repartidor repartidor) {
        if (repartidor==null){
            System.out.println("Repartidor nulo");
            return;
        }
        this.repartidor = repartidor;
    }

    //gets
    public int getIdPedido() {
        return idPedido;
    }
    public String getDireccionEntrega() {
        return direccionEntrega;
    }
    public double getDistanciaKm() {return distanciaKm;}
    public EstadoPedido getEstado() {return estadoPedido;}
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

    public void agregarPedidoGestor(){
        getControladorEnvios().agregarPedido(this);
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

    //estados pedido
    @Override
    public synchronized void cancelar() {
        if (estadoPedido!=EstadoPedido.ENTREGADO) {
            this.setEstado(EstadoPedido.CANCELADO);
            if (repartidor==null){
                System.out.println("Repartidor nulo");
                return;
            }
            repartidor.entregaCancelada();
            repartidor = null;
            System.out.println("Pedido Cancelado");
        }else{
            System.out.println("Pedido ya fue entregado, no se puede cancelar");
        }

    }

    public synchronized void entregar(){
        if (estadoPedido==EstadoPedido.ASIGNADO && repartidor!=null) {
            estadoPedido = EstadoPedido.ENTREGADO;
            repartidor.setAsignado(false);
        }
    }

    public abstract boolean necesitaMochila();

    public int compareTo(Pedido otro){
        return this.prioridadPedido.compareTo(otro.prioridadPedido);
    }



}
