package vista;

import dao.RepartidorDAO;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//semana 7, formulario para guardar repartidores en la BD
public class VentanaRegistroRepartidor extends JFrame {
    private JPanel jPaneRepartidor;
    private JTextField jTextNombre;
    private JTable tablaRepartidores;
    private JButton btnAtras;
    private JButton btnRegistrar;

    private final DefaultTableModel modeloTabla;
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    public VentanaRegistroRepartidor() throws HeadlessException {
        setTitle("SpeedFast - Registrar Repartidor");
        setContentPane(jPaneRepartidor);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        //tabla con los repartidores que ya estan en la BD
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaRepartidores.setModel(modeloTabla);
        cargarTabla();

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarRepartidor();
            }
        });

        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }


    public void registrarRepartidor() {
        String nombre = jTextNombre.getText();
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El campo Nombre no puede estar vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jTextNombre.requestFocus();
            return;
        }

        //el id lo pone la BD
        Repartidor repartidor = new Repartidor(0, nombre.trim());
        if (!repartidorDAO.guardar(repartidor)) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el repartidor en la base de datos.\nRevisa que MySQL este encendido y los datos de ConexionBD.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Repartidor " + repartidor.getNombre() + " registrado con ID " + repartidor.getId() + ".",
                "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        jTextNombre.setText("");
        cargarTabla();
    }

    //usa listarTodos() del RepartidorDAO
    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Repartidor repartidor : repartidorDAO.listarTodos()) {
            modeloTabla.addRow(new Object[]{repartidor.getId(), repartidor.getNombre()});
        }
    }


}
