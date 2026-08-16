package app;

import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.Repartidor;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        //pedido de comida
        Repartidor repartidor1 = new Repartidor("Brian Vallejos", LocalDate.of(2001,02,16),"20.647.205-7",true);
        PedidoComida pedidoComida1 = new PedidoComida(1,"Volcan Conchali #094");
        pedidoComida1.asignarRepartidor(repartidor1);

        //pedido de emcomienda
        PedidoEncomienda pedidoEncomienda1 = new PedidoEncomienda(2,"Volcan quilpue #024");
        pedidoEncomienda1.asignarRepartidor("Juan Gonzales");

        //pedido express
        PedidoExpress pedidoExpress1 = new PedidoExpress(3,"trinidad #024");
        pedidoExpress1.asignarRepartidor("kiko lopez");
    }

}