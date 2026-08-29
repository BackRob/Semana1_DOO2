package model;

import static Services.ControladorDeEnvios.getControladorEnvios;

public class PedidoComida extends Pedido {
    public PedidoComida(int idPedido, String direccionEntrega,double distanciaKm) {
        super(idPedido,direccionEntrega,distanciaKm);
    }


    //sobrecarga
    @Override
    public void asignarRepartidor(){
            System.out.println("[pedido Comida]");
            Repartidor repartidorAsignado = getControladorEnvios().buscarRepartidorLibre(true);
            System.out.println("Repartidor Asignado con exito!");
            System.out.println(repartidorAsignado);
            repartidorAsignado.setAsignado(true);
            System.out.println(this);

    }

    @Override
    public void asignarRepartidor(String nombreRepartidor){
        System.out.println("[pedido de encomienda]");
        super.asignarRepartidor(nombreRepartidor);
        System.out.println("Validando mochila termica... OK");
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
