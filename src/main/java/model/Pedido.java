package model;

public abstract class Pedido {
    private int idPedido;
    private String direccionEntrega;

    //constructor
    public Pedido(int idPedido, String direccionEntrega) {
        setIdPedido(idPedido);
        setDireccionEntrega(direccionEntrega);
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

    //gets
    public int getIdPedido() {
        return idPedido;
    }
    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    //metodo solicitado y sobrecarga
    public void asignarRepartidor(Repartidor repartidor) {
        System.out.println("Repartidor esta siendo asignado...");
    }

    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Repartidor esta siendo asignado...");
    }

    @Override
    public String toString() {
        return  "idPedido:" + idPedido +
                ", Direccion de Entrega:'" + direccionEntrega + '\'';
    }
}
