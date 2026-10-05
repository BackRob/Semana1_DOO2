package vista;

import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.EstadoPedido;
import model.Pedido;
import model.Repartidor;
import model.ZonaDeCarga;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class VentanaPrincipal extends JFrame{

    private JPanel jInicio;
    private JButton btnRegistrarPedido;
    private JButton btnAsignarPedidos;
    private JButton btnListarPedidos;
    private JButton btnRegistrarRepartidor;

    public VentanaPrincipal() throws HeadlessException {
        setTitle("SpeedFast - Menu Principal");
        setContentPane(jInicio);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        //abre el formulario de registro
        btnRegistrarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaRegistroPedido ventanaRegistro = new VentanaRegistroPedido();
                ventanaRegistro.setVisible(true);
            }
        });

        //semana 7: formulario de repartidores
        btnRegistrarRepartidor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaRegistroRepartidor ventanaRepartidor = new VentanaRegistroRepartidor();
                ventanaRepartidor.setVisible(true);
            }
        });

        //abre la tabla con los pedidos
        btnListarPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaListaPedidos ventanaLista = new VentanaListaPedidos();
                ventanaLista.setVisible(true);
            }
        });

        //asigna un repartidor y parte la entrega en un hilo
        btnAsignarPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                asignarRepartidor();
            }
        });

        cargarPendientesBD();

    }


    //al abrir la app, los pedidos PENDIENTE que quedaron en la BD vuelven a la zona de carga
    public void cargarPendientesBD() {
        PedidoDAO pedidoDAO = new PedidoDAO();
        for (Pedido pedido : pedidoDAO.listarPorEstado(EstadoPedido.PENDIENTE)) {
            ZonaDeCarga.getInstance().agregarPedido(pedido);
        }
    }


    //elige un repartidor de la BD y lo pone a trabajar con los pedidos de la zona de carga
    public void asignarRepartidor() {
        ZonaDeCarga zonaDeCarga = ZonaDeCarga.getInstance();

        if (zonaDeCarga.estaVacia()) {
            JOptionPane.showMessageDialog(this,
                    "No hay pedidos pendientes en la zona de carga.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //semana 7: los repartidores salen de la base de datos
        RepartidorDAO repartidorDAO = new RepartidorDAO();
        List<Repartidor> repartidores = repartidorDAO.listarTodos();
        if (repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay repartidores en la base de datos, registra uno primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opciones = new String[repartidores.size()];
        for (int i = 0; i < repartidores.size(); i++) {
            opciones[i] = repartidores.get(i).getId() + " - " + repartidores.get(i).getNombre();
        }
        String elegido = (String) JOptionPane.showInputDialog(this,
                "Selecciona el repartidor:", "Asignar Repartidor",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (elegido == null) {
            return;
        }

        Repartidor repartidor = null;
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(elegido)) {
                repartidor = repartidores.get(i);
            }
        }

        int pendientes = zonaDeCarga.cantidadPendientes();

        //el repartidor es Runnable (semana 5), asi que se lanza en su propio hilo
        Thread hilo = new Thread(repartidor, "Repartidor-" + repartidor.getNombre());
        hilo.start();

        JOptionPane.showMessageDialog(this,
                "Repartidor " + repartidor.getNombre() + " inicio la entrega.\nPedidos pendientes en la zona de carga: " + pendientes,
                "Entrega iniciada", JOptionPane.INFORMATION_MESSAGE);
    }


    // Pide String
    public String pedirString(String mensaje) {
        while (true) {
            String entrada = JOptionPane.showInputDialog(null, mensaje);

            // El usuario presionó "Cancelar" o cerró la ventana
            if (entrada == null) {
                return null;
            }

            if (esStringValido(entrada)) {
                return entrada.trim();
            }

            JOptionPane.showMessageDialog(null,
                    "El texto no puede estar vacío ni contener solo espacios.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //validador
    public boolean esStringValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }


}
