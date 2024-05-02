//Metodo para construir los gastos
//Esta sobrecargado con metodos getter y setter de cada variable

package Modelo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Gastos {

    private String comprobante;
    private String descripcion;
    private double precio;
    private int id,formaPago;
    private String fecha, hora;    

    public Gastos(String hora, String comprobante, String descripcion, double precio, int id, int formaPago, String fecha) {
        this.comprobante = comprobante;
        this.descripcion = descripcion;
        this.precio = precio;
        this.id = id;
        this.formaPago = formaPago;
        this.fecha = fecha;
        this.hora=hora;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
    
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }
    
  
    
    public Gastos() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    
}
