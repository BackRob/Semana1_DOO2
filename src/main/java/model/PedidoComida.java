package model;

public class PedidoComida extends Pedido {

    public PedidoComida(String direccionEntrega, double distanciaKm) {
        super(direccionEntrega, distanciaKm);
    }

    @Override
    public double calcularTiempoEntrega() {
        return 15 + 2 * getDistanciaKm();
    }

    @Override
    public String toString() {
        return "PedidoComida -> " + super.toString();
    }
}
