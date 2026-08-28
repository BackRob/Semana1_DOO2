package Services;

public class ControladorDeEnvios {

    private static ControladorDeEnvios instancia;

    private ControladorDeEnvios() {
        // Constructor privado
    }

    public static ControladorDeEnvios getInstancia() {
        if (instancia == null) {
            instancia = new ControladorDeEnvios();
        }
        return instancia;
    }
}