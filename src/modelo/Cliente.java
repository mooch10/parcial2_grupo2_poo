package modelo;

import modelo.enums.EstadoCliente;

public class Cliente {
    private String dniCuit;
    private String nombreRazonSocial;
    private String telefono;
    private String email;
    private String direccion;
    private EstadoCliente estado;
    private double creditoAFavor;

    public Cliente(String dniCuit, String nombreRazonSocial,
                   String telefono, String email, String direccion) {
        this.dniCuit = dniCuit;
        this.nombreRazonSocial = nombreRazonSocial;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.estado = EstadoCliente.INACTIVO;
        this.creditoAFavor = 0;
    }

    public void activar() {
        this.estado = EstadoCliente.ACTIVO;
    }

    public void agregarCredito(double importe) {
        this.creditoAFavor += importe;
    }

    public String getDniCuit()              { return dniCuit; }
    public String getNombreRazonSocial()    { return nombreRazonSocial; }
    public String getTelefono()             { return telefono; }
    public String getEmail()                { return email; }
    public String getDireccion()            { return direccion; }
    public EstadoCliente getEstado()        { return estado; }
    public double getCreditoAFavor()        { return creditoAFavor; }

    @Override
    public String toString() {
        return dniCuit + " - " + nombreRazonSocial;
    }
}
