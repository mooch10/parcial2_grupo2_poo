package controladores;

import modelo.Alquiler;
import modelo.Vehiculo;
import modelo.enums.TipoCombustible;
import modelo.enums.TipoEntidad;
import modelo.enums.TipoVehiculo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VehiculoController {
    private static VehiculoController instancia;
    private List<Vehiculo> vehiculos;

    private VehiculoController() {
        vehiculos = new ArrayList<>();
    }

    public static VehiculoController getInstancia() {
        if (instancia == null) {
            instancia = new VehiculoController();
        }
        return instancia;
    }

    public Vehiculo registrarVehiculo(String patente, String marca, String modelo,
                                       int anio, int km, double valorDiario,
                                       TipoVehiculo tipo, TipoCombustible combustible,
                                       int kmD, double cKm, String usuario) {
        if (buscarVehiculoPorPatente(patente) != null) {
            throw new IllegalArgumentException("Ya existe un vehículo con patente: " + patente);
        }
        Vehiculo v = new Vehiculo(patente, marca, modelo, anio, km,
                valorDiario, tipo, combustible, kmD, cKm);
        vehiculos.add(v);
        HistorialController.getInstancia().registrar(
                "-", "DISPONIBLE", TipoEntidad.VEHICULO, patente, usuario);
        return v;
    }

    public Vehiculo buscarVehiculoPorPatente(String patente) {
        for (Vehiculo v : vehiculos) {
            if (v.getPatente().equalsIgnoreCase(patente)) return v;
        }
        return null;
    }

    public List<Vehiculo> consultarVehiculosDisponibles(Date fInicio, Date fFin, TipoVehiculo tipo) {
        List<Alquiler> alquileres = AlquilerController.getInstancia().getAlquileres();
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            if (v.getTipoVehiculo() == tipo && v.estaDisponible(fInicio, fFin, alquileres)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public List<Vehiculo> getVehiculos() { return vehiculos; }

    public static void resetInstancia() { instancia = null; }
}
