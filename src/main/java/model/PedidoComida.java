package model;

public class PedidoComida extends Pedido {
    public PedidoComida(int idPedido, String direccionEntrega) {
        super(idPedido,direccionEntrega);
    }

    //sobrecarga
    @Override
    public void asignarRepartidor(Repartidor repartidor){
        if (repartidor.getMochilaTermica()){
            super.asignarRepartidor(repartidor);
            System.out.println("Validando mochila termica...");
            System.out.println("Repartidor Asignado con exito!");
            System.out.println(repartidor);
            System.out.println(this);
        }
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor){
        super.asignarRepartidor(nombreRepartidor);
        System.out.println("Validando mochila termica...");
        System.out.println("Repartidor Asignado con exito!");
        System.out.println("Repartidor: " + nombreRepartidor);
        System.out.println(this);
    }
}
