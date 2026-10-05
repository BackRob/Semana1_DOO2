package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ZonaDeCarga {
    private final BlockingQueue<Pedido> pedidosPendientes = new LinkedBlockingQueue<>();
    //lista comun con todos los pedidos registrados, la usan las ventanas para mostrar la tabla
    private final List<Pedido> listaPedidos = new ArrayList<>();
    private static final ZonaDeCarga instance =  new ZonaDeCarga();


    public static ZonaDeCarga getInstance() {
        return instance;
    }

    public synchronized void agregarPedido(Pedido pedido) {
        pedidosPendientes.offer(pedido);
        listaPedidos.add(pedido);
        System.out.println("Pedido " + pedido.getIdPedido() + " ingresado a la zona de carga.");
    }

    //semana 8: vacia la cola y la vuelve a llenar con los pedidos PENDIENTE de la BD
    //asi no quedan pedidos que se editaron o eliminaron desde la ventana de pedidos
    public synchronized void recargar(List<Pedido> pendientes) {
        pedidosPendientes.clear();
        pedidosPendientes.addAll(pendientes);
    }

    public synchronized Pedido retirarPedido() {
        return pedidosPendientes.poll();
    }

    public synchronized int cantidadPendientes() {
        return pedidosPendientes.size();
    }

    public synchronized boolean estaVacia() {
        return pedidosPendientes.isEmpty();
    }

    //devuelve una copia para que la tabla no choque con los hilos de los repartidores
    public synchronized List<Pedido> getPedidos() {
        return new ArrayList<>(listaPedidos);
    }

    //valida que no se repita el id al registrar
    public synchronized boolean existePedido(int idPedido) {
        for (Pedido pedido : listaPedidos) {
            if (pedido.getIdPedido() == idPedido) {
                return true;
            }
        }
        return false;
    }
}
