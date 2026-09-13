package model;

public class PedidoComida extends Pedido {
    public PedidoComida(String direccionEntrega,double distanciaKm) {
        super(direccionEntrega,distanciaKm);
        this.prioridadPedido = PrioridadPedido.MEDIA;
    }


    @Override
    public void mostrarResumen(){
        System.out.print("Pedido comida #");
        System.out.printf("%03d\n", getIdPedido());
        super.mostrarResumen();
        calcularTiempoEntrega();
    }

    @Override
    public double calcularTiempoEntrega(){
        return (15+2*getDistanciaKm());
    }

    @Override
    public boolean necesitaMochila() {
        return true;
    }




}
