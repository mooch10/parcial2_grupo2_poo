package modelo;

import java.util.Date;

public class AlquilerCorporativo extends Alquiler {
    private double porcentajeDescuento;

    public AlquilerCorporativo(Cliente cliente, Vehiculo vehiculo,
                                Date fInicio, Date fDevEst, int kmIni,
                                double porcentajeDescuento) {
        super(cliente, vehiculo, fInicio, fDevEst, kmIni);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public double calcularImporteTotal() {
        double base = calcularImporteBase() + calcularKmExcedentes();
        this.importeTotal = base * (1 - porcentajeDescuento / 100.0);
        return this.importeTotal;
    }

    public double getPorcentajeDescuento()              { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double p)        { this.porcentajeDescuento = p; }
}
