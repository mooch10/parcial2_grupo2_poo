package modelo;

import modelo.enums.TipoEntidad;
import java.util.Date;

public class HistorialCambioEstado {
    private static int contadorId = 1;

    private int id;
    private Date fechaCambio;
    private String estadoAnterior;
    private String estadoNuevo;
    private TipoEntidad tipoEntidad;
    private String referenciaEntidad;
    private String usuarioResponsable;

    public HistorialCambioEstado(Date fecha, String ant, String nuevo,
                                  TipoEntidad tipo, String ref, String usr) {
        this.id = contadorId++;
        this.fechaCambio = fecha;
        this.estadoAnterior = ant;
        this.estadoNuevo = nuevo;
        this.tipoEntidad = tipo;
        this.referenciaEntidad = ref;
        this.usuarioResponsable = usr;
    }

    public int getId()                      { return id; }
    public Date getFechaCambio()            { return fechaCambio; }
    public String getEstadoAnterior()       { return estadoAnterior; }
    public String getEstadoNuevo()          { return estadoNuevo; }
    public TipoEntidad getTipoEntidad()     { return tipoEntidad; }
    public String getReferenciaEntidad()    { return referenciaEntidad; }
    public String getUsuarioResponsable()   { return usuarioResponsable; }

    @Override
    public String toString() {
        return "[" + tipoEntidad + "] " + referenciaEntidad + ": " + estadoAnterior + " -> " + estadoNuevo;
    }
}
