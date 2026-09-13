package model;

public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(String direccionEntrega, double distanciaKm) {
        super(direccionEntrega, distanciaKm);
    }

    @Override
    public double calcularTiempoEntrega() {
        return Math.round(20 + 1.5 * getDistanciaKm());
    }

    @Override
    public String toString() {
        return "PedidoEncomienda -> " + super.toString();
    }
}
