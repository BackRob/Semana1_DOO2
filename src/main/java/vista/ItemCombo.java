package vista;

/**
 * Elemento para los JComboBox: muestra un texto legible (ej: "3 - Juan Perez")
 * pero guarda internamente el id de la base de datos.
 */
public class ItemCombo {
    private final int id;
    private final String texto;

    public ItemCombo(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public int getId() {return id;}

    //el JComboBox usa toString() para saber que mostrar
    @Override
    public String toString() {
        return texto;
    }
}
