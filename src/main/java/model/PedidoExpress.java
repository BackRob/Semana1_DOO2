package model;

public class PedidoExpress extends Pedido {

    public PedidoExpress(String direccionEntrega, double distanciaKm) {
        super(direccionEntrega, distanciaKm);
    }

    @Override
    public double calcularTiempoEntrega() {
        return getDistanciaKm() > 5 ? 15 : 10;
    }

    @Override
    public String toString() {
        return "PedidoExpress -> " + super.toString();
    }
}
