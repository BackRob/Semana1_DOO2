package model;

import dao.EntregaDAO;
import dao.PedidoDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public class Repartidor extends Persona implements Runnable {
    private final ZonaDeCarga zonaDeCarga;
    private int contadorpedidos;
    private int id; //id de la tabla repartidor

    public Repartidor(String nombre, LocalDate fechaNacimiento, String rut, ZonaDeCarga zonaDeCarga) {
        super(nombre, fechaNacimiento, rut);
        this.zonaDeCarga = zonaDeCarga;
        contadorpedidos = 0;
    }

    //constructor semana 7, para los repartidores que vienen de la base de datos (solo id y nombre)
    public Repartidor(int id, String nombre) {
        super(nombre, null, null);
        this.id = id;
        this.zonaDeCarga = ZonaDeCarga.getInstance();
        contadorpedidos = 0;
    }

    @Override
    public void run() {
        PedidoDAO pedidoDAO = new PedidoDAO();
        EntregaDAO entregaDAO = new EntregaDAO();
        Pedido pedido;
        while ((pedido = zonaDeCarga.retirarPedido()) != null) {
            try {
                pedido.setEstado(EstadoPedido.EN_REPARTO);
                //se registra la entrega en la BD y se actualiza el estado del pedido
                entregaDAO.guardar(new Entrega(pedido.getIdPedido(), id, LocalDate.now(), LocalTime.now()));
                pedidoDAO.actualizarEstado(pedido.getIdPedido(), EstadoPedido.EN_REPARTO);
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
                pedidoDAO.actualizarEstado(pedido.getIdPedido(), EstadoPedido.ENTREGADO);
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
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    private int tiempoAleatorio() {
        return ThreadLocalRandom.current().nextInt(1000, 3001);
    }

    @Override
    public String toString() {
        return "Repartidor{" + super.toString() + "pedidos entregados=" + contadorpedidos + '}';
    }
}
