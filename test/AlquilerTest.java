import modelo.*;
import modelo.enums.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlquilerTest {

    public static void main(String[] args) {
        testCalcularImporteTotalComun();
        testCalcularImporteTotalCorporativo();
        testCalcularImporteTotalTuristico();
        testVehiculoNoDisponible();
    }

    private static void testCalcularDiasAlquiler() {
        try {
            Cliente c = new Cliente("20111111111", "Juan Perez", "1111-2222", "jp@mail.com", "Calle 1");
            Vehiculo v = new Vehiculo("ABC123", "Toyota", "Corolla", 2022, 15000, 5000.0, TipoVehiculo.AUTO, TipoCombustible.NAFTA, 100, 50.0);
            Calendar cal = Calendar.getInstance();
            cal.set(2026, Calendar.AUGUST, 1, 0, 0, 0);
            Date inicio = cal.getTime();
            cal.set(2026, Calendar.AUGUST, 5, 0, 0, 0);
            Date fin = cal.getTime();

            Alquiler a = new AlquilerComun(c, v, inicio, fin, 15000);
            int dias = a.calcularDias();
            if (dias == 4) {
                System.out.println("[PASS] - testCalcularDiasAlquiler");
            } else {
                System.out.println("[FAIL] - testCalcularDiasAlquiler: Se esperaban 4 dias pero se obtuvo " + dias);
            }
        } catch (Exception e) {
            System.out.println("[FAIL] - testCalcularDiasAlquiler: Ocurrio una excepcion: " + e.getMessage());
        }
    }

    private static void testCalcularImporteTotalComun() {
        try {
            Cliente c = new Cliente("20111111111", "Juan Perez", "1111-2222", "jp@mail.com", "Calle 1");
            Vehiculo v = new Vehiculo("ABC123", "Toyota", "Corolla", 2022, 15000, 5000.0, TipoVehiculo.AUTO, TipoCombustible.NAFTA, 100, 50.0);
            Calendar cal = Calendar.getInstance();
            cal.set(2026, Calendar.AUGUST, 1, 0, 0, 0);
            Date inicio = cal.getTime();
            cal.set(2026, Calendar.AUGUST, 5, 0, 0, 0);
            Date fin = cal.getTime();

            Alquiler a = new AlquilerComun(c, v, inicio, fin, 15000);
            a.setKilometrajeFinal(15000);
            double total = a.calcularImporteTotal();
            if (Math.abs(total - 20000.0) < 0.001) {
                System.out.println("[PASS] - testCalcularImporteTotalComun");
            } else {
                System.out.println("[FAIL] - testCalcularImporteTotalComun: Se esperaba 20000.0 pero se obtuvo " + total);
            }
        } catch (Exception e) {
            System.out.println("[FAIL] - testCalcularImporteTotalComun: Ocurrio una excepcion: " + e.getMessage());
        }
    }

    private static void testCalcularImporteTotalCorporativo() {
        try {
            Cliente c = new Cliente("20111111111", "Juan Perez", "1111-2222", "jp@mail.com", "Calle 1");
            Vehiculo v = new Vehiculo("ABC123", "Toyota", "Corolla", 2022, 15000, 5000.0, TipoVehiculo.AUTO, TipoCombustible.NAFTA, 100, 50.0);
            Calendar cal = Calendar.getInstance();
            cal.set(2026, Calendar.AUGUST, 1, 0, 0, 0);
            Date inicio = cal.getTime();
            cal.set(2026, Calendar.AUGUST, 5, 0, 0, 0);
            Date fin = cal.getTime();

            Alquiler a = new AlquilerCorporativo(c, v, inicio, fin, 15000, 10.0);
            a.setKilometrajeFinal(15000);
            double total = a.calcularImporteTotal();
            if (Math.abs(total - 18000.0) < 0.001) {
                System.out.println("[PASS] - testCalcularImporteTotalCorporativo");
            } else {
                System.out.println("[FAIL] - testCalcularImporteTotalCorporativo: Se esperaba 18000.0 pero se obtuvo " + total);
            }
        } catch (Exception e) {
            System.out.println("[FAIL] - testCalcularImporteTotalCorporativo: Ocurrio una excepcion: " + e.getMessage());
        }
    }

    private static void testCalcularImporteTotalTuristico() {
        try {
            Cliente c = new Cliente("20111111111", "Juan Perez", "1111-2222", "jp@mail.com", "Calle 1");
            Vehiculo v = new Vehiculo("ABC123", "Toyota", "Corolla", 2022, 15000, 5000.0, TipoVehiculo.AUTO, TipoCombustible.NAFTA, 100, 50.0);
            Calendar cal = Calendar.getInstance();
            cal.set(2026, Calendar.AUGUST, 1, 0, 0, 0);
            Date inicio = cal.getTime();
            cal.set(2026, Calendar.AUGUST, 5, 0, 0, 0);
            Date fin = cal.getTime();

            Alquiler a = new AlquilerTuristico(c, v, inicio, fin, 15000, 15.0);
            a.setKilometrajeFinal(15000);
            double total = a.calcularImporteTotal();
            if (Math.abs(total - 23000.0) < 0.001) {
                System.out.println("[PASS] - testCalcularImporteTotalTuristico");
            } else {
                System.out.println("[FAIL] - testCalcularImporteTotalTuristico: Se esperaba 23000.0 pero se obtuvo " + total);
            }
        } catch (Exception e) {
            System.out.println("[FAIL] - testCalcularImporteTotalTuristico: Ocurrio una excepcion: " + e.getMessage());
        }
    }

    private static void testVehiculoNoDisponible() {
        try {
            Cliente c = new Cliente("20111111111", "Juan Perez", "1111-2222", "jp@mail.com", "Calle 1");
            Vehiculo v = new Vehiculo("ABC123", "Toyota", "Corolla", 2022, 15000, 5000.0, TipoVehiculo.AUTO, TipoCombustible.NAFTA, 100, 50.0);
            
            Calendar cal = Calendar.getInstance();
            cal.set(2026, Calendar.AUGUST, 5, 0, 0, 0);
            Date inicioActivo = cal.getTime();
            cal.set(2026, Calendar.AUGUST, 10, 0, 0, 0);
            Date finActivo = cal.getTime();

            Alquiler alquilerActivo = new AlquilerComun(c, v, inicioActivo, finActivo, 15000);
            alquilerActivo.cambiarEstado(EstadoAlquiler.CONFIRMADO);
            
            List<Alquiler> alquileres = new java.util.ArrayList<>();
            alquileres.add(alquilerActivo);

            cal.set(2026, Calendar.AUGUST, 8, 0, 0, 0);
            Date inicioSuperpuesto = cal.getTime();
            cal.set(2026, Calendar.AUGUST, 12, 0, 0, 0);
            Date finSuperpuesto = cal.getTime();

            boolean disponible = v.estaDisponible(inicioSuperpuesto, finSuperpuesto, alquileres);
            if (!disponible) {
                System.out.println("[PASS] - testVehiculoNoDisponible");
            } else {
                System.out.println("[FAIL] - testVehiculoNoDisponible: Se esperaba false pero se obtuvo true");
            }
        } catch (Exception e) {
            System.out.println("[FAIL] - testVehiculoNoDisponible: Ocurrio una excepcion: " + e.getMessage());
        }
    }
}
