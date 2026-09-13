package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ZonaDeCarga {
    private final BlockingQueue<Pedido> pedidosPendientes = new LinkedBlockingQueue<>();

    public synchronized void agregarPedido(Pedido pedido) {
        pedidosPendientes.offer(pedido);
        System.out.println("Pedido " + pedido.getIdPedido() + " ingresado a la zona de carga.");
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
}
