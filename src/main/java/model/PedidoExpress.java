package model;

import static Services.ControladorDeEnvios.getControladorEnvios;

public class PedidoExpress extends Pedido {

    public PedidoExpress(String direccionEntrega,double distanciaKm) {
        super(direccionEntrega,distanciaKm);
        this.prioridadPedido = PrioridadPedido.ALTA;
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        super.asignarRepartidor(nombreRepartidor);
        System.out.println("[Pedido Express]");
        System.out.println("Validando repartidor mas cercano... OK");
        System.out.println("Repartidor Asignado con exito!");
        System.out.println("Repartidor: " + nombreRepartidor);
        System.out.println(this);
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Express]");
        asignarRepartidorAutomatico(false);
    }


    @Override
    public void mostrarResumen(){
        System.out.print("Pedido Express #");
        System.out.printf("%03d\n", getIdPedido());
        super.mostrarResumen();
        calcularTiempoEntrega();
    }


    public double calcularTiempoEntrega(){
        int tiempoEntrega;
        if(getDistanciaKm()>5){
            tiempoEntrega = 15;
        }else{
            tiempoEntrega = 10;
        }
        return tiempoEntrega;
    }

}
