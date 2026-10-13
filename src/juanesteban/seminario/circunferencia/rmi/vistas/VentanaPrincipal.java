package juanesteban.seminario.circunferencia.rmi.vistas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import juanesteban.seminario.circunferencia.rmi.lib.IRemotaCalculoCircunferencia;
import juanesteban.seminario.circunferencia.rmi.lib.*;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Client;

public class VentanaPrincipal extends JFrame {

    CallHandler invocadorRemoto;
    String ipServidor = "localhost";
    int puerto = 9007;
    IRemotaCalculoCircunferencia calculoRemoto;
    Client cliente;

    private JButton btnIniciar;
    private JButton btnIniciar1;
    private JTextField campoIPServidor;
    private JTextField campoPuertoServidor;
    private JTextField campoRadio;
    private JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel7;
    private JPanel jPanel1;
    private JPanel jPanel3;
    private JTabbedPane jTabbedPane1;
    private JLabel txtEstado;
    private JLabel txtMensaje;
    private JLabel txtResultado;

    public VentanaPrincipal() {
        initComponents();
    }

    // Agrega un componente al panel en la celda (x, y), ocupando "ancho" columnas y "alto" filas
    private void agregar(JPanel panel, Component c, int x, int y, int ancho, int alto) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = ancho;
        gbc.gridheight = alto;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(c, gbc);
    }

    private void initComponents() {
        // ---------- Creación de componentes ----------
        jLabel1 = new JLabel("CLIENTE CIRCUNFERENCIA", SwingConstants.CENTER);
        jLabel1.setFont(new Font("Tahoma", Font.PLAIN, 24));

        jTabbedPane1 = new JTabbedPane();
        jPanel1 = new JPanel(new GridBagLayout());
        jPanel3 = new JPanel(new GridBagLayout());

        // Pestaña CONEXION
        jLabel2 = new JLabel("DIRECCIÓN IP: ");
        campoIPServidor = new JTextField("localhost", 12);
        jLabel3 = new JLabel("PUERTO DE RED:");
        campoPuertoServidor = new JTextField("9007", 12);
        jLabel4 = new JLabel("ESTADO: ");
        txtEstado = new JLabel("Desconectado");
        txtEstado.setForeground(new Color(255, 0, 51));
        btnIniciar = new JButton("Conectar");
        btnIniciar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnIniciar.setForeground(new Color(0, 153, 51));
        btnIniciar.addActionListener(evt -> btnIniciarActionPerformed(evt));

        // Pestaña CALCULAR
        jLabel5 = new JLabel("RADIO:");
        campoRadio = new JTextField(12);
        btnIniciar1 = new JButton("CALCULAR");
        btnIniciar1.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnIniciar1.setForeground(new Color(0, 153, 51));
        btnIniciar1.addActionListener(evt -> btnIniciar1ActionPerformed(evt));
        jLabel7 = new JLabel("LONGITUD:  ");
        txtResultado = new JLabel("0.0");
        txtResultado.setFont(new Font("Tahoma", Font.BOLD, 12));
        txtResultado.setForeground(new Color(255, 0, 51));
        txtMensaje = new JLabel(" ");
        txtMensaje.setBorder(BorderFactory.createTitledBorder(""));
        txtMensaje.setPreferredSize(new Dimension(285, 30));

        // ---------- Armado del panel CONEXION ----------
        agregar(jPanel1, jLabel2, 0, 0, 1, 1);
        agregar(jPanel1, campoIPServidor, 1, 0, 1, 1);
        agregar(jPanel1, jLabel3, 0, 1, 1, 1);
        agregar(jPanel1, campoPuertoServidor, 1, 1, 1, 1);
        agregar(jPanel1, jLabel4, 0, 2, 1, 1);
        agregar(jPanel1, txtEstado, 1, 2, 1, 1);
        agregar(jPanel1, btnIniciar, 0, 3, 2, 1);

        // ---------- Armado del panel CALCULAR ----------
        agregar(jPanel3, jLabel5, 0, 0, 1, 1);
        agregar(jPanel3, campoRadio, 1, 0, 1, 1);
        agregar(jPanel3, btnIniciar1, 2, 0, 1, 1);
        agregar(jPanel3, jLabel7, 0, 1, 1, 1);
        agregar(jPanel3, txtResultado, 1, 1, 1, 1);
        agregar(jPanel3, txtMensaje, 2, 1, 1, 1);

        jTabbedPane1.addTab("CONEXION", jPanel1);
        jTabbedPane1.addTab("CALCULAR", jPanel3);

        // ---------- Ventana principal ----------
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cliente Circunferencia");
        setLayout(new GridBagLayout());

        GridBagConstraints g1 = new GridBagConstraints();
        g1.gridx = 0;
        g1.gridy = 0;
        g1.insets = new Insets(25, 20, 10, 20);
        g1.fill = GridBagConstraints.HORIZONTAL;
        g1.weightx = 1.0;
        add(jLabel1, g1);

        GridBagConstraints g2 = new GridBagConstraints();
        g2.gridx = 0;
        g2.gridy = 1;
        g2.insets = new Insets(10, 20, 30, 20);
        g2.fill = GridBagConstraints.BOTH;
        g2.weightx = 1.0;
        g2.weighty = 1.0;
        add(jTabbedPane1, g2);

        pack();
    }

private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {
    try {
        if (btnIniciar.getText().equalsIgnoreCase("Conectar")) {
            puerto = Integer.parseInt(campoPuertoServidor.getText().trim());
            ipServidor = campoIPServidor.getText().trim();
            invocadorRemoto = new CallHandler();
            cliente = new Client(ipServidor, puerto, invocadorRemoto);
            calculoRemoto = (IRemotaCalculoCircunferencia) cliente.getGlobal(IRemotaCalculoCircunferencia.class);
            btnIniciar.setText("Desconectar");
            btnIniciar.setForeground(Color.RED);
            txtEstado.setText("Conectado");
            txtEstado.setForeground(Color.GREEN);
        } else if (btnIniciar.getText().equalsIgnoreCase("Desconectar")) {
            cliente.close();
            calculoRemoto = null;
            btnIniciar.setText("Conectar");
            txtEstado.setText("Desconectado");
            btnIniciar.setForeground(Color.GREEN);
            txtEstado.setForeground(Color.RED);
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "El puerto debe ser un número entero.",
                "Dato inválido", JOptionPane.WARNING_MESSAGE);
    } catch (IOException ex) {
        calculoRemoto = null;
        JOptionPane.showMessageDialog(this,
                "No se pudo conectar a " + ipServidor + ":" + puerto
                + "\nVerifica que el servidor esté en ejecución.",
                "Error de conexión", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

private void btnIniciar1ActionPerformed(java.awt.event.ActionEvent evt) {
    if (calculoRemoto == null) {
        JOptionPane.showMessageDialog(this,
                "Primero debes conectarte al servidor (pestaña CONEXION).",
                "Sin conexión", JOptionPane.WARNING_MESSAGE);
        return;
    }
    final double radio;
    try {
        radio = Double.parseDouble(campoRadio.getText().trim().replace(',', '.'));
        if (Double.isNaN(radio) || Double.isInfinite(radio)) {
            throw new NumberFormatException();
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this,
                "Ingresa un radio numérico válido, por ejemplo 5 o 2.5",
                "Dato inválido", JOptionPane.WARNING_MESSAGE);
        return;
    }
    Thread hilo = new Thread() {
        @Override
        public void run() {
            try {
                DatosCircunferencia datos = new DatosCircunferencia();
                datos.setRadio(radio);
                datos = calculoRemoto.calcularLongitud(datos);
                final DatosCircunferencia respuesta = datos;
                SwingUtilities.invokeLater(() -> {
                    txtResultado.setText(respuesta.getLongitud() + "");
                    txtMensaje.setText(respuesta.getMensaje());
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        VentanaPrincipal.this,
                        "Se perdió la comunicación con el servidor.\nVuelve a conectarte.",
                        "Error de comunicación", JOptionPane.ERROR_MESSAGE));
                ex.printStackTrace();
            }
        }
    };
    hilo.start();
}
}