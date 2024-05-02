
package Modelo;

public class Nota {
    
    private double anticipo;
    private int cantidad;
    private String diaEntrega;
    private String horaEntrega;
    private String nombre;
    private String apellido;
    private int idRecibe;
    private double totalPagar;
    private double ventaTotal, iva, subtotal;
    private int codigoCLiente;
    private int folio;
    private int estado,entrega, saldo,formaPago;  //SALDO, SI ESTA PAGADO O NO, ENTREGA SI SE HA ENTREGADO O NO, ESTADO SI SE CANCELA O NO
    private String fecha,hora, entregaDia, entregaHora, comentario;

    public Nota(double anticipo, int cantidad, String diaEntrega, String horaEntrega, String nombre, String apellido, int idRecibe, double totalPagar, double ventaTotal, double iva, double subtotal, int codigoCLiente, int folio, int estado, int entrega, int saldo, int formaPago, String fecha, String hora, String entregaDia, String entregaHora, String comentario) {
        this.anticipo = anticipo;
        this.cantidad = cantidad;
        this.diaEntrega = diaEntrega;
        this.horaEntrega = horaEntrega;
        this.nombre = nombre;
        this.apellido = apellido;
        this.idRecibe = idRecibe;
        this.totalPagar = totalPagar;
        this.ventaTotal = ventaTotal;
        this.iva = iva;
        this.subtotal = subtotal;
        this.codigoCLiente = codigoCLiente;
        this.folio = folio;
        this.estado = estado;
        this.entrega = entrega;
        this.saldo = saldo;
        this.formaPago = formaPago;
        this.fecha = fecha;
        this.hora = hora;
        this.entregaDia = entregaDia;
        this.entregaHora = entregaHora;
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


    
    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }

 

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    

    
    
    public String getFecha() {
        return fecha;
    }

    public String getEntregaDia() {
        return entregaDia;
    }

    public void setEntregaDia(String entregaDia) {
        this.entregaDia = entregaDia;
    }

    public String getEntregaHora() {
        return entregaHora;
    }

    public void setEntregaHora(String entregaHora) {
        this.entregaHora = entregaHora;
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
    
    

    public int getEntrega() {
        return entrega;
    }

    public void setEntrega(int entrega) {
        this.entrega = entrega;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
    

    

    
    
    

    
    
    

   

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    

    
    
    
    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    
    
    public Nota() {
    }
    
    public double getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(double anticipo) {
        this.anticipo = anticipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDiaEntrega() {
        return diaEntrega;
    }

    public void setDiaEntrega(String diaEntrega) {
        this.diaEntrega = diaEntrega;
    }

    public String getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getIdRecibe() {
        return idRecibe;
    }

    public void setIdRecibe(int idRecibe) {
        this.idRecibe = idRecibe;
    }

  

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public double getVentaTotal() {
        return ventaTotal;
    }

    public void setVentaTotal(double ventaTotal) {
        this.ventaTotal = ventaTotal;
    }

    public int getCodigoCLiente() {
        return codigoCLiente;
    }

    public void setCodigoCLiente(int codigoCLiente) {
        this.codigoCLiente = codigoCLiente;
    }
    
    
}
