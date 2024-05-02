//Clase para construir el registro de nomina diario
//La clase esta sobrecargada y cuenta con getter y setter para cada atributo

package Modelo;

/**
 *
 * @author Jonathan Gil
 */
public class NominaDiario {
    String nombre, fecha, horaInicio, horaSalida;
    int codigoEmpleado;
    int codigo;

    public NominaDiario() {
    }

    public NominaDiario(String nombre, String fecha, String horaInicio, String horaSalida, int codigoEmpleado, int codigo) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaSalida = horaSalida;
        this.codigoEmpleado = codigoEmpleado;
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }
    
    
}
