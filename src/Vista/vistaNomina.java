/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.EmpleadosDao;
import Modelo.GastosDao;
import Modelo.NominaDiario;
import Modelo.NominaDiarioDao;
import Modelo.config;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.raven.swing.Table;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import javaswingdev.Notification;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class vistaNomina extends javax.swing.JDialog {

     GastosDao gastos = new GastosDao();
     EmpleadosDao emple = new EmpleadosDao();
    NominaDiarioDao nominaDiarioDao = new NominaDiarioDao();
   DefaultTableModel modeloTabla = new DefaultTableModel();
    
    public vistaNomina(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
         this.setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Software Lavandería - Nómina");
        Seticon();
        
        TablaNomina.setBackground(Color.WHITE);
                        jScrollPane14.getViewport().setBackground(new Color(204, 204, 204));
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        TablaNomina.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TablaNomina.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TablaNomina.getColumnModel().getColumn(2).setCellRenderer(tcr);
        TablaNomina.getColumnModel().getColumn(3).setCellRenderer(tcr);
        TablaNomina.getColumnModel().getColumn(4).setCellRenderer(tcr);
        ((DefaultTableCellRenderer) TablaNomina.getTableHeader().getDefaultRenderer())
                       .setHorizontalAlignment(SwingConstants.CENTER);
       
        LimpiarTablasNomina();
        listarNomina();        
        
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
    
      public void LimpiarTablasNomina() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            i = i - 1;
        }
        
    }
      
      
      public void eventoF1(){
        vistaReportes at = new vistaReportes(new javax.swing.JFrame(), true);
                                   at.paraConcentradoGeneral();
                                   at.setVisible(true);
                                   if(at.accionRealizada==true){
                                       reporteNomina(at.fechaInicial,at.fechaFinal);
                                   }
      }
      
      public void eventoF2(){
           NominaEntradaSalida ne = new NominaEntradaSalida(new javax.swing.JFrame(), true);
       ne.vaciarDatos(true);
       ne.setVisible(true);
       if(ne.accion==true){
             LimpiarTablasNomina();
              listarNomina();
       }
      }
      
      public void eventoF3(){
           NominaEntradaSalida ne = new NominaEntradaSalida(new javax.swing.JFrame(), true);
       ne.vaciarDatos(false);
       ne.setVisible(true);
       if(ne.accion==true){
             LimpiarTablasNomina();
              listarNomina();
       }
      }
      
        public void listarPorPeriodoDeFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<NominaDiario> lsNomina = nominaDiarioDao.listarNominasPorPeriodo(sdf.format(jDateChooser1.getDate()), sdf.format(jDateChooser2.getDate()));
        modeloTabla = (DefaultTableModel) TablaNomina.getModel();
        for (int i = lsNomina.size()-1; i >= 0; i--) {
                Calendar fechaLista = Calendar.getInstance();
                Date fechaNomina = StringADate(lsNomina.get(i).getFecha());
                fechaLista.setTime(fechaNomina);
                Object[] ob = new Object[5];
                ob[0] = lsNomina.get(i).getCodigoEmpleado();
                ob[1] = lsNomina.get(i).getNombre();
                ob[2] = fechaFormatoCorrecto(lsNomina.get(i).getFecha());
                Date horaBien = StringADateHora(lsNomina.get(i).getHoraInicio());
                Formatter obj = new Formatter();
                String horaEntradaB = horaBien.getHours() + ":" + String.valueOf(obj.format("%02d", horaBien.getMinutes()));
                ob[3] = horaEntradaB;

                if ("00:00:00".equals(lsNomina.get(i).getHoraSalida())) {
                    ob[4] = "---";
                } else {
                    horaBien = StringADateHora(lsNomina.get(i).getHoraSalida());
                    obj = new Formatter();
                    horaEntradaB = horaBien.getHours() + ":" + String.valueOf(obj.format("%02d", horaBien.getMinutes()));
                    ob[4] = horaEntradaB;
                }
                modeloTabla.addRow(ob);
        }
        TablaNomina.setModel(modeloTabla);
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
        panelRegistrarNota3 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JLabel();
        panelRegistrarNota4 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JLabel();
        panelRegistrarNota6 = new javax.swing.JPanel();
        jButton20 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        TablaNomina = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        primeraFecha = new textfield.TextField();
        segundaFecha = new textfield.TextField();

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
        jLabel1.setText("Nómina");

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
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ESTADISTICAS.png"))); // NOI18N
        jButton17.setToolTipText("F1 - Visualizar nominas");
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
        jButton18.setToolTipText("F2 - Registrar entrada");
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

        panelRegistrarNota6.setBackground(new java.awt.Color(255, 255, 255));
        panelRegistrarNota6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelRegistrarNota6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRegistrarNota6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelRegistrarNota6MouseEntered(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(255, 255, 255));
        jButton20.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jButton20.setForeground(new java.awt.Color(255, 255, 255));
        jButton20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imagen5.png"))); // NOI18N
        jButton20.setToolTipText("F3 - Registrar salida");
        jButton20.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton20MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton20MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton20MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelRegistrarNota6Layout = new javax.swing.GroupLayout(panelRegistrarNota6);
        panelRegistrarNota6.setLayout(panelRegistrarNota6Layout);
        panelRegistrarNota6Layout.setHorizontalGroup(
            panelRegistrarNota6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelRegistrarNota6Layout.setVerticalGroup(
            panelRegistrarNota6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelRegistrarNota6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 50, 50));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaNomina = new Table();
        TablaNomina.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        TablaNomina.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Fecha", "Entrada", "Salida"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaNomina.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TablaNomina.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablaNominaKeyPressed(evt);
            }
        });
        jScrollPane14.setViewportView(TablaNomina);
        if (TablaNomina.getColumnModel().getColumnCount() > 0) {
            TablaNomina.getColumnModel().getColumn(0).setPreferredWidth(13);
            TablaNomina.getColumnModel().getColumn(1).setPreferredWidth(150);
            TablaNomina.getColumnModel().getColumn(2).setPreferredWidth(30);
            TablaNomina.getColumnModel().getColumn(3).setPreferredWidth(30);
            TablaNomina.getColumnModel().getColumn(4).setPreferredWidth(30);
        }

        jPanel6.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 620, 480));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 640, 500));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("     Buscar por fecha");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 130, 20));

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
        jPanel1.add(primeraFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, 130, 40));

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
        jPanel1.add(segundaFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 130, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseClicked
        eventoF1();
    }//GEN-LAST:event_jButton17MouseClicked

    private void jButton17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseEntered

        panelRegistrarNota3.setBackground(new Color(153,204,255));
        jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    }//GEN-LAST:event_jButton17MouseEntered

    private void jButton17MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseExited
        panelRegistrarNota3.setBackground(new Color(255,255,255));
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

        panelRegistrarNota4.setBackground(new Color(153,204,255));
        jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    }//GEN-LAST:event_jButton18MouseEntered

    private void jButton18MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseExited
        panelRegistrarNota4.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_jButton18MouseExited

    private void panelRegistrarNota4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota4MouseClicked

    private void panelRegistrarNota4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota4MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota4MouseEntered

    private void jButton20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseClicked
        eventoF3();
    }//GEN-LAST:event_jButton20MouseClicked

    private void jButton20MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseEntered

        panelRegistrarNota6.setBackground(new Color(153,204,255));
        jButton20.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jButton20MouseEntered

    private void jButton20MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseExited
        panelRegistrarNota6.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_jButton20MouseExited

    private void panelRegistrarNota6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota6MouseClicked

    private void panelRegistrarNota6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota6MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota6MouseEntered

    private void TablaNominaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablaNominaKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_F1:
            eventoF1();
            break;

            case KeyEvent.VK_F2:
            eventoF2();
            break;

            case KeyEvent.VK_F3:
            eventoF3();
            break;

            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_TablaNominaKeyPressed

    private void primeraFechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_primeraFechaKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
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
        int codigo=evt.getKeyCode();
        switch(codigo){
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

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        jDateChooser2.setMinSelectableDate(jDateChooser1.getDate());
        jDateChooser2.setSelectedDate(jDateChooser1.getDate());
        if(jDateChooser1.getDate()!=null){
            try {
                LimpiarTablasNomina();
                listarPorPeriodoDeFecha();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ALGO SUCEDIO MAL, PRUEBA OTRA VEZ" + e);
            }}
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        if(jDateChooser2.getDate()!=null){
            try {
                LimpiarTablasNomina();
                listarPorPeriodoDeFecha();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ALGO SUCEDIO MAL, PRUEBA OTRA VEZ" + e);
            }}
    }//GEN-LAST:event_jDateChooser2PropertyChange

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
            java.util.logging.Logger.getLogger(vistaNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(vistaNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(vistaNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(vistaNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                vistaNomina dialog = new vistaNomina(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable TablaNomina;
    private javax.swing.JLabel jButton17;
    private javax.swing.JLabel jButton18;
    private javax.swing.JLabel jButton20;
    private com.raven.datechooser.DateChooser jDateChooser1;
    private com.raven.datechooser.DateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JPanel panelRegistrarNota3;
    private javax.swing.JPanel panelRegistrarNota4;
    private javax.swing.JPanel panelRegistrarNota6;
    private textfield.TextField primeraFecha;
    private textfield.TextField segundaFecha;
    // End of variables declaration//GEN-END:variables

private void Seticon() {
    setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
   }
 
 public String fechaFormatoCorrecto(String fechaHoy){
    Date diaActual = StringADate(fechaHoy);
        DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
        String fechaUsar;
        Date fechaNueva;
        Calendar c = Calendar.getInstance();
        c.setTime(diaActual);
        fechaUsar = cam.format(diaActual);
        return fechaUsar;
    }
 
 public void listarNomina() {
        modeloTabla = (DefaultTableModel) TablaNomina.getModel();
       
        List<NominaDiario> lsNomina = nominaDiarioDao.listarNominas();

        for (int i = lsNomina.size()-1; i >= 0; i--) {
                Calendar fechaLista = Calendar.getInstance();
                Date fechaNomina = StringADate(lsNomina.get(i).getFecha());
                fechaLista.setTime(fechaNomina);
                Object[] ob = new Object[5];
                ob[0] = lsNomina.get(i).getCodigoEmpleado();
                ob[1] = lsNomina.get(i).getNombre();
                ob[2] = fechaFormatoCorrecto(lsNomina.get(i).getFecha());
                Date horaBien = StringADateHora(lsNomina.get(i).getHoraInicio());
                Formatter obj = new Formatter();
                String horaEntradaB = horaBien.getHours() + ":" + String.valueOf(obj.format("%02d", horaBien.getMinutes()));
                ob[3] = horaEntradaB;

                if ("00:00:00".equals(lsNomina.get(i).getHoraSalida())) {
                    ob[4] = "---";
                } else {
                    horaBien = StringADateHora(lsNomina.get(i).getHoraSalida());
                    obj = new Formatter();
                    horaEntradaB = horaBien.getHours() + ":" + String.valueOf(obj.format("%02d", horaBien.getMinutes()));
                    ob[4] = horaEntradaB;
                }
                modeloTabla.addRow(ob);
        }
        TablaNomina.setModel(modeloTabla);
    }
 
 public void reporteNomina(String fechaInicial, String fechaFinal){
        try {
             List<NominaDiario> lsNomina = nominaDiarioDao.listarNominasPorPeriodo(fechaInicial, fechaFinal);
              Formatter obj = new Formatter();
                      Formatter obj2 = new Formatter();

            LocalDateTime m = LocalDateTime.now(); //Obtenemos la fecha actual
        String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
        String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
        String DiagHoy =dia+"-"+ mes + "-" +m.getYear();
        
            
                config configura = new config();
            configura = gastos.buscarDatos();
                        Font letra = new Font(Font.FontFamily.TIMES_ROMAN, 11);
                        Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            FileOutputStream archivo;
            //File file = new File("corte" + fechaHoy + ".pdf");
                        File file = new File("Nomina\\reporteNomina.pdf");
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
            cell = new PdfPCell(new Phrase(configura.getNomnbre()+"\nREPORTE DE NOMINA\nPERIODO DEL "+fechaFormatoCorrecto(fechaInicial)+" AL "+fechaFormatoCorrecto(fechaFinal)+"\nFECHA DE EMISION "+DiagHoy,letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            encabezado.addCell(img);
            doc.add(encabezado);
                        doc.add(Chunk.NEWLINE);


            PdfPTable tablapro = new PdfPTable(5);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] columnapro = new float[]{8f, 50f, 20f,15f, 15f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
           
            PdfPCell pro1 = new PdfPCell(new Phrase("Codigo",letra));
            PdfPCell pro2 = new PdfPCell(new Phrase("Nombre",letra));
            PdfPCell pro3 = new PdfPCell(new Phrase("Fecha",letra));
            PdfPCell pro4 = new PdfPCell(new Phrase("Hora Entrada",letra));
            PdfPCell pro5 = new PdfPCell(new Phrase("Hora Salida",letra));
                   
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
            
          
        for (int i = 0; lsNomina.size() > i; i++) {

                String fechaF = String.format("%0" + 2 + "d", Integer.valueOf(lsNomina.get(i).getCodigoEmpleado()));
                    PdfPCell cellA1 = new PdfPCell();
                     cellA1 = new PdfPCell(new Phrase(fechaF,letra));
                     cellA1.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cellA1.setBorder(0);
                     tablapro.addCell(cellA1);
                     
                String nombreF =lsNomina.get(i).getNombre();
                     PdfPCell cellA2 = new PdfPCell();
                     cellA2 = new PdfPCell(new Phrase(nombreF,letra));
                     cellA2.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cellA2.setBorder(0);
                     tablapro.addCell(cellA2);
                     
                     cellA2 = new PdfPCell();
                     cellA2 = new PdfPCell(new Phrase(fechaFormatoCorrecto(lsNomina.get(i).getFecha()),letra));
                     cellA2.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cellA2.setBorder(0);
                     tablapro.addCell(cellA2);
                
                Date horaBien = StringADateHora(lsNomina.get(i).getHoraInicio());
                String horaEntradaB = horaBien.getHours() + ":" +String.format("%0" + 2 + "d", Integer.valueOf(horaBien.getMinutes()));
                 PdfPCell cellA3 = new PdfPCell();
                     cellA3 = new PdfPCell(new Phrase(horaEntradaB,letra));
                     cellA3.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cellA3.setBorder(0);
                     tablapro.addCell(cellA3);

                
                String fechaSalidaF="";
                if ("00:00:00".equals(lsNomina.get(i).getHoraSalida())) {
                    fechaSalidaF="---";
                } else {
                    horaBien = StringADateHora(lsNomina.get(i).getHoraSalida());
                    obj = new Formatter();
                    horaEntradaB = horaBien.getHours() + ":" +String.format("%0" + 2 + "d", Integer.valueOf(horaBien.getMinutes()));
                   fechaSalidaF = horaEntradaB;
                }
                PdfPCell cellA4 = new PdfPCell();
                     cellA4 = new PdfPCell(new Phrase(fechaSalidaF,letra));
                     cellA4.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cellA4.setBorder(0);
                     tablapro.addCell(cellA4);
            
        }
        doc.add(tablapro);
        doc.add(Chunk.NEWLINE);
                 doc.add(Chunk.NEWLINE);
                             doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);

                 
            PdfPTable tablaFinal = new PdfPTable(3);
            tablaFinal.setWidthPercentage(100);
            tablaFinal.getDefaultCell().setBorder(0);
            columnapro = new float[]{50f,50f,50f};
            tablaFinal.setWidths(columnapro);
            tablaFinal.setHorizontalAlignment(Element.ALIGN_LEFT);
            for(int i=0;i<3;i++){
                 cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("____________________"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablaFinal.addCell(cell); 
             }
            
             cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Elaboró",letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablaFinal.addCell(cell); 
            
             cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Gerencia",letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablaFinal.addCell(cell); 
            
             cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Auditoria",letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablaFinal.addCell(cell); 
            
                        doc.add(Chunk.NEWLINE);

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
