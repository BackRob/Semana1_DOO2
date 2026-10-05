package model;

public class PedidoExpress extends Pedido {

    public PedidoExpress(String direccionEntrega, double distanciaKm) {
        super(direccionEntrega, distanciaKm);
    }

    //constructor con id, usado desde la ventana de registro
    public PedidoExpress(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    @Override
    public double calcularTiempoEntrega() {
        return getDistanciaKm() > 5 ? 15 : 10;
    }

    @Override
    public String getTipo() {
        return "EXPRESS";
    }

    @Override
    public String toString() {
        return "PedidoExpress -> " + super.toString();
    }
}
