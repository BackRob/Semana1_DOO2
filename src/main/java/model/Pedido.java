package model;

import interfaces.Cancelable;
import interfaces.Despachable;
import interfaces.Rastreable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Pedido implements Cancelable, Despachable, Rastreable {
    private static final AtomicInteger contadorGlobal = new AtomicInteger(1); // contador seguro para hilos
    private final int idPedido;
    private String direccionEntrega;
    private double distanciaKm;
    private EstadoPedido estadoPedido;

    //constructor
    public Pedido(String direccionEntrega, double distanciaKm) {
        idPedido = contadorGlobal.getAndIncrement();
        setDireccionEntrega(direccionEntrega);
        setDistanciaKm(distanciaKm);
        estadoPedido = EstadoPedido.PENDIENTE;
    }

    //sets
    public void setDireccionEntrega(String direccionEntrega) {
        if (direccionEntrega != null && !direccionEntrega.isEmpty()) {
            this.direccionEntrega = direccionEntrega;
        }
    }

    public void setDistanciaKm(double distanciaKm) {
        if (distanciaKm > 0) {
            this.distanciaKm = distanciaKm;
        }
    }

    public synchronized void setEstado(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    //metodo pedido por la pauta, actualiza el estado recibiendo un String
    public synchronized void setEstado(String estado) {
        if (estado == null) return;
        try {
            this.estadoPedido = EstadoPedido.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Estado invalido para el pedido " + idPedido + ": " + estado);
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
    public synchronized EstadoPedido getEstado() {return estadoPedido;}

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

    public abstract double calcularTiempoEntrega();

    //METODO ToString
    @Override
    public synchronized String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", distanciaKm=" + distanciaKm +
                ", estado='" + estadoPedido + '\'' +
                '}';
    }

    //Implementacion Interfases
    @Override
    public synchronized void cancelar() {
        if (estadoPedido != EstadoPedido.ENTREGADO) {
            estadoPedido = EstadoPedido.CANCELADO;
            System.out.println("Pedido Cancelado");
        } else {
            System.out.println("Pedido ya fue entregado, no se puede cancelar");
        }
    }
}
