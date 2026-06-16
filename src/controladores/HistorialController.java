package controladores;

import modelo.HistorialCambioEstado;
import modelo.enums.TipoEntidad;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistorialController {
    private static HistorialController instancia;
    private List<HistorialCambioEstado> historiales;

    private HistorialController() {
        historiales = new ArrayList<>();
    }

    public static HistorialController getInstancia() {
        if (instancia == null) {
            instancia = new HistorialController();
        }
        return instancia;
    }

    public HistorialCambioEstado registrar(String ant, String nuevo,
                                            TipoEntidad tipo, String ref, String usuario) {
        HistorialCambioEstado h = new HistorialCambioEstado(new Date(), ant, nuevo, tipo, ref, usuario);
        historiales.add(h);
        return h;
    }

    public List<HistorialCambioEstado> getHistoriales() { return historiales; }

    public static void resetInstancia() { instancia = null; }
}
