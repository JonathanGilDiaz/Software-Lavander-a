//La siguiente clase "cliente" representa los datos y la informacion cliente
//La clase esta sobrecargada, tiene un constructor vacio y uno con todos los datos de entrada
//Para cada atributo, hay un metodo get y set
package Modelo;

public class Cliente {
    
    private int id;
    private String nombre;
    private String telefono;
    private double adeudo;
    private int vecesAsistidas,estado;
    private String apellido, correo, domicilio,fecha;
    
    public Cliente() {
    }

    public Cliente(int id, String nombre, String telefono, double adeudo, int vecesAsistidas, int estado, String apellido, String correo, String domicilio, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.adeudo = adeudo;
        this.vecesAsistidas = vecesAsistidas;
        this.estado = estado;
        this.apellido = apellido;
        this.correo = correo;
        this.domicilio = domicilio;
        this.fecha = fecha;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
    
    

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getAdeudo() {
        return adeudo;
    }

    public void setAdeudo(double adeudo) {
        this.adeudo = adeudo;
    }

    public int getVecesAsistidas() {
        return vecesAsistidas;
    }

    public void setVecesAsistidas(int vecesAsistidas) {
        this.vecesAsistidas = vecesAsistidas;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    
    
}
