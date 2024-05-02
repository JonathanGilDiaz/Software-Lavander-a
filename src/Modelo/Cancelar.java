/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

public class Cancelar {
    int id,folio,idEmpleada;
    String fecha,comentario,hora;
    double cantidad;

    public Cancelar(int id, int folio, int idEmpleada, String fecha, String comentario, String hora, double cantidad) {
        this.id = id;
        this.folio = folio;
        this.idEmpleada = idEmpleada;
        this.fecha = fecha;
        this.comentario = comentario;
        this.hora = hora;
        this.cantidad = cantidad;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    
    

    public Cancelar() {
    }

   

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

  

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public int getIdEmpleada() {
        return idEmpleada;
    }

    public void setIdEmpleada(int idEmpleada) {
        this.idEmpleada = idEmpleada;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    
}
