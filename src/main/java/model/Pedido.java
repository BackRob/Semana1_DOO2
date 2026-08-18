package model;

public abstract class Pedido {
    private int idPedido;
    private String direccionEntrega;
    private int distanciaKm;

    //constructor
    public Pedido(int idPedido, String direccionEntrega, int distanciaKm) {
        setIdPedido(idPedido);
        setDireccionEntrega(direccionEntrega);
        setDistanciaKm(distanciaKm);
    }

    //sets
    public void setDireccionEntrega(String direccionEntrega) {
        if (direccionEntrega != null && !direccionEntrega.isEmpty()) {
            this.direccionEntrega = direccionEntrega;
        }
    }
    public void setIdPedido(int idPedido) {
        if (idPedido > 0) {
            this.idPedido = idPedido;
        }
    }
    public void setDistanciaKm(int distanciaKm) {
        if (distanciaKm > 0) {
            this.distanciaKm = distanciaKm;
        }
    }

    //gets
    public int getIdPedido() {
        return idPedido;
    }
    public String getDireccionEntrega() {
        return direccionEntrega;
    }
    public int getDistanciaKm() {return distanciaKm;}

    //metodo solicitado y sobrecarga semana 1
    public void asignarRepartidor(Repartidor repartidor) {
        System.out.println("Repartidor esta siendo asignado...");
    }

    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Repartidor esta siendo asignado...");
    }


    //metodos solicitados
    public void mostrarResumen(){}
    public abstract void calcularTiempoEntrega();




    @Override
    public String toString() {
        return  "idPedido:" + idPedido +
                ", Direccion de Entrega:'" + direccionEntrega + '\''+"\n";
    }
}
