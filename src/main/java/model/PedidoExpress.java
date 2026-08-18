package model;

public class PedidoExpress extends Pedido {

    public PedidoExpress(int idPedido, String direccionEntrega,double distanciaKm) {
        super(idPedido, direccionEntrega,distanciaKm);
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        super.asignarRepartidor(nombreRepartidor);
        System.out.println("[pedido Express]");
        System.out.println("Validando repartidor mas cercano... OK");
        System.out.println("Repartidor Asignado con exito!");
        System.out.println("Repartidor: " + nombreRepartidor);
        System.out.println(this);
    }

    @Override
    public void asignarRepartidor(Repartidor repartidor) {
        System.out.println("[pedido Express]");
        super.asignarRepartidor(repartidor);
        System.out.println("Validando repartidor mas cercano con disponibilidad inmediata... OK");
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
        int tiempoEntrega;
        if(getDistanciaKm()>5){
            tiempoEntrega = 15;
        }else{
            tiempoEntrega = 10;
        }
        System.out.println("Tiempo estimado de entrega: "+tiempoEntrega+" minutos\n");
    }

}
