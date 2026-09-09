package model;

import static Services.ControladorDeEnvios.getControladorEnvios;

public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(String direccionEntrega,double distanciaKm) {
        super(direccionEntrega, distanciaKm);
        prioridadPedido = PrioridadPedido.BAJA;
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[pedido de encomienda]");
        super.asignarRepartidor(nombreRepartidor);
        System.out.println("Validando peso y embalaje... OK");
        System.out.println("Repartidor Asignado con exito!");
        System.out.println("Repartidor: " + nombreRepartidor);
        System.out.println(this);
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("[pedido Encomienda]");
        asignarRepartidorAutomatico(false);
    }

    @Override
    public void mostrarResumen(){
        System.out.print("Pedido comida #");
        System.out.printf("%03d\n", getIdPedido());
        super.mostrarResumen();
        calcularTiempoEntrega();
    }

    public double calcularTiempoEntrega(){
        return Math.round(20+1.5*getDistanciaKm());
    }
}
