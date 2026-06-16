package vistas;

import controladores.AlquilerController;
import controladores.ClienteController;
import controladores.VehiculoController;
import modelo.Alquiler;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SolicitarAlquilerView extends JFrame {
    private AlquilerController  ctrlAlq = AlquilerController.getInstancia();
    private ClienteController   ctrlCli = ClienteController.getInstancia();
    private VehiculoController  ctrlVeh = VehiculoController.getInstancia();

    private JTextField  txtDniCuit      = new JTextField(15);
    private JTextField  txtPatente      = new JTextField(10);
    private JTextField  txtFechaInicio  = new JTextField(10);
    private JTextField  txtFechaFin     = new JTextField(10);
    private JComboBox<String> cmbTipo   = new JComboBox<>(new String[]{"COMUN", "CORPORATIVO", "TURISTICO"});
    private JButton     btnSolicitar    = new JButton("Solicitar Alquiler");

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public SolicitarAlquilerView() {
        inicializarUI();
    }

    public void inicializarUI() {
        setTitle("Solicitar Alquiler");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 8, 6, 8);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        agregarFila(gbc, 0, "DNI/CUIT Cliente:",           txtDniCuit);
        agregarFila(gbc, 1, "Patente Vehículo:",           txtPatente);
        agregarFila(gbc, 2, "Fecha Inicio (dd/MM/yyyy):",  txtFechaInicio);
        agregarFila(gbc, 3, "Fecha Fin (dd/MM/yyyy):",     txtFechaFin);
        agregarFila(gbc, 4, "Tipo de Alquiler:",           cmbTipo);

        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnSolicitar, gbc);

        btnSolicitar.addActionListener(e -> solicitarAlquiler());

        pack();
        setLocationRelativeTo(null);
    }

    private void agregarFila(GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; add(new JLabel(label), gbc);
        gbc.gridx = 1;                   add(campo, gbc);
    }

    private void solicitarAlquiler() {
        String dniCuit = txtDniCuit.getText().trim();
        String patente = txtPatente.getText().trim();
        String clase   = (String) cmbTipo.getSelectedItem();

        if (dniCuit.isEmpty() || patente.isEmpty()
                || txtFechaInicio.getText().trim().isEmpty()
                || txtFechaFin.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Date fInicio = sdf.parse(txtFechaInicio.getText().trim());
            Date fFin    = sdf.parse(txtFechaFin.getText().trim());

            if (!fFin.after(fInicio)) {
                JOptionPane.showMessageDialog(this,
                        "La fecha de fin debe ser posterior a la de inicio.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Alquiler a = ctrlAlq.solicitarAlquiler(dniCuit, patente, fInicio, fFin, clase, "sistema");
            JOptionPane.showMessageDialog(this,
                    "Alquiler registrado.\nID: " + a.getId() + "\nEstado: " + a.getEstado(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
