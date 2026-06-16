import vistas.ConsultarDisponibilidadView;
import vistas.RegistrarClienteView;
import vistas.SolicitarAlquilerView;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame menu = new JFrame("Sistema de Alquiler de Vehículos");
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

            JButton btnCliente   = new JButton("Registrar Cliente");
            JButton btnAlquiler  = new JButton("Solicitar Alquiler");
            JButton btnConsultar = new JButton("Consultar Disponibilidad");
            JButton btnSalir     = new JButton("Salir");

            btnCliente.addActionListener(e   -> new RegistrarClienteView().setVisible(true));
            btnAlquiler.addActionListener(e  -> new SolicitarAlquilerView().setVisible(true));
            btnConsultar.addActionListener(e -> new ConsultarDisponibilidadView().setVisible(true));
            btnSalir.addActionListener(e     -> System.exit(0));

            panel.add(btnCliente);
            panel.add(btnAlquiler);
            panel.add(btnConsultar);
            panel.add(btnSalir);

            menu.add(panel);
            menu.pack();
            menu.setLocationRelativeTo(null);
            menu.setVisible(true);
        });
    }
}
