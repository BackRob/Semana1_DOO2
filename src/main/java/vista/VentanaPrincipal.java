package vista;

import model.Repartidor;
import model.ZonaDeCarga;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class VentanaPrincipal extends JFrame{

    private JPanel jInicio;
    private JButton btnRegistrarPedido;
    private JButton btnAsignarPedidos;
    private JButton btnListarPedidos;

    public VentanaPrincipal() throws HeadlessException {
        setTitle("SpeedFast - Menu Principal");
        setContentPane(jInicio);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 250);
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

    }


    //pide los datos del repartidor y lo pone a trabajar con los pedidos de la zona de carga
    public void asignarRepartidor() {
        ZonaDeCarga zonaDeCarga = ZonaDeCarga.getInstance();

        if (zonaDeCarga.estaVacia()) {
            JOptionPane.showMessageDialog(this,
                    "No hay pedidos pendientes en la zona de carga.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = pedirString("Nombre del repartidor:");
        if (nombre == null) {
            return;
        }
        String rut = pedirString("Rut del repartidor:");
        if (rut == null) {
            return;
        }

        int pendientes = zonaDeCarga.cantidadPendientes();

        //el repartidor es Runnable (semana 5), asi que se lanza en su propio hilo
        Repartidor repartidor = new Repartidor(nombre, null, rut, zonaDeCarga);
        Thread hilo = new Thread(repartidor, "Repartidor-" + nombre);
        hilo.start();

        JOptionPane.showMessageDialog(this,
                "Repartidor " + nombre + " inicio la entrega.\nPedidos pendientes en la zona de carga: " + pendientes,
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
