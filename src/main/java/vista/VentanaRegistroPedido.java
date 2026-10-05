package vista;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.ZonaDeCarga;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRegistroPedido extends JFrame {
    private JPanel jPaneRegistro;
    private JTextField jTextID;
    private JTextField jTextDireccion;
    private JTextField jTextDistancia;
    private JComboBox<TipoPedido> jComboTipo;
    private JButton btnAtras;
    private JButton btnRegistrar;

    public VentanaRegistroPedido() throws HeadlessException {
        setTitle("SpeedFast - Registrar Pedido");
        setContentPane(jPaneRegistro);
        //solo cierra esta ventana, no toda la app
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //pack ajusta el tamaño de la ventana a los componentes, asi no se corta nada
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        //llena el combo con los tipos del enum
        jComboTipo.setModel(new DefaultComboBoxModel<>(TipoPedido.values()));
        jComboTipo.setSelectedIndex(-1);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrarPedido();
            }
        });

        //vuelve al menu principal
        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }



    public void RegistrarPedido() {
        String id=capturarJtextField(jTextID,"ID");
        if (id==null) {
            return;
        }
        String direccion=capturarJtextField(jTextDireccion,"Direccion");
        if (direccion==null) {
            return;
        }
        String distancia=capturarJtextField(jTextDistancia,"Distancia");
        if (distancia==null) {
            return;
        }
        TipoPedido tipoPedido = capturarCombo(jComboTipo,"Tipo de pedido");
        if (tipoPedido==null) {
            return;
        }

        //validacion de numeros
        int idPedido = capturarEntero(jTextID, id, "ID");
        if (idPedido == -1) {
            return;
        }
        double distanciaKm = capturarDecimal(jTextDistancia, distancia, "Distancia");
        if (distanciaKm == -1) {
            return;
        }

        //el id no se puede repetir
        if (ZonaDeCarga.getInstance().existePedido(idPedido)) {
            JOptionPane.showMessageDialog(this,
                    "Ya existe un pedido con el ID " + idPedido + ".",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jTextID.requestFocus();
            return;
        }

        //se crea el pedido segun el tipo elegido (polimorfismo de semana 4)
        Pedido pedido = switch (tipoPedido) {
            case Encomienda -> new PedidoEncomienda(direccion, distanciaKm);
            case Comida -> new PedidoComida(direccion, distanciaKm);
            case Express -> new PedidoExpress(direccion, distanciaKm);
        };

        ZonaDeCarga.getInstance().agregarPedido(pedido);

        JOptionPane.showMessageDialog(this,
                "Pedido " + idPedido + " (" + tipoPedido + ") registrado correctamente.",
                "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        limpiarCampos();
    }

    //deja el formulario listo para otro pedido
    public void limpiarCampos() {
        jTextID.setText("");
        jTextDireccion.setText("");
        jTextDistancia.setText("");
        jComboTipo.setSelectedIndex(-1);
        jTextID.requestFocus();
    }



    public <T> T capturarCombo(JComboBox<T> combo, String nombreCampo) {
        if (combo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(combo,
                    "Debe seleccionar una opción en " + nombreCampo + ".",
                    "Error", JOptionPane.ERROR_MESSAGE);
            combo.requestFocus();
            return null;
        }

        return combo.getItemAt(combo.getSelectedIndex());
    }




    public String capturarJtextField(JTextField campo, String nombreCampo) {
        String texto = campo.getText();

        if (!esStringValido(texto)) {
            JOptionPane.showMessageDialog(campo,
                    "El campo " + nombreCampo + " no puede estar vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            campo.requestFocus();
            return null;
        }

        return texto.trim();
    }

    //valida que sea entero mayor a 0, si no devuelve -1
    public int capturarEntero(JTextField campo, String texto, String nombreCampo) {
        try {
            int numero = Integer.parseInt(texto);
            if (numero > 0) {
                return numero;
            }
        } catch (NumberFormatException e) {
            //cae al mensaje de error de abajo
        }
        JOptionPane.showMessageDialog(campo,
                "El campo " + nombreCampo + " debe ser un numero entero mayor a 0.",
                "Error", JOptionPane.ERROR_MESSAGE);
        campo.requestFocus();
        return -1;
    }

    //valida que sea numero mayor a 0 (acepta coma o punto), si no devuelve -1
    public double capturarDecimal(JTextField campo, String texto, String nombreCampo) {
        try {
            double numero = Double.parseDouble(texto.replace(",", "."));
            if (numero > 0) {
                return numero;
            }
        } catch (NumberFormatException e) {
            //cae al mensaje de error de abajo
        }
        JOptionPane.showMessageDialog(campo,
                "El campo " + nombreCampo + " debe ser un numero mayor a 0.",
                "Error", JOptionPane.ERROR_MESSAGE);
        campo.requestFocus();
        return -1;
    }

    //validador
    public boolean esStringValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }


}
