package modelo;

import modelo.enums.EstadoPago;
import modelo.enums.MedioPago;
import modelo.enums.TipoPago;
import java.util.Date;

public class Pago {
    private int id;
    private Date fecha;
    private double importe;
    private MedioPago medioPago;
    private EstadoPago estado;
    private String usuarioRegistrador;
    private TipoPago tipoPago;

    public Pago(int id, Date fecha, double importe,
                MedioPago medioPago, String usuario, TipoPago tipoPago) {
        this.id = id;
        this.fecha = fecha;
        this.importe = importe;
        this.medioPago = medioPago;
        this.usuarioRegistrador = usuario;
        this.tipoPago = tipoPago;
        this.estado = EstadoPago.REGISTRADO;
    }

    public void confirmar() {
        this.estado = EstadoPago.CONFIRMADO;
    }

    public void anular() {
        this.estado = EstadoPago.ANULADO;
    }

    public boolean esSeniaConfirmada() {
        return tipoPago == TipoPago.SENIA && estado == EstadoPago.CONFIRMADO;
    }

    public int getId()                      { return id; }
    public Date getFecha()                  { return fecha; }
    public double getImporte()              { return importe; }
    public MedioPago getMedioPago()         { return medioPago; }
    public EstadoPago getEstado()           { return estado; }
    public String getUsuarioRegistrador()   { return usuarioRegistrador; }
    public TipoPago getTipoPago()           { return tipoPago; }
}
