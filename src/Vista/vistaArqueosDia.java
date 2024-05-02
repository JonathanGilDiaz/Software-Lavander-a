/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Arqueo;
import Modelo.ArqueoDao;
import Modelo.EmpleadosDao;
import Modelo.GastosDao;
import Modelo.config;
import Modelo.corteDia;
import Modelo.corteDiaDao;
import Modelo.imprimiendo;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.raven.swing.Table2;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class vistaArqueosDia extends javax.swing.JDialog {

    EmpleadosDao emple = new EmpleadosDao();
    DefaultTableModel modeloTabla = new DefaultTableModel();
    ArqueoDao arqueoDao = new ArqueoDao();
    corteDiaDao corte = new corteDiaDao();
    GastosDao gastosDao = new GastosDao();

    DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
    List<Arqueo> ListaCl = new ArrayList<Arqueo>();
    double saldoCaja;

    public vistaArqueosDia(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Software Lavandería - Arqueos");
        Seticon();

        TableArqueos.setBackground(Color.WHITE);
        jScrollPane14.getViewport().setBackground(new Color(204, 204, 204));
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer tcrDerecha = new DefaultTableCellRenderer();
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        TableArqueos.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TableArqueos.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TableArqueos.getColumnModel().getColumn(2).setCellRenderer(tcr);
        TableArqueos.getColumnModel().getColumn(3).setCellRenderer(tcrDerecha);
        TableArqueos.getColumnModel().getColumn(4).setCellRenderer(tcrDerecha);
        TableArqueos.getColumnModel().getColumn(5).setCellRenderer(tcrDerecha);
        TableArqueos.getColumnModel().getColumn(6).setCellRenderer(tcr);
        ((DefaultTableCellRenderer) TableArqueos.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
        listarArqueos();
    }

    public void getSaldoCaja(double saldoCaja) {
        this.saldoCaja = saldoCaja;
    }

    public void listarArqueos() {
        ListaCl = arqueoDao.listarArqueosCorte(corte.idMax());
        modeloTabla = (DefaultTableModel) TableArqueos.getModel();
        Object[] ob = new Object[7];
        for (int i = 0; i < ListaCl.size(); i++) {
            ob[0] = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
            Date horaFecha = StringADateHora(ListaCl.get(i).getHora());
            Formatter obj2 = new Formatter();
            ob[1] = horaFecha.getHours() + ":" + String.valueOf(obj2.format("%02d", horaFecha.getMinutes()));
            ob[2] = ListaCl.get(i).getEmpleado();
            ob[3] = "$" + formateador.format(ListaCl.get(i).getSaldo());
            ob[4] = "$" + formateador.format(ListaCl.get(i).getRetiro());
            ob[5] = "$" + formateador.format(ListaCl.get(i).getSaldoFinal());
            ob[6] = ListaCl.get(i).getComentario();
            modeloTabla.addRow(ob);
        }
        TableArqueos.setModel(modeloTabla);
        txtTotal.setText("$" + formateador.format(arqueoDao.totalArqueos(corte.idMax())));
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

    public void LimpiarTabla() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            i = i - 1;
        }

    }

    public void eventoF1() {
        if (TableArqueos.getSelectedRow() != -1) {
            try {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Imprimiendo comprobante");
                panel.showNotification();
                File file = new File("C:\\Program Files (x86)\\AppLavanderia\\Tickets\\Arqueo" + ListaCl.get(TableArqueos.getSelectedRow()).getId() + ".pdf");
                ticketArqueo(ListaCl.get(TableArqueos.getSelectedRow()).getId());
                imprimiendo m = new imprimiendo();
                m.imprimir(file);

            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null, "Error de impresion", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } catch (ParseException ex) {
                Logger.getLogger(vistaNota.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Seleccione un arqueo antes");
            panel.showNotification();
        }
    }

    public void eventoF2() {
        generarArqueo vg = new generarArqueo(new javax.swing.JFrame(), true);
        vg.getSaldoCaja(saldoCaja);
        vg.setVisible(true);
        if (vg.indicador == true) {
            LimpiarTabla();
            listarArqueos();
            saldoCaja = saldoCaja - arqueoDao.getUltimoRetiro();
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
        panelRegistrarNota3 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JLabel();
        panelRegistrarNota4 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        TableArqueos = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Arqueos");

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
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/imprimir nota.png"))); // NOI18N
        jButton17.setToolTipText("F1 - Imprimir arqueo");
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

        jPanel1.add(panelRegistrarNota3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 50, 50));

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
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/MODIFICAR.png"))); // NOI18N
        jButton18.setToolTipText("F2 - Registrar nuevo arqueo");
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

        jPanel1.add(panelRegistrarNota4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 50, 50));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableArqueos = new Table2();
        TableArqueos.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        TableArqueos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Hora", "Registró", "Saldo", "Retiro", "Saldo final", "Observación"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableArqueos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TableArqueos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableArqueosKeyPressed(evt);
            }
        });
        jScrollPane14.setViewportView(TableArqueos);
        if (TableArqueos.getColumnModel().getColumnCount() > 0) {
            TableArqueos.getColumnModel().getColumn(0).setPreferredWidth(25);
            TableArqueos.getColumnModel().getColumn(1).setPreferredWidth(15);
            TableArqueos.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableArqueos.getColumnModel().getColumn(3).setPreferredWidth(25);
            TableArqueos.getColumnModel().getColumn(4).setPreferredWidth(25);
            TableArqueos.getColumnModel().getColumn(5).setPreferredWidth(25);
            TableArqueos.getColumnModel().getColumn(6).setPreferredWidth(100);
        }

        jPanel6.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 720, 180));

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel29.setText("Total de arqueos");
        jLabel29.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel6.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 200, 90, 20));

        txtTotal.setBackground(new java.awt.Color(204, 204, 204));
        txtTotal.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtTotal.setText("-----");
        txtTotal.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTotal.setEnabled(false);
        txtTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalKeyPressed(evt);
            }
        });
        jPanel6.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 200, 170, -1));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 740, 230));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 759, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseClicked
        eventoF1();
    }//GEN-LAST:event_jButton17MouseClicked

    private void jButton17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseEntered

        panelRegistrarNota3.setBackground(new Color(153, 204, 255));
        jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    }//GEN-LAST:event_jButton17MouseEntered

    private void jButton17MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseExited
        panelRegistrarNota3.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton17MouseExited

    private void panelRegistrarNota3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota3MouseClicked

    private void panelRegistrarNota3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota3MouseEntered

    private void jButton18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseClicked
        eventoF2();
    }//GEN-LAST:event_jButton18MouseClicked

    private void jButton18MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseEntered

        panelRegistrarNota4.setBackground(new Color(153, 204, 255));
        jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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

    private void TableArqueosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableArqueosKeyPressed
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
    }//GEN-LAST:event_TableArqueosKeyPressed

    private void txtTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalKeyPressed
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
    }//GEN-LAST:event_txtTotalKeyPressed

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
            java.util.logging.Logger.getLogger(vistaArqueosDia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(vistaArqueosDia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(vistaArqueosDia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(vistaArqueosDia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                vistaArqueosDia dialog = new vistaArqueosDia(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable TableArqueos;
    private javax.swing.JLabel jButton17;
    private javax.swing.JLabel jButton18;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JPanel panelRegistrarNota3;
    private javax.swing.JPanel panelRegistrarNota4;
    private javax.swing.JTextField txtTotal;
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

    public void ticketArqueo(int id) throws ParseException {
        try {
            FileOutputStream archivo;
            File file = new File("C:\\Program Files (x86)\\AppLavanderia\\Tickets\\Arqueo" + id + ".pdf");
            Arqueo miArqueo = arqueoDao.buscarPorFolio(id);
            //File file = new File("src/pdf/ticket" + id + ".pdf");
            //File file = new File("ticket" + id + ".pdf");
            archivo = new FileOutputStream(file);
            Rectangle pageSize = new Rectangle(140.76f, 500f); //ancho y alto
            Document doc = new Document(pageSize, 0, 0, 0, 0);
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            //Image img = Image.getInstance("src/Imagenes/logo 100x100.jpg");
            Image img = Image.getInstance("C:\\Program Files (x86)\\AppLavanderia\\Iconos\\logo 100x100.jpg");
            config configura = gastosDao.buscarDatos();

            Font letra2 = new Font(Font.FontFamily.HELVETICA, configura.getLetraChica() + 4, Font.BOLD);
                        Font letraChiquita = new Font(Font.FontFamily.HELVETICA, configura.getLetraChica() + 2, Font.BOLD);


            PdfPTable logo = new PdfPTable(5);
            logo.setWidthPercentage(100);
            logo.getDefaultCell().setBorder(0);
            float[] columnaEncabezadoLogo = new float[]{20f, 20, 60f, 20f, 20f};
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
            cell = new PdfPCell(new Phrase("Registró:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(miArqueo.getEmpleado(), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Fecha:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(fechaFormatoCorrecto(miArqueo.getFecha()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Hora:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            Date horaFecha = StringADateHora(miArqueo.getHora());
            Formatter obj2 = new Formatter();
            String horaPDF = horaFecha.getHours() + ":" + String.valueOf(obj2.format("%02d", horaFecha.getMinutes()));
            cell = new PdfPCell(new Phrase(horaPDF, letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Saldo:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miArqueo.getSaldo()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Retiro:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miArqueo.getRetiro()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Saldo final:", letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(miArqueo.getSaldoFinal()), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            if (!"".equals(miArqueo.getComentario())) {
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase("Observación:", letra2));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(miArqueo.getComentario(), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(0);
                tablapro.addCell(cell);
            }

            doc.add(Chunk.NEWLINE);
            doc.add(tablapro);

            tablapro = new PdfPTable(1);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnaEncabezado = new float[]{100f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);

            if (miArqueo.getIdCorte() != corte.idMax()) {
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase("Corte", letra2));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                cell = new PdfPCell();
                corteDia miCorte = corte.regresarCorte(miArqueo.getIdCorte());
                horaFecha = StringADateHora(miCorte.getHoraInicio());
                obj2 = new Formatter();
                String horaInicio = horaFecha.getHours() + ":" + String.valueOf(obj2.format("%02d", horaFecha.getMinutes()));
                horaFecha = StringADateHora(miCorte.getHoraCierre());
                obj2 = new Formatter();
                String horaFinal = horaFecha.getHours() + ":" + String.valueOf(obj2.format("%02d", horaFecha.getMinutes()));

                cell = new PdfPCell(new Phrase(fechaFormatoCorrecto(miCorte.getFechaInicio() + " " + horaInicio + " - " + fechaFormatoCorrecto(miCorte.getFechaCierre()) + " " + horaFinal), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);
                doc.add(tablapro);

                tablapro = new PdfPTable(1);
                tablapro.setWidthPercentage(100);
                tablapro.getDefaultCell().setBorder(0);
                columnaEncabezado = new float[]{100f};
                tablapro.setWidths(columnaEncabezado);
                tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);
            }
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
