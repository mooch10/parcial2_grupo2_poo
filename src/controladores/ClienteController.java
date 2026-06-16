package controladores;

import modelo.Alquiler;
import modelo.Cliente;
import modelo.enums.EstadoAlquiler;
import modelo.enums.TipoEntidad;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    private static ClienteController instancia;
    private List<Cliente> clientes;

    private ClienteController() {
        clientes = new ArrayList<>();
    }

    public static ClienteController getInstancia() {
        if (instancia == null) {
            instancia = new ClienteController();
        }
        return instancia;
    }

    public Cliente registrarCliente(String dniCuit, String nombre, String telefono,
                                     String email, String direccion, String usuario) {
        if (buscarClientePorDniCuit(dniCuit) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con DNI/CUIT: " + dniCuit);
        }
        Cliente cliente = new Cliente(dniCuit, nombre, telefono, email, direccion);
        cliente.activar();
        clientes.add(cliente);
        HistorialController.getInstancia().registrar(
                "-", "ACTIVO", TipoEntidad.CLIENTE, dniCuit, usuario);
        return cliente;
    }

    public Cliente buscarClientePorDniCuit(String dniCuit) {
        for (Cliente c : clientes) {
            if (c.getDniCuit().equals(dniCuit)) return c;
        }
        return null;
    }

    public List<Alquiler> alquileresConfirmadosDeCliente(String dniCuit) {
        List<Alquiler> resultado = new ArrayList<>();
        for (Alquiler a : AlquilerController.getInstancia().getAlquileres()) {
            if (a.getCliente().getDniCuit().equals(dniCuit)
                    && a.getEstado() == EstadoAlquiler.CONFIRMADO) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public List<Cliente> getClientes() { return clientes; }

    public static void resetInstancia() { instancia = null; }
}
