package interfaces;

public interface Despachable {
    void despachar();
    void asignarRepartidor();
    void asignarRepartidor(String repartidor);
    String getEstado();

}
