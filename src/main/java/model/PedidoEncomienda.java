package model;

import static Services.ControladorDeEnvios.getControladorEnvios;

public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(String direccionEntrega,double distanciaKm) {
        super(direccionEntrega, distanciaKm);
        prioridadPedido = PrioridadPedido.BAJA;
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

    @Override
    public boolean necesitaMochila() {
        return false;
    }
}
