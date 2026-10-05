package main;

import vista.VentanaPrincipal;

import javax.swing.*;

//la pauta pide que la app parta desde la clase Main del paquete main
public class Main {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
            });
        }
    }
