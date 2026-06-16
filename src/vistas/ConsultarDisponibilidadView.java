package vistas;

import controladores.VehiculoController;
import modelo.Vehiculo;
import modelo.enums.TipoVehiculo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConsultarDisponibilidadView extends JFrame {
    private VehiculoController ctrlVeh = VehiculoController.getInstancia();

    private JTextField          txtFechaInicio  = new JTextField(10);
    private JTextField          txtFechaFin     = new JTextField(10);
    private JComboBox<TipoVehiculo> cmbTipo     = new JComboBox<>(TipoVehiculo.values());
    private JButton             btnConsultar    = new JButton("Consultar");
    private DefaultTableModel   modeloTabla;
    private JTable              tabla;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public ConsultarDisponibilidadView() {
        inicializarUI();
    }

    public void inicializarUI() {
        setTitle("Consultar Vehículos Disponibles");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel de filtros
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panelFiltros.add(new JLabel("Fecha Inicio (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; panelFiltros.add(txtFechaInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelFiltros.add(new JLabel("Fecha Fin (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; panelFiltros.add(txtFechaFin, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelFiltros.add(new JLabel("Tipo de Vehículo:"), gbc);
        gbc.gridx = 1; panelFiltros.add(cmbTipo, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFiltros.add(btnConsultar, gbc);

        // Tabla de resultados
        String[] columnas = {"Patente", "Marca", "Modelo", "Año", "Tipo", "Combustible", "Valor Diario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(620, 200));

        add(panelFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnConsultar.addActionListener(e -> consultar());

        pack();
        setLocationRelativeTo(null);
    }

    private void consultar() {
        String sInicio = txtFechaInicio.getText().trim();
        String sFin    = txtFechaFin.getText().trim();

        if (sInicio.isEmpty() || sFin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Las fechas son obligatorias.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Date fInicio     = sdf.parse(sInicio);
            Date fFin        = sdf.parse(sFin);
            TipoVehiculo tipo = (TipoVehiculo) cmbTipo.getSelectedItem();

            List<Vehiculo> disponibles = ctrlVeh.consultarVehiculosDisponibles(fInicio, fFin, tipo);
            modeloTabla.setRowCount(0);
            for (Vehiculo v : disponibles) {
                modeloTabla.addRow(new Object[]{
                        v.getPatente(), v.getMarca(), v.getModelo(),
                        v.getAnio(), v.getTipoVehiculo(), v.getTipoCombustible(),
                        String.format("$%.2f", v.getValorDiario())
                });
            }
            if (disponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay vehículos disponibles para el período y tipo seleccionados.",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
