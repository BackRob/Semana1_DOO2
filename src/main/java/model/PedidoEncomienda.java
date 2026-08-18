package model;

public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(int idPedido, String direccionEntrega,double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
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

    @Override
    public void mostrarResumen(){
        System.out.print("Pedido comida #");
        System.out.printf("%03d\n", getIdPedido());
        super.mostrarResumen();
        calcularTiempoEntrega();
    }

    public void calcularTiempoEntrega(){
        System.out.println("Tiempo estimado de entrega: "+Math.round(20+1.5*getDistanciaKm())+" minutos\n");
    }
}
