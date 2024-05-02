//clase para guardar cada uno de los productos que contenga la nota
//metodo sobrecargado con getters y setters para cada variable
package Modelo;

public class Detalle {
    private int id, idVenta, codigoPrecio; //id es su identificador unico, idVenta indica a que nota pertenecen
    private double cantidad;
    private String descripcion, comentario;
    private double precioUnitario, precioFinal;

    public Detalle() {
    }

    public Detalle(int id, int idVenta, int codigoPrecio, double cantidad, String descripcion, String comentario, double precioUnitario, double precioFinal) {
        this.id = id;
        this.idVenta = idVenta;
        this.codigoPrecio = codigoPrecio;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.comentario = comentario;
        this.precioUnitario = precioUnitario;
        this.precioFinal = precioFinal;
    }

    
    
    public int getCodigoPrecio() {
        return codigoPrecio;
    }

    public void setCodigoPrecio(int codigoPrecio) {
        this.codigoPrecio = codigoPrecio;
    }

    

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }
    
    
}
