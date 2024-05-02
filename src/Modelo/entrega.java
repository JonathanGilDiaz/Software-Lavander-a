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
public class entrega {
    int id,idnota,idEmpleado,formaPago;
    double cantidadAPagar, pago;
    String fechaEntrega, horaEntrega, comentario;

    public entrega() {
    }

    public entrega(int id, int idnota, int idEmpleado, int formaPago, double cantidadAPagar, double pago, String fechaEntrega, String horaEntrega, String comentario) {
        this.id = id;
        this.idnota = idnota;
        this.idEmpleado = idEmpleado;
        this.formaPago = formaPago;
        this.cantidadAPagar = cantidadAPagar;
        this.pago = pago;
        this.fechaEntrega = fechaEntrega;
        this.horaEntrega = horaEntrega;
        this.comentario = comentario;
    }

    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }

    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdnota() {
        return idnota;
    }

    public void setIdnota(int idnota) {
        this.idnota = idnota;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public double getCantidadAPagar() {
        return cantidadAPagar;
    }

    public void setCantidadAPagar(double cantidadAPagar) {
        this.cantidadAPagar = cantidadAPagar;
    }

    public double getPago() {
        return pago;
    }

    public void setPago(double pago) {
        this.pago = pago;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    
    
}
