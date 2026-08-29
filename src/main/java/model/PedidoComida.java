package model;

import static Services.ControladorDeEnvios.getControladorEnvios;

public class PedidoComida extends Pedido {
    public PedidoComida(int idPedido, String direccionEntrega,double distanciaKm) {
        super(idPedido,direccionEntrega,distanciaKm);
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
        this.setEstado("Asignado");
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
    public void calcularTiempoEntrega(){
        System.out.println("Tiempo estimado de entrega: "+(15+2*getDistanciaKm())+" minutos\n");
    }

}
