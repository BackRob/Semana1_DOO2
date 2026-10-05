package vista;

import dao.PedidoDAO;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaListaPedidos extends JFrame {
    private JPanel jPaneLista;
    private JTable tablaPedidos;
    private JButton btnAtras;
    private JButton btnRefrescar;

    private final DefaultTableModel modeloTabla;
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final Timer timerRefresco;

    public VentanaListaPedidos() throws HeadlessException {
        setTitle("SpeedFast - Listado de Pedidos");
        setContentPane(jPaneLista);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        //modelo de la tabla, las celdas no se pueden editar
        //columnas iguales a la tabla pedido de la BD
        String[] columnas = {"ID", "Tipo", "Direccion", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPedidos.setModel(modeloTabla);

        cargarTabla();

        btnRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTabla();
            }
        });

        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //se refresca sola cada 2 segundos para ver como cambian los estados mientras reparten
        timerRefresco = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTabla();
            }
        });
        timerRefresco.start();

    }


    //semana 7: la tabla se llena con lo que hay en la base de datos
    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Pedido pedido : pedidoDAO.listarTodos()) {
            modeloTabla.addRow(new Object[]{
                    pedido.getIdPedido(),
                    pedido.getTipo(),
                    pedido.getDireccionEntrega(),
                    pedido.getEstado()
            });
        }
    }

    //al cerrar la ventana se detiene el timer
    @Override
    public void dispose() {
        timerRefresco.stop();
        super.dispose();
    }


}
