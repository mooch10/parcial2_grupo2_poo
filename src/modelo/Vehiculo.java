package modelo;

import modelo.enums.EstadoAlquiler;
import modelo.enums.EstadoVehiculo;
import modelo.enums.TipoCombustible;
import modelo.enums.TipoVehiculo;
import java.util.Date;
import java.util.List;

public class Vehiculo {
    private String patente;
    private String marca;
    private String modelo;
    private int anio;
    private int kilometrajeActual;
    private EstadoVehiculo estado;
    private double valorDiario;
    private TipoVehiculo tipoVehiculo;
    private TipoCombustible tipoCombustible;
    private int kmIncluidosPorDia;
    private double costoKmExcedente;

    public Vehiculo(String patente, String marca, String modelo, int anio,
                    int kilometrajeActual, double valorDiario,
                    TipoVehiculo tipoVehiculo, TipoCombustible tipoCombustible,
                    int kmIncluidosPorDia, double costoKmExcedente) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.kilometrajeActual = kilometrajeActual;
        this.valorDiario = valorDiario;
        this.tipoVehiculo = tipoVehiculo;
        this.tipoCombustible = tipoCombustible;
        this.kmIncluidosPorDia = kmIncluidosPorDia;
        this.costoKmExcedente = costoKmExcedente;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    public void cambiarEstado(EstadoVehiculo nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean estaDisponible(Date fInicio, Date fFin, List<Alquiler> alquileres) {
        if (estado == EstadoVehiculo.BAJA || estado == EstadoVehiculo.MANTENIMIENTO) {
            return false;
        }
        for (Alquiler a : alquileres) {
            if (a.getVehiculo() == this
                    && a.getEstado() != EstadoAlquiler.CANCELADO
                    && a.getEstado() != EstadoAlquiler.FINALIZADO) {
                if (a.seSuperpone(fInicio, fFin)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void actualizarKilometraje(int kmFinal) {
        this.kilometrajeActual = kmFinal;
    }

    public String getPatente()              { return patente; }
    public String getMarca()                { return marca; }
    public String getModelo()               { return modelo; }
    public int getAnio()                    { return anio; }
    public int getKilometrajeActual()       { return kilometrajeActual; }
    public EstadoVehiculo getEstado()       { return estado; }
    public double getValorDiario()          { return valorDiario; }
    public TipoVehiculo getTipoVehiculo()   { return tipoVehiculo; }
    public TipoCombustible getTipoCombustible() { return tipoCombustible; }
    public int getKmIncluidosPorDia()       { return kmIncluidosPorDia; }
    public double getCostoKmExcedente()     { return costoKmExcedente; }

    @Override
    public String toString() {
        return patente + " - " + marca + " " + modelo + " (" + anio + ")";
    }
}
