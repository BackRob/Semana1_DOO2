package Services;


import interfaces.Despachable;
import model.Repartidor;

import java.util.ArrayList;

public class ControladorDeEnvios  {
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

    //gets
    public ArrayList<Despachable> getListaDespachable() {
        return new ArrayList<>(listaDespachable);
    }



    //agregar repartidor
    public void agregarRepartidor(Repartidor repartidor) {
        if(repartidor == null){
            System.out.println("Repartidor nulo");
            return;
        }
        if (listaRepartidor.contains(repartidor)) {
            System.out.println("ERROR al agregar repartidor con rut: "+repartidor.getRut()+" ya existe");
        }else {
            listaRepartidor.add(repartidor);
        }
    }

    //Agregar Pedido Despachable
    private void agregarPedido(Despachable pedido){
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

        if (requiereMochilaTermica){
            System.out.println("Validando mochila termica...");
        }
        for (Repartidor repartidor : listaRepartidor){
            if (repartidor.getAsignado()){
                continue;
            }
            if(requiereMochilaTermica){
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
    public void agregarYAsignarPedido(Despachable pedido, String repartidor){
        agregarPedido(pedido);
            pedido.asignarRepartidor(repartidor);
    }

    //Agregar pedido y con asignacion automatica
    public void agregarYAsignarPedido(Despachable pedido){
        agregarPedido(pedido);
            pedido.asignarRepartidor();
    }


}