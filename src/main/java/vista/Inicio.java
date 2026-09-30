package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Inicio extends JFrame{

    private JPanel inicio;
    private JButton btnRegistrarPedido;
    private JButton btnAsignarPedidos;
    private JButton btnListarPedidos;

    public Inicio() throws HeadlessException {
        setTitle("Login Verdulería al Paso");
        setContentPane(inicio);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        btnRegistrarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });




    }





}
