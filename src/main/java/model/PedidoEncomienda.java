package model;

public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega);
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
    public void asignarRepartidor(Repartidor repartidor) {
        System.out.println("[pedido de encomienda]");
        super.asignarRepartidor(repartidor);
        System.out.println("Validando peso y embalaje... OK");
        System.out.println("Repartidor Asignado con exito!");
        System.out.println("Repartidor: " + repartidor);
        System.out.println(this);
    }
}
