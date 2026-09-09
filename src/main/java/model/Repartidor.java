package model;

import java.time.LocalDate;

import static Services.ControladorDeEnvios.getControladorEnvios;


public class Repartidor extends Persona implements Runnable {
    private boolean mochilaTermica;
    private boolean asignado;
    private Pedido pedido;

    public Repartidor(String nombre, LocalDate fechaNacimiento, String rut,Boolean mochilaTermica) {
        super(nombre, fechaNacimiento, rut);
        setMochilaTermica(mochilaTermica);
        asignado = false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if(pedido==null){
                    Thread.sleep(1000);
                }else{
                    System.out.println("Recepcionando pedido");
                    Thread.sleep(1000);
                    System.out.println("Pedido recibido por el Repartidor");
                    Thread.sleep(1000);
                    System.out.println("llendo a entregar");
                    Thread.sleep((long) (1000*pedido.calcularTiempoEntrega()));
                    if(pedido!=null){
                        entregarPedido();
                    }

                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }


    public Pedido getPedido() {
        return pedido;
    }
    public void setPedido(Pedido pedido) {
        if (pedido == null) {
            System.err.println("Pedido no puede ser nulo.");
            return;
        }
        if (this.pedido!=null){ //solo cuando ya entrego se le puede cambiar el pedido
            System.err.println("Repartidor ocupado con un pedido");
            return;
        }
        this.pedido = pedido;
        asignado = true;
    }

    public void setMochilaTermica(boolean mochilaTermica) {
        this.mochilaTermica = mochilaTermica;
    }
    public boolean getMochilaTermica() {
        return mochilaTermica;
    }

    public void setAsignado(boolean asignado) {this.asignado = asignado;}
    public boolean getAsignado() {return asignado;}


    //AutoAsignacion
    public void agregarALaCola(){
        System.out.println("Agregando al repartidor "+getNombre()+ " A La Cola");
        synchronized (getControladorEnvios()) {
            getControladorEnvios().agregarRepartidor(this);
            getControladorEnvios().notifyAll();

        }


    }


    //poo
    public boolean estaDisponible(){
        return !getAsignado();
    }

    //cancelacion del pedido
    public synchronized void entregaCancelada(){
        if (pedido!=null){
            pedido = null;
            asignado = false;
            System.err.println("Se ha cancelado con exito la entrega del repartidor: "+getNombre());
        }
    }
    //Entrega del pedido
    public synchronized void entregarPedido(){
        pedido.entregar();
        System.err.println("Se ha entregado el pedido Numero: "+pedido.getIdPedido()+ "con exito");
        pedido = null;
        asignado = false;
        synchronized (getControladorEnvios()) {getControladorEnvios().notify();}

    }

    @Override
    public String toString() {
        if (mochilaTermica) {
            return "Repartidor{" + super.toString() +
                    "mochila Termica= SI";

        }else{
            return "Repartidor{" + super.toString() +
                    "mochila Termica= NO";
        }
    }





}
