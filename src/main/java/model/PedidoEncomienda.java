package model;

public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega);
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        super.asignarRepartidor(nombreRepartidor);
        System.out.println("Validando peso y embalaje...");
        System.out.println("Repartidor Asignado con exito!");
        System.out.println("Repartidor: " + nombreRepartidor);
        System.out.println(this);
    }

    @Override
    public void asignarRepartidor(Repartidor repartidor) {
        super.asignarRepartidor(repartidor);
        System.out.println("Validando peso y embalaje...");
        System.out.println("Repartidor Asignado con exito!");
        System.out.println("Repartidor: " + repartidor);
        System.out.println(this);
    }
}
