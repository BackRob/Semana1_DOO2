package model;

public class PedidoComida extends Pedido {

    public PedidoComida(String direccionEntrega, double distanciaKm) {
        super(direccionEntrega, distanciaKm);
    }

    //constructor con id, usado desde la ventana de registro
    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
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
