package Services;



import model.Pedido;
import model.Repartidor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ControladorDeEnvios implements Runnable  {
    //atributos
    private final ExecutorService executor;
    private final List<Repartidor> listaRepartidor;
    private final BlockingQueue<Pedido> pedidos;
    private final AtomicInteger contadorPedidos = new AtomicInteger(0); // contador seguro para hilos
    private final AtomicInteger contadorPedidosEntregados = new AtomicInteger(0); // contador seguro para hilos
    private static ControladorDeEnvios instancia;


    private ControladorDeEnvios() {// Constructor privado
        executor = Executors.newCachedThreadPool();
        listaRepartidor = Collections.synchronizedList(new ArrayList<>());
        pedidos = new LinkedBlockingQueue<>();
    }
    //singleton
    public synchronized static ControladorDeEnvios getControladorEnvios() {
        if (instancia == null) {
            instancia = new ControladorDeEnvios();
            instancia.executor.submit(instancia);
        }
        return instancia;
    }


    //Agregar repartidor lista global (synchronized pensando en futuro hilo creador de repartidor)
    public synchronized void agregarRepartidor(Repartidor repartidor){
        listaRepartidor.add(repartidor);
        executor.submit(repartidor);
    }
    //AGREGAR PEDIDO
    public void agregarPedido(Pedido pedido) {
        pedidos.offer(pedido);
        contadorPedidos.incrementAndGet();
    }


    //recorrer repartidores para asignar parejos repartir parejo
    public synchronized Repartidor asignarRepartidorMenosPedidos(Pedido pedido) {
        Repartidor repartidorMenor = primerRepartidorAsignable(pedido);
        if (repartidorMenor == null) {
            System.out.println("NO hay repartidor registrado para repartir dicho pedido");
            return null;
        }
        for (Repartidor repartidor : listaRepartidor) {
            if (repartidor.sePuedeRepartir(pedido)&&repartidorMenor.getContadorpedidos()>repartidor.getContadorpedidos()){
                repartidorMenor = repartidor;
            }
        }
        return repartidorMenor;
    }

    //Obtener el primero que si se pueda asinar
    public Repartidor primerRepartidorAsignable(Pedido pedido) {
        for (Repartidor repartidor : listaRepartidor) {
            if (repartidor.sePuedeRepartir(pedido)) {
                return repartidor;
            }
        }
        return null;
    }

    public synchronized void sumarContadorAtomico(){
        contadorPedidosEntregados.incrementAndGet();
        System.out.println("--Contador Pedidos Entregados: "+contadorPedidosEntregados.get()+"--");
        System.out.println("--Contador Pedidos: "+contadorPedidos.get()+"--");
        if (contadorPedidos.get()>0 && contadorPedidosEntregados.get() == contadorPedidos.get()) {
            System.out.println("Todos Los pedidos Entregados");
            executor.shutdownNow();
        }
    }



    @Override
    public void run() {
        synchronized (this) {
            if (listaRepartidor.isEmpty()){
                try {
                    wait();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        while (true) {
            try {
                Pedido pedido = pedidos.take();
                Repartidor repartidor = asignarRepartidorMenosPedidos(pedido);
                if (repartidor != null) {
                    repartidor.agregarPedido(pedido);
                    System.out.println("Pedido Assignado al Repartidor: "+repartidor.getNombre());
                }else{
                    System.out.println("No hay repartidor valido para asignar pedido, pedido cancelado");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }
    }
}