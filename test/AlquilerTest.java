import controladores.*;
import modelo.*;
import modelo.enums.*;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlquilerTest {

    @BeforeEach
    void resetear() {
        ClienteController.resetInstancia();
        VehiculoController.resetInstancia();
        AlquilerController.resetInstancia();
        PagoController.resetInstancia();
        HistorialController.resetInstancia();
        Alquiler.resetContador();
    }

    // Test 1: registrar cliente queda ACTIVO
    @Test
    @Order(1)
    void testRegistrarCliente() {
        ClienteController ctrl = ClienteController.getInstancia();
        Cliente c = ctrl.registrarCliente("20111111111", "Juan Perez",
                "1111-2222", "jp@mail.com", "Corrientes 1", "admin");

        assertNotNull(c);
        assertEquals("20111111111", c.getDniCuit());
        assertEquals(EstadoCliente.ACTIVO, c.getEstado());
    }

    // Test 2: registrar vehiculo queda DISPONIBLE
    @Test
    @Order(2)
    void testRegistrarVehiculo() {
        VehiculoController ctrl = VehiculoController.getInstancia();
        Vehiculo v = ctrl.registrarVehiculo("ABC123", "Toyota", "Corolla", 2022,
                15000, 5000.0, TipoVehiculo.AUTO, TipoCombustible.NAFTA, 100, 50.0, "admin");

        assertNotNull(v);
        assertEquals("ABC123", v.getPatente());
        assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado());
    }

    // Test 3: solicitar alquiler con vehiculo disponible
    @Test
    @Order(3)
    void testSolicitarAlquilerDisponible() {
        ClienteController.getInstancia().registrarCliente(
                "20111111111", "Juan Perez", "1111-2222", "jp@mail.com", "Av. 1", "admin");
        VehiculoController.getInstancia().registrarVehiculo(
                "ABC123", "Toyota", "Corolla", 2022,
                15000, 5000.0, TipoVehiculo.AUTO, TipoCombustible.NAFTA, 0, 0, "admin");

        Date inicio = fecha(2026, 8, 1);
        Date fin    = fecha(2026, 8, 5);

        Alquiler a = AlquilerController.getInstancia()
                .solicitarAlquiler("20111111111", "ABC123", inicio, fin, "COMUN", "admin");

        assertNotNull(a);
        assertEquals(EstadoAlquiler.INGRESADO, a.getEstado());
        assertInstanceOf(AlquilerComun.class, a);
    }

    // Test 4: cancelar con >48h devuelve senia como credito a favor
    @Test
    @Order(4)
    void testCancelarAlquilerConCreditoAFavor() {
        ClienteController.getInstancia().registrarCliente(
                "20222222222", "Ana Garcia", "2222-3333", "ag@mail.com", "Calle 123", "admin");
        VehiculoController.getInstancia().registrarVehiculo(
                "XYZ789", "Ford", "Focus", 2021,
                20000, 4000.0, TipoVehiculo.AUTO, TipoCombustible.NAFTA, 0, 0, "admin");

        AlquilerController ctrlAlq = AlquilerController.getInstancia();
        Alquiler a = ctrlAlq.solicitarAlquiler(
                "20222222222", "XYZ789",
                fecha(2026, 9, 10), fecha(2026, 9, 12), "COMUN", "admin");

        // Confirmar con seña de $2000
        ctrlAlq.confirmarAlquilerConSenia(a.getId(), 2000.0, MedioPago.EFECTIVO, "admin");
        assertEquals(EstadoAlquiler.CONFIRMADO, a.getEstado());

        // Cancelar 5 días antes (> 48h)
        ctrlAlq.cancelarAlquiler(a.getId(), fecha(2026, 9, 5), "admin");

        assertEquals(EstadoAlquiler.CANCELADO, a.getEstado());
        Cliente cliente = ClienteController.getInstancia().buscarClientePorDniCuit("20222222222");
        assertEquals(2000.0, cliente.getCreditoAFavor(), 0.01);
    }

    // Test 5: calcular importe con descuento corporativo del 10%
    @Test
    @Order(5)
    void testCalcularImporteTotalCorporativo() {
        ClienteController.getInstancia().registrarCliente(
                "30333333333", "Empresa SA", "3333-4444", "emp@corp.com", "Av. Empresa 1", "admin");
        VehiculoController.getInstancia().registrarVehiculo(
                "DEF456", "Renault", "Kangoo", 2023,
                5000, 3000.0, TipoVehiculo.UTILITARIO, TipoCombustible.DIESEL, 0, 0, "admin");

        // 3 dias * $3000 * (1 - 10%) = $8100
        Alquiler a = AlquilerController.getInstancia().solicitarAlquiler(
                "30333333333", "DEF456",
                fecha(2026, 10, 1), fecha(2026, 10, 4), "CORPORATIVO", "admin");

        double total = a.calcularImporteTotal();
        assertEquals(8100.0, total, 0.01);
    }

    // Helper: crea un Date sin hora
    private Date fecha(int anio, int mes, int dia) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 1, dia, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
