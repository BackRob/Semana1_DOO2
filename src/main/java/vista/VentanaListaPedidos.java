package vista;

import model.Pedido;
import model.ZonaDeCarga;

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
    private final Timer timerRefresco;

    public VentanaListaPedidos() throws HeadlessException {
        setTitle("SpeedFast - Listado de Pedidos");
        setContentPane(jPaneLista);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        //modelo de la tabla, las celdas no se pueden editar
        String[] columnas = {"ID", "Tipo", "Direccion", "Distancia (km)", "Tiempo estimado (min)", "Estado"};
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

        //se refresca sola cada 1 segundo para ver como cambian los estados mientras reparten
        timerRefresco = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTabla();
            }
        });
        timerRefresco.start();

    }


    //vuelve a llenar la tabla con la lista comun de la zona de carga
    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Pedido pedido : ZonaDeCarga.getInstance().getPedidos()) {
            modeloTabla.addRow(new Object[]{
                    pedido.getIdPedido(),
                    pedido.getClass().getSimpleName().replace("Pedido", ""),
                    pedido.getDireccionEntrega(),
                    pedido.getDistanciaKm(),
                    pedido.calcularTiempoEntrega(),
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
