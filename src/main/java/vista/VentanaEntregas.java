package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Entrega;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Semana 8: gestion de entregas (agregar, listar con filtros, editar y eliminar).
 * El pedido y el repartidor se eligen en JComboBox cargados desde la BD:
 * se muestra "id - direccion" o "id - nombre" pero internamente se guarda el id (ItemCombo).
 */
public class VentanaEntregas extends JFrame {
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private JPanel jPaneEntregas;
    private JComboBox<ItemCombo> jComboPedido;
    private JComboBox<ItemCombo> jComboRepartidor;
    private JComboBox<ItemCombo> jComboFiltroPedido;
    private JComboBox<ItemCombo> jComboFiltroRepartidor;
    private JTextField jTextFecha;
    private JTextField jTextHora;
    private JTable tablaEntregas;
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnAtras;

    private final DefaultTableModel modeloTabla;
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    //evita recargar la tabla mientras se llenan los combos de filtro
    private boolean cargandoCombos = false;

    public VentanaEntregas() throws HeadlessException {
        setTitle("SpeedFast - Entregas");
        setContentPane(jPaneEntregas);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        modeloTabla = Tablas.modeloNoEditable(new String[]{"ID", "Pedido", "Repartidor", "Fecha", "Hora"});
        tablaEntregas.setModel(modeloTabla);
        tablaEntregas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //al seleccionar una fila se cargan sus datos en el formulario
        tablaEntregas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        //al cambiar un filtro se recarga la tabla
        jComboFiltroPedido.addActionListener(e -> {
            if (!cargandoCombos) cargarTabla();
        });
        jComboFiltroRepartidor.addActionListener(e -> {
            if (!cargandoCombos) cargarTabla();
        });

        //si se crearon/editaron/eliminaron pedidos o repartidores en otra ventana,
        //al volver a esta se refrescan los combos
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                //sin mensaje de error aqui: si fallara, cerrar el mensaje volveria a activar la ventana
                cargarCombos(false);
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

        cargarCombos(true);
        limpiar();
        cargarTabla();
        pack();
        setLocationRelativeTo(null);
    }


    //CREATE
    public void agregar() {
        Entrega entrega = leerFormulario();
        if (entrega == null) {
            return;
        }
        try {
            entregaDAO.create(entrega);
            Mensajes.exito(this, "Entrega " + entrega.getId() + " registrada.");
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
        Entrega entrega = leerFormulario();
        if (entrega == null) {
            return;
        }
        entrega.setId(id);
        try {
            entregaDAO.update(entrega);
            Mensajes.exito(this, "Entrega " + id + " actualizada.");
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
        if (!Mensajes.confirmar(this, "¿Seguro que quieres eliminar la entrega " + id + "?")) {
            return;
        }
        try {
            entregaDAO.delete(id);
            Mensajes.exito(this, "Entrega " + id + " eliminada.");
            limpiar();
            cargarTabla();
        } catch (SQLException e) {
            Mensajes.error(this, e.getMessage());
        }
    }

    //READ: llena la tabla aplicando los filtros por pedido o repartidor
    public void cargarTabla() {
        Integer idPedido = idFiltro(jComboFiltroPedido);
        Integer idRepartidor = idFiltro(jComboFiltroRepartidor);
        modeloTabla.setRowCount(0);
        try {
            for (Entrega entrega : entregaDAO.readFiltrado(idPedido, idRepartidor)) {
                modeloTabla.addRow(new Object[]{
                        entrega.getId(),
                        entrega.getIdPedido() + " - " + entrega.getDireccionPedido(),
                        entrega.getIdRepartidor() + " - " + entrega.getNombreRepartidor(),
                        entrega.getFecha(),
                        entrega.getHora() != null ? entrega.getHora().format(FORMATO_HORA) : ""
                });
            }
        } catch (SQLException e) {
            Mensajes.error(this, e.getMessage());
        }
    }

    //llena los 4 combos desde la BD, manteniendo lo que estaba seleccionado
    public void cargarCombos(boolean avisarError) {
        List<Pedido> pedidos;
        List<Repartidor> repartidores;
        try {
            pedidos = pedidoDAO.readAll();
            repartidores = repartidorDAO.readAll();
        } catch (SQLException e) {
            if (avisarError) {
                Mensajes.error(this, e.getMessage());
            } else {
                System.out.println(e.getMessage());
            }
            return;
        }

        cargandoCombos = true;
        Integer pedidoElegido = idElegido(jComboPedido);
        Integer repartidorElegido = idElegido(jComboRepartidor);
        Integer filtroPedido = idElegido(jComboFiltroPedido);
        Integer filtroRepartidor = idElegido(jComboFiltroRepartidor);

        DefaultComboBoxModel<ItemCombo> modeloPedidos = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<ItemCombo> modeloFiltroPedidos = new DefaultComboBoxModel<>();
        modeloFiltroPedidos.addElement(new ItemCombo(0, "TODOS"));
        for (Pedido pedido : pedidos) {
            //texto legible, pero el ItemCombo guarda el id
            ItemCombo item = new ItemCombo(pedido.getIdPedido(), pedido.getIdPedido() + " - " + pedido.getDireccionEntrega());
            modeloPedidos.addElement(item);
            modeloFiltroPedidos.addElement(item);
        }

        DefaultComboBoxModel<ItemCombo> modeloRepartidores = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<ItemCombo> modeloFiltroRepartidores = new DefaultComboBoxModel<>();
        modeloFiltroRepartidores.addElement(new ItemCombo(0, "TODOS"));
        for (Repartidor repartidor : repartidores) {
            ItemCombo item = new ItemCombo(repartidor.getId(), repartidor.getId() + " - " + repartidor.getNombre());
            modeloRepartidores.addElement(item);
            modeloFiltroRepartidores.addElement(item);
        }

        jComboPedido.setModel(modeloPedidos);
        jComboRepartidor.setModel(modeloRepartidores);
        jComboFiltroPedido.setModel(modeloFiltroPedidos);
        jComboFiltroRepartidor.setModel(modeloFiltroRepartidores);

        seleccionarPorId(jComboPedido, pedidoElegido);
        seleccionarPorId(jComboRepartidor, repartidorElegido);
        seleccionarPorId(jComboFiltroPedido, filtroPedido == null ? 0 : filtroPedido);
        seleccionarPorId(jComboFiltroRepartidor, filtroRepartidor == null ? 0 : filtroRepartidor);
        cargandoCombos = false;
    }

    //valida el formulario y arma la entrega, devuelve null si algo esta mal
    private Entrega leerFormulario() {
        ItemCombo pedido = (ItemCombo) jComboPedido.getSelectedItem();
        if (pedido == null) {
            Mensajes.aviso(this, "Debes seleccionar un Pedido (si no hay, registra uno primero).");
            return null;
        }
        ItemCombo repartidor = (ItemCombo) jComboRepartidor.getSelectedItem();
        if (repartidor == null) {
            Mensajes.aviso(this, "Debes seleccionar un Repartidor (si no hay, registra uno primero).");
            return null;
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(jTextFecha.getText().trim());
        } catch (DateTimeParseException e) {
            Mensajes.aviso(this, "La fecha debe tener el formato aaaa-mm-dd (ej: 2026-10-05).");
            jTextFecha.requestFocus();
            return null;
        }

        LocalTime hora;
        try {
            hora = LocalTime.parse(jTextHora.getText().trim());
        } catch (DateTimeParseException e) {
            Mensajes.aviso(this, "La hora debe tener el formato hh:mm (ej: 14:30).");
            jTextHora.requestFocus();
            return null;
        }

        return new Entrega(pedido.getId(), repartidor.getId(), fecha, hora);
    }

    //devuelve el id de la fila seleccionada, o -1 si no hay ninguna
    private int idSeleccionado() {
        int fila = tablaEntregas.getSelectedRow();
        if (fila == -1) {
            Mensajes.aviso(this, "Selecciona una entrega en la tabla.");
            return -1;
        }
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    //pasa los datos de la fila seleccionada al formulario
    private void cargarSeleccion() {
        int fila = tablaEntregas.getSelectedRow();
        if (fila == -1) {
            return;
        }
        //las columnas Pedido y Repartidor tienen "id - texto", se saca el id
        seleccionarPorId(jComboPedido, idDeTexto(modeloTabla.getValueAt(fila, 1)));
        seleccionarPorId(jComboRepartidor, idDeTexto(modeloTabla.getValueAt(fila, 2)));
        jTextFecha.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        jTextHora.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
    }

    //deja el formulario con la fecha y hora actual
    private void limpiar() {
        jComboPedido.setSelectedIndex(-1);
        jComboRepartidor.setSelectedIndex(-1);
        jTextFecha.setText(LocalDate.now().toString());
        jTextHora.setText(LocalTime.now().format(FORMATO_HORA));
        tablaEntregas.clearSelection();
    }

    //busca en el combo el item con ese id y lo selecciona
    private void seleccionarPorId(JComboBox<ItemCombo> combo, Integer id) {
        combo.setSelectedIndex(-1);
        if (id == null) {
            return;
        }
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private Integer idElegido(JComboBox<ItemCombo> combo) {
        ItemCombo item = (ItemCombo) combo.getSelectedItem();
        return item == null ? null : item.getId();
    }

    //en los filtros el id 0 es "TODOS" (sin filtro)
    private Integer idFiltro(JComboBox<ItemCombo> combo) {
        Integer id = idElegido(combo);
        return (id == null || id == 0) ? null : id;
    }

    private int idDeTexto(Object texto) {
        return Integer.parseInt(texto.toString().split(" - ")[0].trim());
    }


}
