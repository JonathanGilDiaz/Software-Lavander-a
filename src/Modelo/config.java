
package Modelo;


public class config {
    private String nomnbre, rfc, razonSocial, direccion, mensaje, hora, indicadorHora;
    private String telefono, encargado, horario;
    private int letraGrande, letraChica;
    
    public config() {
    }

    public config(String nomnbre, String rfc, String razonSocial, String direccion, String mensaje, String hora, String indicadorHora, String telefono, String encargado, String horario, int letraGrande, int letraChica) {
        this.nomnbre = nomnbre;
        this.rfc = rfc;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.mensaje = mensaje;
        this.hora = hora;
        this.indicadorHora = indicadorHora;
        this.telefono = telefono;
        this.encargado = encargado;
        this.horario = horario;
        this.letraGrande = letraGrande;
        this.letraChica = letraChica;
    }

    public String getIndicadorHora() {
        return indicadorHora;
    }

    public void setIndicadorHora(String indicadorHora) {
        this.indicadorHora = indicadorHora;
    }


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    
    
    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getLetraGrande() {
        return letraGrande;
    }

    public void setLetraGrande(int letraGrande) {
        this.letraGrande = letraGrande;
    }

    public int getLetraChica() {
        return letraChica;
    }

    public void setLetraChica(int letraChica) {
        this.letraChica = letraChica;
    }

    
    
    public String getNomnbre() {
        return nomnbre;
    }

    public void setNomnbre(String nomnbre) {
        this.nomnbre = nomnbre;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
}
