package vistas;

import controladores.ClienteController;
import modelo.Cliente;
import javax.swing.*;
import java.awt.*;

public class RegistrarClienteView extends JFrame {
    private ClienteController ctrl = ClienteController.getInstancia();

    private JTextField txtDniCuit    = new JTextField(15);
    private JTextField txtNombre     = new JTextField(20);
    private JTextField txtTelefono   = new JTextField(15);
    private JTextField txtEmail      = new JTextField(20);
    private JTextField txtDireccion  = new JTextField(25);
    private JButton    btnRegistrar  = new JButton("Registrar");

    public RegistrarClienteView() {
        inicializarUI();
    }

    public void inicializarUI() {
        setTitle("Registrar Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 8, 6, 8);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        agregarFila(gbc, 0, "DNI/CUIT:",            txtDniCuit);
        agregarFila(gbc, 1, "Nombre / Razón Social:", txtNombre);
        agregarFila(gbc, 2, "Teléfono:",             txtTelefono);
        agregarFila(gbc, 3, "Email:",                txtEmail);
        agregarFila(gbc, 4, "Dirección:",            txtDireccion);

        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnRegistrar, gbc);

        btnRegistrar.addActionListener(e -> registrarCliente());

        pack();
        setLocationRelativeTo(null);
    }

    private void agregarFila(GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; add(new JLabel(label), gbc);
        gbc.gridx = 1;                   add(campo, gbc);
    }

    private void registrarCliente() {
        String dniCuit   = txtDniCuit.getText().trim();
        String nombre    = txtNombre.getText().trim();
        String telefono  = txtTelefono.getText().trim();
        String email     = txtEmail.getText().trim();
        String direccion = txtDireccion.getText().trim();

        if (dniCuit.isEmpty() || nombre.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "DNI/CUIT, Nombre y Email son obligatorios.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Cliente c = ctrl.registrarCliente(dniCuit, nombre, telefono, email, direccion, "sistema");
            JOptionPane.showMessageDialog(this,
                    "Cliente registrado exitosamente: " + c.getNombreRazonSocial(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtDniCuit.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
    }
}
