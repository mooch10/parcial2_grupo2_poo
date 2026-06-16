package modelo;

import java.util.Date;

public class AlquilerTuristico extends Alquiler {
    private double porcentajeRecargo;

    public AlquilerTuristico(Cliente cliente, Vehiculo vehiculo,
                              Date fInicio, Date fDevEst, int kmIni,
                              double porcentajeRecargo) {
        super(cliente, vehiculo, fInicio, fDevEst, kmIni);
        this.porcentajeRecargo = porcentajeRecargo;
    }

    @Override
    public double calcularImporteTotal() {
        double base = calcularImporteBase() + calcularKmExcedentes();
        this.importeTotal = base * (1 + porcentajeRecargo / 100.0);
        return this.importeTotal;
    }

    public double getPorcentajeRecargo()            { return porcentajeRecargo; }
    public void setPorcentajeRecargo(double p)      { this.porcentajeRecargo = p; }
}
