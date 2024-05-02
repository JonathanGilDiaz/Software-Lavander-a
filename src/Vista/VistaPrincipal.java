/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Cliente;
import Modelo.ClienteDao;
import Modelo.EmpleadosDao;
import Modelo.GastosDao;
import Modelo.PromocionDao;
import Modelo.Respaldos;
import Modelo.config;
import static Vista.vistaPrecios.reportePrecios;
import static Vista.vistaPrecios.reportePreciosExcel;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
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
import javaswingdev.GradientDropdownMenu;
import javaswingdev.MenuEvent;
import javaswingdev.Notification;

/**
 *
 * @author Jonathan Gil
 */
public class VistaPrincipal extends javax.swing.JFrame implements Runnable {

    GastosDao gastos = new GastosDao();
    EmpleadosDao emple = new EmpleadosDao();
    ClienteDao cli = new ClienteDao();
    PromocionDao promocionDao = new PromocionDao();
    config configura = gastos.buscarDatos();

    DefaultListModel model;

    String horas, min, seg, ampm, diaActual;

    Calendar calendario;
    Thread hi;
    DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
    DecimalFormat df = new DecimalFormat("$ #,##0.00;($ #,##0.00)");

    public VistaPrincipal() {
        initComponents();
        ZoneId mexicoCityZone = ZoneId.of("America/Mexico_City");
        ZonedDateTime nowInMexicoCity = ZonedDateTime.now(mexicoCityZone);
        System.out.println("Hora en Ciudad de México: " + nowInMexicoCity);
        
        gastos.cambiarLicencia("2025-06-01");
        //this.setLocation(-5, 0);
        this.setTitle("Software Lavandería");
        Seticon();
        model = new DefaultListModel();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;

        int width = screenSize.width;
        int height = screenSize.height - taskBarSize;

        this.setSize(width, height);
        this.setLocation(0, 0);
        hi = new Thread(this);
        hi.start();

        GradientDropdownMenu menu = new GradientDropdownMenu();
        menu.setBackground(new Color(0, 0, 102));
        menu.setGradientColor(new Color(0, 0, 204), new Color(51, 51, 255));
        if (promocionDao.promocionActiva() == 1) {
            menu.addItem("Venta ", "Nueva nota", "Control de notas", "Boletos");
        } else {
            menu.addItem("Venta ", "Nueva nota", "Control de notas");
        }
        menu.addItem("Catálogo ", "Clientes", "Empleados", "Precios", "Gastos");
        menu.addItem("Control ", "Corte de venta", "Nómina");
        menu.addItem("Configuraciones ", "Datos de la empresa", "Promoción", "Saldo inicial", "Respaldar datos", "Restaurar datos", "Cerrar Sesión", "Licencia");
        menu.addItem("Reportes ", "Nóminas", "Gastos", "Precios", "Corte diario", "Ventas", "Entregas", "Notas de tienda", "Abonos", "Concentrado general", "Notas canceladas", "Datos de clientes", "Arqueos de caja");
        menu.setFont(new java.awt.Font("sansserif", 1, 13));
        menu.setMenuHeight(30);
        menu.setFont(new java.awt.Font("Tahoma", 1, 13));
        menu.setHeaderGradient(false);
        menu.applay(this);
        String fechaString = gastos.retornarLicencia(); // Tu fecha en formato yyyy-MM-dd
        LocalDate fechaIngresada = LocalDate.parse(fechaString, DateTimeFormatter.ISO_DATE);
        LocalDate fechaActual = LocalDate.now();

        if (!fechaIngresada.isBefore(fechaActual)) {
            menu.addEvent(new MenuEvent() {
                @Override
                public void selected(int index, int subIndex, boolean menuItem) {
                    if (menuItem) {
                        switch (index) {
                            case 0:
                                switch (subIndex) {
                                    case 1:
                                        vistaNota vN = new vistaNota(new javax.swing.JFrame(), true);
                                        vN.setLocation(250, 80);
                                        vN.validandoDatos(0, true);
                                        vN.setVisible(true);
                                        break;
                                    case 2:
                                        VisualizarNotasCopia vn = new VisualizarNotasCopia(new javax.swing.JFrame(), true);
                                        vn.setLocation(200, 80);
                                        vn.setVisible(true);
                                        break;

                                    case 3:
                                        vistaPromocion vr = new vistaPromocion(new javax.swing.JFrame(), true);
                                        vr.setLocation(200, 80);
                                        vr.setVisible(true);
                                        break;
                                }
                                break;

                            case 1:
                                switch (subIndex) {
                                    case 1:
                                        vistaClientes vC = new vistaClientes(new javax.swing.JFrame(), true);
                                        vC.setLocation(170, 80);
                                        vC.setVisible(true);
                                        break;
                                    case 2:
                                        if (validarAcceso() == true) {
                                            vistaEmpleados vC1 = new vistaEmpleados(new javax.swing.JFrame(), true);
                                            vC1.setLocation(200, 80);
                                            vC1.setVisible(true);
                                        }
                                        break;
                                    case 3:
                                        if (validarAcceso() == true) {
                                            vistaPrecios vC2 = new vistaPrecios(new javax.swing.JFrame(), true);
                                            vC2.setLocation(200, 80);
                                            vC2.setVisible(true);
                                        }
                                        break;
                                    case 4:
                                        vistaGastos vC3 = new vistaGastos(new javax.swing.JFrame(), true);
                                        vC3.setLocation(200, 80);
                                        vC3.setVisible(true);
                                        break;
                                }
                                break;

                            case 2:
                                switch (subIndex) {
                                    case 1:
                                        VistaCorte vC = new VistaCorte(new javax.swing.JFrame(), true);
                                        vC.setLocation(50, 60);
                                        vC.setVisible(true);
                                        break;
                                    case 2:
                                        vistaNomina vC1 = new vistaNomina(new javax.swing.JFrame(), true);
                                        vC1.setLocationRelativeTo(null);
                                        vC1.setVisible(true);
                                        break;
                                }
                                break;
                            case 3:
                                switch (subIndex) {
                                    case 1:
                                        if (validarAcceso() == true) {
                                            DatosEmpresa vC = new DatosEmpresa(new javax.swing.JFrame(), true);
                                            vC.setVisible(true);
                                            labelImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/imagen_fondo.jpg"))); // NOI18N
                                        }
                                        break;
                                    case 2:
                                        int validar = promocionDao.promocionActiva();
                                        vistaDatosPromocion vP = new vistaDatosPromocion(new javax.swing.JFrame(), true);
                                        vP.setVisible(true);
                                        if (validar != promocionDao.promocionActiva()) {
                                            LoginN lg = new LoginN();
                                            dispose();
                                            lg.setVisible(true);
                                        }
                                        break;
                                    case 3:
                                        if (validarAcceso() == true) {
                                            AgregarSaldoInicial a = new AgregarSaldoInicial(new javax.swing.JFrame(), true);
                                            a.setVisible(true);
                                        }
                                        break;
                                    case 4:
                                        if (validarAcceso() == true) {
                                            Respaldos r = new Respaldos();
                                            r.respaldo();
                                        }
                                        break;

                                    case 5:
                                        if (validarAcceso() == true) {
                                            Respaldos ra = new Respaldos();
                                            ra.restaurar();
                                        }
                                        break;
                                    case 6:
                                        vistaConfirmacion vCerrar = new vistaConfirmacion(new javax.swing.JFrame(), true);
                                        vCerrar.setVisible(true);
                                        if (vCerrar.accionRealizada == true) {
                                            LoginN lg = new LoginN();
                                            dispose();
                                            lg.setVisible(true);
                                        }
                                        break;
                                    case 7:
                                        ContraseñaConUsuarioLicencia vCU = new ContraseñaConUsuarioLicencia(new javax.swing.JFrame(), true);
                                        vCU.paraLicencia();
                                        vCU.setVisible(true);
                                        if (vCU.contraseñaAceptada == true) {
                                            abrirReporteDiario cvR = new abrirReporteDiario(new javax.swing.JFrame(), true);
                                            cvR.setVisible(true);
                                            if (cvR.accionRealizada == 1) {
                                                gastos.cambiarLicencia(cvR.fechaAUsar);
                                                LoginN lg = new LoginN();
                                                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Licencia actualizada");
                                                panel.showNotification();
                                                dispose();
                                                lg.setVisible(true);
                                            }
                                        }
                                        break;
                                }

                                break;

                            case 4:
                                switch (subIndex) {
                                    case 1://Nomina
                                        vistaReportes at = new vistaReportes(new javax.swing.JFrame(), true);
                                        at.paraConcentradoGeneral();
                                        at.setVisible(true);
                                        if (at.accionRealizada == true) {
                                            vistaNomina vC1 = new vistaNomina(new javax.swing.JFrame(), true);
                                            vC1.reporteNomina(at.fechaInicial, at.fechaFinal);
                                            vC1.dispose();
                                        }
                                        break;
                                    case 2://Gastos
                                        vistaReportesGastos vg = new vistaReportesGastos(new javax.swing.JFrame(), true);
                                        vg.setLocation(200, 51);
                                        vg.setVisible(true);
                                        break;
                                    case 3://Precios
                                        elegirPdfExcel cc5 = new elegirPdfExcel(new javax.swing.JFrame(), true);
                                        cc5.setVisible(true);
                                        if (cc5.accionRealizada == true) {
                                            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Generando reporte");
                                            panel.showNotification();
                                            if (cc5.tipo == true) {
                                                reportePrecios();
                                            } else {
                                                reportePreciosExcel();
                                            }
                                        }
                                        break;

                                    case 4://Corte Diario
                                        visualizarCortes vcc = new visualizarCortes(new javax.swing.JFrame(), true);
                                        vcc.setLocation(230, 60);
                                        vcc.setVisible(true);
                                        break;

                                    case 5://Ventas
                                        vistaReportesVentas vC1 = new vistaReportesVentas(new javax.swing.JFrame(), true);
                                        vC1.setLocation(200, 51);
                                        vC1.setVisible(true);
                                        break;
                                    case 6://Entregas
                                        vistaReportesEntregas ve = new vistaReportesEntregas(new javax.swing.JFrame(), true);
                                        ve.setLocation(200, 51);
                                        ve.setVisible(true);
                                        break;
                                    case 7://Notas en tienda
                                        elegirPdfExcel ccq = new elegirPdfExcel(new javax.swing.JFrame(), true);
                                        ccq.setVisible(true);
                                        if (ccq.accionRealizada == true) {
                                            DatosReporte aq = new DatosReporte(new javax.swing.JFrame(), true);
                                            if (ccq.tipo == true) {
                                                aq.notasEnTienda();
                                            } else {
                                                aq.notasEnTiendaExcel();
                                            }
                                        }
                                        break;

                                    case 8://Abonos
                                        vistaReportesAbonos vab = new vistaReportesAbonos(new javax.swing.JFrame(), true);
                                        vab.setLocation(200, 51);
                                        vab.setVisible(true);
                                        break;

                                    case 9://Concentrado general
                                        vistaReportesConcentradoGeneral vcg = new vistaReportesConcentradoGeneral(new javax.swing.JFrame(), true);
                                        vcg.setLocation(200, 51);
                                        vcg.setVisible(true);
                                        break;
                                    case 10://Notas canceladas
                                        vistaReportesNotasCanceladas vNC = new vistaReportesNotasCanceladas(new javax.swing.JFrame(), true);
                                        vNC.setLocation(200, 51);
                                        vNC.setVisible(true);
                                        break;

                                    case 11://Datos de clientes
                                        elegirPdfExcel ccx = new elegirPdfExcel(new javax.swing.JFrame(), true);
                                        ccx.setVisible(true);
                                        if (ccx.accionRealizada == true) {
                                            if (ccx.tipo == true) {
                                                reporteClientePDF();
                                            } else {
                                                reporteClienteExcel();
                                            }
                                        }
                                        break;
                                    case 12:
                                        vistaArqueos va  = new vistaArqueos(new javax.swing.JFrame(), true);
                                        va.setLocation(230, 60);
                                        va.setVisible(true);
                                        break;
                                }
                                break;

                        }
                    }
                }
            });
        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.INFO, Notification.Location.TOP_CENTER, "Renueve su licencia");
            panel.showNotification();
            menu.addEvent(new MenuEvent() {
                @Override
                public void selected(int index, int subIndex, boolean menuItem) {
                    if (menuItem) {
                        switch (index) {
                            case 3:
                                switch (subIndex) {
                                    case 6:
                                        ContraseñaConUsuarioLicencia vCU = new ContraseñaConUsuarioLicencia(new javax.swing.JFrame(), true);
                                        vCU.paraLicencia();
                                        vCU.setVisible(true);
                                        if (vCU.contraseñaAceptada == true) {
                                            abrirReporteDiario cvR = new abrirReporteDiario(new javax.swing.JFrame(), true);
                                            cvR.setVisible(true);
                                            if (cvR.accionRealizada == 1) {
                                                gastos.cambiarLicencia(cvR.fechaAUsar);
                                                LoginN lg = new LoginN();
                                                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Licencia actualizada");
                                                panel.showNotification();
                                                dispose();
                                                lg.setVisible(true);
                                            }
                                        }
                                        break;
                                }
                                break;
                        }
                    }
                }
            });
        }
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
        labelImagen = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel1KeyPressed(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1370, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 600, 1370, -1));

        labelImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/imagen_fondo.jpg"))); // NOI18N
        jPanel1.add(labelImagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 200, -1, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 153));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 50, 310, 50));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 153));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 110, 190, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        int codigo = evt.getKeyCode();

    }//GEN-LAST:event_formKeyPressed

    private void jPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyPressed

    }//GEN-LAST:event_jPanel1KeyPressed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked

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
            java.util.logging.Logger.getLogger(VistaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel labelImagen;
    // End of variables declaration//GEN-END:variables

    private void Seticon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
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

    private static CellStyle getContabilidadCellStyle(Workbook workbook, DecimalFormat df) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat(df.format(0)));
        return style;
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

    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == hi) {
            calcula();
            horas = String.valueOf(Integer.parseInt(horas));
            jLabel7.setText(horas + ":" + min);
            jLabel8.setText(diaActual);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }

    public boolean validarAcceso() {
        boolean bandera = false;
        ContraseñaConUsuario cc = new ContraseñaConUsuario(new javax.swing.JFrame(), true);
        cc.cambiandoEncabezado(3);
        cc.setVisible(true);
        if (cc.contraseñaAceptada == true) {
            bandera = true;
        }
        return bandera;
    }

    private void calcula() {
        Calendar calendario = new GregorianCalendar();
        LocalTime horaActual = LocalTime.now();
        LocalTime horaDesdeString = LocalTime.parse(configura.getHora());

        if ("mas".equals(configura.getIndicadorHora())) {
            horaActual = horaActual.plusHours(horaDesdeString.getHour())
                    .plusMinutes(horaDesdeString.getMinute())
                    .plusSeconds(horaDesdeString.getSecond());

        } else {
            horaActual = horaActual.minusHours(horaDesdeString.getHour())
                    .minusMinutes(horaDesdeString.getMinute())
                    .minusSeconds(horaDesdeString.getSecond());
        }
        // Crear un objeto LocalDateTime a partir de LocalTime actual y la fecha actual
        LocalDateTime fechaYHoraActual = LocalDateTime.of(LocalDate.now(), horaActual);

// Convertir LocalDateTime a Instant, que es compatible con Date
        Instant instant = fechaYHoraActual.atZone(ZoneId.systemDefault()).toInstant();

// Crear un objeto Date a partir del Instant
        Date dateActual = Date.from(instant);

        calendario.setTime(dateActual);
        ampm = calendario.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

        horas = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);
        Formatter obj2 = new Formatter();
        Formatter obj = new Formatter();
        min = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
        seg = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND);
        diaActual = "" + String.valueOf(obj2.format("%02d", calendario.get(Calendar.DAY_OF_MONTH))) + "-" + String.valueOf(obj.format("%02d", 1 + calendario.get(Calendar.MONTH))) + "-" + calendario.get(Calendar.YEAR);
    }

    public void reporteClientePDF() {
        try {

            Formatter obj = new Formatter();
            Formatter obj2 = new Formatter();

            LocalDateTime m = LocalDateTime.now(); //Obtenemos la fecha actual
            String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
            String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
            String DiagHoy = dia + "-" + mes + "-" + m.getYear();
            String fechaHoyResta = m.getYear() + "-" + mes + "-" + dia;

            config configura = new config();
            configura = gastos.buscarDatos();
            Font letra = new Font(Font.FontFamily.TIMES_ROMAN, 11);
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            FileOutputStream archivo;
            //File file = new File("corte" + fechaHoy + ".pdf");
            File file = new File("ReportesPDF\\reporteDstosClientes.pdf");
            archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            //Image img = Image.getInstance("Iconos\\logo 100x100.jpg");
            Image img = Image.getInstance("Iconos\\logo 100x100.jpg");

            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{85f, 15f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell = new PdfPCell();
            String tituloEncabezado = "";
            tituloEncabezado = "DATOS DE CLIENTES";

            cell = new PdfPCell(new Phrase(configura.getNomnbre() + "\nLISTADO DE LOS DATOS\n" + tituloEncabezado + "\nFECHA DE EMISIÓN " + DiagHoy, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            encabezado.addCell(img);
            doc.add(encabezado);

            PdfPTable tablapro = new PdfPTable(7);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] columnapro = new float[]{4f, 15f, 9f, 9f, 15f, 15f, 5f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1 = new PdfPCell(new Phrase("ID", letra));
            PdfPCell pro2 = new PdfPCell(new Phrase("Nombre", letra));
            PdfPCell pro3 = new PdfPCell(new Phrase("Creación", letra));
            PdfPCell pro4 = new PdfPCell(new Phrase("Teléfono ", letra));
            PdfPCell pro5 = new PdfPCell(new Phrase("Dirección", letra));
            PdfPCell pro6 = new PdfPCell(new Phrase("Correo", letra));
            PdfPCell pro7 = new PdfPCell(new Phrase("Estado", letra));

            pro1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro4.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro5.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro6.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro7.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            tablapro.addCell(pro5);
            tablapro.addCell(pro6);
            tablapro.addCell(pro7);

            List<Cliente> ListaCl = cli.ListarCliente();

            for (int i = 0; i < ListaCl.size(); i++) {
                String idF = String.format("%0" + 2 + "d", ListaCl.get(i).getId());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(idF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String clienteF = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(clienteF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(fechaF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String telefonoF = ListaCl.get(i).getTelefono();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(telefonoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String direccionF = ListaCl.get(i).getDomicilio();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(direccionF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String correoF = ListaCl.get(i).getCorreo();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(correoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String estadof = "";
                if (ListaCl.get(i).getEstado() == 1) {
                    estadof = "A";
                } else {
                    estadof = "I";
                }
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(estadof, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

            }

            doc.add(tablapro);

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

            Paragraph primero = new Paragraph();
            primero.setFont(negrita);
            primero.add(Chunk.NEWLINE);
            primero.add(Chunk.NEWLINE);
            primero.add(Chunk.NEWLINE);
            primero.add(Chunk.NEWLINE);
            doc.add(primero);

            doc.add(tablaFinal);

            doc.close();
            archivo.close();
            Desktop.getDesktop().open(file);
        } catch (DocumentException | IOException e) {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe cerrar el documento antes");
            panel.showNotification();
        }
    }

    public void reporteClienteExcel() {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Datos de cliente");

        try {
            InputStream is = new FileInputStream("Iconos\\logo 100x100.jpg");
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

            Formatter obj = new Formatter();
            Formatter obj2 = new Formatter();

            LocalDateTime m = LocalDateTime.now(); //Obtenemos la fecha actual
            String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
            String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
            String DiagHoy = dia + "-" + mes + "-" + m.getYear();
            String fechaHoyResta = m.getYear() + "-" + mes + "-" + dia;

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
            celdaTitulo.setCellValue("DATOS DE CLIENTES");
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 7));

            String tituloEncabezado = "";
            tituloEncabezado = "LISTADO DE INFORMACIÓN";
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

            String[] cabecera = new String[]{"ID", "Nombre", "Creación", "Teléfono", "Dirección", "Correo", "Estado"};

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

            Row filaEncabezados = sheet.createRow(6);
            for (int i = 0; i < cabecera.length; i++) {
                Cell celdaEncabezado = filaEncabezados.createCell(i);
                celdaEncabezado.setCellStyle(headerStyle);
                celdaEncabezado.setCellValue(cabecera[i]);
            }

            int numFilaDatos = 7;

            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.NONE);
            datosEstilo.setBorderRight(BorderStyle.NONE);
            datosEstilo.setBorderLeft(BorderStyle.NONE);
            datosEstilo.setBorderTop(BorderStyle.NONE);
            datosEstilo.setAlignment(HorizontalAlignment.CENTER);
            datosEstilo.setVerticalAlignment(VerticalAlignment.CENTER);

            List<Cliente> ListaCl = cli.ListarCliente();

            int numCol = ListaCl.size();

            double totalVendido = 0;
            double totalCobrado = 0;
            for (int i = 0; i < ListaCl.size(); i++) {
                Row filaDatos = sheet.createRow(numFilaDatos);

                List<String> datosEscribir = new ArrayList<String>();

                String folioF = String.format("%0" + 2 + "d", ListaCl.get(i).getId());
                datosEscribir.add(folioF);

                String clienteF = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
                datosEscribir.add(clienteF);

                String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
                datosEscribir.add(fechaF);

                String telefonoF = ListaCl.get(i).getTelefono();
                datosEscribir.add(telefonoF);

                String direccionF = ListaCl.get(i).getDomicilio();
                datosEscribir.add(direccionF);

                String correoF = ListaCl.get(i).getCorreo();
                datosEscribir.add(correoF);

                String estadof = "";
                if (ListaCl.get(i).getEstado() == 1) {
                    estadof = "A";
                } else {
                    estadof = "I";
                }
                datosEscribir.add(estadof);

                for (int j = 0; j < datosEscribir.size(); j++) {
                    Cell celdaDatos = filaDatos.createCell(j);
                    celdaDatos.setCellStyle(datosEstilo);
                    celdaDatos.setCellValue(datosEscribir.get(j));
                }

                numFilaDatos++;

            }

            //reajustamos al tamaño
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);

            //hacemos zoom
            sheet.setZoom(150);

            FileOutputStream fileOut = new FileOutputStream("ReportesExcel\\ReporteDatosDeClientes.xlsx");
            book.write(fileOut);
            fileOut.close();
            File file = new File("ReportesExcel\\ReporteDatosDeClientes.xlsx");
            Desktop.getDesktop().open(file);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(vistaPrecios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(vistaPrecios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
