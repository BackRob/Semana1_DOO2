package model;

import Services.ControladorDeEnvios;

import java.time.LocalDate;

import static Services.ControladorDeEnvios.getControladorEnvios;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Repartidor extends Persona implements Runnable {
    private final PriorityBlockingQueue<Pedido> pedidos;
    private Pedido pedidoActual;
    private boolean mochilaTermica;
    private boolean asignado;
    private int contadorpedidos;

    public Repartidor(String nombre, LocalDate fechaNacimiento, String rut,Boolean mochilaTermica) {
        super(nombre, fechaNacimiento, rut);
        synchronized (getControladorEnvios()) {
            getControladorEnvios().notifyAll();
        }
        contadorpedidos=0;
        setMochilaTermica(mochilaTermica);
        asignado = false;
        pedidoActual  = null;
        pedidos = new PriorityBlockingQueue<>();
        getControladorEnvios().agregarRepartidor(this);
    }

    @Override
    public void run() {
        while (true) {
            try {

                setPedidoActual(pedidos.take());
                System.out.println("Entregando pedido: "+pedidoActual+ "Por: "+getNombre());
                Thread.sleep(tiempoAleatorio());
                System.out.println("Repartidor " +getNombre()+" llegara en: "+pedidoActual.calcularTiempoEntrega());
                System.out.println();
                Thread.sleep(tiempoAleatorio());
                System.out.println("Repartidor " +getNombre()+" en punto de destino");
                Thread.sleep(tiempoAleatorio());
                System.out.println("Entregando paquete Numero" + pedidoActual.getIdPedido()+"...");
                Thread.sleep(tiempoAleatorio());
                System.out.println("Paquete Numero " + pedidoActual.getIdPedido()+" entregado");
                entregarPedido();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(
                        getNombre() + " finaliza su jornada."
                );
                break;
            }
        }
    }

    public void setMochilaTermica(boolean mochilaTermica) {
        this.mochilaTermica = mochilaTermica;
    }
    public int getContadorpedidos() {return contadorpedidos;}
    public void setAsignado(boolean asignado) {this.asignado = asignado;}
    public boolean getAsignado() {return asignado;}

    public Pedido getPedidoActual() {
        return pedidoActual;
    }
    private void setPedidoActual(Pedido pedidoActual) {
        this.pedidoActual = pedidoActual;
    }

    //AutoAsignacion
    public void agregarALaCola(){
        System.out.println("Agregando al repartidor "+getNombre()+ " al servicio");
        getControladorEnvios().agregarRepartidor(this);
        if (mochilaTermica) {
            synchronized (getControladorEnvios()) {
                getControladorEnvios().notifyAll();
            }
        }
    }

    //cancelacion del pedido
    public synchronized void entregaCancelada(){
        if (pedidoActual!=null){
            pedidoActual = null;
            asignado = false;
            System.out.println("Se ha cancelado con exito la entrega del repartidor: "+getNombre());
        }
    }
    //Entrega del pedido
    public synchronized void entregarPedido(){
        pedidoActual.entregar();
        System.out.println("Se ha entregado el pedido Numero: "+pedidoActual.getIdPedido()+ " con exito");
        pedidoActual = null;
        asignado = false;

        getControladorEnvios().sumarContadorAtomico();
    }

    //consultar numeros de pedidos
    public int cantidadPedidosCola(){
        return pedidos.size();
    }

    //agregar pedido a la lista bloqueante
    public void agregarPedido(Pedido pedido){
        pedidos.add(pedido);
        contadorpedidos++;
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

    //puedes repartir?
    public boolean sePuedeRepartir(Pedido pedido) {
        return !pedido.necesitaMochila()||mochilaTermica;
    }

    private int tiempoAleatorio() {
        return ThreadLocalRandom.current().nextInt(1000, 5001);
    }




}
