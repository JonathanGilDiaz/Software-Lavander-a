/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Modelo.ArqueoDao;
import Modelo.Detalle;
import Modelo.Eventos;
import Modelo.Gastos;
import Modelo.GastosDao;
import Modelo.Nota;
import Modelo.NotaDao;
import Modelo.anticipo;
import Modelo.anticipoDao;
import Modelo.config;
import Modelo.corteDia;
import Modelo.corteDiaDao;
import Modelo.entrega;
import Modelo.establecerFecha;
import Modelo.imprimiendo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.raven.swing.Table;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class VistaCorte extends javax.swing.JDialog {

    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel modelo1 = new DefaultTableModel();
    DefaultTableModel modelo2 = new DefaultTableModel();
    DefaultTableModel modeloForma = new DefaultTableModel();
    anticipoDao anticip = new anticipoDao();
    corteDiaDao corte = new corteDiaDao();
    establecerFecha establecer = new establecerFecha();
    GastosDao gastos = new GastosDao();
    NotaDao notaDao = new NotaDao();
    Eventos event = new Eventos();
    ArqueoDao arqueoDao = new ArqueoDao();
    String hoy = corte.getDia();
    String fecha, hora, fechaInicial, fechaFinal;
    DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
    DecimalFormat df = new DecimalFormat("$ #,##0.00;($ #,##0.00)");
    corteDia corteReciente = corte.regresarCorte(corte.idMax());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalTime tiempo = LocalTime.parse(corteReciente.getHoraInicio(), formatter);
    LocalTime tiempoInicialSumado = tiempo.plusHours(1);

    config configura = gastos.buscarDatos();

    double totalArqueo, efectivoTotal, transferenciaTotal, tarjetaTotal, ventaTotal, ventaTotalTabla, abonoTotal, entregasTotal, gastosTotal, ventaEfectivo, ventaTransferencia, ventaTarjeta, abonoEfectivo, abonoTransferencia, abonoTarjeta, entregaEfectivo, entregaTransferencia, entregaTarjeta, gastosEfectivo, gastosTransferencia, gastosTarjeta = 0;

    public VistaCorte(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(200, 50);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Software Lavandería - Corte de caja");
        Seticon();

        TableVentas.setBackground(Color.WHITE);
        jScrollPane4.getViewport().setBackground(new Color(204, 204, 204));
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer tcrDerecha = new DefaultTableCellRenderer();
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        TableVentas.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TableVentas.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TableVentas.getColumnModel().getColumn(2).setCellRenderer(tcrDerecha);
        TableVentas.getColumnModel().getColumn(3).setCellRenderer(tcrDerecha);
        TableVentas.getColumnModel().getColumn(4).setCellRenderer(tcr);
        ((DefaultTableCellRenderer) TableVentas.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        TableEntregas.setBackground(Color.WHITE);
        jScrollPane6.getViewport().setBackground(new Color(204, 204, 204));
        tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tcrDerecha = new DefaultTableCellRenderer();
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        TableEntregas.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TableEntregas.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TableEntregas.getColumnModel().getColumn(2).setCellRenderer(tcrDerecha);
        TableEntregas.getColumnModel().getColumn(3).setCellRenderer(tcrDerecha);
        TableEntregas.getColumnModel().getColumn(4).setCellRenderer(tcr);
        ((DefaultTableCellRenderer) TableEntregas.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        TableGastos.setBackground(Color.WHITE);
        jScrollPane7.getViewport().setBackground(new Color(204, 204, 204));
        tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tcrDerecha = new DefaultTableCellRenderer();
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        TableGastos.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TableGastos.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TableGastos.getColumnModel().getColumn(2).setCellRenderer(tcrDerecha);
        TableGastos.getColumnModel().getColumn(3).setCellRenderer(tcr);
        ((DefaultTableCellRenderer) TableGastos.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        TablaFormas.setBackground(Color.WHITE);
        jScrollPane9.getViewport().setBackground(new Color(204, 204, 204));
        tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tcrDerecha = new DefaultTableCellRenderer();
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        TablaFormas.getColumnModel().getColumn(0).setCellRenderer(tcrDerecha);
        TablaFormas.getColumnModel().getColumn(1).setCellRenderer(tcrDerecha);
        TablaFormas.getColumnModel().getColumn(2).setCellRenderer(tcrDerecha);
        TablaFormas.getColumnModel().getColumn(3).setCellRenderer(tcrDerecha);
        ((DefaultTableCellRenderer) TablaFormas.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        vaciarDatos();

        cargando.setVisible(false);
        SetImagenLabel(cargando, "/Imagenes/loading.gif");
        // TableGastos.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 20));

    }

    public void reiniciarSumas() {
        totalArqueo = 0;
        efectivoTotal = 0;
        transferenciaTotal = 0;
        tarjetaTotal = 0;
        ventaTotal = 0;
        ventaTotalTabla = 0;
        abonoTotal = 0;
        entregasTotal = 0;
        gastosTotal = 0;
        ventaEfectivo = 0;
        ventaTransferencia = 0;
        ventaTarjeta = 0;
        abonoEfectivo = 0;
        abonoTransferencia = 0;
        abonoTarjeta = 0;
        entregaEfectivo = 0;
        entregaTransferencia = 0;
        entregaTarjeta = 0;
        gastosEfectivo = 0;
        gastosTransferencia = 0;
        gastosTarjeta = 0;

    }

    public void vaciarDatos() {

        LocalTime horaActual = LocalTime.now();
        LocalTime horaDesdeString = LocalTime.parse(configura.getHora());

        if ("mas".equals(configura.getIndicadorHora())) {
            horaActual = horaActual.plusHours(horaDesdeString.getHour())
                    .plusMinutes(horaDesdeString.getMinute())
                    .plusSeconds(horaDesdeString.getSecond());
            System.out.println(horaDesdeString);

        } else {
            horaActual = horaActual.minusHours(horaDesdeString.getHour())
                    .minusMinutes(horaDesdeString.getMinute())
                    .minusSeconds(horaDesdeString.getSecond());

        }
        LocalTime tiempo2 = LocalTime.parse(horaActual.format(formatter), formatter);
        LocalTime tiempoFinalSumado = tiempo2.plusHours(1);

        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //   fechaInicial = corteReciente.getFechaInicio() + " " + tiempoInicialSumado.format(formatter);
        // fechaFinal = horaActual.format(formatterFecha) + " " + tiempoFinalSumado.format(formatter);
        LocalDate fechaLocal = LocalDate.parse(corteReciente.getFechaInicio());
        LocalTime horaLocal = LocalTime.parse(corteReciente.getHoraInicio());
        LocalDateTime fechaInicialF = LocalDateTime.of(fechaLocal, horaLocal);
        LocalDateTime fechaFinalF = LocalDateTime.of(LocalDate.now(), horaActual);

        DateTimeFormatter formatterFechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        fechaInicial = fechaInicialF.format(formatterFechaHora);
        fechaFinal = fechaFinalF.format(formatterFechaHora);

        establecerFecha();
        reiniciarSumas();
        txtSaldoEnCaja.setText("");
        txtRetiro1.setText("");
        LimpiarTabla();
        LimpiarTabla3();
        LimpiarTabla2();
        LimpiarForma();

        listarEntregas();
        listarVentas();
        listarGastos();

        modeloForma = (DefaultTableModel) TablaFormas.getModel();
        Object[] ob = new Object[4];
        ob[0] = "$" + formateador.format(ventaEfectivo);
        ob[1] = "$" + formateador.format(ventaTransferencia);
        ob[2] = "$" + formateador.format(ventaTarjeta);
        ventaTotalTabla = ventaEfectivo + ventaTransferencia + ventaTarjeta;
        ob[3] = "$" + formateador.format(ventaTotalTabla);
        modeloForma.addRow(ob);

        ob = new Object[4];
        ob[0] = "$" + formateador.format(abonoEfectivo);
        ob[1] = "$" + formateador.format(abonoTransferencia);
        ob[2] = "$" + formateador.format(abonoTarjeta);
        abonoTotal = abonoEfectivo + abonoTransferencia + abonoTarjeta;
        ob[3] = "$" + formateador.format(abonoTotal);
        modeloForma.addRow(ob);
        ob = new Object[4];
        ob[0] = "$" + formateador.format(entregaEfectivo);
        ob[1] = "$" + formateador.format(entregaTransferencia);
        ob[2] = "$" + formateador.format(entregaTarjeta);
        entregasTotal = entregaEfectivo + entregaTransferencia + entregaTarjeta;
        ob[3] = "$" + formateador.format(entregasTotal);
        modeloForma.addRow(ob);
        ob = new Object[4];
        ob[0] = "$" + formateador.format(gastosEfectivo);
        ob[1] = "$" + formateador.format(gastosTransferencia);
        ob[2] = "$" + formateador.format(gastosTarjeta);
        gastosTotal = gastosEfectivo + gastosTransferencia + gastosTarjeta;
        ob[3] = "$" + formateador.format(gastosTotal);
        modeloForma.addRow(ob);

        ob = new Object[4];
        efectivoTotal = ventaEfectivo + abonoEfectivo + entregaEfectivo - gastosEfectivo;
        transferenciaTotal = ventaTransferencia + abonoTransferencia + entregaTransferencia - gastosTransferencia;
        tarjetaTotal = ventaTarjeta + entregaTarjeta + abonoTarjeta - gastosTarjeta;

        ob[0] = "$" + formateador.format(efectivoTotal);
        ob[1] = "$" + formateador.format(transferenciaTotal);
        ob[2] = "$" + formateador.format(tarjetaTotal);
        ob[3] = "";
        modeloForma.addRow(ob);
        TablaFormas.setModel(modeloForma);

        txtSaldoInicial.setText("$" + formateador.format(corte.getSaldoInicial()));

        txtSaldoCaja1.setText("$" + formateador.format(regresarDinero(true) + corte.getSaldoInicial()));
        txtTotalVentas.setText("$" + formateador.format(regresarVentas()));
        txtTotalCobros1.setText("$" + formateador.format(regresarDinero(false)));

        Date diaActual = StringADate(fecha);
        DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
        String fechaUsar;
        Date fechaNueva;
        Calendar c = Calendar.getInstance();
        c.setTime(diaActual);
        fechaUsar = cam.format(diaActual);
        txtFecha.setText(fechaUsar);
    }

    public java.util.Date StringADate(String cambiar) { //Este metodo te transforma un String a date (dia)
        SimpleDateFormat formato_del_Texto = new SimpleDateFormat("yyyy-MM-dd"); //El formato del string
        Date fechaE = null;
        try {
            fechaE = formato_del_Texto.parse(cambiar);
            return fechaE; //retornamos la fecha
        } catch (ParseException ex) {
            return null;
        }
    }

    public double regresarGastos() {
        double gastosF = 0;

        List<Gastos> ListaClGastos = gastos.listarGastosPorDiaHora(fechaInicial, fechaFinal);
        for (int i = 0; i < ListaClGastos.size(); i++) {
            gastosF = gastosF + ListaClGastos.get(i).getPrecio();
        }
        return gastosF;

    }

    public void SetImagenLabel(JLabel labelName, String root) {
        labelName.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(root)).getImage().getScaledInstance(labelName.getWidth(), labelName.getHeight(), java.awt.Image.SCALE_DEFAULT)));
    }

    public void proceso() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoginN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void metodoEfectivoCaja(char c) {

        double b = 0;
        if (!"".equals(txtSaldoEnCaja.getText())) {
            b = Double.parseDouble(txtSaldoEnCaja.getText() + c);
        }

        //double a =Double.parseDouble(txtSaldoCaja.getText());
        double a = regresarDinero(true) + corte.getSaldoInicial();
        double cantidad1 = a - b;
        double b2;
        if (!"".equals(txtRetiro1.getText())) {
            b2 = Double.parseDouble(txtRetiro1.getText());
        } else {
            b2 = 0;
        }

        double cantidad2 = b - b2;
        txtSaldoFInal1.setText("$" + formateador.format(cantidad2));
    }

    public double regresarVentas() {
        List<Nota> ListaCl = notaDao.listarNotasPorDiaHora(fechaInicial, fechaFinal);
        for (int i = 0; i < ListaCl.size(); i++) {
            ventaTotal = ventaTotal + ListaCl.get(i).getVentaTotal();
        }

        List<Nota> ListaAntiCl = notaDao.listarAntiNotasPorDiaHora(fechaInicial, fechaFinal);
        for (int i = 0; i < ListaAntiCl.size(); i++) {
            ventaTotal = ventaTotal + ListaAntiCl.get(i).getVentaTotal();
        }

        return ventaTotal;
    }

    public static String removefirstChar(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(1);
    }

    public double regresarDinero(boolean indicador) { //true saldo en caja, false solo cobros
        double dinero = 0;
        List<anticipo> listaAn = anticip.listarAnticipoPorDiaHora(fechaInicial, fechaFinal);
        List<Nota> ListaClVentas = notaDao.listarNotasPorDiaHora(fechaInicial, fechaFinal);
        List<Nota> ListaCl = notaDao.listarNotasEntregaPorDiaHora(fechaInicial, fechaFinal);
        List<entrega> ListaEntregas = notaDao.listarNotasEntregas();
        List<Gastos> ListaClGastos = gastos.listarGastosPorDiaHora(fechaInicial, fechaFinal);
        for (int i = 0; i < ListaClVentas.size(); i++) {
            if (indicador == true) {//Saldo en caja
                if (ListaClVentas.get(i).getFormaPago() == 1) {
                    dinero = dinero + ListaClVentas.get(i).getAnticipo();
                }
            } else {
                dinero = dinero + ListaClVentas.get(i).getAnticipo();
            }
        }
        List<Nota> ListaAntiCl = notaDao.listarAntiNotasPorDiaHora(fechaInicial, fechaFinal);
        for (int i = 0; i < ListaAntiCl.size(); i++) {
            dinero = dinero + ListaAntiCl.get(i).getAnticipo();
        }

        for (int i = 0; i < ListaCl.size(); i++) {
            for (int k = 0; k < ListaEntregas.size(); k++) {
                if (ListaEntregas.get(k).getIdnota() == ListaCl.get(i).getFolio()) {
                    double cantidadPago = ListaEntregas.get(k).getPago();
                    if (indicador == true) {//Saldo en caja
                        if (ListaEntregas.get(k).getFormaPago() == 1) {
                            dinero = dinero + cantidadPago;
                        }
                    } else {
                        dinero = dinero + cantidadPago;
                    }
                }
            }
        }

        for (int i = 0; i < listaAn.size(); i++) {
            if (indicador == true) {//Saldo en caja
                if (listaAn.get(i).getFormaPago() == 1) {
                    dinero = dinero + listaAn.get(i).getCantidad();
                }
            } else {
                dinero = dinero + listaAn.get(i).getCantidad();
            }
        }

        if (indicador == true) {//Saldo en caja
            for (int i = 0; i < ListaClGastos.size(); i++) {
                if (ListaClGastos.get(i).getFormaPago() == 1) //efectivo
                {
                    dinero = dinero - ListaClGastos.get(i).getPrecio();
                }
            }
        }
        totalArqueo = arqueoDao.totalArqueos(corte.idMax());
        if (indicador == true) {
            dinero = dinero - totalArqueo;
        }
        txtTotalArqueos.setText("$" + formateador.format(totalArqueo));
        return dinero;
    }

    public void listarGastos() {
        List<Gastos> ListaCl = gastos.listarGastosPorDiaHora(fechaInicial, fechaFinal);
        modelo2 = (DefaultTableModel) TableGastos.getModel();
        Object[] ob = new Object[4];
        for (int i = 0; i < ListaCl.size(); i++) {
            ob[0] = ListaCl.get(i).getComprobante();
            ob[1] = ListaCl.get(i).getDescripcion();
            ob[2] = "$" + formateador.format(ListaCl.get(i).getPrecio());
            switch (ListaCl.get(i).getFormaPago()) {
                case 1:
                    gastosEfectivo = gastosEfectivo + ListaCl.get(i).getPrecio();
                    break;
                case 3:
                    gastosTransferencia = gastosTransferencia + ListaCl.get(i).getPrecio();
                    break;
                case 4:
                    gastosTarjeta = gastosTarjeta + ListaCl.get(i).getPrecio();
                    break;
            }
            ob[3] = "0" + ListaCl.get(i).getFormaPago();
            modelo2.addRow(ob);
        }
        TableGastos.setModel(modelo2);
    }

    public void establecerFecha() { //Establecemos la fecha a usar, esto se hace ya que hasta que no se cierre caja, no se puede avanzar de dia
        List<String> datos = establecer.establecerFecha();
        fecha = datos.get(0);
        hora = datos.get(1);

    }

    public void LimpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    public void LimpiarTabla2() {
        for (int i = 0; i < modelo1.getRowCount(); i++) {
            modelo1.removeRow(i);
            i = i - 1;
        }
    }

    public void LimpiarTabla3() {
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            modelo2.removeRow(i);
            i = i - 1;
        }
    }

    public void LimpiarForma() {
        for (int i = 0; i < modeloForma.getRowCount(); i++) {
            modeloForma.removeRow(i);
            i = i - 1;
        }
    }

    public void listarVentas() {
        List<Nota> ListaCl = notaDao.listarNotasPorDiaHora(fechaInicial, fechaFinal);
        modelo1 = (DefaultTableModel) TableVentas.getModel();
        Object[] ob = new Object[5];
        for (int i = 0; i < ListaCl.size(); i++) {
            ob[0] = ListaCl.get(i).getFolio();
            ob[1] = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
            ob[2] = "$" + formateador.format(ListaCl.get(i).getVentaTotal());
            ob[3] = "$" + formateador.format(ListaCl.get(i).getAnticipo());
            switch (ListaCl.get(i).getFormaPago()) {
                case 1:
                    ventaEfectivo = ventaEfectivo + ListaCl.get(i).getAnticipo();
                    break;
                case 3:
                    ventaTransferencia = ventaTransferencia + ListaCl.get(i).getAnticipo();
                    break;
                case 4:
                    ventaTarjeta = ventaTarjeta + ListaCl.get(i).getAnticipo();
                    break;
            }
            ob[4] = "0" + ListaCl.get(i).getFormaPago();
            modelo1.addRow(ob);
        }

        List<Nota> ListaAntiCl = notaDao.listarAntiNotasPorDiaHora(fechaInicial, fechaFinal);
        for (int i = 0; i < ListaAntiCl.size(); i++) {
            ob[0] = ListaAntiCl.get(i).getFolio();
            ob[1] = ListaAntiCl.get(i).getNombre() + " " + ListaAntiCl.get(i).getApellido();
            ob[2] = "$" + formateador.format(ListaAntiCl.get(i).getVentaTotal());
            ob[3] = "$" + formateador.format(ListaAntiCl.get(i).getAnticipo());
            ob[4] = "01";
            ventaEfectivo = ventaEfectivo + ListaAntiCl.get(i).getAnticipo();
            modelo1.addRow(ob);
        }

        TableVentas.setModel(modelo1);
    }

    public void listarEntregas() {

        List<Nota> ListaCl = notaDao.listarNotasEntregaPorDiaHora(fechaInicial, fechaFinal);
        List<entrega> ListaEntregas = notaDao.listarNotasEntregas();
        List<anticipo> listaAn = anticip.listarAnticipoPorDiaHora(fechaInicial, fechaFinal);
        modelo = (DefaultTableModel) TableEntregas.getModel();
        Object[] ob = new Object[5];
        for (int i = 0; i < ListaCl.size(); i++) {
            ob[0] = ListaCl.get(i).getFolio();
            ob[1] = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
            ob[2] = "$" + formateador.format(ListaCl.get(i).getVentaTotal());
            for (int k = 0; k < ListaEntregas.size(); k++) {
                if (ListaEntregas.get(k).getIdnota() == ListaCl.get(i).getFolio()) {
                    double cantidadPago = ListaEntregas.get(k).getPago();
                    if (cantidadPago == 0) {
                        ob[3] = "---";
                    } else {
                        ob[3] = "$" + formateador.format(cantidadPago);
                        switch (ListaEntregas.get(k).getFormaPago()) {
                            case 1:
                                entregaEfectivo = entregaEfectivo + ListaEntregas.get(k).getPago();
                                ;
                                break;
                            case 3:
                                entregaTransferencia = entregaTransferencia + ListaEntregas.get(k).getPago();
                                break;
                            case 4:
                                entregaTarjeta = entregaTarjeta + ListaEntregas.get(k).getPago();
                                break;
                        }

                    }
                    ob[4] = "0" + ListaEntregas.get(k).getFormaPago();
                }
            }
            modelo.addRow(ob);
        }

        for (int i = 0; i < listaAn.size(); i++) {
            Nota no = notaDao.buscarPorFolio(listaAn.get(i).getFolio());
            ob[0] = listaAn.get(i).getFolio();
            ob[1] = listaAn.get(i).getNombre() + listaAn.get(i).getApellido() + " (ABONO)";
            ob[2] = "$" + formateador.format(no.getVentaTotal());
            ob[3] = "$" + formateador.format(listaAn.get(i).getCantidad());
            switch (listaAn.get(i).getFormaPago()) {
                case 1:
                    abonoEfectivo = abonoEfectivo + listaAn.get(i).getCantidad();
                    ;
                    break;
                case 3:
                    abonoTransferencia = abonoTransferencia + listaAn.get(i).getCantidad();
                    break;
                case 4:
                    abonoTarjeta = abonoTarjeta + listaAn.get(i).getCantidad();
                    break;

            }
            ob[4] = "0" + listaAn.get(i).getFormaPago();
            modelo.addRow(ob);
        }
        TableEntregas.setModel(modelo);
    }

    public void eventoF2() {
        vistaGasto vg = new vistaGasto(new javax.swing.JFrame(), true);
        vg.setVisible(true);
        if (vg.indicador == true) {
            vaciarDatos();
        }
    }

    public void eventoF3() throws DocumentException {
        if (!"".equals(txtSaldoEnCaja.getText()) && !"".equals(txtRetiro1.getText())) {
            corteDiafinal(corte.idMax(), 0);//0 es prueba
        }
    }

    public void eventoF4() {
        vistaArqueosDia vC1 = new vistaArqueosDia(new javax.swing.JFrame(), true);
        vC1.setLocationRelativeTo(null);
        vC1.getSaldoCaja(regresarDinero(true) + corte.getSaldoInicial());
        vC1.setVisible(true);
        vaciarDatos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JLabel();
        panelRegistrarNota1 = new javax.swing.JPanel();
        jButton15 = new javax.swing.JLabel();
        panelRegistrarNota3 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JLabel();
        panelRegistrarNota4 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        txtSaldoInicial = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTotalCobros1 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtSaldoCaja1 = new javax.swing.JTextField();
        txtSaldoEnCaja = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtSaldoFInal1 = new javax.swing.JTextField();
        txtRetiro1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        txtTotalArqueos = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtTotalVentas = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TableEntregas = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableVentas = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TableGastos = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        cargando = new javax.swing.JLabel();
        panelRegistrarNota5 = new javax.swing.JPanel();
        jButton19 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        TablaFormas = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CORTE DE CAJA");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1017, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-5, 0, 1310, 50));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText(" FECHA ACTIVA:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 70, 150, 20));

        txtFecha.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtFecha.setText("-");
        jPanel1.add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 70, 100, 20));

        panelRegistrarNota1.setBackground(new java.awt.Color(255, 255, 255));
        panelRegistrarNota1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelRegistrarNota1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRegistrarNota1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelRegistrarNota1MouseEntered(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(153, 204, 255));
        jButton15.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar nota 2.png"))); // NOI18N
        jButton15.setToolTipText("F1 - Cerrar corte");
        jButton15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton15MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton15MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton15MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelRegistrarNota1Layout = new javax.swing.GroupLayout(panelRegistrarNota1);
        panelRegistrarNota1.setLayout(panelRegistrarNota1Layout);
        panelRegistrarNota1Layout.setHorizontalGroup(
            panelRegistrarNota1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelRegistrarNota1Layout.setVerticalGroup(
            panelRegistrarNota1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelRegistrarNota1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 50, 50));

        panelRegistrarNota3.setBackground(new java.awt.Color(255, 255, 255));
        panelRegistrarNota3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelRegistrarNota3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRegistrarNota3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelRegistrarNota3MouseEntered(evt);
            }
        });

        jButton17.setBackground(new java.awt.Color(255, 255, 255));
        jButton17.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ENRROLLADO.jpg"))); // NOI18N
        jButton17.setToolTipText("F2 - Registrar un gasto");
        jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton17MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton17MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton17MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelRegistrarNota3Layout = new javax.swing.GroupLayout(panelRegistrarNota3);
        panelRegistrarNota3.setLayout(panelRegistrarNota3Layout);
        panelRegistrarNota3Layout.setHorizontalGroup(
            panelRegistrarNota3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelRegistrarNota3Layout.setVerticalGroup(
            panelRegistrarNota3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelRegistrarNota3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 50, 50));

        panelRegistrarNota4.setBackground(new java.awt.Color(255, 255, 255));
        panelRegistrarNota4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelRegistrarNota4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRegistrarNota4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelRegistrarNota4MouseEntered(evt);
            }
        });

        jButton18.setBackground(new java.awt.Color(255, 255, 255));
        jButton18.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jButton18.setForeground(new java.awt.Color(255, 255, 255));
        jButton18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ESTADISTICAS.png"))); // NOI18N
        jButton18.setToolTipText("F3 - Visualizar el pdf del corte actual");
        jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton18MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton18MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton18MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelRegistrarNota4Layout = new javax.swing.GroupLayout(panelRegistrarNota4);
        panelRegistrarNota4.setLayout(panelRegistrarNota4Layout);
        panelRegistrarNota4Layout.setHorizontalGroup(
            panelRegistrarNota4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelRegistrarNota4Layout.setVerticalGroup(
            panelRegistrarNota4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelRegistrarNota4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 50, 50));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel8.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 47, 130, 10));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel10.setText("Saldo Inicial");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel8.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 20));

        txtSaldoInicial.setBackground(new java.awt.Color(204, 204, 204));
        txtSaldoInicial.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtSaldoInicial.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSaldoInicial.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSaldoInicial.setEnabled(false);
        txtSaldoInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaldoInicialActionPerformed(evt);
            }
        });
        jPanel8.add(txtSaldoInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 120, -1));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel12.setText("  Cobros Total");
        jPanel8.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 32, 80, 20));

        txtTotalCobros1.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalCobros1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtTotalCobros1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalCobros1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTotalCobros1.setEnabled(false);
        txtTotalCobros1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalCobros1ActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotalCobros1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 120, -1));

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel27.setText("    Saldo Corte");
        jLabel27.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel8.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 6, 80, 20));

        txtSaldoCaja1.setBackground(new java.awt.Color(204, 204, 204));
        txtSaldoCaja1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtSaldoCaja1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSaldoCaja1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSaldoCaja1.setEnabled(false);
        txtSaldoCaja1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaldoCaja1ActionPerformed(evt);
            }
        });
        jPanel8.add(txtSaldoCaja1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 5, 130, 22));

        txtSaldoEnCaja.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtSaldoEnCaja.setToolTipText("Introduce la cantidad de efectivo en caja");
        txtSaldoEnCaja.setBorder(null);
        txtSaldoEnCaja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSaldoEnCajaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSaldoEnCajaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSaldoEnCajaKeyTyped(evt);
            }
        });
        jPanel8.add(txtSaldoEnCaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 27, 130, 20));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel15.setText("            Efectivo");
        jLabel15.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel8.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 27, 80, 20));

        txtSaldoFInal1.setBackground(new java.awt.Color(204, 204, 204));
        txtSaldoFInal1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtSaldoFInal1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSaldoFInal1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSaldoFInal1.setEnabled(false);
        jPanel8.add(txtSaldoFInal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 74, 130, 22));

        txtRetiro1.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtRetiro1.setToolTipText("Introduce la cantidad a retirar de la caja");
        txtRetiro1.setBorder(null);
        txtRetiro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRetiro1ActionPerformed(evt);
            }
        });
        txtRetiro1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRetiro1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRetiro1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRetiro1KeyTyped(evt);
            }
        });
        jPanel8.add(txtRetiro1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 130, 20));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel17.setText("        Saldo Final");
        jLabel17.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel8.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 90, 20));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel16.setText("          Retiro");
        jLabel16.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel8.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 70, 20));
        jPanel8.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, 130, 10));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Arqueos  ");
        jPanel8.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 72, 80, 20));

        txtTotalArqueos.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalArqueos.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtTotalArqueos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalArqueos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTotalArqueos.setEnabled(false);
        txtTotalArqueos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalArqueosActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotalArqueos, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 120, -1));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel22.setText("  Ventas Total");
        jPanel8.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 52, 80, 20));

        txtTotalVentas.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalVentas.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtTotalVentas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalVentas.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTotalVentas.setEnabled(false);
        txtTotalVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalVentasActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotalVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 120, -1));

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(865, 465, 423, 101));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setBackground(new java.awt.Color(0, 0, 204));
        jLabel25.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 10)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 255));
        jLabel25.setText("Reporte de Entregas");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        TableEntregas = new Table();
        TableEntregas.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        TableEntregas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Cliente", "Total ", "Pago", "Fpa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableEntregas.setToolTipText("Haz doble click para visualizar la nota");
        TableEntregas.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TableEntregas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableEntregasMouseClicked(evt);
            }
        });
        TableEntregas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableEntregasKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(TableEntregas);
        if (TableEntregas.getColumnModel().getColumnCount() > 0) {
            TableEntregas.getColumnModel().getColumn(0).setPreferredWidth(10);
            TableEntregas.getColumnModel().getColumn(1).setResizable(false);
            TableEntregas.getColumnModel().getColumn(1).setPreferredWidth(130);
            TableEntregas.getColumnModel().getColumn(2).setPreferredWidth(35);
            TableEntregas.getColumnModel().getColumn(3).setPreferredWidth(35);
            TableEntregas.getColumnModel().getColumn(4).setPreferredWidth(1);
        }

        jPanel2.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 30, 413, 420));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(439, 105, 423, 460));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setBackground(new java.awt.Color(0, 0, 204));
        jLabel26.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 10)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 255));
        jLabel26.setText("Reporte de ventas");
        jPanel4.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        TableVentas = new Table();
        TableVentas.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        TableVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Cliente", "Total", "Pago", "Fpa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableVentas.setToolTipText("Haz doble click para visualizar la nota");
        TableVentas.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TableVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableVentasMouseClicked(evt);
            }
        });
        TableVentas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableVentasKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(TableVentas);
        if (TableVentas.getColumnModel().getColumnCount() > 0) {
            TableVentas.getColumnModel().getColumn(0).setPreferredWidth(10);
            TableVentas.getColumnModel().getColumn(1).setPreferredWidth(130);
            TableVentas.getColumnModel().getColumn(2).setPreferredWidth(35);
            TableVentas.getColumnModel().getColumn(3).setPreferredWidth(35);
            TableVentas.getColumnModel().getColumn(4).setPreferredWidth(1);
        }

        jPanel4.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 30, 413, 420));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 423, 460));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableGastos = new Table();
        TableGastos.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        TableGastos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Comprobante", "Concepto", "Pago", "Fpa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableGastos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TableGastos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableGastosKeyPressed(evt);
            }
        });
        jScrollPane7.setViewportView(TableGastos);
        if (TableGastos.getColumnModel().getColumnCount() > 0) {
            TableGastos.getColumnModel().getColumn(0).setPreferredWidth(85);
            TableGastos.getColumnModel().getColumn(1).setPreferredWidth(85);
            TableGastos.getColumnModel().getColumn(2).setPreferredWidth(70);
            TableGastos.getColumnModel().getColumn(3).setPreferredWidth(1);
        }

        jPanel5.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 30, 413, 160));

        jLabel28.setBackground(new java.awt.Color(0, 0, 204));
        jLabel28.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 10)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 255));
        jLabel28.setText("Reporte de Gastos");
        jPanel5.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(865, 105, 423, 200));

        cargando.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.add(cargando, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, 70, 40));

        panelRegistrarNota5.setBackground(new java.awt.Color(255, 255, 255));
        panelRegistrarNota5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelRegistrarNota5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRegistrarNota5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelRegistrarNota5MouseEntered(evt);
            }
        });

        jButton19.setBackground(new java.awt.Color(255, 255, 255));
        jButton19.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jButton19.setForeground(new java.awt.Color(255, 255, 255));
        jButton19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/arqueo.png"))); // NOI18N
        jButton19.setToolTipText("F4 - Arqueo de caja");
        jButton19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton19MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton19MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton19MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelRegistrarNota5Layout = new javax.swing.GroupLayout(panelRegistrarNota5);
        panelRegistrarNota5.setLayout(panelRegistrarNota5Layout);
        panelRegistrarNota5Layout.setHorizontalGroup(
            panelRegistrarNota5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelRegistrarNota5Layout.setVerticalGroup(
            panelRegistrarNota5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelRegistrarNota5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 50, 50));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaFormas = new Table();
        TablaFormas.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        TablaFormas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Efectivo", "Trans.", "Tarjeta", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaFormas.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TablaFormas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablaFormasKeyPressed(evt);
            }
        });
        jScrollPane9.setViewportView(TablaFormas);
        if (TablaFormas.getColumnModel().getColumnCount() > 0) {
            TablaFormas.getColumnModel().getColumn(0).setPreferredWidth(15);
            TablaFormas.getColumnModel().getColumn(1).setPreferredWidth(15);
            TablaFormas.getColumnModel().getColumn(2).setPreferredWidth(15);
            TablaFormas.getColumnModel().getColumn(3).setPreferredWidth(15);
        }

        jPanel7.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 30, 350, 110));

        jLabel30.setBackground(new java.awt.Color(0, 0, 204));
        jLabel30.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 10)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 255));
        jLabel30.setText("Formas de pago");
        jPanel7.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Ventas");
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel7.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 52, 50, 12));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Abonos");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 50, 12));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Entregas");
        jPanel7.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 87, 50, 12));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Saldos");
        jPanel7.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 121, 50, 12));

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("   Gastos");
        jPanel7.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 104, 50, 12));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(865, 310, 423, 150));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseClicked
        try {
            eventoF1();
        } catch (DocumentException ex) {
            Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton15MouseClicked

    private void jButton15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseEntered
        if (!"".equals(txtSaldoEnCaja.getText()) && !"".equals(txtRetiro1.getText())) {
            panelRegistrarNota1.setBackground(new Color(153, 204, 255));
            jButton15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }
        jButton15.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jButton15MouseEntered

    private void jButton15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseExited
        panelRegistrarNota1.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton15MouseExited

    private void panelRegistrarNota1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota1MouseClicked

    private void panelRegistrarNota1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota1MouseEntered

    private void jButton17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseClicked
        eventoF2();
    }//GEN-LAST:event_jButton17MouseClicked

    private void jButton17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseEntered

        panelRegistrarNota3.setBackground(new Color(153, 204, 255));
        jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    }//GEN-LAST:event_jButton17MouseEntered

    private void jButton17MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseExited
        panelRegistrarNota5.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton17MouseExited

    private void panelRegistrarNota3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota3MouseClicked

    private void panelRegistrarNota3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota3MouseEntered

    private void jButton18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseClicked

        try {
            eventoF3();
        } catch (DocumentException ex) {
            Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton18MouseClicked

    private void jButton18MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseEntered
        if (!"".equals(txtSaldoEnCaja.getText()) && !"".equals(txtRetiro1.getText())) {
            panelRegistrarNota4.setBackground(new Color(153, 204, 255));
            jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }
        jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jButton18MouseEntered

    private void jButton18MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseExited
        panelRegistrarNota4.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton18MouseExited

    private void panelRegistrarNota4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota4MouseClicked

    private void panelRegistrarNota4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota4MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota4MouseEntered

    private void txtTotalCobros1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalCobros1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalCobros1ActionPerformed

    private void txtSaldoEnCajaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSaldoEnCajaKeyPressed

        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1: {
                try {
                    eventoF1();
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3: {
                try {
                    eventoF3();
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_txtSaldoEnCajaKeyPressed

    private void txtSaldoEnCajaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSaldoEnCajaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoEnCajaKeyReleased

    private void txtSaldoEnCajaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSaldoEnCajaKeyTyped
        char c = event.numberDecimalKeyPress(evt, txtSaldoEnCaja);
        metodoEfectivoCaja(c);
    }//GEN-LAST:event_txtSaldoEnCajaKeyTyped

    private void txtRetiro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRetiro1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRetiro1ActionPerformed

    private void txtRetiro1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRetiro1KeyPressed

        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1: {
                try {
                    eventoF1();
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3: {
                try {
                    eventoF3();
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_txtRetiro1KeyPressed

    private void txtRetiro1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRetiro1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRetiro1KeyReleased

    private void txtRetiro1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRetiro1KeyTyped
        char c = event.numberDecimalKeyPress(evt, txtRetiro1);
        double alfa = 0;
        double beta = 0;
        if (!"".equals(txtSaldoEnCaja.getText())) {
            alfa = Double.parseDouble(txtSaldoEnCaja.getText());
        }
        if (!"".equals(txtRetiro1.getText())) {
            beta = Double.parseDouble(txtRetiro1.getText() + c);
        }
        double cantidad1 = alfa - beta;
        txtSaldoFInal1.setText("$" + formateador.format(cantidad1));
    }//GEN-LAST:event_txtRetiro1KeyTyped

    private void TableEntregasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableEntregasMouseClicked
        if (evt.getClickCount() == 2) {
            int fila = TableEntregas.rowAtPoint(evt.getPoint());
            int buscando = Integer.parseInt(TableEntregas.getValueAt(fila, 0).toString());
            vistaNota vN = new vistaNota(new javax.swing.JFrame(), true);
            vN.validandoDatos(buscando, false);
            vN.setVisible(true);

        }
    }//GEN-LAST:event_TableEntregasMouseClicked

    private void TableEntregasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableEntregasKeyPressed

        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1: {
                try {
                    eventoF1();
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3: {
                try {
                    eventoF3();
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_TableEntregasKeyPressed

    private void TableVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentasMouseClicked
        if (evt.getClickCount() == 2) {
            int fila = TableVentas.rowAtPoint(evt.getPoint());
            int buscando = Integer.parseInt(TableVentas.getValueAt(fila, 0).toString());
            vistaNota vN = new vistaNota(new javax.swing.JFrame(), true);
            vN.validandoDatos(buscando, false);
            vN.setVisible(true);

        }
    }//GEN-LAST:event_TableVentasMouseClicked

    private void TableVentasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableVentasKeyPressed

        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1: {
                try {
                    eventoF1();
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3: {
                try {
                    eventoF3();
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_TableVentasKeyPressed

    private void TableGastosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableGastosKeyPressed

        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1: {
                try {
                    eventoF1();
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3: {
                try {
                    eventoF3();
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_TableGastosKeyPressed

    private void txtSaldoCaja1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaldoCaja1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoCaja1ActionPerformed

    private void jButton19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseClicked
        eventoF4();
    }//GEN-LAST:event_jButton19MouseClicked

    private void jButton19MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseEntered
        panelRegistrarNota5.setBackground(new Color(153, 204, 255));
        jButton19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    }//GEN-LAST:event_jButton19MouseEntered

    private void jButton19MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseExited
        panelRegistrarNota5.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton19MouseExited

    private void panelRegistrarNota5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota5MouseClicked

    private void panelRegistrarNota5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota5MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota5MouseEntered

    private void TablaFormasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablaFormasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TablaFormasKeyPressed

    private void txtTotalArqueosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalArqueosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalArqueosActionPerformed

    private void txtTotalVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalVentasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalVentasActionPerformed

    private void txtSaldoInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaldoInicialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoInicialActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaCorte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaCorte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaCorte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaCorte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VistaCorte dialog = new VistaCorte(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaFormas;
    private javax.swing.JTable TableEntregas;
    private javax.swing.JTable TableGastos;
    private javax.swing.JTable TableVentas;
    private javax.swing.JLabel cargando;
    private javax.swing.JLabel jButton15;
    private javax.swing.JLabel jButton17;
    private javax.swing.JLabel jButton18;
    private javax.swing.JLabel jButton19;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPanel panelRegistrarNota1;
    private javax.swing.JPanel panelRegistrarNota3;
    private javax.swing.JPanel panelRegistrarNota4;
    private javax.swing.JPanel panelRegistrarNota5;
    private javax.swing.JLabel txtFecha;
    private javax.swing.JTextField txtRetiro1;
    private javax.swing.JTextField txtSaldoCaja1;
    private javax.swing.JTextField txtSaldoEnCaja;
    private javax.swing.JTextField txtSaldoFInal1;
    private javax.swing.JTextField txtSaldoInicial;
    private javax.swing.JTextField txtTotalArqueos;
    private javax.swing.JTextField txtTotalCobros1;
    private javax.swing.JTextField txtTotalVentas;
    // End of variables declaration//GEN-END:variables

    private void Seticon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
    }

    public String fechaFormatoCorrecto(String fechaHoy) {
        Date diaActual = StringADate(fechaHoy);
        DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
        String fechaUsar;
        Date fechaNueva;
        Calendar c = Calendar.getInstance();
        c.setTime(diaActual);
        fechaUsar = cam.format(diaActual);
        return fechaUsar;
    }

    public java.util.Date StringADateHora(String cambiar) {//Este metodo te transforma un String a date (hora)
        SimpleDateFormat formato_del_Texto = new SimpleDateFormat("HH:mm");
        Date fechaE = null;
        try {
            fechaE = formato_del_Texto.parse(cambiar);
            return fechaE;
        } catch (ParseException ex) {
            return null;
        }
    }

    private static CellStyle getContabilidadCellStyle(Workbook workbook, DecimalFormat df) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat(df.format(0)));
        return style;
    }

    public void corteDiafinal(int corteUsar, int prueba) throws DocumentException {
        try {

            corteDia cd = corte.regresarCorte(corteUsar);
            if (prueba == 0) {
                cd.setFechaCierre(cd.getFechaInicio());
                cd.setHoraCierre("23:59:59");
            }
            Formatter obj = new Formatter();
            Formatter obj2 = new Formatter();
            String fechaHoy = cd.getFechaCierre();

            LocalDateTime m = LocalDateTime.now(); //Obtenemos la fecha actual
            String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
            String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
            String DiagHoy = dia + "-" + mes + "-" + m.getYear();
            String fechaFinalBien = fechaFormatoCorrecto(fechaHoy);
            String fechaInicioBien = fechaFormatoCorrecto(cd.getFechaInicio());
            Date horaFecha = StringADateHora(cd.getHoraInicio());
            Formatter objHora = new Formatter();
            String horaInicioBien = horaFecha.getHours() + "-" + String.valueOf(objHora.format("%02d", horaFecha.getMinutes()));
            horaFecha = StringADateHora(cd.getHoraCierre());
            objHora = new Formatter();
            String horaFinalBien = horaFecha.getHours() + "-" + String.valueOf(objHora.format("%02d", horaFecha.getMinutes()));
            String fechaInicialCompleto = cd.getFechaInicio() + " " + cd.getHoraInicio();
            String fechaFinalCompleto = cd.getFechaCierre() + " " + cd.getHoraCierre();

            reiniciarSumas();

            config configura = new config();
            configura = gastos.buscarDatos();
            Font letra = new Font(Font.FontFamily.TIMES_ROMAN, 11);
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            FileOutputStream archivo;
            String nombreDocumento = fechaInicioBien + " " + horaInicioBien + " a " + fechaFinalBien + " " + horaFinalBien + " ";
            //File file = new File("corte" + fechaHoy + ".pdf");
            File file = new File("C:\\Program Files (x86)\\AppLavanderia\\CorteDiario\\corte " + nombreDocumento + ".pdf");
            archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            horaFecha = StringADateHora(cd.getHoraInicio());
            objHora = new Formatter();
            horaInicioBien = horaFecha.getHours() + ":" + String.valueOf(objHora.format("%02d", horaFecha.getMinutes()));
            horaFecha = StringADateHora(cd.getHoraCierre());
            objHora = new Formatter();
            horaFinalBien = horaFecha.getHours() + ":" + String.valueOf(objHora.format("%02d", horaFecha.getMinutes()));

            //Image img = Image.getInstance("Iconos\\logo 100x100.jpg");
            Image img = Image.getInstance("C:\\Program Files (x86)\\AppLavanderia\\Iconos\\logo 100x100.jpg");

            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{85f, 15f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(configura.getNomnbre() + "\nCORTE DIARIO\nCORTE DEL DÍA DE " + fechaInicioBien + " " + horaInicioBien + " - " + fechaFinalBien + " " + horaFinalBien + "\nFECHA DE EMISIÓN " + DiagHoy, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            encabezado.addCell(img);
            doc.add(encabezado);

            PdfPTable tablapro = new PdfPTable(8);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] columnapro = new float[]{15f, 10f, 20f, 15f, 13f, 13f, 8f, 8f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1 = new PdfPCell(new Phrase("Hora", letra));
            PdfPCell pro2 = new PdfPCell(new Phrase("Folio", letra));
            PdfPCell pro3 = new PdfPCell(new Phrase("Cliente", letra));
            PdfPCell pro4 = new PdfPCell(new Phrase("Concepto", letra));
            PdfPCell pro5 = new PdfPCell(new Phrase("Importe", letra));
            PdfPCell pro6 = new PdfPCell(new Phrase("Cobrado", letra));
            PdfPCell pro7 = new PdfPCell(new Phrase("Pago", letra));
            PdfPCell pro8 = new PdfPCell(new Phrase("Recibe", letra));

            pro1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro4.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro5.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro6.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro7.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro8.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            tablapro.addCell(pro5);
            tablapro.addCell(pro6);
            tablapro.addCell(pro7);
            tablapro.addCell(pro8);

            double totalVendido = 0;
            double totalCobrado = 0;

            ///SE ENLISTAN LAS VENTAS
            List<Nota> ListaCl = notaDao.listarNotasPorDiaHora(fechaInicialCompleto, fechaFinalCompleto);

            for (int i = 0; i < ListaCl.size(); i++) {

                Date horaBien = StringADateHora(ListaCl.get(i).getHora());
                String horaF = horaBien.getHours() + ":" + String.format("%0" + 2 + "d", Integer.valueOf(horaBien.getMinutes()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(horaF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String folioF = String.valueOf(ListaCl.get(i).getFolio());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(folioF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String clienteF = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(clienteF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                List<Detalle> lsDetalle = notaDao.regresarDetalles(ListaCl.get(i).getFolio());
                List<Integer> repetidos = new ArrayList<Integer>();
                for (int j = 0; j < lsDetalle.size(); j++) {
                    boolean checando = false;
                    for (int h = 0; h < repetidos.size(); h++) {
                        if (repetidos.get(h) == lsDetalle.get(j).getCodigoPrecio()) {
                            checando = true;
                        }
                    }
                    if (checando == false) {
                        repetidos.add(lsDetalle.get(j).getCodigoPrecio());

                    }
                }
                String conceptoF = "";
                for (int j = 0; j < repetidos.size(); j++) {
                    conceptoF += String.format("%0" + 2 + "d", Integer.valueOf(repetidos.get(j))) + "-";
                }
                conceptoF = conceptoF.replaceFirst(".$", "");
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(conceptoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String totalF = "$" + formateador.format(ListaCl.get(i).getVentaTotal());;
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(totalF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                totalVendido = totalVendido + ListaCl.get(i).getVentaTotal();

                String cobradoF = "$" + formateador.format(ListaCl.get(i).getAnticipo());;
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(cobradoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                totalCobrado = totalCobrado + ListaCl.get(i).getAnticipo();
                switch (ListaCl.get(i).getFormaPago()) {
                    case 1:
                        ventaEfectivo = ventaEfectivo + ListaCl.get(i).getAnticipo();
                        break;
                    case 3:
                        ventaTransferencia = ventaTransferencia + ListaCl.get(i).getAnticipo();
                        break;
                    case 4:
                        ventaTarjeta = ventaTarjeta + ListaCl.get(i).getAnticipo();
                        break;
                }

                String formaPagoF = String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getFormaPago()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(formaPagoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String recibeF = String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getIdRecibe()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(recibeF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

            }

            //antinotas
            List<Nota> ListaAntiCl = notaDao.listarAntiNotasPorDiaHora(fechaInicialCompleto, fechaFinalCompleto);
            for (int i = 0; i < ListaAntiCl.size(); i++) {
                Date horaBien = StringADateHora(ListaAntiCl.get(i).getHora());
                String horaF = horaBien.getHours() + ":" + String.format("%0" + 2 + "d", Integer.valueOf(horaBien.getMinutes()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(horaF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String folioF = String.valueOf(ListaAntiCl.get(i).getFolio());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(folioF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String clienteF = ListaAntiCl.get(i).getNombre() + " " + ListaAntiCl.get(i).getApellido();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(clienteF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                List<Detalle> lsDetalle = notaDao.regresarDetalles(ListaAntiCl.get(i).getFolio());
                List<Integer> repetidos = new ArrayList<Integer>();
                for (int j = 0; j < lsDetalle.size(); j++) {
                    boolean checando = false;
                    for (int h = 0; h < repetidos.size(); h++) {
                        if (repetidos.get(h) == lsDetalle.get(j).getCodigoPrecio()) {
                            checando = true;
                        }
                    }
                    if (checando == false) {
                        repetidos.add(lsDetalle.get(j).getCodigoPrecio());

                    }
                }
                String conceptoF = "";
                for (int j = 0; j < repetidos.size(); j++) {
                    conceptoF += String.format("%0" + 2 + "d", Integer.valueOf(repetidos.get(j))) + "-";
                }
                conceptoF = conceptoF.replaceFirst(".$", "");
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(conceptoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String totalF = "$" + formateador.format(ListaAntiCl.get(i).getVentaTotal());;
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(totalF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                totalVendido = totalVendido + ListaAntiCl.get(i).getVentaTotal();

                String cobradoF = "$" + formateador.format(ListaAntiCl.get(i).getAnticipo());;
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(cobradoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                totalCobrado = totalCobrado + ListaAntiCl.get(i).getAnticipo();
                ventaEfectivo = ventaEfectivo + ListaAntiCl.get(i).getAnticipo();

                String formaPagoF = "0";
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(formaPagoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String recibeF = String.format("%0" + 2 + "d", Integer.valueOf(ListaAntiCl.get(i).getIdRecibe()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(recibeF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

            }

            for (int i = 0; i < 3; i++) {
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(""));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);
            }

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Total", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(totalVendido), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(totalCobrado), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            Paragraph primero = new Paragraph();
            primero.setFont(letra);
            primero.add("VENTAS\n");
            primero.add(Chunk.NEWLINE);
            doc.add(primero);
            doc.add(tablapro);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            //FIN DE VENTAS

            //COMIENZO DE ENTREGAS
            tablapro = new PdfPTable(8);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnapro = new float[]{15f, 10f, 20f, 15f, 13f, 13f, 8f, 8f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            pro1 = new PdfPCell(new Phrase("Hora", letra));
            pro2 = new PdfPCell(new Phrase("Folio", letra));
            pro3 = new PdfPCell(new Phrase("Cliente", letra));
            pro4 = new PdfPCell(new Phrase("Concepto", letra));
            pro5 = new PdfPCell(new Phrase("Importe", letra));
            pro6 = new PdfPCell(new Phrase("Cobrado", letra));
            pro7 = new PdfPCell(new Phrase("Pago", letra));
            pro8 = new PdfPCell(new Phrase("Recibe", letra));

            pro1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro4.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro5.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro6.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro7.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro8.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            tablapro.addCell(pro5);
            tablapro.addCell(pro6);
            tablapro.addCell(pro7);
            tablapro.addCell(pro8);

            totalVendido = 0;
            totalCobrado = 0;
            ListaCl = notaDao.listarNotasEntregaPorDiaHora(fechaInicialCompleto, fechaFinalCompleto);

            for (int i = 0; i < ListaCl.size(); i++) {

                Date horaBien = StringADateHora(ListaCl.get(i).getHora());
                String horaF = horaBien.getHours() + ":" + String.format("%0" + 2 + "d", Integer.valueOf(horaBien.getMinutes()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(horaF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String folioF = String.valueOf(ListaCl.get(i).getFolio());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(folioF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String clienteF = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(clienteF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                List<Detalle> lsDetalle = notaDao.regresarDetalles(ListaCl.get(i).getFolio());
                List<Integer> repetidos = new ArrayList<Integer>();
                for (int j = 0; j < lsDetalle.size(); j++) {
                    boolean checando = false;
                    for (int h = 0; h < repetidos.size(); h++) {
                        if (repetidos.get(h) == lsDetalle.get(j).getCodigoPrecio()) {
                            checando = true;
                        }
                    }
                    if (checando == false) {
                        repetidos.add(lsDetalle.get(j).getCodigoPrecio());

                    }
                }
                String conceptoF = "";
                for (int j = 0; j < repetidos.size(); j++) {
                    conceptoF += String.format("%0" + 2 + "d", Integer.valueOf(repetidos.get(j))) + "-";
                }
                conceptoF = conceptoF.replaceFirst(".$", "");
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(conceptoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String totalF = "$" + formateador.format(ListaCl.get(i).getVentaTotal());;
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(totalF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                totalVendido = totalVendido + ListaCl.get(i).getVentaTotal();

                String cobradoF = "";
                List<entrega> ListaEntregas = notaDao.listarNotasEntregas();

                int formaPago = 0;
                int idEmpleadoEntrega = 0;
                for (int k = 0; k < ListaEntregas.size(); k++) {
                    if (ListaEntregas.get(k).getIdnota() == ListaCl.get(i).getFolio()) {
                        formaPago = ListaEntregas.get(k).getFormaPago();
                        idEmpleadoEntrega = ListaEntregas.get(k).getIdEmpleado();
                        double cantidadPago = ListaEntregas.get(k).getPago();
                        if (cantidadPago == 0) {
                            cobradoF = "---";
                        } else {
                            cobradoF = "$" + formateador.format(cantidadPago);
                            totalCobrado = totalCobrado + cantidadPago;
                            switch (ListaEntregas.get(k).getFormaPago()) {
                                case 1:
                                    entregaEfectivo = entregaEfectivo + ListaEntregas.get(k).getPago();
                                    ;
                                    break;
                                case 3:
                                    entregaTransferencia = entregaTransferencia + ListaEntregas.get(k).getPago();
                                    break;
                                case 4:
                                    entregaTarjeta = entregaTarjeta + ListaEntregas.get(k).getPago();
                                    break;
                            }
                        }
                    }
                }

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(cobradoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String formaPagoF = String.format("%0" + 2 + "d", Integer.valueOf(formaPago));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(formaPagoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String recibeF = String.format("%0" + 2 + "d", Integer.valueOf(idEmpleadoEntrega));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(recibeF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

            }
            //aqui se enlistan los anticiposghfg
            List<anticipo> listaAnti = anticip.listarAnticipoPorDiaHora(fechaInicialCompleto, fechaFinalCompleto);
            for (int i = 0; i < listaAnti.size(); i++) {

                Date horaBien = StringADateHora(listaAnti.get(i).getHora());
                String horaF = horaBien.getHours() + ":" + String.format("%0" + 2 + "d", Integer.valueOf(horaBien.getMinutes()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(horaF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String folioF = String.valueOf(listaAnti.get(i).getFolio());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(folioF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String clienteF = listaAnti.get(i).getNombre() + " " + listaAnti.get(i).getApellido() + "(ABONO)";
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(clienteF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                List<Detalle> lsDetalle = notaDao.regresarDetalles(listaAnti.get(i).getFolio());
                List<Integer> repetidos = new ArrayList<Integer>();
                for (int j = 0; j < lsDetalle.size(); j++) {
                    boolean checando = false;
                    for (int h = 0; h < repetidos.size(); h++) {
                        if (repetidos.get(h) == lsDetalle.get(j).getCodigoPrecio()) {
                            checando = true;
                        }
                    }
                    if (checando == false) {
                        repetidos.add(lsDetalle.get(j).getCodigoPrecio());

                    }
                }
                String conceptoF = "";
                for (int j = 0; j < repetidos.size(); j++) {
                    conceptoF += String.format("%0" + 2 + "d", Integer.valueOf(repetidos.get(j))) + "-";
                }
                conceptoF = conceptoF.replaceFirst(".$", "");
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(conceptoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                Nota miNota = notaDao.buscarPorFolio(listaAnti.get(i).getFolio());
                String totalF = "$" + formateador.format(miNota.getVentaTotal());;
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(totalF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                totalVendido = totalVendido + miNota.getVentaTotal();

                String cobradoF = "$" + formateador.format(listaAnti.get(i).getCantidad());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(cobradoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                totalCobrado = totalCobrado + listaAnti.get(i).getCantidad();
                switch (listaAnti.get(i).getFormaPago()) {
                    case 1:
                        abonoEfectivo = abonoEfectivo + listaAnti.get(i).getCantidad();
                        ;
                        break;
                    case 3:
                        abonoTransferencia = abonoTransferencia + listaAnti.get(i).getCantidad();
                        break;
                    case 4:
                        abonoTarjeta = abonoTarjeta + listaAnti.get(i).getCantidad();
                        break;
                }

                String formaPagoF = String.format("%0" + 2 + "d", Integer.valueOf(listaAnti.get(i).getFormaPago()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(formaPagoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String recibeF = String.format("%0" + 2 + "d", Integer.valueOf(listaAnti.get(i).getIdEmpleado()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(recibeF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

            }

            for (int i = 0; i < 3; i++) {
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(""));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);
            }

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Total", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(totalVendido), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(totalCobrado), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            primero = new Paragraph();
            primero.setFont(letra);
            primero.add("ENTREGAS\n");
            primero.add(Chunk.NEWLINE);
            doc.add(primero);
            doc.add(tablapro);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);

            //FIN DE ENTREGAS
            //INICIO GASTOS
            tablapro = new PdfPTable(6);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnapro = new float[]{15f, 10f, 20f, 15f, 12f, 12f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            pro1 = new PdfPCell(new Phrase("Hora", letra));
            pro2 = new PdfPCell(new Phrase("Folio", letra));
            pro3 = new PdfPCell(new Phrase("Comprobante", letra));
            pro4 = new PdfPCell(new Phrase("Concepto", letra));
            pro5 = new PdfPCell(new Phrase("Importe", letra));
            pro6 = new PdfPCell(new Phrase("FormaPago", letra));

            pro1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro4.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro5.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro6.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            tablapro.addCell(pro5);
            tablapro.addCell(pro6);
            double totalGastado = 0;

            List<Gastos> ListaGastos = gastos.listarGastosPorDiaHora(fechaInicialCompleto, fechaFinalCompleto);

            for (int i = 0; i < ListaGastos.size(); i++) {

                String fechaF = fechaFormatoCorrecto(ListaGastos.get(i).getFecha());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(fechaF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String folioF = String.valueOf(ListaGastos.get(i).getId());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(folioF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String comprobanteF = ListaGastos.get(i).getComprobante();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(comprobanteF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String conceptoF = ListaGastos.get(i).getDescripcion();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(conceptoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String totalF = "$" + formateador.format(ListaGastos.get(i).getPrecio());;
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(totalF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                totalGastado = totalGastado + ListaGastos.get(i).getPrecio();
                switch (ListaGastos.get(i).getFormaPago()) {
                    case 1:
                        gastosEfectivo = gastosEfectivo + ListaGastos.get(i).getPrecio();
                        break;
                    case 3:
                        gastosTransferencia = gastosTransferencia + ListaGastos.get(i).getPrecio();
                        break;
                    case 4:
                        gastosTarjeta = gastosTarjeta + ListaGastos.get(i).getPrecio();
                        break;
                }

                String formaPagoF = String.format("%0" + 2 + "d", Integer.valueOf(ListaGastos.get(i).getFormaPago()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(formaPagoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

            }

            for (int i = 0; i < 3; i++) {
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(""));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);
            }

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Total", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(totalGastado), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            primero = new Paragraph();
            primero.setFont(letra);
            primero.add("GASTOS\n");
            primero.add(Chunk.NEWLINE);
            doc.add(primero);
            doc.add(tablapro);
            doc.add(Chunk.NEWLINE);

            //FIN GASTOS
            ventaTotalTabla = ventaEfectivo + ventaTransferencia + ventaTarjeta;
            abonoTotal = abonoEfectivo + abonoTransferencia + abonoTarjeta;
            entregasTotal = entregaEfectivo + entregaTransferencia + entregaTarjeta;
            gastosTotal = gastosEfectivo + gastosTransferencia + gastosTarjeta;

            efectivoTotal = ventaEfectivo + abonoEfectivo + entregaEfectivo - gastosEfectivo;
            transferenciaTotal = ventaTransferencia + abonoTransferencia + entregaTransferencia - gastosTransferencia;
            tarjetaTotal = ventaTarjeta + entregaTarjeta + abonoTarjeta - gastosTarjeta;

            //FORMAS DE PAGO
            tablapro = new PdfPTable(5);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnapro = new float[]{25f, 40f, 40f, 40f, 40f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            pro1 = new PdfPCell(new Phrase("Concepto", letra));
            pro2 = new PdfPCell(new Phrase("Efectivo", letra));
            pro3 = new PdfPCell(new Phrase("Transferencia", letra));
            pro4 = new PdfPCell(new Phrase("Tarjeta", letra));
            pro5 = new PdfPCell(new Phrase("Total", letra));

            pro1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro4.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro5.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            tablapro.addCell(pro5);

            String formas[] = {"Ventas", "Abonos", "Entregas", "Gastos", "Saldos"};

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Ventas", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(ventaEfectivo), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(ventaTransferencia), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(ventaTarjeta), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(ventaTotalTabla), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Abonos", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(abonoEfectivo), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(abonoTransferencia), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(abonoTarjeta), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(abonoTotal), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Entregas", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(entregaEfectivo), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(entregaTransferencia), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(entregaTarjeta), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(entregasTotal), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Gastos", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(gastosEfectivo), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(gastosTransferencia), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(gastosTarjeta), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(gastosTotal), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Saldos", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(efectivoTotal), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(transferenciaTotal), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(tarjetaTotal), letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("-", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            primero = new Paragraph();
            primero.setFont(letra);
            primero.add("FORMA DE PAGO\n");
            primero.add(Chunk.NEWLINE);
            doc.add(primero);
            doc.add(tablapro);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);

            //Inicio Resumen
            tablapro = new PdfPTable(5);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnapro = new float[]{30f, 15f, 30f, 15f, 30};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            pro1 = new PdfPCell(new Phrase("Concepto ", letra));
            pro2 = new PdfPCell(new Phrase("Monto", letra));
            pro3 = new PdfPCell(new Phrase("Concepto", letra));
            pro4 = new PdfPCell(new Phrase("Monto", letra));
            pro5 = new PdfPCell(new Phrase("Revisó", letra));

            pro1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro4.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro5.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            tablapro.addCell(pro5);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Saldo inicial", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(cd.getSaldoInicial()), letra));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();

            cell = new PdfPCell(new Phrase("Saldo en corte", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            if (prueba == 1) {
                cell = new PdfPCell(new Phrase("$" + formateador.format(cd.getSaldoEnCaja()), letra));
            } else {
                cell = new PdfPCell(new Phrase("$" + txtSaldoCaja1.getText(), letra));
            }
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(""));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);
            ////

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Total de ventas", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            if (prueba == 1) {
                cell = new PdfPCell(new Phrase("$" + formateador.format(cd.getTotalVentas()), letra));
            } else {
                cell = new PdfPCell(new Phrase("$" + txtTotalVentas.getText(), letra));
            }
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Saldo en efectivo", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            if (prueba == 1) {
                cell = new PdfPCell(new Phrase("$" + formateador.format(cd.getEfectivo()), letra));
            } else {
                cell = new PdfPCell(new Phrase("$" + txtSaldoEnCaja.getText(), letra));
            }
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(""));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            ///
            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Total de cobros", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            if (prueba == 1) {
                cell = new PdfPCell(new Phrase("$" + formateador.format(cd.getTotalCobros()), letra));
            } else {
                cell = new PdfPCell(new Phrase("$" + txtTotalCobros1.getText(), letra));
            }
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Retiro en efectivo", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            if (prueba == 1) {
                cell = new PdfPCell(new Phrase("$" + formateador.format(cd.getRetiro()), letra));
            } else {
                cell = new PdfPCell(new Phrase("$" + txtRetiro1.getText(), letra));
            }
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(""));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);
            ///

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Total de gastos", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            if (prueba == 1) {
                cell = new PdfPCell(new Phrase("$" + formateador.format(cd.getTotalGastos()), letra));
            } else {
                cell = new PdfPCell(new Phrase("$" + TablaFormas.getValueAt(3, 3).toString(), letra));
            }
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Saldo final", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            if (prueba == 1) {
                cell = new PdfPCell(new Phrase("$" + formateador.format(cd.getSaldoFinal()), letra));
            } else {
                cell = new PdfPCell(new Phrase("$" + txtSaldoFInal1.getText(), letra));
            }
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            if (prueba == 1) {
                cell = new PdfPCell(new Phrase(cd.getPersona()));
            } else {
                cell = new PdfPCell(new Phrase("CORTE", letra));
            }
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();

            cell = new PdfPCell(new Phrase("Arqueos de caja", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            if (prueba == 1) {
                cell = new PdfPCell(new Phrase("$" + formateador.format(cd.getArqueos()), letra));
            } else {
                cell = new PdfPCell(new Phrase("$" + txtTotalArqueos.getText(), letra));
            }
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();

            cell = new PdfPCell(new Phrase("", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);
            tablapro.addCell(cell);
            tablapro.addCell(cell);

            ////
            primero = new Paragraph();
            primero.setFont(letra);
            primero.add("RESUMEN");
            primero.add(Chunk.NEWLINE);
            doc.add(primero);
            doc.add(Chunk.NEWLINE);
            doc.add(tablapro);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);

            //fin resumen
            Paragraph pg = new Paragraph(cd.getComentario(), new Font(Font.FontFamily.TIMES_ROMAN, 13));
            pg.setAlignment(Element.ALIGN_CENTER);
            doc.add(pg);

            PdfPTable tablaFinal = new PdfPTable(3);
            tablaFinal.setWidthPercentage(100);
            tablaFinal.getDefaultCell().setBorder(0);
            columnapro = new float[]{50f, 50f, 50f};
            tablaFinal.setWidths(columnapro);
            tablaFinal.setHorizontalAlignment(Element.ALIGN_LEFT);
            for (int i = 0; i < 3; i++) {
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase("____________________"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablaFinal.addCell(cell);
            }

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Elaboró", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablaFinal.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Gerencia", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablaFinal.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Auditoria", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablaFinal.addCell(cell);

            primero = new Paragraph();
            primero.setFont(negrita);
            primero.add(Chunk.NEWLINE);
            primero.add(Chunk.NEWLINE);
            primero.add(Chunk.NEWLINE);
            primero.add(Chunk.NEWLINE);
            doc.add(primero);

            doc.add(tablaFinal);

            if (prueba == 0) {
                Font negrita2 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.RED);
                doc.add(Chunk.NEWLINE);
                doc.add(Chunk.NEWLINE);
                doc.add(new Paragraph(" -------------------------- ESTE CORTE NO HA SIDO CERRADO-----------------------", negrita2));
            }

            doc.close();
            archivo.close();
            Desktop.getDesktop().open(file);
        } catch (DocumentException | IOException e) {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe cerrar antes el documento");
            panel.showNotification();
        }
    }

    public void corteDiafinalExcel(int corteUsar, int prueba) {
        corteDia cd = corte.regresarCorte(corteUsar);
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Corte diario");

        Formatter obj2 = new Formatter();
        Formatter obj = new Formatter();
        String fechaHoy = cd.getFechaCierre();
        LocalDateTime m = LocalDateTime.now(); //Obtenemos la fecha actual
        String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
        String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
        String DiagHoy = dia + "-" + mes + "-" + m.getYear();
        String fechaFinalBien = fechaFormatoCorrecto(fechaHoy);
        String fechaInicioBien = fechaFormatoCorrecto(cd.getFechaInicio());
        Date horaFecha = StringADateHora(cd.getHoraInicio());
        Formatter objHora = new Formatter();
        String horaInicioBien = horaFecha.getHours() + "-" + String.valueOf(objHora.format("%02d", horaFecha.getMinutes()));
        horaFecha = StringADateHora(cd.getHoraCierre());
        objHora = new Formatter();
        String horaFinalBien = horaFecha.getHours() + "-" + String.valueOf(objHora.format("%02d", horaFecha.getMinutes()));
        String fechaInicialCompleto = cd.getFechaInicio() + " " + cd.getHoraInicio();
        String fechaFinalCompleto = cd.getFechaCierre() + " " + cd.getHoraCierre();
        String nombreDocumento = fechaInicioBien + " " + horaInicioBien + " a " + fechaFinalBien + " " + horaFinalBien + " ";
        double entradaEfectivo = 0;
        double entradaCheque = 0;
        double entradaTransferencia = 0;
        double entradaTarjeta = 0;
        double entradaDefinir = 0;
        double salidaEfectivo = 0;
        double salidaCheque = 0;
        double salidaTransferencia = 0;
        double salidaTarjeta = 0;
        double salidaDefinir = 0;
        horaFecha = StringADateHora(cd.getHoraInicio());
        objHora = new Formatter();
        horaInicioBien = horaFecha.getHours() + ":" + String.valueOf(objHora.format("%02d", horaFecha.getMinutes()));
        horaFecha = StringADateHora(cd.getHoraCierre());
        objHora = new Formatter();
        horaFinalBien = horaFecha.getHours() + ":" + String.valueOf(objHora.format("%02d", horaFecha.getMinutes()));
        try {
            InputStream is = new FileInputStream("C:\\Program Files (x86)\\AppLavanderia\\Iconos\\logo 100x100.jpg");
            byte[] bytes = IOUtils.toByteArray(is);
            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            is.close();

            CreationHelper help = book.getCreationHelper();
            Drawing draw = sheet.createDrawingPatriarch();

            ClientAnchor anchor = help.createClientAnchor();
            anchor.setCol1(0);//posicionar en que columna
            anchor.setRow1(1); // en que fila va a estar
            Picture pict = draw.createPicture(anchor, imgIndex);
            pict.resize(1, 3);

            CellStyle tituloEstilo = book.createCellStyle();
            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
            org.apache.poi.ss.usermodel.Font fuenteTitulo = book.createFont();
            fuenteTitulo.setFontName(Font.FontFamily.TIMES_ROMAN.name());
            fuenteTitulo.setBold(true);
            fuenteTitulo.setFontHeightInPoints((short) 12);
            tituloEstilo.setFont(fuenteTitulo);

            config configura = new config();
            configura = gastos.buscarDatos();
            Row filaTitulo = sheet.createRow(1);
            Cell celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue(configura.getNomnbre());
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 7));

            filaTitulo = sheet.createRow(2);
            celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("CORTE DIARIO");
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 7));

            String tituloEncabezado = "";
            tituloEncabezado += " CORTE DEL DÍA DE " + fechaInicioBien + " " + horaInicioBien + " - " + fechaFinalBien + " " + horaFinalBien;
            filaTitulo = sheet.createRow(3);
            celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue(tituloEncabezado);
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

            filaTitulo = sheet.createRow(4);
            celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("FECHA DE EMISIÓN " + DiagHoy);
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 7));

            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            org.apache.poi.ss.usermodel.Font font = book.createFont();
            font.setFontName(Font.FontFamily.TIMES_ROMAN.name());
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);

            //Aqui empiezan las ventas
            String[] cabecera = new String[]{"Hora", "Folio", "Cliente", "Concepto", "Importe", "Cobrado", "Pago", "Recibe"};

            Row filaEncabezados = sheet.createRow(6);
            Cell celdaEncabezado = filaEncabezados.createCell(0);
            celdaEncabezado.setCellStyle(tituloEstilo);
            celdaEncabezado.setCellValue("VENTAS");

            filaEncabezados = sheet.createRow(7);
            for (int i = 0; i < cabecera.length; i++) {
                celdaEncabezado = filaEncabezados.createCell(i);
                celdaEncabezado.setCellStyle(headerStyle);
                celdaEncabezado.setCellValue(cabecera[i]);
            }

            int numFilaDatos = 8;

            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.NONE);
            datosEstilo.setBorderRight(BorderStyle.NONE);
            datosEstilo.setBorderLeft(BorderStyle.NONE);
            datosEstilo.setBorderTop(BorderStyle.NONE);
            datosEstilo.setAlignment(HorizontalAlignment.CENTER);
            datosEstilo.setVerticalAlignment(VerticalAlignment.CENTER);

            List<Nota> ListaCl = notaDao.listarNotasPorDiaHora(fechaInicialCompleto, fechaFinalCompleto);
            int contador = 0;

            int numCol = contador;

            double totalVendido = 0;
            double totalCobrado = 0;

            for (int i = 0; i < ListaCl.size(); i++) {
                Row filaDatos = sheet.createRow(numFilaDatos);
                List<String> datosEscribir = new ArrayList<String>();

                String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
                datosEscribir.add(fechaF);

                String folioF = String.valueOf(ListaCl.get(i).getFolio());
                datosEscribir.add(folioF);

                String clienteF = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
                datosEscribir.add(clienteF);

                List<Detalle> lsDetalle = notaDao.regresarDetalles(ListaCl.get(i).getFolio());
                List<Integer> repetidos = new ArrayList<Integer>();
                for (int j = 0; j < lsDetalle.size(); j++) {
                    boolean checando = false;
                    for (int h = 0; h < repetidos.size(); h++) {
                        if (repetidos.get(h) == lsDetalle.get(j).getCodigoPrecio()) {
                            checando = true;
                        }
                    }
                    if (checando == false) {
                        repetidos.add(lsDetalle.get(j).getCodigoPrecio());

                    }
                }
                String conceptoF = "";
                for (int j = 0; j < repetidos.size(); j++) {
                    conceptoF += String.format("%0" + 2 + "d", Integer.valueOf(repetidos.get(j))) + "-";
                }
                conceptoF = conceptoF.replaceFirst(".$", "");
                datosEscribir.add(conceptoF);

                String totalF = "" + ListaCl.get(i).getVentaTotal();
                datosEscribir.add(totalF);

                String cobradoF = "" + ListaCl.get(i).getAnticipo();
                datosEscribir.add(cobradoF);
                switch (ListaCl.get(i).getFormaPago()) {
                    case 1:
                        entradaEfectivo = entradaEfectivo + ListaCl.get(i).getAnticipo();
                        break;
                    case 3:
                        entradaTransferencia = entradaTransferencia + ListaCl.get(i).getAnticipo();
                        break;
                    case 4:
                        entradaTarjeta = entradaTarjeta + ListaCl.get(i).getAnticipo();
                        break;
                }

                String formaPagoF = "";
                formaPagoF = String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getFormaPago()));
                datosEscribir.add(formaPagoF);

                String recibeF = String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getIdRecibe()));
                datosEscribir.add(recibeF);

                for (int j = 0; j < datosEscribir.size(); j++) {
                    if (j == 4 || j == 5) {
                        Cell celdaDatos = filaDatos.createCell(j, CellType.NUMERIC);
                        celdaDatos.setCellValue(Double.parseDouble(datosEscribir.get(j)));
                        celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));
                    } else {
                        Cell celdaDatos = filaDatos.createCell(j);
                        celdaDatos.setCellStyle(datosEstilo);
                        celdaDatos.setCellValue(datosEscribir.get(j));
                    }
                }
                totalVendido = totalVendido + ListaCl.get(i).getVentaTotal();
                totalCobrado = totalCobrado + ListaCl.get(i).getAnticipo();
                numFilaDatos++;

            }

            //antinotas
            List<Nota> ListaAntiCl = notaDao.listarAntiNotasPorDiaHora(fechaInicialCompleto, fechaFinalCompleto);
            for (int i = 0; i < ListaAntiCl.size(); i++) {
                Row filaDatos = sheet.createRow(numFilaDatos);
                List<String> datosEscribir = new ArrayList<String>();

                String fechaF = fechaFormatoCorrecto(ListaAntiCl.get(i).getFecha());
                datosEscribir.add(fechaF);

                String folioF = String.valueOf(ListaAntiCl.get(i).getFolio());
                datosEscribir.add(folioF);

                String clienteF = ListaAntiCl.get(i).getNombre() + " " + ListaAntiCl.get(i).getApellido();
                datosEscribir.add(clienteF);

                List<Detalle> lsDetalle = notaDao.regresarDetalles(ListaAntiCl.get(i).getFolio());
                List<Integer> repetidos = new ArrayList<Integer>();
                for (int j = 0; j < lsDetalle.size(); j++) {
                    boolean checando = false;
                    for (int h = 0; h < repetidos.size(); h++) {
                        if (repetidos.get(h) == lsDetalle.get(j).getCodigoPrecio()) {
                            checando = true;
                        }
                    }
                    if (checando == false) {
                        repetidos.add(lsDetalle.get(j).getCodigoPrecio());

                    }
                }
                String conceptoF = "";
                for (int j = 0; j < repetidos.size(); j++) {
                    conceptoF += String.format("%0" + 2 + "d", Integer.valueOf(repetidos.get(j))) + "-";
                }
                conceptoF = conceptoF.replaceFirst(".$", "");
                datosEscribir.add(conceptoF);

                String totalF = "" + ListaAntiCl.get(i).getVentaTotal();
                datosEscribir.add(totalF);

                String cobradoF = "" + ListaAntiCl.get(i).getAnticipo();
                datosEscribir.add(cobradoF);
                entradaEfectivo = entradaEfectivo + ListaAntiCl.get(i).getAnticipo();

                String formaPagoF = "";
                formaPagoF = "0";
                datosEscribir.add(formaPagoF);

                String recibeF = String.format("%0" + 2 + "d", Integer.valueOf(ListaAntiCl.get(i).getIdRecibe()));
                datosEscribir.add(recibeF);

                for (int j = 0; j < datosEscribir.size(); j++) {
                    if (j == 4 || j == 5) {
                        Cell celdaDatos = filaDatos.createCell(j, CellType.NUMERIC);
                        celdaDatos.setCellValue(Double.parseDouble(datosEscribir.get(j)));
                        celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));
                    } else {
                        Cell celdaDatos = filaDatos.createCell(j);
                        celdaDatos.setCellStyle(datosEstilo);
                        celdaDatos.setCellValue(datosEscribir.get(j));
                    }
                }
                totalVendido = totalVendido + ListaAntiCl.get(i).getVentaTotal();
                totalCobrado = totalCobrado + ListaAntiCl.get(i).getAnticipo();
                numFilaDatos++;

            }

            Row filaDatos = sheet.createRow(numFilaDatos);
            Cell celdaDatos = filaDatos.createCell(3);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("TOTAL");

            celdaDatos = filaDatos.createCell(4, CellType.NUMERIC);
            celdaDatos.setCellValue(totalVendido);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(5, CellType.NUMERIC);
            celdaDatos.setCellValue(totalCobrado);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            //inicio de entregas      
            cabecera = new String[]{"Hora", "Folio", "Cliente", "Concepto", "Importe", "Cobrado", "Pago", "Recibe"};
            numFilaDatos = numFilaDatos + 2;
            filaEncabezados = sheet.createRow(numFilaDatos);
            celdaEncabezado = filaEncabezados.createCell(0);
            celdaEncabezado.setCellStyle(tituloEstilo);
            celdaEncabezado.setCellValue("ENTREGAS");

            numFilaDatos++;
            filaEncabezados = sheet.createRow(numFilaDatos);
            for (int i = 0; i < cabecera.length; i++) {
                celdaEncabezado = filaEncabezados.createCell(i);
                celdaEncabezado.setCellStyle(headerStyle);
                celdaEncabezado.setCellValue(cabecera[i]);
            }

            numFilaDatos++;

            datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.NONE);
            datosEstilo.setBorderRight(BorderStyle.NONE);
            datosEstilo.setBorderLeft(BorderStyle.NONE);
            datosEstilo.setBorderTop(BorderStyle.NONE);
            datosEstilo.setAlignment(HorizontalAlignment.CENTER);
            datosEstilo.setVerticalAlignment(VerticalAlignment.CENTER);

            ListaCl = notaDao.listarNotasEntregaPorDiaHora(fechaInicialCompleto, fechaFinalCompleto);
            contador = 0;
            numCol = contador;

            totalVendido = 0;
            totalCobrado = 0;

            for (int i = 0; i < ListaCl.size(); i++) {
                filaDatos = sheet.createRow(numFilaDatos);
                List<String> datosEscribir = new ArrayList<String>();

                String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
                datosEscribir.add(fechaF);

                String folioF = String.valueOf(ListaCl.get(i).getFolio());
                datosEscribir.add(folioF);

                String clienteF = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
                datosEscribir.add(clienteF);

                List<Detalle> lsDetalle = notaDao.regresarDetalles(ListaCl.get(i).getFolio());
                List<Integer> repetidos = new ArrayList<Integer>();
                for (int j = 0; j < lsDetalle.size(); j++) {
                    boolean checando = false;
                    for (int h = 0; h < repetidos.size(); h++) {
                        if (repetidos.get(h) == lsDetalle.get(j).getCodigoPrecio()) {
                            checando = true;
                        }
                    }
                    if (checando == false) {
                        repetidos.add(lsDetalle.get(j).getCodigoPrecio());

                    }
                }
                String conceptoF = "";
                for (int j = 0; j < repetidos.size(); j++) {
                    conceptoF += String.format("%0" + 2 + "d", Integer.valueOf(repetidos.get(j))) + "-";
                }
                conceptoF = conceptoF.replaceFirst(".$", "");
                datosEscribir.add(conceptoF);

                String totalF = "" + ListaCl.get(i).getVentaTotal();
                datosEscribir.add(totalF);

                String cobradoF = "";
                List<entrega> ListaEntregas = notaDao.listarNotasEntregas();

                int formaPago = 0;
                int idEmpleadoEntrega = 0;
                double cantidadPago = 0;
                for (int k = 0; k < ListaEntregas.size(); k++) {
                    if (ListaEntregas.get(k).getIdnota() == ListaCl.get(i).getFolio()) {
                        formaPago = ListaEntregas.get(k).getFormaPago();
                        idEmpleadoEntrega = ListaEntregas.get(k).getIdEmpleado();
                        cantidadPago = ListaEntregas.get(k).getPago();
                        if (cantidadPago == 0) {
                            cobradoF = "00";
                        } else {
                            cobradoF = "" + cantidadPago;
                        }
                    }
                }
                datosEscribir.add(cobradoF);
                switch (ListaEntregas.get(i).getFormaPago()) {
                    case 1:
                        entradaEfectivo = entradaEfectivo + ListaEntregas.get(i).getPago();
                        break;
                    case 3:
                        entradaTransferencia = entradaTransferencia + ListaEntregas.get(i).getPago();
                        break;
                    case 4:
                        entradaTarjeta = entradaTarjeta + ListaEntregas.get(i).getPago();
                        break;

                }

                String recibeF = String.format("%0" + 2 + "d", Integer.valueOf(formaPago));
                datosEscribir.add(recibeF);

                for (int j = 0; j < datosEscribir.size(); j++) {
                    if (j == 4 || j == 5) {
                        celdaDatos = filaDatos.createCell(j, CellType.NUMERIC);
                        celdaDatos.setCellValue(Double.parseDouble(datosEscribir.get(j)));
                        celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));
                    } else {
                        celdaDatos = filaDatos.createCell(j);
                        celdaDatos.setCellStyle(datosEstilo);
                        celdaDatos.setCellValue(datosEscribir.get(j));
                    }
                }
                totalVendido = totalVendido + ListaCl.get(i).getVentaTotal();
                totalCobrado = totalCobrado + cantidadPago;
                numFilaDatos++;

            }

            List<anticipo> listaAnti = anticip.listarAnticipoPorDiaHora(fechaInicialCompleto, fechaFinalCompleto);
            for (int i = 0; i < listaAnti.size(); i++) {
                filaDatos = sheet.createRow(numFilaDatos);
                List<String> datosEscribir = new ArrayList<String>();

                String fechaF = fechaFormatoCorrecto(listaAnti.get(i).getFecha());
                datosEscribir.add(fechaF);

                String folioF = String.valueOf(listaAnti.get(i).getFolio());
                datosEscribir.add(folioF);

                String clienteF = listaAnti.get(i).getNombre() + " " + listaAnti.get(i).getApellido() + "(ABONO)";
                datosEscribir.add(clienteF);

                List<Detalle> lsDetalle = notaDao.regresarDetalles(listaAnti.get(i).getFolio());
                List<Integer> repetidos = new ArrayList<Integer>();
                for (int j = 0; j < lsDetalle.size(); j++) {
                    boolean checando = false;
                    for (int h = 0; h < repetidos.size(); h++) {
                        if (repetidos.get(h) == lsDetalle.get(j).getCodigoPrecio()) {
                            checando = true;
                        }
                    }
                    if (checando == false) {
                        repetidos.add(lsDetalle.get(j).getCodigoPrecio());

                    }
                }
                String conceptoF = "";
                for (int j = 0; j < repetidos.size(); j++) {
                    conceptoF += String.format("%0" + 2 + "d", Integer.valueOf(repetidos.get(j))) + "-";
                }
                conceptoF = conceptoF.replaceFirst(".$", "");
                datosEscribir.add(conceptoF);

                Nota miNota = notaDao.buscarPorFolio(listaAnti.get(i).getFolio());
                String totalF = "" + miNota.getVentaTotal();
                datosEscribir.add(totalF);

                List<entrega> ListaEntregas = notaDao.listarNotasEntregas();

                int formaPago = listaAnti.get(i).getFormaPago();
                int idEmpleadoEntrega = listaAnti.get(i).getIdEmpleado();
                double cantidadPago = listaAnti.get(i).getCantidad();

                datosEscribir.add("" + cantidadPago);

                switch (listaAnti.get(i).getFormaPago()) {
                    case 1:
                        entradaEfectivo = entradaEfectivo + listaAnti.get(i).getCantidad();
                        break;
                    case 3:
                        entradaTransferencia = entradaTransferencia + listaAnti.get(i).getCantidad();
                        break;
                    case 4:
                        entradaTarjeta = entradaTarjeta + listaAnti.get(i).getCantidad();
                        break;
                }
                String recibeF = String.format("%0" + 2 + "d", Integer.valueOf(formaPago));
                datosEscribir.add(recibeF);

                for (int j = 0; j < datosEscribir.size(); j++) {
                    if (j == 4 || j == 5) {
                        celdaDatos = filaDatos.createCell(j, CellType.NUMERIC);
                        celdaDatos.setCellValue(Double.parseDouble(datosEscribir.get(j)));
                        celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));
                    } else {
                        celdaDatos = filaDatos.createCell(j);
                        celdaDatos.setCellStyle(datosEstilo);
                        celdaDatos.setCellValue(datosEscribir.get(j));
                    }
                }
                totalVendido = totalVendido + miNota.getVentaTotal();
                totalCobrado = totalCobrado + cantidadPago;
                numFilaDatos++;

            }

            filaDatos = sheet.createRow(numFilaDatos);
            celdaDatos = filaDatos.createCell(3);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("TOTAL");

            celdaDatos = filaDatos.createCell(4, CellType.NUMERIC);
            celdaDatos.setCellValue(totalVendido);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(5, CellType.NUMERIC);
            celdaDatos.setCellValue(totalCobrado);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            //inicio de gastos     
            cabecera = new String[]{"Hora", "Folio", "Comprobante", "Concepto", "Importe", "FormaPago"};
            numFilaDatos = numFilaDatos + 2;
            filaEncabezados = sheet.createRow(numFilaDatos);
            celdaEncabezado = filaEncabezados.createCell(0);
            celdaEncabezado.setCellStyle(tituloEstilo);
            celdaEncabezado.setCellValue("GASTOS");

            numFilaDatos++;
            filaEncabezados = sheet.createRow(numFilaDatos);
            for (int i = 0; i < cabecera.length; i++) {
                celdaEncabezado = filaEncabezados.createCell(i);
                celdaEncabezado.setCellStyle(headerStyle);
                celdaEncabezado.setCellValue(cabecera[i]);
            }

            numFilaDatos++;

            datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.NONE);
            datosEstilo.setBorderRight(BorderStyle.NONE);
            datosEstilo.setBorderLeft(BorderStyle.NONE);
            datosEstilo.setBorderTop(BorderStyle.NONE);
            datosEstilo.setAlignment(HorizontalAlignment.CENTER);
            datosEstilo.setVerticalAlignment(VerticalAlignment.CENTER);

            List<Gastos> ListaCl2 = gastos.listarGastosPorDiaHora(fechaInicialCompleto, fechaFinalCompleto);

            contador = 0;

            numCol = contador;

            totalVendido = 0;
            totalCobrado = 0;

            for (int i = 0; i < ListaCl2.size(); i++) {
                filaDatos = sheet.createRow(numFilaDatos);
                List<String> datosEscribir = new ArrayList<String>();

                String fechaF = fechaFormatoCorrecto(ListaCl2.get(i).getFecha());
                datosEscribir.add(fechaF);

                String folioF = String.valueOf(ListaCl2.get(i).getId());
                datosEscribir.add(folioF);

                String clienteF = ListaCl2.get(i).getComprobante();
                datosEscribir.add(clienteF);

                String conceptoF = ListaCl2.get(i).getDescripcion();
                datosEscribir.add(conceptoF);

                String totalF = "" + ListaCl2.get(i).getPrecio();
                datosEscribir.add(totalF);

                String formaPagoF = String.format("%0" + 2 + "d", Integer.valueOf(ListaCl2.get(i).getFormaPago()));
                datosEscribir.add(formaPagoF);
                switch (ListaCl2.get(i).getFormaPago()) {
                    case 1:
                        salidaEfectivo = entradaEfectivo + ListaCl2.get(i).getPrecio();
                        break;
                    case 3:
                        salidaTransferencia = entradaTransferencia + ListaCl2.get(i).getPrecio();
                        break;
                    case 4:
                        salidaTarjeta = entradaTarjeta + ListaCl2.get(i).getPrecio();
                        break;
                }

                for (int j = 0; j < datosEscribir.size(); j++) {
                    if (j == 4) {
                        celdaDatos = filaDatos.createCell(j, CellType.NUMERIC);
                        celdaDatos.setCellValue(Double.parseDouble(datosEscribir.get(j)));
                        celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));
                    } else {
                        celdaDatos = filaDatos.createCell(j);
                        celdaDatos.setCellStyle(datosEstilo);
                        celdaDatos.setCellValue(datosEscribir.get(j));
                    }
                }
                totalVendido = totalVendido + ListaCl2.get(i).getPrecio();
                numFilaDatos++;

            }

            filaDatos = sheet.createRow(numFilaDatos);
            celdaDatos = filaDatos.createCell(3);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("TOTAL");

            celdaDatos = filaDatos.createCell(4, CellType.NUMERIC);
            celdaDatos.setCellValue(totalVendido);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            //formas de pago
            cabecera = new String[]{"01 Efectivo", "02 Transferencia", "03 Tarjeta"};
            numFilaDatos = numFilaDatos + 2;
            filaEncabezados = sheet.createRow(numFilaDatos);
            celdaEncabezado = filaEncabezados.createCell(2);
            celdaEncabezado.setCellStyle(tituloEstilo);
            celdaEncabezado.setCellValue("ENTRADAS - FORMA DE PAGO");

            numFilaDatos++;
            filaEncabezados = sheet.createRow(numFilaDatos);
            for (int i = 0; i < cabecera.length; i++) {
                celdaEncabezado = filaEncabezados.createCell(i);
                celdaEncabezado.setCellStyle(headerStyle);
                celdaEncabezado.setCellValue(cabecera[i]);
            }

            numFilaDatos++;

            datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.NONE);
            datosEstilo.setBorderRight(BorderStyle.NONE);
            datosEstilo.setBorderLeft(BorderStyle.NONE);
            datosEstilo.setBorderTop(BorderStyle.NONE);
            datosEstilo.setAlignment(HorizontalAlignment.CENTER);
            datosEstilo.setVerticalAlignment(VerticalAlignment.CENTER);

            contador = 0;

            numCol = contador;
            filaDatos = sheet.createRow(numFilaDatos);

            celdaDatos = filaDatos.createCell(0, CellType.NUMERIC);
            celdaDatos.setCellValue(entradaEfectivo);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(1, CellType.NUMERIC);
            celdaDatos.setCellValue(entradaTransferencia);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(2, CellType.NUMERIC);
            celdaDatos.setCellValue(entradaTarjeta);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            cabecera = new String[]{"01 Efectivo", "02 Transferencia", "03 Tarjeta"};
            numFilaDatos = numFilaDatos + 2;
            filaEncabezados = sheet.createRow(numFilaDatos);
            celdaEncabezado = filaEncabezados.createCell(2);
            celdaEncabezado.setCellStyle(tituloEstilo);
            celdaEncabezado.setCellValue("SALIDAS - FORMA DE PAGO");

            numFilaDatos++;
            filaEncabezados = sheet.createRow(numFilaDatos);
            for (int i = 0; i < cabecera.length; i++) {
                celdaEncabezado = filaEncabezados.createCell(i);
                celdaEncabezado.setCellStyle(headerStyle);
                celdaEncabezado.setCellValue(cabecera[i]);
            }

            numFilaDatos++;

            datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.NONE);
            datosEstilo.setBorderRight(BorderStyle.NONE);
            datosEstilo.setBorderLeft(BorderStyle.NONE);
            datosEstilo.setBorderTop(BorderStyle.NONE);
            datosEstilo.setAlignment(HorizontalAlignment.CENTER);
            datosEstilo.setVerticalAlignment(VerticalAlignment.CENTER);

            contador = 0;

            numCol = contador;
            filaDatos = sheet.createRow(numFilaDatos);

            celdaDatos = filaDatos.createCell(0, CellType.NUMERIC);
            celdaDatos.setCellValue(salidaEfectivo);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(1, CellType.NUMERIC);
            celdaDatos.setCellValue(salidaTransferencia);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(2, CellType.NUMERIC);
            celdaDatos.setCellValue(salidaTarjeta);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            //Datos finales 
            cabecera = new String[]{"Concepto", "Monto", "Concepto", "Monto", "Revisó"};
            numFilaDatos = numFilaDatos + 2;
            filaEncabezados = sheet.createRow(numFilaDatos);
            celdaEncabezado = filaEncabezados.createCell(0);
            celdaEncabezado.setCellStyle(tituloEstilo);
            celdaEncabezado.setCellValue("RESUMEN");

            numFilaDatos++;
            filaEncabezados = sheet.createRow(numFilaDatos);
            for (int i = 0; i < cabecera.length; i++) {
                celdaEncabezado = filaEncabezados.createCell(i);
                celdaEncabezado.setCellStyle(headerStyle);
                celdaEncabezado.setCellValue(cabecera[i]);
            }

            numFilaDatos++;

            datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.NONE);
            datosEstilo.setBorderRight(BorderStyle.NONE);
            datosEstilo.setBorderLeft(BorderStyle.NONE);
            datosEstilo.setBorderTop(BorderStyle.NONE);
            datosEstilo.setAlignment(HorizontalAlignment.CENTER);
            datosEstilo.setVerticalAlignment(VerticalAlignment.CENTER);

            contador = 0;

            numCol = contador;

            filaDatos = sheet.createRow(numFilaDatos);

            celdaDatos = filaDatos.createCell(0);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("Saldo inicial");

            celdaDatos = filaDatos.createCell(1, CellType.NUMERIC);
            celdaDatos.setCellValue(cd.getSaldoInicial());
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(2);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("Saldo en corte");

            celdaDatos = filaDatos.createCell(3, CellType.NUMERIC);
            celdaDatos.setCellValue(cd.getSaldoEnCaja());
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            numFilaDatos++;
            filaDatos = sheet.createRow(numFilaDatos);
            //espacio para reviso

            celdaDatos = filaDatos.createCell(0);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("Total de ventas");

            celdaDatos = filaDatos.createCell(1, CellType.NUMERIC);
            celdaDatos.setCellValue(cd.getTotalVentas());
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(2);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("Saldo en efectivo");

            celdaDatos = filaDatos.createCell(3, CellType.NUMERIC);
            celdaDatos.setCellValue(cd.getEfectivo());
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            numFilaDatos++;
            filaDatos = sheet.createRow(numFilaDatos);

            //espacio para reviso
            celdaDatos = filaDatos.createCell(0);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("Total de cobros");

            celdaDatos = filaDatos.createCell(1, CellType.NUMERIC);
            celdaDatos.setCellValue(cd.getTotalCobros());
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(2);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("Retiro en efectivo");

            celdaDatos = filaDatos.createCell(3, CellType.NUMERIC);
            celdaDatos.setCellValue(cd.getRetiro());
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            numFilaDatos++;
            filaDatos = sheet.createRow(numFilaDatos);

            //espacio para reviso
            celdaDatos = filaDatos.createCell(0);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("Total de gastos");

            celdaDatos = filaDatos.createCell(1, CellType.NUMERIC);
            celdaDatos.setCellValue(cd.getTotalGastos());
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(2);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("Saldo final");

            celdaDatos = filaDatos.createCell(3, CellType.NUMERIC);
            celdaDatos.setCellValue(cd.getSaldoFinal());
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(4);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue(cd.getPersona());

            numFilaDatos++;
            filaDatos = sheet.createRow(numFilaDatos);

            celdaDatos = filaDatos.createCell(0);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("Arqueos de caja");

            celdaDatos = filaDatos.createCell(1, CellType.NUMERIC);
            celdaDatos.setCellValue(cd.getArqueos());
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            //reajustamos al tamaño
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);

            //hacemos zoom
            sheet.setZoom(150);

            FileOutputStream fileOut = new FileOutputStream("ReportesExcel\\corte " + nombreDocumento + ".xlsx");
            book.write(fileOut);
            fileOut.close();
            File file = new File("ReportesExcel\\corte " + nombreDocumento + ".xlsx");
            Desktop.getDesktop().open(file);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(vistaPrecios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(vistaPrecios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eventoF1() throws DocumentException, ParseException {
        if (!"".equals(txtSaldoEnCaja.getText()) && !"".equals(txtRetiro1.getText())) {
            Number verificando = formateador.parse(removefirstChar(txtSaldoCaja1.getText()));
            if (verificando.doubleValue() == Double.parseDouble(txtSaldoEnCaja.getText())) {
                if (Double.parseDouble(txtRetiro1.getText()) <= Double.parseDouble(txtSaldoEnCaja.getText())) {

                    establecerFecha();
                    //try {
                    corteDia c = new corteDia();
                    Number setTotalVentas = formateador.parse(removefirstChar(txtTotalVentas.getText()));
                    c.setTotalVentas(setTotalVentas.doubleValue());

                    Number setTotalCobros = formateador.parse(removefirstChar(txtTotalCobros1.getText()));
                    c.setTotalCobros(setTotalCobros.doubleValue());

                    Number setSaldoEnCaja = formateador.parse(removefirstChar(txtSaldoCaja1.getText()));
                    c.setSaldoEnCaja(setSaldoEnCaja.doubleValue());

                    Number setTotalGastos = formateador.parse(removefirstChar(TablaFormas.getValueAt(3, 3).toString()));
                    c.setTotalGastos(setTotalGastos.doubleValue());

                    c.setEfectivo(Double.parseDouble(txtSaldoEnCaja.getText()));

                    c.setRetiro(Double.parseDouble(txtRetiro1.getText()));

                    Number setSaldoFinal = formateador.parse(removefirstChar(txtSaldoFInal1.getText()));
                    c.setSaldoFinal(setSaldoFinal.doubleValue());

                    Number setArqueoFinal = formateador.parse(removefirstChar(txtTotalArqueos.getText()));
                    c.setArqueos(setArqueoFinal.doubleValue());

                    c.setDiferencia(c.getSaldoEnCaja() - c.getEfectivo());
                    Date date = new Date();
                    c.setFechaCierre(fecha);

                    CerrarCaja cc = new CerrarCaja(new javax.swing.JFrame(), true);
                    cc.recibirDatosCorte(c);
                    cc.setVisible(true);
                    if (cc.indicador == true) {
                        c = cc.c;

                        corte.terminarCorte(c);
                        int idCorte = corte.idMax();
                        corteDiafinal(idCorte, 1);//el correcto
                        ticketCorte(idCorte);
                        File file = new File("C:\\Program Files (x86)\\AppLavanderia\\Tickets\\Corte" + idCorte + ".pdf");
                        imprimiendo m = new imprimiendo();
                        try {
                            m.imprimir(file);
                        } catch (PrinterException ex) {
                            Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(VistaCorte.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        hoy = "d";
                        establecerFecha();
                        corte.crearNuevoDia(c, fecha, hora);
                        vaciarDatos();
                    }
                    dispose();
                } else {
                    Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "El retiro es mayor al efectivo");
                    panel.showNotification();
                }
            } else {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "El efectivo en caja debe coincidir");
                panel.showNotification();
            }

        }
    }

    public void ticketCorte(int id) throws ParseException {
        try {
            FileOutputStream archivo;
            File file = new File("C:\\Program Files (x86)\\AppLavanderia\\Tickets\\Corte" + id + ".pdf");
            corteDia miCorte = corte.regresarCorte(id);
            archivo = new FileOutputStream(file);
            Rectangle pageSize = new Rectangle(140.76f, 500f); //ancho y alto
            Document doc = new Document(pageSize, 0, 0, 0, 0);
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            Image img = Image.getInstance("C:\\Program Files (x86)\\AppLavanderia\\Iconos\\logo 100x100.jpg");
            config configura = gastos.buscarDatos();
            Font letra2 = new Font(Font.FontFamily.HELVETICA, configura.getLetraChica() + 4, Font.BOLD);
            Font letraChiquita = new Font(Font.FontFamily.HELVETICA, configura.getLetraChica() + 2, Font.BOLD);

            PdfPTable logo = new PdfPTable(5);
            logo.setWidthPercentage(100);
            logo.getDefaultCell().setBorder(0);
            float[] columnaEncabezadoLogo = new float[]{20f, 20f, 60f, 20f, 20f};
            logo.setWidths(columnaEncabezadoLogo);
            logo.setHorizontalAlignment(Chunk.ALIGN_MIDDLE);
            logo.addCell("");
            logo.addCell("");
            logo.addCell(img);
            logo.addCell("");
            logo.addCell("");

            logo.addCell("");
            logo.addCell("");
            PdfPCell cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("CORTE", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            logo.addCell(cell);
            logo.addCell("");
            logo.addCell("");
            doc.add(logo);

            PdfPTable tablapro = new PdfPTable(1);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{100f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(configura.getNomnbre(), letraChiquita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);
            doc.add(tablapro);

            tablapro = new PdfPTable(2);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnaEncabezado = new float[]{50f, 50f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Inicio:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            DateFormat formatoSalida = new SimpleDateFormat("HH:mm");
            cell = new PdfPCell();
            Date horaInicioDate = StringADateHora(miCorte.getHoraInicio());
            cell = new PdfPCell(new Phrase(fechaFormatoCorrecto(miCorte.getFechaInicio()) + " " + formatoSalida.format(horaInicioDate), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Cierre:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            Date horaFinalDate = StringADateHora(miCorte.getHoraCierre());
            cell = new PdfPCell(new Phrase(fechaFormatoCorrecto(miCorte.getFechaCierre()) + " " + formatoSalida.format(horaFinalDate), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Registró:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(miCorte.getPersona(), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Saldo Inicial:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miCorte.getSaldoInicial()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Ventas:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miCorte.getTotalVentas()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Cobros:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miCorte.getTotalCobros()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Gastos:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miCorte.getTotalGastos()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Arqueos:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miCorte.getArqueos()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Saldo corte:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miCorte.getSaldoEnCaja()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Efectivo:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miCorte.getEfectivo()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Retiro:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miCorte.getRetiro()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Saldo final:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miCorte.getSaldoFinal()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Observación:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(miCorte.getComentario(), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            doc.add(Chunk.NEWLINE);
            doc.add(tablapro);

            tablapro = new PdfPTable(1);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnaEncabezado = new float[]{100f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);

            int i = 0;
            while (i < 4) {
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(".", letra2));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);
                i++;
            }

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("__________________", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("FIRMA", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            doc.add(tablapro);
            doc.close();
            archivo.close();
        } catch (DocumentException | IOException e) {
            System.out.println(e.toString());
        }
    }

}
