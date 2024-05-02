/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Jonathan Gil
 */
public class BoletoPromocion {
    int folioNota, id;
    String fecha, hora, nombre;
    double total;

    public BoletoPromocion() {
    }

    public BoletoPromocion(int folioNota, int id, String fecha, String hora, String nombre, double total) {
        this.folioNota = folioNota;
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.nombre = nombre;
        this.total = total;
    }

    public int getFolioNota() {
        return folioNota;
    }

    public void setFolioNota(int folioNota) {
        this.folioNota = folioNota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
    
}
