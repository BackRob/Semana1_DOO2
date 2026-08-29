package app;

import Services.ControladorDeEnvios;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.Repartidor;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        //singleton
        ControladorDeEnvios controladorEnvios = ControladorDeEnvios.getControladorEnvios();


        //Pedidos
        PedidoComida pComida = new PedidoComida(1, "laguna Lagunilla #12", 4);
        PedidoEncomienda pEncomienda = new PedidoEncomienda(2, "Volvan Volvanico #213", 6);
        PedidoExpress pExpress = new PedidoExpress(3, "vicuna mackena #4356", 7);

        //Repartidores
        Repartidor r1 = new Repartidor("Juan Pérez",LocalDate.of(1995,5,12),"12.345.678-9", false);
        Repartidor r2 = new Repartidor("María González",LocalDate.of(1998,11,3),"18.765.432-1", false);
        Repartidor r3 = new Repartidor("Carlos Soto",LocalDate.of(1992,7,20),"15.987.654-3",true);
        Repartidor r4 = new Repartidor("Carlos roberto",LocalDate.of(1992,7,20),"15.987.654-3",false);

        //agregar repartidores
        controladorEnvios.agregarRepartidor(r1);
        controladorEnvios.agregarRepartidor(r2);
        controladorEnvios.agregarRepartidor(r3);
        controladorEnvios.agregarRepartidor(r4);

        //Asignar Automatico sin mochila
        controladorEnvios.agregarYAsignarPedidoAuto(pEncomienda);

        //Asignar Automatico con mochila
        controladorEnvios.agregarYAsignarPedidoAuto(pComida);


        //Asignar manual
        controladorEnvios.agregarYAsignarPedidoManual(pComida, "Brian Vallejos"); // pedido duplicado, ya no se vuelve a gestionar.

    }
}