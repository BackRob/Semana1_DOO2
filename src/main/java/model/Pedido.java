package model;

import interfaces.Cancelable;
import interfaces.Despachable;
import interfaces.Rastreable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Pedido implements Cancelable, Despachable, Rastreable {
    private int idPedido; //ya no es final, en semana 7 el id lo genera la base de datos
    private String direccionEntrega;
    private double distanciaKm;
    private EstadoPedido estadoPedido;

    //constructor
    public Pedido(String direccionEntrega, double distanciaKm) {
        setDireccionEntrega(direccionEntrega);
        setDistanciaKm(distanciaKm);
        estadoPedido = EstadoPedido.PENDIENTE;
    }

    //constructor semana 6, el id lo ingresa el usuario desde el formulario
    public Pedido(int idPedido, String direccionEntrega, double distanciaKm) {
        this.idPedido = idPedido;
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

    //se usa cuando la BD devuelve el id generado (AUTO_INCREMENT)
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
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

    //tipo que se guarda en la columna tipo de la tabla pedido
    public abstract String getTipo();

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
