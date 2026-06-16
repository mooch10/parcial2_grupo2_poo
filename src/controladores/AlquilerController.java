package controladores;

import modelo.*;
import modelo.enums.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlquilerController {
    private static AlquilerController instancia;
    private List<Alquiler> alquileres;
    private double porcentajeDescuentoCorporativo = 10.0;
    private double porcentajeRecargoTuristico = 15.0;

    private AlquilerController() {
        alquileres = new ArrayList<>();
    }

    public static AlquilerController getInstancia() {
        if (instancia == null) {
            instancia = new AlquilerController();
        }
        return instancia;
    }

    // UC3 — Solicitar alquiler
    public Alquiler solicitarAlquiler(String dniCuit, String patente,
                                       Date fInicio, Date fFin,
                                       String clase, String usuario) {
        Cliente cliente = ClienteController.getInstancia().buscarClientePorDniCuit(dniCuit);
        if (cliente == null)
            throw new IllegalArgumentException("Cliente no encontrado: " + dniCuit);

        Vehiculo vehiculo = VehiculoController.getInstancia().buscarVehiculoPorPatente(patente);
        if (vehiculo == null)
            throw new IllegalArgumentException("Vehículo no encontrado: " + patente);

        if (!vehiculo.estaDisponible(fInicio, fFin, alquileres))
            throw new IllegalStateException("El vehículo no está disponible para el período solicitado.");

        int kmInicial = vehiculo.getKilometrajeActual();
        Alquiler alquiler;
        switch (clase.toUpperCase()) {
            case "CORPORATIVO":
                alquiler = new AlquilerCorporativo(cliente, vehiculo, fInicio, fFin,
                        kmInicial, porcentajeDescuentoCorporativo);
                break;
            case "TURISTICO":
                alquiler = new AlquilerTuristico(cliente, vehiculo, fInicio, fFin,
                        kmInicial, porcentajeRecargoTuristico);
                break;
            default:
                alquiler = new AlquilerComun(cliente, vehiculo, fInicio, fFin, kmInicial);
        }

        alquiler.cambiarEstado(EstadoAlquiler.INGRESADO);
        alquileres.add(alquiler);
        HistorialController.getInstancia().registrar(
                "-", "INGRESADO", TipoEntidad.ALQUILER,
                String.valueOf(alquiler.getId()), usuario);
        return alquiler;
    }

    // Confirmar alquiler con seña
    public Pago confirmarAlquilerConSenia(int idAlquiler, double importe,
                                           MedioPago medioPago, String usuario) {
        Alquiler alquiler = buscarPorId(idAlquiler);
        if (alquiler == null)
            throw new IllegalArgumentException("Alquiler no encontrado: " + idAlquiler);
        if (alquiler.getEstado() != EstadoAlquiler.INGRESADO)
            throw new IllegalStateException("El alquiler no está en estado INGRESADO.");

        Pago pago = PagoController.getInstancia()
                .registrarPago(idAlquiler, importe, medioPago, TipoPago.SENIA, usuario);
        alquiler.agregarPago(pago);

        String estadoAnt = alquiler.getEstado().name();
        alquiler.cambiarEstado(EstadoAlquiler.CONFIRMADO);
        alquiler.getVehiculo().cambiarEstado(EstadoVehiculo.ALQUILADO);

        HistorialController.getInstancia().registrar(
                estadoAnt, "CONFIRMADO", TipoEntidad.ALQUILER,
                String.valueOf(idAlquiler), usuario);
        HistorialController.getInstancia().registrar(
                "DISPONIBLE", "ALQUILADO", TipoEntidad.VEHICULO,
                alquiler.getVehiculo().getPatente(), usuario);
        return pago;
    }

    // Cancelar alquiler (regla 48h)
    public void cancelarAlquiler(int idAlquiler, Date fCancelacion, String usuario) {
        Alquiler alquiler = buscarPorId(idAlquiler);
        if (alquiler == null)
            throw new IllegalArgumentException("Alquiler no encontrado: " + idAlquiler);

        long horas = alquiler.calcularHorasAnticipacion(fCancelacion);
        if (horas > 48) {
            double senia = alquiler.calcularSeniaAbonada();
            if (senia > 0) alquiler.getCliente().agregarCredito(senia);
        }

        for (Pago p : alquiler.getPagos()) {
            PagoController.getInstancia().anularPago(p.getId(), usuario);
        }

        String estadoAnt = alquiler.getEstado().name();
        alquiler.cambiarEstado(EstadoAlquiler.CANCELADO);

        if (alquiler.getVehiculo().getEstado() == EstadoVehiculo.ALQUILADO) {
            alquiler.getVehiculo().cambiarEstado(EstadoVehiculo.DISPONIBLE);
            HistorialController.getInstancia().registrar(
                    "ALQUILADO", "DISPONIBLE", TipoEntidad.VEHICULO,
                    alquiler.getVehiculo().getPatente(), usuario);
        }

        HistorialController.getInstancia().registrar(
                estadoAnt, "CANCELADO", TipoEntidad.ALQUILER,
                String.valueOf(idAlquiler), usuario);
    }

    // Iniciar alquiler
    public void iniciarAlquiler(int idAlquiler, String usuario) {
        Alquiler alquiler = buscarPorId(idAlquiler);
        if (alquiler == null)
            throw new IllegalArgumentException("Alquiler no encontrado: " + idAlquiler);
        if (alquiler.getEstado() != EstadoAlquiler.CONFIRMADO)
            throw new IllegalStateException("El alquiler no está CONFIRMADO.");

        String estadoAnt = alquiler.getEstado().name();
        alquiler.cambiarEstado(EstadoAlquiler.EN_CURSO);
        HistorialController.getInstancia().registrar(
                estadoAnt, "EN_CURSO", TipoEntidad.ALQUILER,
                String.valueOf(idAlquiler), usuario);
    }

    // UC4 — Finalizar alquiler y calcular saldo
    public double finalizarAlquiler(int idAlquiler, int kmFinal,
                                     Date fDevReal, String usuario) {
        Alquiler alquiler = buscarPorId(idAlquiler);
        if (alquiler == null)
            throw new IllegalArgumentException("Alquiler no encontrado: " + idAlquiler);

        Vehiculo vehiculo = alquiler.getVehiculo();
        vehiculo.actualizarKilometraje(kmFinal);
        alquiler.setKilometrajeFinal(kmFinal);
        alquiler.setFechaDevolucionReal(fDevReal);

        double importeTotal = alquiler.calcularImporteTotal();
        alquiler.setImporteTotal(importeTotal);
        double saldoPendiente = alquiler.calcularSaldoPendiente();
        alquiler.setImportePendiente(saldoPendiente);

        alquiler.cambiarEstado(EstadoAlquiler.FINALIZADO);
        vehiculo.cambiarEstado(EstadoVehiculo.DISPONIBLE);

        HistorialController.getInstancia().registrar(
                "EN_CURSO", "FINALIZADO", TipoEntidad.ALQUILER,
                String.valueOf(idAlquiler), usuario);
        HistorialController.getInstancia().registrar(
                "ALQUILADO", "DISPONIBLE", TipoEntidad.VEHICULO,
                vehiculo.getPatente(), usuario);

        return saldoPendiente;
    }

    public Alquiler buscarPorId(int id) {
        for (Alquiler a : alquileres) {
            if (a.getId() == id) return a;
        }
        return null;
    }

    // Consulta 1: Total recaudado por período
    public double totalRecaudadoPorPeriodo(Date desde, Date hasta) {
        double total = 0;
        for (Alquiler a : alquileres) {
            if (a.getEstado() == EstadoAlquiler.FINALIZADO) {
                Date fDev = a.getFechaDevolucionReal();
                if (fDev != null && !fDev.before(desde) && !fDev.after(hasta)) {
                    total += a.getImporteTotal();
                }
            }
        }
        return total;
    }

    // Consulta 3: Porcentaje aplicable al alquiler según tipo
    public double obtenerPorcentajeAplicable(Alquiler alquiler) {
        if (alquiler instanceof AlquilerCorporativo)
            return ((AlquilerCorporativo) alquiler).getPorcentajeDescuento();
        if (alquiler instanceof AlquilerTuristico)
            return ((AlquilerTuristico) alquiler).getPorcentajeRecargo();
        return 0;
    }

    public List<Alquiler> getAlquileres()                   { return alquileres; }
    public double getPorcentajeDescuentoCorporativo()        { return porcentajeDescuentoCorporativo; }
    public void setPorcentajeDescuentoCorporativo(double p)  { this.porcentajeDescuentoCorporativo = p; }
    public double getPorcentajeRecargoTuristico()            { return porcentajeRecargoTuristico; }
    public void setPorcentajeRecargoTuristico(double p)      { this.porcentajeRecargoTuristico = p; }

    public static void resetInstancia() { instancia = null; }
}
