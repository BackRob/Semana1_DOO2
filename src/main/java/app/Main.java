package app;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.Repartidor;
import model.ZonaDeCarga;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        //zona de carga
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

        //Repartidores
        Repartidor r1 = new Repartidor("Juan Pérez",LocalDate.of(1995,5,12),"12.345.678-9", zonaDeCarga);
        Repartidor r2 = new Repartidor("María González",LocalDate.of(1998,11,3),"18.765.432-1", zonaDeCarga);
        Repartidor r3 = new Repartidor("Carlos Soto",LocalDate.of(1992,7,20),"15.987.654-3", zonaDeCarga);
        Repartidor r4 = new Repartidor("Carlos roberto",LocalDate.of(1992,7,20),"15.987.654-3", zonaDeCarga);

        //Pedidos
        // PedidoComida
        PedidoComida pc1 = new PedidoComida("Av. Central #101", 3);
        PedidoComida pc2 = new PedidoComida("Los Robles #245", 5);
        PedidoComida pc3 = new PedidoComida("San Martín #87", 2);
        PedidoComida pc4 = new PedidoComida("Las Flores #900", 7);
        PedidoComida pc5 = new PedidoComida("Pje. Norte #12", 4);
        PedidoComida pc6 = new PedidoComida("Colón #1500", 8);
        PedidoComida pc7 = new PedidoComida("Pedro Aguirre Cerda #500", 6);
        PedidoComida pc8 = new PedidoComida("Av. La Paz #333", 2);
        PedidoComida pc9 = new PedidoComida("Los Pinos #111", 9);
        PedidoComida pc10 = new PedidoComida("Santa Rosa #444", 5);

// PedidoEncomienda
        PedidoEncomienda pe1 = new PedidoEncomienda("Av. Central #210", 4);
        PedidoEncomienda pe2 = new PedidoEncomienda("Los Olmos #350", 7);
        PedidoEncomienda pe3 = new PedidoEncomienda("San Diego #123", 5);
        PedidoEncomienda pe4 = new PedidoEncomienda("La Estrella #901", 6);
        PedidoEncomienda pe5 = new PedidoEncomienda("Pje. Sur #80", 3);
        PedidoEncomienda pe6 = new PedidoEncomienda("Arturo Prat #200", 10);
        PedidoEncomienda pe7 = new PedidoEncomienda("Camino Real #456", 8);
        PedidoEncomienda pe8 = new PedidoEncomienda("Las Acacias #77", 4);
        PedidoEncomienda pe9 = new PedidoEncomienda("Los Alerces #19", 11);
        PedidoEncomienda pe10 = new PedidoEncomienda("República #600", 5);

// PedidoExpress
        PedidoExpress px1 = new PedidoExpress("Av. Libertad #10", 2);
        PedidoExpress px2 = new PedidoExpress("Las Camelias #250", 5);
        PedidoExpress px3 = new PedidoExpress("San Joaquín #700", 3);
        PedidoExpress px4 = new PedidoExpress("Manuel Rodríguez #120", 6);
        PedidoExpress px5 = new PedidoExpress("Av. Matta #333", 4);
        PedidoExpress px6 = new PedidoExpress("Ecuador #450", 8);
        PedidoExpress px7 = new PedidoExpress("Franklin #99", 7);
        PedidoExpress px8 = new PedidoExpress("Los Canelos #22", 2);
        PedidoExpress px9 = new PedidoExpress("Av. Grecia #1800", 9);
        PedidoExpress px10 = new PedidoExpress("Macul #808", 5);

        Pedido[] pedidos = {
                pc1, pc2, pc3, pc4, pc5, pc6, pc7, pc8, pc9, pc10,
                pe1, pe2, pe3, pe4, pe5, pe6, pe7, pe8, pe9, pe10,
                px1, px2, px3, px4, px5, px6, px7, px8, px9, px10
        };
        for (Pedido pedido : pedidos) {
            zonaDeCarga.agregarPedido(pedido);
        }

        //hilos de los repartidores
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(r1);
        executor.submit(r2);
        executor.submit(r3);
        executor.submit(r4);

        executor.shutdown();
        try {
            while (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                //sigue esperando hasta que los repartidores terminen
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Verificar asignacion de pedidos bien r1:"+r1.getContadorpedidos());
        System.out.println("Verificar asignacion de pedidos bien r2:"+r2.getContadorpedidos());
        System.out.println("Verificar asignacion de pedidos bien r3:"+r3.getContadorpedidos());
        System.out.println("Verificar asignacion de pedidos bien r4:"+r4.getContadorpedidos());
    }
}
