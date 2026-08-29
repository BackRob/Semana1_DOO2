package Services;


import interfaces.Despachable;
import model.Repartidor;

import java.util.ArrayList;

public class ControladorDeEnvios {
    //atributos
    private static ControladorDeEnvios instancia;
    private static ArrayList<Despachable> listaDespachable;
    private static ArrayList<Repartidor> listaRepartidor;



    private ControladorDeEnvios() {// Constructor privado
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

    //Agregar Pedido Despachable
    public void agregarPedido(Despachable pedido){
        if (pedido == null) {
            System.out.println("Pedido Nulo");
            return;
        }
        if (listaDespachable.contains(pedido)){
            System.out.println("Pedido ya existe");
            return;
        }
        listaDespachable.add(pedido);
        System.out.println("Pedido agregado correctamente");
    }

    //Recorrer repartidores disponibles filtrando por mochila
    public Repartidor buscarRepartidorLibre(boolean requiereMochilaTermica){
        for (Repartidor repartidor : listaRepartidor){
            if (repartidor.getAsignado()){
                continue;
            }
            if(requiereMochilaTermica){
                System.out.println("Validando mochila termica... OK");
                if (repartidor.getMochilaTermica()){
                    return repartidor;
                }
            }else{
                return  repartidor;
            }
        }
        return null;
    }


    //Agregar pedido y con asignacion manual
    public void agregarYAsignarPedidoManual(Despachable pedido, String repartidor){
        agregarPedido(pedido);
        pedido.asignarRepartidor(repartidor);
    }

    //Agregar pedido y con asignacion automatica
    public void agregarYAsignarPedidoAuto(Despachable pedido){
        agregarPedido(pedido);
        pedido.asignarRepartidor();
    }


}