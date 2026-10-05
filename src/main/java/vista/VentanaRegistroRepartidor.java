package vista;

import dao.RepartidorDAO;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Semana 8: gestion de repartidores (agregar, listar, editar y eliminar).
 * Se selecciona un repartidor en la tabla para editarlo o eliminarlo.
 */
public class VentanaRegistroRepartidor extends JFrame {
    private JPanel jPaneRepartidor;
    private JTextField jTextNombre;
    private JTable tablaRepartidores;
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnAtras;

    private final DefaultTableModel modeloTabla;
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    public VentanaRegistroRepartidor() throws HeadlessException {
        setTitle("SpeedFast - Repartidores");
        setContentPane(jPaneRepartidor);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        modeloTabla = Tablas.modeloNoEditable(new String[]{"ID", "Nombre"});
        tablaRepartidores.setModel(modeloTabla);
        tablaRepartidores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //al seleccionar una fila se cargan sus datos en el formulario
        tablaRepartidores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregar();
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizar();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        cargarTabla();
    }


    //CREATE
    public void agregar() {
        String nombre = validarNombre();
        if (nombre == null) {
            return;
        }
        try {
            Repartidor repartidor = new Repartidor(0, nombre);
            repartidorDAO.create(repartidor);
            Mensajes.exito(this, "Repartidor " + nombre + " registrado con ID " + repartidor.getId() + ".");
            limpiar();
            cargarTabla();
        } catch (SQLException e) {
            Mensajes.error(this, e.getMessage());
        }
    }

    //UPDATE
    public void actualizar() {
        int id = idSeleccionado();
        if (id == -1) {
            return;
        }
        String nombre = validarNombre();
        if (nombre == null) {
            return;
        }
        try {
            repartidorDAO.update(new Repartidor(id, nombre));
            Mensajes.exito(this, "Repartidor " + id + " actualizado.");
            limpiar();
            cargarTabla();
        } catch (SQLException e) {
            Mensajes.error(this, e.getMessage());
        }
    }

    //DELETE
    public void eliminar() {
        int id = idSeleccionado();
        if (id == -1) {
            return;
        }
        if (!Mensajes.confirmar(this, "¿Seguro que quieres eliminar el repartidor " + id + "?")) {
            return;
        }
        try {
            repartidorDAO.delete(id);
            Mensajes.exito(this, "Repartidor " + id + " eliminado.");
            limpiar();
            cargarTabla();
        } catch (SQLException e) {
            Mensajes.error(this, e.getMessage());
        }
    }

    //READ: llena la tabla con readAll()
    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            for (Repartidor repartidor : repartidorDAO.readAll()) {
                modeloTabla.addRow(new Object[]{repartidor.getId(), repartidor.getNombre()});
            }
        } catch (SQLException e) {
            Mensajes.error(this, e.getMessage());
        }
    }

    //validacion: obligatorio, maximo 100 caracteres (VARCHAR(100)) y solo letras
    private String validarNombre() {
        String nombre = jTextNombre.getText().trim();
        if (nombre.isEmpty()) {
            Mensajes.aviso(this, "El campo Nombre es obligatorio.");
            jTextNombre.requestFocus();
            return null;
        }
        if (nombre.length() > 100) {
            Mensajes.aviso(this, "El nombre no puede tener mas de 100 caracteres.");
            jTextNombre.requestFocus();
            return null;
        }
        if (!nombre.matches("[\\p{L} .'-]+")) {
            Mensajes.aviso(this, "El nombre solo puede tener letras y espacios.");
            jTextNombre.requestFocus();
            return null;
        }
        return nombre;
    }

    //devuelve el id de la fila seleccionada, o -1 si no hay ninguna
    private int idSeleccionado() {
        int fila = tablaRepartidores.getSelectedRow();
        if (fila == -1) {
            Mensajes.aviso(this, "Selecciona un repartidor en la tabla.");
            return -1;
        }
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    private void cargarSeleccion() {
        int fila = tablaRepartidores.getSelectedRow();
        if (fila != -1) {
            jTextNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        }
    }

    private void limpiar() {
        jTextNombre.setText("");
        tablaRepartidores.clearSelection();
        jTextNombre.requestFocus();
    }


}
