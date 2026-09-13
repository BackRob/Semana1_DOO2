package model;


public class PedidoExpress extends Pedido {

    public PedidoExpress(String direccionEntrega,double distanciaKm) {
        super(direccionEntrega,distanciaKm);
        this.prioridadPedido = PrioridadPedido.ALTA;
    }

    @Override
    public void mostrarResumen(){
        System.out.print("Pedido Express #");
        System.out.printf("%03d\n", getIdPedido());
        super.mostrarResumen();
        calcularTiempoEntrega();
    }
    public double calcularTiempoEntrega(){
        int tiempoEntrega;
        if(getDistanciaKm()>5){
            tiempoEntrega = 15;
        }else{
            tiempoEntrega = 10;
        }
        return tiempoEntrega;
    }

    @Override
    public boolean necesitaMochila() {
        return false;
    }

}
