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


}