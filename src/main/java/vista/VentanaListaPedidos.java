package vista;

import dao.PedidoDAO;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class VentanaListaPedidos extends JFrame {
    private JPanel jPaneLista;
    private JTable tablaPedidos;
    private JButton btnAtras;
    private JButton btnRefrescar;

    private final DefaultTableModel modeloTabla;
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private Timer timerRefresco;

    public VentanaListaPedidos() throws HeadlessException {
        setTitle("SpeedFast - Listado de Pedidos");
        setContentPane(jPaneLista);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        //modelo de la tabla, las celdas no se pueden editar
        //columnas iguales a la tabla pedido de la BD
        String[] columnas = {"ID", "Tipo", "Direccion", "Estado"};
        modeloTabla = Tablas.modeloNoEditable(columnas);
        tablaPedidos.setModel(modeloTabla);

        cargarTabla();

        btnRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTabla();
                timerRefresco.start();
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


    //la tabla se llena con lo que hay en la base de datos (readAll)
    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            for (Pedido pedido : pedidoDAO.readAll()) {
                modeloTabla.addRow(new Object[]{
                        pedido.getIdPedido(),
                        pedido.getTipo(),
                        pedido.getDireccionEntrega(),
                        pedido.getEstado()
                });
            }
        } catch (SQLException e) {
            //se detiene el refresco automatico para no repetir el error cada 2 segundos
            if (timerRefresco != null) timerRefresco.stop();
            Mensajes.error(this, e.getMessage());
        }
    }

    //al cerrar la ventana se detiene el timer
    @Override
    public void dispose() {
        timerRefresco.stop();
        super.dispose();
    }


}
