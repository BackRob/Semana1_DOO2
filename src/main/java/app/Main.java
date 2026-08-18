package app;

import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.Repartidor;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        PedidoComida pComida = new PedidoComida(1,"laguna Lagunilla #12",4);
        PedidoEncomienda pEncomienda = new PedidoEncomienda(2, "Volvan Volvanico #213",6);
        PedidoExpress pExpress = new PedidoExpress(3,"vicuna mackena #4356",7);


        pComida.mostrarResumen();
        pEncomienda.mostrarResumen();
        pExpress.mostrarResumen();
    }

}