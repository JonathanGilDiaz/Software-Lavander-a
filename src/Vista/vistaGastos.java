/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Gastos;
import Modelo.GastosDao;
import Modelo.config;
import static Vista.vistaNota.isNumeric;
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
import com.raven.swing.Table2;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import javaswingdev.Notification;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class vistaGastos extends javax.swing.JDialog {

    DefaultTableModel modelo = new DefaultTableModel();
    GastosDao gastos = new GastosDao();
    int fila = -1;
    DecimalFormat formateador = new DecimalFormat("#,###,##0.00");

    public vistaGastos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(200, 50);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Software Lavandería - Gastos");
        Seticon();

        jScrollPane9.getViewport().setBackground(new Color(204, 204, 204));
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        DefaultTableCellRenderer tcrDerecha = new DefaultTableCellRenderer();

        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        TablaDeGastos.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TablaDeGastos.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TablaDeGastos.getColumnModel().getColumn(2).setCellRenderer(tcr);
        TablaDeGastos.getColumnModel().getColumn(3).setCellRenderer(tcrDerecha);
        TablaDeGastos.getColumnModel().getColumn(4).setCellRenderer(tcr);
        TablaDeGastos.getColumnModel().getColumn(5).setCellRenderer(tcr);
        TablaDeGastos.getColumnModel().getColumn(5).setCellRenderer(tcr);
        TablaDeGastos.getColumnModel().getColumn(6).setCellRenderer(tcr);

        ((DefaultTableCellRenderer) TablaDeGastos.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
        listarGastosTodos();

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

    public void listarGastosTodos() {
        List<Gastos> lsGastos = gastos.listarGastos();//Obtenemos gastos de la BD
        modelo = (DefaultTableModel) TablaDeGastos.getModel();
        Object[] ob = new Object[7];
        for (int i = lsGastos.size() - 1; i >= 0; i--) { //vaciamos los datos en la tabla
            ob[0] = lsGastos.get(i).getId();
            ob[1] = lsGastos.get(i).getComprobante();
            ob[2] = lsGastos.get(i).getDescripcion();
            ob[3] = "$" + formateador.format(lsGastos.get(i).getPrecio());
            ob[4] = String.format("%0" + 2 + "d", lsGastos.get(i).getFormaPago());
            ob[5] = fechaFormatoCorrecto(lsGastos.get(i).getFecha());
            Date horaFecha = StringADateHora(lsGastos.get(i).getHora());
            Formatter obj2 = new Formatter();
            ob[6] = horaFecha.getHours() + ":" + String.valueOf(obj2.format("%02d", horaFecha.getMinutes()));
            modelo.addRow(ob);
        }
        TablaDeGastos.setModel(modelo);
    }

    public void LimpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    public void eventoF1() {
        vistaGasto vg = new vistaGasto(new javax.swing.JFrame(), true);
        vg.setVisible(true);
        if (vg.indicador == true) {
            LimpiarTabla();
            listarGastosTodos();
        }
    }

    public void eventoF2() {

        vistaReportes a = new vistaReportes(new javax.swing.JFrame(), true);
        a.paraGastos();
        a.setVisible(true);
        if (a.accionRealizadaGastos == 1) {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Generando reporte");
            panel.showNotification();
            reporteGastos(a.fechaInicial, a.fechaFinal);
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new textfield.TextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        TablaDeGastos = new javax.swing.JTable();
        panelReporte = new javax.swing.JPanel();
        btnReporte = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Gastos");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(667, Short.MAX_VALUE))
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

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField1.setLabelText("Buscar gasto");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 410, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 620, 60));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaDeGastos = new Table2();
        TablaDeGastos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Comprobante", "Descripción", "Total", "Fpa", "Fecha", "Hora"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaDeGastos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TablaDeGastos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaDeGastosMouseClicked(evt);
            }
        });
        TablaDeGastos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablaDeGastosKeyPressed(evt);
            }
        });
        jScrollPane9.setViewportView(TablaDeGastos);
        if (TablaDeGastos.getColumnModel().getColumnCount() > 0) {
            TablaDeGastos.getColumnModel().getColumn(0).setResizable(false);
            TablaDeGastos.getColumnModel().getColumn(0).setPreferredWidth(2);
            TablaDeGastos.getColumnModel().getColumn(1).setResizable(false);
            TablaDeGastos.getColumnModel().getColumn(1).setPreferredWidth(180);
            TablaDeGastos.getColumnModel().getColumn(2).setResizable(false);
            TablaDeGastos.getColumnModel().getColumn(2).setPreferredWidth(180);
            TablaDeGastos.getColumnModel().getColumn(3).setResizable(false);
            TablaDeGastos.getColumnModel().getColumn(3).setPreferredWidth(10);
            TablaDeGastos.getColumnModel().getColumn(4).setResizable(false);
            TablaDeGastos.getColumnModel().getColumn(4).setPreferredWidth(10);
            TablaDeGastos.getColumnModel().getColumn(5).setResizable(false);
            TablaDeGastos.getColumnModel().getColumn(5).setPreferredWidth(15);
            TablaDeGastos.getColumnModel().getColumn(6).setPreferredWidth(15);
        }

        jPanel4.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 17, 920, 420));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 940, 450));

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
        btnReporte.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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

        jPanel1.add(panelReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 50, 50));

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

    private void TablaDeGastosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaDeGastosMouseClicked
        if (evt.getClickCount() == 2) {
            fila = TablaDeGastos.rowAtPoint(evt.getPoint());
        }
    }//GEN-LAST:event_TablaDeGastosMouseClicked

    private void TablaDeGastosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablaDeGastosKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
        }
    }//GEN-LAST:event_TablaDeGastosKeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        fila = -1;
        LimpiarTabla();
        if (!"".equals(jTextField1.getText())) {
            if (isNumeric(jTextField1.getText()) == true) {
                Gastos alistar = gastos.BuscarPorCodigo(Integer.parseInt(jTextField1.getText()));
                if (alistar.getId() == 0) {
                    modelo = (DefaultTableModel) TablaDeGastos.getModel();
                    TablaDeGastos.setModel(modelo);
                } else {
                    modelo = (DefaultTableModel) TablaDeGastos.getModel();
                    Object[] ob = new Object[7];
                    ob[0] = alistar.getId();
                    ob[1] = alistar.getComprobante();
                    ob[2] = alistar.getDescripcion();
                    ob[3] = alistar.getPrecio();
                    ob[4] = String.format("%0" + 2 + "d", alistar.getFormaPago());
                    ob[5] = fechaFormatoCorrecto(alistar.getFecha());
                    Date horaFecha = StringADateHora(alistar.getHora());
                    Formatter obj2 = new Formatter();
                    ob[6] = horaFecha.getHours() + ":" + String.valueOf(obj2.format("%02d", horaFecha.getMinutes()));
                    modelo.addRow(ob);

                    TablaDeGastos.setModel(modelo);
                }
            } else {
                List<Gastos> lsGastos = gastos.buscarLetra(jTextField1.getText());

                modelo = (DefaultTableModel) TablaDeGastos.getModel();
                Object[] ob = new Object[7];
                for (int i = 0; i < lsGastos.size(); i++) {
                    ob[0] = lsGastos.get(i).getId();
                    ob[1] = lsGastos.get(i).getComprobante();
                    ob[2] = lsGastos.get(i).getDescripcion();
                    ob[3] = "$" + formateador.format(lsGastos.get(i).getPrecio());
                    ob[4] = String.format("%0" + 2 + "d", lsGastos.get(i).getFormaPago());
                    ob[5] = fechaFormatoCorrecto(lsGastos.get(i).getFecha());
                    Date horaFecha = StringADateHora(lsGastos.get(i).getHora());
                    Formatter obj2 = new Formatter();
                    ob[6] = horaFecha.getHours() + ":" + String.valueOf(obj2.format("%02d", horaFecha.getMinutes()));
                    modelo.addRow(ob);
                }
                TablaDeGastos.setModel(modelo);
            }
        } else {
            listarGastosTodos();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void btnReporteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReporteMouseClicked
        eventoF2();
    }//GEN-LAST:event_btnReporteMouseClicked

    private void btnReporteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReporteMouseEntered

        btnReporte.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelReporte.setBackground(new Color(153, 204, 255));

    }//GEN-LAST:event_btnReporteMouseEntered

    private void btnReporteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReporteMouseExited
        panelReporte.setBackground(new Color(255, 255, 255));
        btnReporte.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnReporteMouseExited

    private void btnReporteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnReporteKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
        }
    }//GEN-LAST:event_btnReporteKeyPressed

    private void panelReporteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelReporteMouseClicked
        // TODO add your handling code here:
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
            java.util.logging.Logger.getLogger(vistaGastos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(vistaGastos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(vistaGastos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(vistaGastos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                vistaGastos dialog = new vistaGastos(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable TablaDeGastos;
    private javax.swing.JLabel btnReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane9;
    private textfield.TextField jTextField1;
    private javax.swing.JPanel panelReporte;
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

    public void reporteGastos(String fechaInicial, String fechaFinal) {
        try {
            String fechaInicialBien = fechaFormatoCorrecto(fechaInicial);
            String fechaFInalBien = fechaFormatoCorrecto(fechaFinal);
            Formatter obj = new Formatter();
            Formatter obj2 = new Formatter();

            LocalDateTime m = LocalDateTime.now(); //Obtenemos la fecha actual
            String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
            String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
            String DiagHoy = dia + "-" + mes + "-" + m.getYear();

            config configura = new config();
            configura = gastos.buscarDatos();
            Font letra = new Font(Font.FontFamily.TIMES_ROMAN, 11);
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            FileOutputStream archivo;
            //File file = new File("corte" + fechaHoy + ".pdf");
            File file = new File("ReportesPDF\\reporteGastos.pdf");
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
            cell = new PdfPCell(new Phrase(configura.getNomnbre() + "\nREPORTE DE GASTOS\n" + " PERIODO DEL " + fechaInicialBien + " AL " + fechaFInalBien + "\nFECHGA DE EMISION " + DiagHoy, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            encabezado.addCell(img);
            doc.add(encabezado);

            PdfPTable tablapro = new PdfPTable(6);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] columnapro = new float[]{15f, 10f, 20f, 15f, 12f, 12f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1 = new PdfPCell(new Phrase("Fecha: ", letra));
            PdfPCell pro2 = new PdfPCell(new Phrase("Folio", letra));
            PdfPCell pro3 = new PdfPCell(new Phrase("Comprobante", letra));
            PdfPCell pro4 = new PdfPCell(new Phrase("Concepto", letra));
            PdfPCell pro5 = new PdfPCell(new Phrase("Importe: ", letra));
            PdfPCell pro6 = new PdfPCell(new Phrase("FormaPago", letra));

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

            List<Gastos> ListaCl = gastos.listarPorFecha(fechaInicial, fechaFinal);

            for (int i = 0; i < ListaCl.size(); i++) {

                String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(fechaF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String folioF = String.valueOf(ListaCl.get(i).getId());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(folioF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String comprobanteF = ListaCl.get(i).getComprobante();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(comprobanteF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String conceptoF = ListaCl.get(i).getDescripcion();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(conceptoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String totalF = "$" + formateador.format(ListaCl.get(i).getPrecio());;
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(totalF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                totalGastado = totalGastado + ListaCl.get(i).getPrecio();

                String formaPagoF = String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getFormaPago()));
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
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe cerrar antes el documento");
            panel.showNotification();
        }
    }

}
