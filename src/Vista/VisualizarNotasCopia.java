/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Cliente;
import Modelo.ClienteDao;
import Modelo.Empleados;
import Modelo.EmpleadosDao;
import Modelo.Nota;
import Modelo.NotaDao;
import Modelo.anticipo;
import Modelo.anticipoDao;
import static Vista.vistaNota.isNumeric;
import com.raven.model.StatusType;
import com.raven.swing.ScrollBar;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;

public class VisualizarNotasCopia extends javax.swing.JDialog {

    ClienteDao client = new ClienteDao();
    NotaDao notaDao = new NotaDao();
    DefaultTableModel modelo = new DefaultTableModel();
    anticipoDao anticip = new anticipoDao();
    EmpleadosDao emple = new EmpleadosDao();
    List<anticipo> lsA = anticip.listarAnticipo();
    List<Empleados> lsE = emple.listarEmpleados();
    List<Cliente> lsC = client.ListarCliente();
    List<Integer> listaFolios = new ArrayList<>();
    DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
    int apoyoR;

    public VisualizarNotasCopia(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(200, 50);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Software Lavandería - Control de notas");
        Seticon();

        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        DefaultTableCellRenderer tcrDerecha = new DefaultTableCellRenderer();
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        TableNotas.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TableNotas.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TableNotas.getColumnModel().getColumn(2).setCellRenderer(tcr);
        TableNotas.getColumnModel().getColumn(3).setCellRenderer(tcrDerecha);
        TableNotas.getColumnModel().getColumn(4).setCellRenderer(tcrDerecha);
        TableNotas.getColumnModel().getColumn(5).setCellRenderer(tcrDerecha);
        TableNotas.getColumnModel().getColumn(7).setCellRenderer(tcr);
        TableNotas.getColumnModel().getColumn(8).setCellRenderer(tcr);

        listarNotas(false);
        controladorLista.setVisible(false);
        jList1.setVisible(false);

        spTable.setVerticalScrollBar(new ScrollBar());
        spTable.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        jTextField1.requestFocus();

    }

    public void LimpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    public void buscarPorFolioTabla(Nota n) {
        if (n.getNombre() != "fallo") {
            modelo = (DefaultTableModel) TableNotas.getModel();
            Object[] ob = new Object[9];
            ob[0] = n.getFecha();
            ob[1] = n.getFolio();
            ob[2] = n.getNombre() + " " + n.getApellido();
            ob[3] = "$" + formateador.format(n.getVentaTotal());

            double abonos = n.getVentaTotal() - n.getTotalPagar();

            ob[4] = "$" + formateador.format(abonos);
            ob[5] = String.format("$%.2f", n.getTotalPagar());
            if (n.getEstado() == 1) { //SI esta cancelada
                ob[6] = StatusType.REJECT;
            } else {
                if (n.getEntrega() == 1) { //Si ya fue entregada
                    ob[6] = StatusType.APPROVED;
                } else {
                    ob[6] = StatusType.PENDING;
                }
            }
            for (int Fe = 0; Fe < lsE.size(); Fe++) {
                if (lsE.get(Fe).getId() == n.getIdRecibe()) {
                    ob[7] = lsE.get(Fe).getNombre();
                }
            }
            for (int Fe = 0; Fe < lsC.size(); Fe++) {
                if (lsC.get(Fe).getId() == n.getCodigoCLiente()) {
                    ob[8] = lsC.get(Fe).getDomicilio();
                }
            }
            modelo.addRow(ob);

            TableNotas.setModel(modelo);
        } else {
            listarNotas(false);
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

    public void listarNotas(boolean bandera) {//Metodo para vaciar las notas en su respectiva tabla
        List<Nota> lsnotas = notaDao.listarNotas(); //Obtenemos los datos de la tabla
        modelo = (DefaultTableModel) TableNotas.getModel();
        Object[] ob = new Object[9];
        for (int i = lsnotas.size() - 1; i >= 0; i--) {//las vaciamos de la mas reciente a la mas vieja
            if (bandera == false) { //false notas sin cancelar
                ob[0] = fechaFormatoCorrecto(lsnotas.get(i).getFecha());
                ob[1] = lsnotas.get(i).getFolio();
                ob[2] = lsnotas.get(i).getNombre() + " " + lsnotas.get(i).getApellido();
                ob[3] = "$" + formateador.format(lsnotas.get(i).getVentaTotal());

                double abonos = lsnotas.get(i).getVentaTotal() - lsnotas.get(i).getTotalPagar();

                ob[4] = "$" + formateador.format(abonos);
                ob[5] = "$" + formateador.format(lsnotas.get(i).getTotalPagar());
                if (lsnotas.get(i).getEstado() == 1) { //SI esta cancelada
                    ob[6] = StatusType.REJECT;
                } else {
                    if (lsnotas.get(i).getEntrega() == 1) { //Si ya fue entregada
                        ob[6] = StatusType.APPROVED;
                    } else {
                        ob[6] = StatusType.PENDING;
                    }
                }
                for (int Fe = 0; Fe < lsE.size(); Fe++) {
                    if (lsE.get(Fe).getId() == lsnotas.get(i).getIdRecibe()) {
                        ob[7] = lsE.get(Fe).getNombre();
                    }
                }
                for (int Fe = 0; Fe < lsC.size(); Fe++) {
                    if (lsC.get(Fe).getId() == lsnotas.get(i).getCodigoCLiente()) {
                        ob[8] = lsC.get(Fe).getDomicilio();
                    }
                }

                modelo.addRow(ob);
            } else {
                if (lsnotas.get(i).getEstado() == 1) { //canceladas
                    ob[0] = lsnotas.get(i).getFecha();
                    ob[1] = lsnotas.get(i).getFolio();
                    ob[2] = lsnotas.get(i).getNombre() + " " + lsnotas.get(i).getApellido();
                    ob[3] = "$" + formateador.format(lsnotas.get(i).getVentaTotal());

                    double abonos = lsnotas.get(i).getVentaTotal() - lsnotas.get(i).getTotalPagar();

                    ob[4] = "$" + formateador.format(abonos);
                    ob[5] = "$" + formateador.format(lsnotas.get(i).getTotalPagar());
                    if (lsnotas.get(i).getEstado() == 1) { //SI esta cancelada
                        ob[6] = StatusType.REJECT;
                    } else {
                        if (lsnotas.get(i).getEntrega() == 1) { //Si ya fue entregada
                            ob[6] = StatusType.APPROVED;
                        } else {
                            ob[6] = StatusType.PENDING;
                        }
                    }
                    for (int Fe = 0; Fe < lsE.size(); Fe++) {
                        if (lsE.get(Fe).getId() == lsnotas.get(i).getIdRecibe()) {
                            ob[7] = lsE.get(Fe).getNombre();
                        }
                    }
                    for (int Fe = 0; Fe < lsC.size(); Fe++) {
                        if (lsC.get(Fe).getId() == lsnotas.get(i).getCodigoCLiente()) {
                            ob[8] = lsC.get(Fe).getDomicilio();
                        }
                    }
                    modelo.addRow(ob);
                }
            }
        }
        TableNotas.setModel(modelo);
    }

    public void listarNotasPorNombre(int idCliente) {
        List<Nota> lsnotas = notaDao.listarNotas();
        modelo = (DefaultTableModel) TableNotas.getModel();
        Object[] ob = new Object[9];
        for (int i = lsnotas.size() - 1; i >= 0; i--) {
            if (lsnotas.get(i).getCodigoCLiente() == idCliente) {
                ob[0] = fechaFormatoCorrecto(lsnotas.get(i).getFecha());
                ob[1] = lsnotas.get(i).getFolio();
                ob[2] = lsnotas.get(i).getNombre() + " " + lsnotas.get(i).getApellido();
                ob[3] = "$" + formateador.format(lsnotas.get(i).getVentaTotal());

                double abonos = lsnotas.get(i).getVentaTotal() - lsnotas.get(i).getTotalPagar();

                ob[4] = "$" + formateador.format(abonos);
                ob[5] = "$" + formateador.format(lsnotas.get(i).getTotalPagar());
                if (lsnotas.get(i).getEstado() == 1) { //SI esta cancelada
                    ob[6] = StatusType.REJECT;
                } else {
                    if (lsnotas.get(i).getEntrega() == 1) { //Si ya fue entregada
                        ob[6] = StatusType.APPROVED;
                    } else {
                        ob[6] = StatusType.PENDING;
                    }
                }
                for (int Fe = 0; Fe < lsE.size(); Fe++) {
                    if (lsE.get(Fe).getId() == lsnotas.get(i).getIdRecibe()) {
                        ob[7] = lsE.get(Fe).getNombre();
                    }
                }
                for (int Fe = 0; Fe < lsC.size(); Fe++) {
                    if (lsC.get(Fe).getId() == lsnotas.get(i).getCodigoCLiente()) {
                        ob[8] = lsC.get(Fe).getDomicilio();
                    }
                }
                modelo.addRow(ob);
            }
        }
        TableNotas.setModel(modelo);
    }

    private static boolean isSubstring(String s, String seq) {
        return Pattern.compile(Pattern.quote(seq), Pattern.CASE_INSENSITIVE)
                .matcher(s).find();
    }

    public void listarPorPeriodoDeFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Nota> lsnotas = notaDao.listarPorFecha(sdf.format(jDateChooser1.getDate()), sdf.format(jDateChooser2.getDate()));
        modelo = (DefaultTableModel) TableNotas.getModel();
        Object[] ob = new Object[9];
        for (int i = 0; i < lsnotas.size(); i++) {
            ob[0] = fechaFormatoCorrecto(lsnotas.get(i).getFecha());
            ob[1] = lsnotas.get(i).getFolio();
            ob[2] = lsnotas.get(i).getNombre() + " " + lsnotas.get(i).getApellido();
            ob[3] = "$" + formateador.format(lsnotas.get(i).getVentaTotal());

            double abonos = lsnotas.get(i).getVentaTotal() - lsnotas.get(i).getTotalPagar();

            ob[4] = "$" + formateador.format(abonos);
            ob[5] = "$" + formateador.format(lsnotas.get(i).getTotalPagar());
            if (lsnotas.get(i).getEstado() == 1) { //SI esta cancelada
                ob[6] = StatusType.REJECT;
            } else {
                if (lsnotas.get(i).getEntrega() == 1) { //Si ya fue entregada
                    ob[6] = StatusType.APPROVED;
                } else {
                    ob[6] = StatusType.PENDING;
                }
            }
            for (int Fe = 0; Fe < lsE.size(); Fe++) {
                if (lsE.get(Fe).getId() == lsnotas.get(i).getIdRecibe()) {
                    ob[7] = lsE.get(Fe).getNombre();
                }
            }
            for (int Fe = 0; Fe < lsC.size(); Fe++) {
                if (lsC.get(Fe).getId() == lsnotas.get(i).getCodigoCLiente()) {
                    ob[8] = lsC.get(Fe).getDomicilio();
                }
            }
            modelo.addRow(ob);

        }
        TableNotas.setModel(modelo);
    }

    public void eventoF1() {
        vistaNota vN = new vistaNota(new javax.swing.JFrame(), true);
        vN.validandoDatos(0, true);

        vN.setVisible(true);
    }

    public void eventoF2() {
        vistaReportesVentas vC1 = new vistaReportesVentas(new javax.swing.JFrame(), true);
        vC1.setLocation(200, 51);
        vC1.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooser1 = new com.raven.datechooser.DateChooser();
        jDateChooser2 = new com.raven.datechooser.DateChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        controladorLista = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel11 = new javax.swing.JLabel();
        jTextField1 = new textfield.TextField();
        primeraFecha = new textfield.TextField();
        segundaFecha = new textfield.TextField();
        jPanel4 = new javax.swing.JPanel();
        spTable = new javax.swing.JScrollPane();
        TableNotas = new com.raven.swing.Table();
        panelNuevaNota = new javax.swing.JPanel();
        btnNota = new javax.swing.JLabel();
        panelReporte = new javax.swing.JPanel();
        btnReporte = new javax.swing.JLabel();

        jDateChooser1.setForeground(new java.awt.Color(51, 102, 255));
        jDateChooser1.setTextRefernce(primeraFecha);
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jDateChooser2.setForeground(new java.awt.Color(51, 102, 255));
        jDateChooser2.setTextRefernce(segundaFecha);
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CONTROL DE NOTAS ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(554, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-5, 0, 960, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        controladorLista.setViewportView(jList1);

        jPanel2.add(controladorLista, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 410, 10));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("     Buscar por fecha");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 130, 20));

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField1.setLabelText("Buscar nota");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 410, 40));

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
        jPanel2.add(primeraFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 130, 40));

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
        jPanel2.add(segundaFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 130, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 940, 70));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        spTable.setBorder(null);

        TableNotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Folio", "Cliente", "Total", "Pagado", "Pendiente", "Estatus", "Recibio", "Domicilio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableNotas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableNotasMouseClicked(evt);
            }
        });
        TableNotas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableNotasKeyPressed(evt);
            }
        });
        spTable.setViewportView(TableNotas);
        if (TableNotas.getColumnModel().getColumnCount() > 0) {
            TableNotas.getColumnModel().getColumn(0).setPreferredWidth(30);
            TableNotas.getColumnModel().getColumn(1).setPreferredWidth(8);
            TableNotas.getColumnModel().getColumn(2).setPreferredWidth(140);
            TableNotas.getColumnModel().getColumn(3).setPreferredWidth(40);
            TableNotas.getColumnModel().getColumn(4).setPreferredWidth(40);
            TableNotas.getColumnModel().getColumn(5).setPreferredWidth(40);
            TableNotas.getColumnModel().getColumn(6).setPreferredWidth(70);
            TableNotas.getColumnModel().getColumn(7).setPreferredWidth(40);
        }

        jPanel4.add(spTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 920, 460));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 940, 480));

        panelNuevaNota.setBackground(new java.awt.Color(255, 255, 255));
        panelNuevaNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelNuevaNota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelNuevaNotaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelNuevaNotaMouseEntered(evt);
            }
        });

        btnNota.setBackground(new java.awt.Color(153, 204, 255));
        btnNota.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        btnNota.setForeground(new java.awt.Color(255, 255, 255));
        btnNota.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnNota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar nota 22.png"))); // NOI18N
        btnNota.setToolTipText("F1 - Nueva nota");
        btnNota.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNotaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNotaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNotaMouseExited(evt);
            }
        });
        btnNota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnNotaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelNuevaNotaLayout = new javax.swing.GroupLayout(panelNuevaNota);
        panelNuevaNota.setLayout(panelNuevaNotaLayout);
        panelNuevaNotaLayout.setHorizontalGroup(
            panelNuevaNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNota, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelNuevaNotaLayout.setVerticalGroup(
            panelNuevaNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNota, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelNuevaNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 52, 50, 50));

        panelReporte.setBackground(new java.awt.Color(255, 255, 255));
        panelReporte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelReporte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelReporteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelReporteMouseEntered(evt);
            }
        });

        btnReporte.setBackground(new java.awt.Color(255, 255, 255));
        btnReporte.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        btnReporte.setForeground(new java.awt.Color(255, 255, 255));
        btnReporte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ESTADISTICAS.png"))); // NOI18N
        btnReporte.setToolTipText("F2 - Reporte");
        btnReporte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReporte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReporteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReporteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReporteMouseExited(evt);
            }
        });
        btnReporte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnReporteKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelReporteLayout = new javax.swing.GroupLayout(panelReporte);
        panelReporte.setLayout(panelReporteLayout);
        panelReporteLayout.setHorizontalGroup(
            panelReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnReporte, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelReporteLayout.setVerticalGroup(
            panelReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnReporte, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 52, 50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (evt.getClickCount() == 2) {
            List<Cliente> lista = client.buscarLetra(jTextField1.getText());
            int k = jList1.getSelectedIndex();
            jTextField1.setText(jList1.getSelectedValue());
            if (lista.size() == 0) {
                jList1.setVisible(false);
                LimpiarTabla();
                listarNotasPorNombre(listaFolios.get(k));
                Nota miNota = notaDao.buscarPorFolio(Integer.parseInt(TableNotas.getValueAt(0, 1).toString()));
                apoyoR = miNota.getCodigoCLiente();
            } else {
                jList1.setVisible(false);
                LimpiarTabla();
                listarNotasPorNombre(lista.get(k).getId());
                apoyoR = lista.get(k).getId();

            }
            controladorLista.setVisible(false);

            jList1.setVisible(false);
            jPanel2.setBounds(jPanel2.getX(), jPanel2.getY(), jPanel2.getWidth(), 60);
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void TableNotasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableNotasMouseClicked
        if (evt.getClickCount() == 2) {
            int fila = TableNotas.rowAtPoint(evt.getPoint());
            int buscando = Integer.parseInt(TableNotas.getValueAt(fila, 1).toString());
            vistaNota vN = new vistaNota(new javax.swing.JFrame(), true);
            vN.validandoDatos(buscando, false);
            vN.setVisible(true);

            if ("".equals(jTextField1.getText())) {
                controladorLista.setVisible(false);
                jList1.setVisible(false);
                jPanel2.setBounds(jPanel2.getX(), jPanel2.getY(), jPanel2.getWidth(), 60);
                LimpiarTabla();
                listarNotas(false);
            } else {
                if (isNumeric(jTextField1.getText()) == true) {
                    Nota n = notaDao.buscarPorFolio(Integer.parseInt(jTextField1.getText()));
                    LimpiarTabla();
                    buscarPorFolioTabla(n);

                } else {
                    jList1.setVisible(false);
                    LimpiarTabla();
                    listarNotasPorNombre(apoyoR);
                }
            }

        }
    }//GEN-LAST:event_TableNotasMouseClicked

    private void TableNotasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableNotasKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_TableNotasKeyPressed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        jDateChooser2.setMinSelectableDate(jDateChooser1.getDate());
        jDateChooser2.setSelectedDate(jDateChooser1.getDate());
        if (jDateChooser1.getDate() != null) {
            try {
                LimpiarTabla();
                listarPorPeriodoDeFecha();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ALGO SUCEDIO MAL, PRUEBA OTRA VEZ" + e);
            }
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

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

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if ("".equals(jTextField1.getText())) {
            controladorLista.setVisible(false);
            jList1.setVisible(false);
            jPanel2.setBounds(jPanel2.getX(), jPanel2.getY(), jPanel2.getWidth(), 60);
            LimpiarTabla();
            listarNotas(false);
        } else {
            if (isNumeric(jTextField1.getText()) == true) {
                Nota n = notaDao.buscarPorFolio(Integer.parseInt(jTextField1.getText()));
                LimpiarTabla();
                buscarPorFolioTabla(n);

            } else {
                DefaultListModel model = new DefaultListModel();
                controladorLista.setVisible(true);
                jList1.setVisible(true);
                jPanel2.setBounds(jPanel2.getX(), jPanel2.getY(), jPanel2.getWidth(), 145);
                controladorLista.setBounds(controladorLista.getX(), controladorLista.getY(), controladorLista.getWidth(), 85);
                jList1.setBounds(jList1.getX(), jList1.getY(), jList1.getWidth(), 85);
                List<Cliente> lista = client.buscarLetra(jTextField1.getText());
                if (lista.size() == 0) {
                    listaFolios = new ArrayList<Integer>();
                    List<Cliente> todosClientes = client.ListarCliente();
                    for (int i = 0; i < todosClientes.size(); i++) {
                        String nombreCompleto = todosClientes.get(i).getNombre() + " " + todosClientes.get(i).getApellido();
                        if (isSubstring(nombreCompleto, jTextField1.getText())) {
                            model.addElement(nombreCompleto);
                            listaFolios.add(todosClientes.get(i).getId());
                        }

                    }
                }

                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getEstado() == 1) {
                        model.addElement(lista.get(i).getNombre() + " " + lista.get(i).getApellido());
                    }
                }
                jList1.setModel(model);
            }
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void primeraFechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_primeraFechaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

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
            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
        }
    }//GEN-LAST:event_segundaFechaKeyPressed

    private void segundaFechaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_segundaFechaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_segundaFechaKeyReleased

    private void btnNotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNotaMouseClicked
        eventoF1();
    }//GEN-LAST:event_btnNotaMouseClicked

    private void btnNotaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNotaMouseEntered
        panelNuevaNota.setBackground(new Color(153, 204, 255));
    }//GEN-LAST:event_btnNotaMouseEntered

    private void btnNotaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNotaMouseExited
        panelNuevaNota.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnNotaMouseExited

    private void btnNotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNotaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
        }
    }//GEN-LAST:event_btnNotaKeyPressed

    private void panelNuevaNotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelNuevaNotaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelNuevaNotaMouseClicked

    private void panelNuevaNotaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelNuevaNotaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelNuevaNotaMouseEntered

    private void btnReporteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReporteMouseClicked
        eventoF2();
    }//GEN-LAST:event_btnReporteMouseClicked

    private void btnReporteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReporteMouseEntered
        panelReporte.setBackground(new Color(153, 204, 255));
    }//GEN-LAST:event_btnReporteMouseEntered

    private void btnReporteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReporteMouseExited
        panelReporte.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnReporteMouseExited

    private void btnReporteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnReporteKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
        }
    }//GEN-LAST:event_btnReporteKeyPressed

    private void panelReporteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelReporteMouseClicked

    }//GEN-LAST:event_panelReporteMouseClicked

    private void panelReporteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelReporteMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelReporteMouseEntered

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
            java.util.logging.Logger.getLogger(VisualizarNotasCopia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VisualizarNotasCopia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VisualizarNotasCopia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VisualizarNotasCopia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VisualizarNotasCopia dialog = new VisualizarNotasCopia(new javax.swing.JFrame(), true);
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
    private com.raven.swing.Table TableNotas;
    private javax.swing.JLabel btnNota;
    private javax.swing.JLabel btnReporte;
    private javax.swing.JScrollPane controladorLista;
    private com.raven.datechooser.DateChooser jDateChooser1;
    private com.raven.datechooser.DateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private textfield.TextField jTextField1;
    private javax.swing.JPanel panelNuevaNota;
    private javax.swing.JPanel panelReporte;
    private textfield.TextField primeraFecha;
    private textfield.TextField segundaFecha;
    private javax.swing.JScrollPane spTable;
    // End of variables declaration//GEN-END:variables

    private void Seticon() {
    setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
    }

}
