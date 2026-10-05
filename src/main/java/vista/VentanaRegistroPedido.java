package vista;

import dao.PedidoDAO;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Semana 8: gestion de pedidos (agregar, listar con filtros, editar y eliminar).
 * Se selecciona un pedido en la tabla para editarlo o eliminarlo.
 */
public class VentanaRegistroPedido extends JFrame {
    //mismos valores que el ENUM estado de la tabla pedidos
    private static final String[] ESTADOS = {"PENDIENTE", "EN_REPARTO", "ENTREGADO"};
    private static final String TODOS = "TODOS";

    private JPanel jPaneRegistro;
    private JTextField jTextDireccion;
    private JComboBox<TipoPedido> jComboTipo;
    private JComboBox<String> jComboEstado;
    private JComboBox<String> jComboFiltroEstado;
    private JComboBox<String> jComboFiltroTipo;
    private JTable tablaPedidos;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnAtras;

    private final DefaultTableModel modeloTabla;
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    public VentanaRegistroPedido() throws HeadlessException {
        setTitle("SpeedFast - Pedidos");
        setContentPane(jPaneRegistro);
        //solo cierra esta ventana, no toda la app
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //combos del formulario
        jComboTipo.setModel(new DefaultComboBoxModel<>(TipoPedido.values()));
        jComboTipo.setSelectedIndex(-1);
        jComboEstado.setModel(new DefaultComboBoxModel<>(ESTADOS));
        jComboEstado.setSelectedItem("PENDIENTE");

        //combos de filtros, con la opcion TODOS al inicio
        DefaultComboBoxModel<String> filtroEstado = new DefaultComboBoxModel<>();
        filtroEstado.addElement(TODOS);
        for (String estado : ESTADOS) filtroEstado.addElement(estado);
        jComboFiltroEstado.setModel(filtroEstado);

        DefaultComboBoxModel<String> filtroTipo = new DefaultComboBoxModel<>();
        filtroTipo.addElement(TODOS);
        for (TipoPedido tipo : TipoPedido.values()) filtroTipo.addElement(tipo.name());
        jComboFiltroTipo.setModel(filtroTipo);

        modeloTabla = Tablas.modeloNoEditable(new String[]{"ID", "Direccion", "Tipo", "Estado"});
        tablaPedidos.setModel(modeloTabla);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //al seleccionar una fila se cargan sus datos en el formulario
        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        //al cambiar un filtro se recarga la tabla
        jComboFiltroEstado.addActionListener(e -> cargarTabla());
        jComboFiltroTipo.addActionListener(e -> cargarTabla());

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrarPedido();
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarPedido();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPedido();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        //vuelve al menu principal
        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        cargarTabla();
        //pack ajusta el tamaño de la ventana a los componentes, asi no se corta nada
        pack();
        setLocationRelativeTo(null);
    }


    //CREATE
    public void RegistrarPedido() {
        Pedido pedido = leerFormulario(0);
        if (pedido == null) {
            return;
        }
        try {
            pedidoDAO.create(pedido);
            Mensajes.exito(this, "Pedido " + pedido.getIdPedido() + " (" + pedido.getTipo() + ") registrado correctamente.");
            limpiarCampos();
            cargarTabla();
        } catch (SQLException e) {
            Mensajes.error(this, e.getMessage());
        }
    }

    //UPDATE
    public void actualizarPedido() {
        int id = idSeleccionado();
        if (id == -1) {
            return;
        }
        Pedido pedido = leerFormulario(id);
        if (pedido == null) {
            return;
        }
        try {
            pedidoDAO.update(pedido);
            Mensajes.exito(this, "Pedido " + id + " actualizado.");
            limpiarCampos();
            cargarTabla();
        } catch (SQLException e) {
            Mensajes.error(this, e.getMessage());
        }
    }

    //DELETE
    public void eliminarPedido() {
        int id = idSeleccionado();
        if (id == -1) {
            return;
        }
        if (!Mensajes.confirmar(this, "¿Seguro que quieres eliminar el pedido " + id + "?")) {
            return;
        }
        try {
            pedidoDAO.delete(id);
            Mensajes.exito(this, "Pedido " + id + " eliminado.");
            limpiarCampos();
            cargarTabla();
        } catch (SQLException e) {
            Mensajes.error(this, e.getMessage());
        }
    }

    //READ: llena la tabla aplicando los filtros elegidos
    public void cargarTabla() {
        if (modeloTabla == null) {
            return; //todavia se esta armando la ventana
        }
        String estado = filtro(jComboFiltroEstado);
        String tipo = filtro(jComboFiltroTipo);
        modeloTabla.setRowCount(0);
        try {
            for (Pedido pedido : pedidoDAO.readFiltrado(estado, tipo)) {
                modeloTabla.addRow(new Object[]{
                        pedido.getIdPedido(),
                        pedido.getDireccionEntrega(),
                        pedido.getTipo(),
                        pedido.getEstado()
                });
            }
        } catch (SQLException e) {
            Mensajes.error(this, e.getMessage());
        }
    }

    //valida el formulario y arma el pedido, devuelve null si algo esta mal
    private Pedido leerFormulario(int id) {
        String direccion = jTextDireccion.getText().trim();
        if (direccion.isEmpty()) {
            Mensajes.aviso(this, "El campo Direccion es obligatorio.");
            jTextDireccion.requestFocus();
            return null;
        }
        if (direccion.length() > 100) {
            Mensajes.aviso(this, "La direccion no puede tener mas de 100 caracteres.");
            jTextDireccion.requestFocus();
            return null;
        }
        if (jComboTipo.getSelectedIndex() == -1) {
            Mensajes.aviso(this, "Debes seleccionar el Tipo de pedido.");
            jComboTipo.requestFocus();
            return null;
        }
        if (jComboEstado.getSelectedIndex() == -1) {
            Mensajes.aviso(this, "Debes seleccionar el Estado del pedido.");
            jComboEstado.requestFocus();
            return null;
        }

        //se crea el pedido segun el tipo elegido (polimorfismo de semana 4)
        TipoPedido tipo = (TipoPedido) jComboTipo.getSelectedItem();
        Pedido pedido = Pedido.crear(tipo.name(), id, direccion);
        pedido.setEstado((String) jComboEstado.getSelectedItem());
        return pedido;
    }

    //devuelve el id de la fila seleccionada, o -1 si no hay ninguna
    private int idSeleccionado() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila == -1) {
            Mensajes.aviso(this, "Selecciona un pedido en la tabla.");
            return -1;
        }
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    private void cargarSeleccion() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila != -1) {
            jTextDireccion.setText((String) modeloTabla.getValueAt(fila, 1));
            jComboTipo.setSelectedItem(TipoPedido.valueOf((String) modeloTabla.getValueAt(fila, 2)));
            jComboEstado.setSelectedItem(modeloTabla.getValueAt(fila, 3).toString());
        }
    }

    //null si esta en TODOS
    private String filtro(JComboBox<String> combo) {
        String valor = (String) combo.getSelectedItem();
        return (valor == null || valor.equals(TODOS)) ? null : valor;
    }

    //deja el formulario listo para otro pedido
    public void limpiarCampos() {
        jTextDireccion.setText("");
        jComboTipo.setSelectedIndex(-1);
        jComboEstado.setSelectedItem("PENDIENTE");
        tablaPedidos.clearSelection();
        jTextDireccion.requestFocus();
    }


}
