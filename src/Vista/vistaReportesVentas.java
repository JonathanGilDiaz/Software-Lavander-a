/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.ClienteDao;
import Modelo.Detalle;
import Modelo.Empleados;
import Modelo.EmpleadosDao;
import Modelo.Nota;
import Modelo.NotaDao;
import Modelo.anticipo;
import Modelo.anticipoDao;
import Modelo.entrega;
import Modelo.reportes;
import com.raven.swing.Table3;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import com.itextpdf.text.DocumentException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jonathan Gil
 */
public class vistaReportesVentas extends javax.swing.JDialog {

    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
    DefaultTableCellRenderer tcrderecha = new DefaultTableCellRenderer();
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel modeloTotales = new DefaultTableModel();
    reportes reporte = new reportes();
    ClienteDao client = new ClienteDao();
    String fecha;
    NotaDao notaDao = new NotaDao();
    anticipoDao anticip = new anticipoDao();
    EmpleadosDao emple = new EmpleadosDao();
    DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
    int apoyoR;
    Empleados e;
    List<anticipo> lsAnti = anticip.listarAnticipo();
    List<Detalle> todosDetalles = notaDao.listarDetalles();
    List<entrega> todasEntregas = notaDao.listarNotasEntregas();

    public void vaciarEmpleado(Empleados e) {
        this.e = e;
    }

    public vistaReportesVentas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Seticon();
        this.setTitle("Software Lavanderia - Reporte de ventas");
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tcrderecha.setHorizontalAlignment(SwingConstants.RIGHT);
        ((DefaultTableCellRenderer) TableLista.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        DefaultTableCellRenderer tcrDerecha = new DefaultTableCellRenderer();
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        TableLista.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TableLista.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TableLista.getColumnModel().getColumn(2).setCellRenderer(tcr);
        TableLista.getColumnModel().getColumn(3).setCellRenderer(tcr);
        TableLista.getColumnModel().getColumn(4).setCellRenderer(tcrDerecha);
        TableLista.getColumnModel().getColumn(5).setCellRenderer(tcrDerecha);
        TableLista.getColumnModel().getColumn(6).setCellRenderer(tcrDerecha);
        TableLista.getColumnModel().getColumn(7).setCellRenderer(tcrDerecha);
        TableLista.getColumnModel().getColumn(8).setCellRenderer(tcrDerecha);
        TableLista.getColumnModel().getColumn(9).setCellRenderer(tcr);
        ((DefaultTableCellRenderer) TableLista.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        TableTotales.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TableTotales.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TableTotales.getColumnModel().getColumn(2).setCellRenderer(tcr);
        TableTotales.getColumnModel().getColumn(3).setCellRenderer(tcr);
        TableTotales.getColumnModel().getColumn(4).setCellRenderer(tcrDerecha);
        TableTotales.getColumnModel().getColumn(5).setCellRenderer(tcrDerecha);
        TableTotales.getColumnModel().getColumn(6).setCellRenderer(tcrDerecha);
        TableTotales.getColumnModel().getColumn(7).setCellRenderer(tcrDerecha);
        TableTotales.getColumnModel().getColumn(8).setCellRenderer(tcrDerecha);
        TableTotales.getColumnModel().getColumn(9).setCellRenderer(tcr);
        ((DefaultTableCellRenderer) TableTotales.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        LocalDate fechaActual = LocalDate.now();
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaActual);

        // Formatear la fecha como una cadena en el formato deseado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        fecha = fechaSQL.toLocalDate().format(formatter);
        listarPorPeriodoDeFecha();

    }

    public void LimpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
        for (int i = 0; i < modeloTotales.getRowCount(); i++) {
            modeloTotales.removeRow(i);
            i = i - 1;
        }
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

    public void listarPorPeriodoDeFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Nota> ListaCl = notaDao.listarPorFecha(sdf.format(jDateChooser1.getDate()), sdf.format(jDateChooser2.getDate()));
        List<Nota> antiListaCl = notaDao.listarPorFechaAnti(sdf.format(jDateChooser1.getDate()), sdf.format(jDateChooser2.getDate()));
        vaciarATabla(ListaCl, antiListaCl);
    }

    public void vaciarATabla(List<Nota> ListaCl, List<Nota> antiListaCl) {
        modelo = (DefaultTableModel) TableLista.getModel();
        Object[] ob = new Object[10];
        double totalVendido = 0;
        double totalCobrado = 0;
        double totalAnticipo = 0;
        double totalAdeudo = 0;
        double cobrosRealizados = 0;
        for (int i = 0; i < ListaCl.size(); i++) {
            ob[0] = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
            ob[1] = String.valueOf(ListaCl.get(i).getFolio());

            ob[2] = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();

            List<Detalle> lsDetalle = new ArrayList<Detalle>();
            for (int m = 0; m < todosDetalles.size(); m++) {
                if (todosDetalles.get(m).getIdVenta() == ListaCl.get(i).getFolio()) {
                    lsDetalle.add(todosDetalles.get(m));
                }
            }
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
                conceptoF += String.format("%0" + 2 + "d", repetidos.get(j)) + "-";
            }
            conceptoF = conceptoF.replaceFirst(".$", "");
            ob[3] = conceptoF;

            ob[4] = "$" + formateador.format(ListaCl.get(i).getVentaTotal());
            totalVendido = totalVendido + ListaCl.get(i).getVentaTotal();

            ob[5] = "$" + formateador.format(ListaCl.get(i).getAnticipo());
            totalAnticipo = totalAnticipo + ListaCl.get(i).getAnticipo();

            ob[6] = "$" + formateador.format(ListaCl.get(i).getVentaTotal() - ListaCl.get(i).getAnticipo());
            totalAdeudo = totalAdeudo + (ListaCl.get(i).getVentaTotal() - ListaCl.get(i).getAnticipo());

            double anticiposAcumulados = 0;
            for (int j = 0; j < lsAnti.size(); j++) {
                if (lsAnti.get(j).getFolio() == ListaCl.get(i).getFolio()) {
                    anticiposAcumulados = anticiposAcumulados + lsAnti.get(j).getCantidad();
                }
            }

            ob[7] = "" + "$" + formateador.format(anticiposAcumulados);;
            totalCobrado = totalCobrado + anticiposAcumulados; //abonos

            String cobrosRealizadosf = "";
            if (ListaCl.get(i).getEntrega() == 0) {
                cobrosRealizadosf = "$" + formateador.format(0.00);
            } else {
                
                entrega sacandoPago = new entrega();
                for(int m = 0; m<todasEntregas.size(); m++){
                    if(todasEntregas.get(m).getIdnota()==ListaCl.get(i).getFolio())
                     sacandoPago = todasEntregas.get(m);
                }
                cobrosRealizadosf = "$" + formateador.format(sacandoPago.getPago());
                cobrosRealizados = cobrosRealizados + sacandoPago.getPago();
            }
            ob[8] = cobrosRealizadosf;

            ob[9] = String.format("%0" + 2 + "d", ListaCl.get(i).getIdRecibe());
            modelo.addRow(ob);
        }
        //antinotas
        for (int i = 0; i < antiListaCl.size(); i++) {

            ob[0] = fechaFormatoCorrecto(antiListaCl.get(i).getFecha());
            ob[1] = String.valueOf(antiListaCl.get(i).getFolio());
            ob[2] = antiListaCl.get(i).getNombre() + " " + antiListaCl.get(i).getApellido();

            List<Detalle> lsDetalle = new ArrayList<>();
            for (int m = 0; m < todosDetalles.size(); m++) {
                if (todosDetalles.get(m).getIdVenta() == antiListaCl.get(i).getFolio()) {
                    lsDetalle.add(todosDetalles.get(m));
                }
            }
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
            ob[3] = conceptoF;

            ob[4] = "$" + formateador.format(antiListaCl.get(i).getVentaTotal());
            totalVendido = totalVendido + antiListaCl.get(i).getVentaTotal();

            ob[5] = "$" + formateador.format(antiListaCl.get(i).getAnticipo());
            totalAnticipo = totalAnticipo + antiListaCl.get(i).getAnticipo();
            ob[6] = "$" + 00;
            ob[7] = "" + "$" + 00;
            ob[8] = "$" + 00;
            ob[9] = String.format("%0" + 2 + "d", antiListaCl.get(i).getIdRecibe());
            modelo.addRow(ob);
        }
        TableLista.setModel(modelo);

        modeloTotales = (DefaultTableModel) TableTotales.getModel();
        ob[0] = "Total";
        ob[1] = "";
        ob[2] = "";
        ob[3] = "";
        ob[4] = "$" + formateador.format(totalVendido);
        ob[5] = "$" + formateador.format(totalAnticipo);
        ob[6] = "$" + formateador.format(totalAdeudo);
        ob[7] = "$" + formateador.format(totalCobrado);
        ob[8] = "$" + formateador.format(cobrosRealizados);
        ob[9] = "";
        modeloTotales.addRow(ob);
        TableTotales.setModel(modeloTotales);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooser2 = new com.raven.datechooser.DateChooser();
        jDateChooser1 = new com.raven.datechooser.DateChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        Titulo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        TableTotales = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        TableLista = new javax.swing.JTable();
        panelNuevo = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JLabel();
        panelModificar = new javax.swing.JPanel();
        btnModificar = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        primeraFecha = new textfield.TextField();
        segundaFecha = new textfield.TextField();

        jDateChooser2.setForeground(new java.awt.Color(51, 102, 255));
        jDateChooser2.setTextRefernce(segundaFecha);
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jDateChooser1.setForeground(new java.awt.Color(51, 102, 255));
        jDateChooser1.setTextRefernce(primeraFecha);
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));

        Titulo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 32)); // NOI18N
        Titulo.setForeground(new java.awt.Color(255, 255, 255));
        Titulo.setText("Reporte de ventas");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(637, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-5, 0, 960, 50));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableTotales = new Table3();
        TableTotales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableTotales.setToolTipText("");
        TableTotales.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TableTotales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableTotalesMouseClicked(evt);
            }
        });
        TableTotales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableTotalesKeyPressed(evt);
            }
        });
        jScrollPane8.setViewportView(TableTotales);
        if (TableTotales.getColumnModel().getColumnCount() > 0) {
            TableTotales.getColumnModel().getColumn(0).setPreferredWidth(15);
            TableTotales.getColumnModel().getColumn(1).setPreferredWidth(15);
            TableTotales.getColumnModel().getColumn(2).setPreferredWidth(65);
            TableTotales.getColumnModel().getColumn(3).setPreferredWidth(20);
            TableTotales.getColumnModel().getColumn(4).setPreferredWidth(20);
            TableTotales.getColumnModel().getColumn(5).setPreferredWidth(20);
            TableTotales.getColumnModel().getColumn(6).setPreferredWidth(20);
            TableTotales.getColumnModel().getColumn(7).setPreferredWidth(20);
            TableTotales.getColumnModel().getColumn(8).setPreferredWidth(10);
            TableTotales.getColumnModel().getColumn(9).setPreferredWidth(5);
        }

        jPanel4.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 920, 40));

        TableLista = new Table3();
        TableLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Folio", "Cliente", "Concepto", "Importe", "Anticipo", "Adeudo", "Abono", "Cobros", "E"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableLista.setToolTipText("");
        TableLista.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TableLista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableListaMouseClicked(evt);
            }
        });
        TableLista.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableListaKeyPressed(evt);
            }
        });
        jScrollPane9.setViewportView(TableLista);
        if (TableLista.getColumnModel().getColumnCount() > 0) {
            TableLista.getColumnModel().getColumn(0).setPreferredWidth(30);
            TableLista.getColumnModel().getColumn(1).setPreferredWidth(25);
            TableLista.getColumnModel().getColumn(2).setPreferredWidth(80);
            TableLista.getColumnModel().getColumn(3).setPreferredWidth(25);
            TableLista.getColumnModel().getColumn(4).setPreferredWidth(30);
            TableLista.getColumnModel().getColumn(5).setPreferredWidth(30);
            TableLista.getColumnModel().getColumn(6).setPreferredWidth(30);
            TableLista.getColumnModel().getColumn(7).setPreferredWidth(30);
            TableLista.getColumnModel().getColumn(8).setPreferredWidth(30);
            TableLista.getColumnModel().getColumn(9).setPreferredWidth(1);
        }

        jPanel4.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 920, 380));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 173, 940, 450));

        panelNuevo.setBackground(new java.awt.Color(255, 255, 255));
        panelNuevo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelNuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelNuevoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelNuevoMouseEntered(evt);
            }
        });

        btnNuevo.setBackground(new java.awt.Color(153, 204, 255));
        btnNuevo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        btnNuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imagen1.png"))); // NOI18N
        btnNuevo.setToolTipText("F1 - Visualizar en PDF");
        btnNuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNuevoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNuevoMouseExited(evt);
            }
        });
        btnNuevo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnNuevoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelNuevoLayout = new javax.swing.GroupLayout(panelNuevo);
        panelNuevo.setLayout(panelNuevoLayout);
        panelNuevoLayout.setHorizontalGroup(
            panelNuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelNuevoLayout.setVerticalGroup(
            panelNuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 50, 50));

        panelModificar.setBackground(new java.awt.Color(255, 255, 255));
        panelModificar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelModificarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelModificarMouseEntered(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(255, 255, 255));
        btnModificar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/excel.png"))); // NOI18N
        btnModificar.setToolTipText("F2 - Visualizar en Excel");
        btnModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModificarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnModificarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnModificarMouseExited(evt);
            }
        });
        btnModificar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnModificarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelModificarLayout = new javax.swing.GroupLayout(panelModificar);
        panelModificar.setLayout(panelModificarLayout);
        panelModificarLayout.setHorizontalGroup(
            panelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelModificarLayout.setVerticalGroup(
            panelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnModificar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 50, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("     Buscar por fecha");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 130, 20));

        primeraFecha.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        primeraFecha.setLabelText("Fecha de inicio");
        primeraFecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                primeraFechaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                primeraFechaKeyReleased(evt);
            }
        });
        jPanel2.add(primeraFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 130, 40));

        segundaFecha.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        segundaFecha.setLabelText("Fecha de fin");
        segundaFecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                segundaFechaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                segundaFechaKeyReleased(evt);
            }
        });
        jPanel2.add(segundaFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 130, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 940, 60));

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

    private void TableTotalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableTotalesMouseClicked
        if (evt.getClickCount() == 2) {

        }
    }//GEN-LAST:event_TableTotalesMouseClicked

    private void TableTotalesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableTotalesKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_TableTotalesKeyPressed

    private void btnNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseClicked
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Formatter obj = new Formatter();
        Formatter obj2 = new Formatter();
        LocalDateTime m = LocalDateTime.now(); //Obtenemos la fecha actual
        String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
        String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
        String DiagHoy = dia + "-" + mes + "-" + m.getYear();
        String fechaInicialBien = fechaFormatoCorrecto(sdf.format(jDateChooser1.getDate()));
        String fechaFInalBien = fechaFormatoCorrecto(sdf.format(jDateChooser2.getDate()));
        String tituloEncabezado = " DEL " + fechaInicialBien + " AL " + fechaFInalBien;
        Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Generando reporte");
        panel.showNotification();
        try {
            reporte.reportePDF("Ventas", TableLista, TableTotales, fecha, fecha, "\nREPORTE DE VENTAS\n" + tituloEncabezado + "\nFECHA DE EMISION " + DiagHoy);
        } catch (DocumentException ex) {
            Logger.getLogger(vistaReportesVentas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnNuevoMouseClicked

    private void btnNuevoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseEntered
        panelNuevo.setBackground(new Color(153, 204, 255));
    }//GEN-LAST:event_btnNuevoMouseEntered

    private void btnNuevoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseExited
        panelNuevo.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnNuevoMouseExited

    private void btnNuevoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNuevoKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_btnNuevoKeyPressed

    private void panelNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelNuevoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelNuevoMouseClicked

    private void panelNuevoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelNuevoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelNuevoMouseEntered

    private void btnModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseClicked
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        reporte.reporteExcel("Ventas", sdf.format(jDateChooser1.getDate()), sdf.format(jDateChooser2.getDate()), TableLista, TableTotales);
        Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Generando reporte");
        panel.showNotification();
    }//GEN-LAST:event_btnModificarMouseClicked

    private void btnModificarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseEntered
        panelModificar.setBackground(new Color(153, 204, 255));
        btnModificar.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }//GEN-LAST:event_btnModificarMouseEntered

    private void btnModificarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseExited
        panelModificar.setBackground(new Color(255, 255, 255));
        btnModificar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnModificarMouseExited

    private void btnModificarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnModificarKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_btnModificarKeyPressed

    private void panelModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelModificarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelModificarMouseClicked

    private void panelModificarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelModificarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelModificarMouseEntered

    private void primeraFechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_primeraFechaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
        }
    }//GEN-LAST:event_primeraFechaKeyPressed

    private void primeraFechaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_primeraFechaKeyReleased

    }//GEN-LAST:event_primeraFechaKeyReleased

    private void segundaFechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_segundaFechaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
        }
    }//GEN-LAST:event_segundaFechaKeyPressed

    private void segundaFechaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_segundaFechaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_segundaFechaKeyReleased

    private void TableListaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableListaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TableListaMouseClicked

    private void TableListaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableListaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TableListaKeyPressed

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        if (jDateChooser2.getDate() != null) {
            try {
                LimpiarTabla();
                listarPorPeriodoDeFecha();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ALGO SUCEDIO MAL, PRUEBA OTRA VEZ" + e);
            }
        }
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        jDateChooser2.setMinSelectableDate(jDateChooser1.getDate());
        jDateChooser2.setSelectedDate(jDateChooser1.getDate());
        if (jDateChooser1.getDate() != null) {
            try {
                LimpiarTabla();
                listarPorPeriodoDeFecha();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ALGO SUCEDIO MAL, PRUEBA OTRA FECHA" + e);
            }
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

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
            java.util.logging.Logger.getLogger(vistaReportesVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(vistaReportesVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(vistaReportesVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(vistaReportesVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                vistaReportesVentas dialog = new vistaReportesVentas(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable TableLista;
    private javax.swing.JTable TableTotales;
    private javax.swing.JLabel Titulo;
    private javax.swing.JLabel btnModificar;
    private javax.swing.JLabel btnNuevo;
    private com.raven.datechooser.DateChooser jDateChooser1;
    private com.raven.datechooser.DateChooser jDateChooser2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPanel panelModificar;
    private javax.swing.JPanel panelNuevo;
    private textfield.TextField primeraFecha;
    private textfield.TextField segundaFecha;
    // End of variables declaration//GEN-END:variables

    private void Seticon() {
    setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
    }

}
