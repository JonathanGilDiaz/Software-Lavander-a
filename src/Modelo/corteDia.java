package Modelo;

public class corteDia {

    private double arqueos, saldoInicial, totalVentas, totalCobros, saldoEnCaja, totalGastos, efectivo, diferencia, retiro, saldoFinal;
    private String comentario, persona, fechaInicio, horaInicio, fechaCierre, horaCierre;
    private int id;

    public corteDia() {
    }

    public corteDia(double arqueos, double saldoInicial, double totalVentas, double totalCobros, double saldoEnCaja, double totalGastos, double efectivo, double diferencia, double retiro, double saldoFinal, String comentario, String persona, String fechaInicio, String horaInicio, String fechaCierre, String horaCierre, int id) {
        this.arqueos = arqueos;
        this.saldoInicial = saldoInicial;
        this.totalVentas = totalVentas;
        this.totalCobros = totalCobros;
        this.saldoEnCaja = saldoEnCaja;
        this.totalGastos = totalGastos;
        this.efectivo = efectivo;
        this.diferencia = diferencia;
        this.retiro = retiro;
        this.saldoFinal = saldoFinal;
        this.comentario = comentario;
        this.persona = persona;
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
        this.fechaCierre = fechaCierre;
        this.horaCierre = horaCierre;
        this.id = id;
    }

    public double getArqueos() {
        return arqueos;
    }

    public void setArqueos(double arqueos) {
        this.arqueos = arqueos;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public double getTotalCobros() {
        return totalCobros;
    }

    public void setTotalCobros(double totalCobros) {
        this.totalCobros = totalCobros;
    }

    public double getSaldoEnCaja() {
        return saldoEnCaja;
    }

    public void setSaldoEnCaja(double saldoEnCaja) {
        this.saldoEnCaja = saldoEnCaja;
    }

    public double getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(double totalGastos) {
        this.totalGastos = totalGastos;
    }

    public double getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(double efectivo) {
        this.efectivo = efectivo;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public double getRetiro() {
        return retiro;
    }

    public void setRetiro(double retiro) {
        this.retiro = retiro;
    }

    public double getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
