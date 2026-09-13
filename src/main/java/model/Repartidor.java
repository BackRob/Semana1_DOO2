package model;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class Repartidor extends Persona implements Runnable {
    private final ZonaDeCarga zonaDeCarga;
    private int contadorpedidos;

    public Repartidor(String nombre, LocalDate fechaNacimiento, String rut, ZonaDeCarga zonaDeCarga) {
        super(nombre, fechaNacimiento, rut);
        this.zonaDeCarga = zonaDeCarga;
        contadorpedidos = 0;
    }

    @Override
    public void run() {
        Pedido pedido;
        while ((pedido = zonaDeCarga.retirarPedido()) != null) {
            try {
                pedido.setEstado(EstadoPedido.EN_REPARTO);
                System.out.println("Entregando pedido: " + pedido + " Por: " + getNombre());
                Thread.sleep(tiempoAleatorio());
                System.out.println("Repartidor " + getNombre() + " llegara en: " + pedido.calcularTiempoEntrega() + "min.");
                System.out.println();
                Thread.sleep(tiempoAleatorio());
                System.out.println("Repartidor " + getNombre() + " en punto de destino");
                Thread.sleep(tiempoAleatorio());
                System.out.println("Entregando paquete Numero " + pedido.getIdPedido() + "...");
                Thread.sleep(tiempoAleatorio());
                pedido.setEstado(EstadoPedido.ENTREGADO);
                contadorpedidos++;
                System.out.println("Paquete Numero " + pedido.getIdPedido() + " entregado");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(getNombre() + " finaliza su jornada.");
                return;
            }
        }
        System.out.println(getNombre() + " no encontro mas pedidos en la zona de carga.");
    }

    public int getContadorpedidos() {return contadorpedidos;}

    private int tiempoAleatorio() {
        return ThreadLocalRandom.current().nextInt(1000, 3001);
    }

    @Override
    public String toString() {
        return "Repartidor{" + super.toString() + "pedidos entregados=" + contadorpedidos + '}';
    }
}
