package modelo;

import java.util.Date;

public class AlquilerComun extends Alquiler {

    public AlquilerComun(Cliente cliente, Vehiculo vehiculo,
                         Date fInicio, Date fDevEst, int kmIni) {
        super(cliente, vehiculo, fInicio, fDevEst, kmIni);
    }

    @Override
    public double calcularImporteTotal() {
        this.importeTotal = calcularImporteBase() + calcularKmExcedentes();
        return this.importeTotal;
    }
}
