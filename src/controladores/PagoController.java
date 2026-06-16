package controladores;

import modelo.Pago;
import modelo.enums.EstadoPago;
import modelo.enums.MedioPago;
import modelo.enums.TipoEntidad;
import modelo.enums.TipoPago;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PagoController {
    private static PagoController instancia;
    private List<Pago> pagos;
    private int contadorId = 1;

    private PagoController() {
        pagos = new ArrayList<>();
    }

    public static PagoController getInstancia() {
        if (instancia == null) {
            instancia = new PagoController();
        }
        return instancia;
    }

    public Pago registrarPago(int idAlquiler, double importe,
                               MedioPago medioPago, TipoPago tipoPago, String usuario) {
        Pago pago = new Pago(contadorId++, new Date(), importe, medioPago, usuario, tipoPago);
        pago.confirmar();
        pagos.add(pago);
        HistorialController.getInstancia().registrar(
                "-", "CONFIRMADO", TipoEntidad.PAGO, String.valueOf(pago.getId()), usuario);
        return pago;
    }

    public void anularPago(int idPago, String usuario) {
        for (Pago p : pagos) {
            if (p.getId() == idPago) {
                String ant = p.getEstado().name();
                p.anular();
                HistorialController.getInstancia().registrar(
                        ant, "ANULADO", TipoEntidad.PAGO, String.valueOf(idPago), usuario);
                return;
            }
        }
    }

    public List<Pago> getPagos() { return pagos; }

    public static void resetInstancia() { instancia = null; }
}
