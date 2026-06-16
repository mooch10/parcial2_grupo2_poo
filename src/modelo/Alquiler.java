package modelo;

import modelo.enums.EstadoAlquiler;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Alquiler {
    private static int contadorId = 1;

    private int id;
    private Date fechaInicio;
    private Date fechaDevolucionEstimada;
    private Date fechaDevolucionReal;
    private EstadoAlquiler estado;
    private int kilometrajeInicial;
    private int kilometrajeFinal;
    protected double importeTotal;
    private double importePendiente;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private List<Pago> pagos;

    public Alquiler(Cliente cliente, Vehiculo vehiculo,
                    Date fInicio, Date fDevEst, int kmIni) {
        this.id = contadorId++;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaInicio = fInicio;
        this.fechaDevolucionEstimada = fDevEst;
        this.kilometrajeInicial = kmIni;
        this.pagos = new ArrayList<>();
        this.estado = EstadoAlquiler.INGRESADO;
    }

    public void cambiarEstado(EstadoAlquiler nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public int calcularDias() {
        Date fin = (fechaDevolucionReal != null) ? fechaDevolucionReal : fechaDevolucionEstimada;
        long diff = fin.getTime() - fechaInicio.getTime();
        return Math.max(1, (int) (diff / (1000L * 60 * 60 * 24)));
    }

    public double calcularImporteBase() {
        return vehiculo.getValorDiario() * calcularDias();
    }

    public double calcularKmExcedentes() {
        if (vehiculo.getKmIncluidosPorDia() <= 0 || vehiculo.getCostoKmExcedente() <= 0) return 0;
        int kmRecorridos = kilometrajeFinal - kilometrajeInicial;
        int kmPermitidos = vehiculo.getKmIncluidosPorDia() * calcularDias();
        int excedentes = Math.max(0, kmRecorridos - kmPermitidos);
        return excedentes * vehiculo.getCostoKmExcedente();
    }

    public double calcularSeniaAbonada() {
        double total = 0;
        for (Pago p : pagos) {
            if (p.esSeniaConfirmada()) {
                total += p.getImporte();
            }
        }
        return total;
    }

    public double calcularSaldoPendiente() {
        return importeTotal - calcularSeniaAbonada();
    }

    public long calcularHorasAnticipacion(Date fCancelacion) {
        long diff = fechaInicio.getTime() - fCancelacion.getTime();
        return diff / (1000L * 60 * 60);
    }

    public void agregarPago(Pago pago) {
        pagos.add(pago);
    }

    public boolean seSuperpone(Date fInicio, Date fFin) {
        return !fFin.before(this.fechaInicio) && !fInicio.after(this.fechaDevolucionEstimada);
    }

    public abstract double calcularImporteTotal();

    // Getters
    public int getId()                          { return id; }
    public Date getFechaInicio()                { return fechaInicio; }
    public Date getFechaDevolucionEstimada()    { return fechaDevolucionEstimada; }
    public Date getFechaDevolucionReal()        { return fechaDevolucionReal; }
    public EstadoAlquiler getEstado()           { return estado; }
    public int getKilometrajeInicial()          { return kilometrajeInicial; }
    public int getKilometrajeFinal()            { return kilometrajeFinal; }
    public double getImporteTotal()             { return importeTotal; }
    public double getImportePendiente()         { return importePendiente; }
    public Cliente getCliente()                 { return cliente; }
    public Vehiculo getVehiculo()               { return vehiculo; }
    public List<Pago> getPagos()                { return pagos; }

    // Setters usados por el controlador al finalizar
    public void setKilometrajeFinal(int km)         { this.kilometrajeFinal = km; }
    public void setFechaDevolucionReal(Date fecha)  { this.fechaDevolucionReal = fecha; }
    public void setImporteTotal(double importe)     { this.importeTotal = importe; }
    public void setImportePendiente(double importe) { this.importePendiente = importe; }

    public static void resetContador()  { contadorId = 1; }

    @Override
    public String toString() {
        return "Alquiler #" + id + " [" + cliente.getDniCuit() + " / " + vehiculo.getPatente() + "]";
    }
}
