package Services;


import interfaces.Despachable;
import model.Pedido;
import model.Repartidor;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ControladorDeEnvios implements Runnable  {
    //atributos
    private final ExecutorService executor;
    private final PriorityBlockingQueue<Pedido> colaPedidos;
    private static final AtomicInteger contadorGlobal = new AtomicInteger(1); // contador seguro para hilos
    private static ControladorDeEnvios instancia;
    private final ArrayList<Despachable> listaDespachable;
    private final ArrayList<Repartidor> listaRepartidor;



    private ControladorDeEnvios() {// Constructor privado
        executor = Executors.newCachedThreadPool();
        colaPedidos = new PriorityBlockingQueue<>();
        listaDespachable = new ArrayList<>();
        listaRepartidor = new ArrayList<>();
    }
    //singleton
    public static ControladorDeEnvios getControladorEnvios() {
        if (instancia == null) {
            instancia = new ControladorDeEnvios();
        }
        return instancia;
    }


    @Override
    public void run() {
        Pedido pedidoRecorrido;
        Repartidor repartidor;


        while (true) {
            try {
                pedidoRecorrido = colaPedidos.take();
                synchronized (this) {
                    while (true) {
                        agregarYAsignarPedido(pedidoRecorrido);
                        if (pedidoRecorrido.getRepartidor() != null) {
                            System.out.println("cargando pedido:" + pedidoRecorrido.getIdPedido() + "recorrido...");
                            break;
                        } else {

                                wait();

                        }
                    }
                }


            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }
    }




    //Agregar a la cola
    public void agregarALaCola(Pedido pedido) {
        if (pedido != null) {
            System.out.println("Encolando pedido " + pedido.getIdPedido());
            colaPedidos.put(pedido);
        }
    }

    //gets
    public ArrayList<Despachable> getListaDespachable() {
        return new ArrayList<>(listaDespachable);
    }

    //agregar repartidor/// autoasignar
    public void agregarRepartidor(Repartidor repartidor) {
        if(repartidor == null){
            System.out.println("Repartidor nulo");
            return;
        }
        if (listaRepartidor.contains(repartidor)) {
            System.out.println("ERROR al agregar repartidor con rut: "+repartidor.getRut()+" ya existe");
        }else {
            listaRepartidor.add(repartidor);
            executor.submit(repartidor);

        }
    }


    //Agregar Pedido Despachable
    private boolean agregarPedido(Despachable pedido){
        if (pedido == null) {
            System.out.println("Pedido Nulo");
            return false;
        }
        if (listaDespachable.contains(pedido)){
            System.out.println("Pedido: "+pedido.getIdPedido()+" en historial");
            return true;
        }else {
            listaDespachable.add(pedido);
            System.out.println("Pedido agregado correctamente");
            return true;
        }
    }

    //Recorrer repartidores disponibles filtrando por mochila
    public Repartidor buscarRepartidorLibre(boolean requiereMochilaTermica){

        if (requiereMochilaTermica){
            System.out.println("Validando mochila termica...");
        }
        for (Repartidor repartidor : listaRepartidor){
            if (!repartidor.estaDisponible()){
                continue;
            }
            if(requiereMochilaTermica){
                if (repartidor.getMochilaTermica()){
                    return repartidor;
                }
            }else{
                return  repartidor;
            }
        }
        return null;
    }

    public boolean hayRepartidorLibre(){

        for (Repartidor repartidor : listaRepartidor){

            System.out.println(
                    repartidor.getNombre()
                            + " asignado="
                            + repartidor.getAsignado()
            );

            if (!repartidor.estaDisponible()){
                continue;
            }

            return true;
        }

        return false;
    }




    //Agregar pedido y con asignacion manual
    public void agregarYAsignarPedido(Despachable pedido, String repartidor){
        if (repartidor == null) {
            System.out.println("Repartidor Nulo");
            return;
        }

        if (agregarPedido(pedido)) {
            pedido.asignarRepartidor(repartidor);
        }
    }

    //Agregar pedido y con asignacion automatica
    public void agregarYAsignarPedido(Despachable pedido){
        if (agregarPedido(pedido)) {
            pedido.asignarRepartidor();

        }
    }


}