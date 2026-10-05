package vista;

import javax.swing.table.DefaultTableModel;

/**
 * Ayuda para las JTable de las ventanas.
 */
public class Tablas {

    //modelo de tabla donde las celdas no se pueden editar (solo se seleccionan)
    public static DefaultTableModel modeloNoEditable(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
