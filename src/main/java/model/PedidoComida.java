package model;

import Services.ControladorDeEnvios;
import interfaces.Despachable;

import static Services.ControladorDeEnvios.getControladorEnvios;

public class PedidoComida extends Pedido {
    public PedidoComida(String direccionEntrega,double distanciaKm) {
        super(direccionEntrega,distanciaKm);
        this.prioridadPedido = PrioridadPedido.MEDIA;
    }


    //sobrecarga
    @Override
    public void asignarRepartidor(){
            System.out.println("[Pedido Comida]");
            asignarRepartidorAutomatico(true);
    }


    @Override
    public void asignarRepartidor(String nombreRepartidor){
        System.out.println("[pedido de Comida]");
        super.asignarRepartidor(nombreRepartidor);
        this.setEstado(EstadoPedido.ASIGNADO);
        System.out.println("Validando mochila termica... ");
        System.out.println("Repartidor Asignado con exito!");
        System.out.println("Repartidor: " + nombreRepartidor);
        System.out.println(this);
    }

    @Override
    public void mostrarResumen(){
        System.out.print("Pedido comida #");
        System.out.printf("%03d\n", getIdPedido());
        super.mostrarResumen();
        calcularTiempoEntrega();
    }

    @Override
    public double calcularTiempoEntrega(){
        return (15+2*getDistanciaKm());
    }


}
