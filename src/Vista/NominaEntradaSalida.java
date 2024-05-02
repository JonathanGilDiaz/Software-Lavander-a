/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Empleados;
import Modelo.EmpleadosDao;
import Modelo.NominaDiario;
import Modelo.NominaDiarioDao;
import Modelo.corteDiaDao;
import Modelo.establecerFecha;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Toolkit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import javaswingdev.Notification;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Jonathan Gil
 */
public class NominaEntradaSalida extends javax.swing.JDialog implements Runnable {

    EmpleadosDao emple = new EmpleadosDao();
    NominaDiarioDao nominaDiarioDao = new NominaDiarioDao();
    establecerFecha establecer = new establecerFecha();
    corteDiaDao corte = new corteDiaDao();
    String hoy = corte.getDia();
    String fecha, horaBien;

    String hora, min, seg, ampm;
    Calendar calendario;
    Thread hi;
    boolean bandera, accion;

    public NominaEntradaSalida(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Seticon();
        listarEmpleados();
        Formatter obj = new Formatter();
        Formatter obj2 = new Formatter();
        LocalDateTime m = LocalDateTime.now();
        String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
        String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
        fecha = dia + "-" + mes + "-" + m.getYear();
        txtFecga.setText(fecha);
        hi = new Thread(this);
        hi.start();
        accion = false;

        txtNombreRecibe.setBackground(Color.WHITE);
        txtFecga.setEnabled(false);
        txtHoraEntregada.setEnabled(false);
        txtHoraEntregada.setBackground(new Color(240, 240, 240));
    }

    public void listarEmpleados() {
        List<Empleados> lsEmpleados = emple.listarEmpleados(); //Se obtienen los empleados de la base de datos (para generar nota
        txtNombreRecibe.removeAllItems();//Se borran los datos del ComboBox para evitar que se sobreencimen
        for (int i = 0; i < lsEmpleados.size(); i++) {//vaciamos los datos
            if (lsEmpleados.get(i).getEstado() == 1) {
                txtNombreRecibe.addItem(lsEmpleados.get(i).getNombre());
            }
        }
    }

    public void establecerFecha() { //Establecemos la fecha a usar, esto se hace ya que hasta que no se cierre caja, no se puede avanzar de dia
        List<String> datos = establecer.establecerFecha();
        horaBien = datos.get(1);
    }

    public void vaciarDatos(boolean bandera) {
        this.bandera = bandera;
        if (bandera == true) {//entrada
            this.setTitle("Registrar entrada");
            jLabel25.setText("Registrar entrada");
        } else {//salida
            this.setTitle("Registrar salida");
            jLabel25.setText("Registrar salida");
        }
    }

    public void eventoEnter() {
        Empleados empleado = emple.seleccionarEmpleado(txtNombreRecibe.getSelectedItem().toString(), 0);
        if (empleado.getContraseña().equals(txtContraseña.getText())) {
            establecerFecha();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm:ss");
            LocalDateTime m = LocalDateTime.of(LocalDate.now(), LocalTime.parse(horaBien, formatter));

            Formatter obj = new Formatter();
            Formatter obj2 = new Formatter();
            String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
            String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
            String nfecha = m.getYear() + "-" + mes + "-" + dia;
            hora = (m.getHour()) + ":" + m.getMinute() + ":" + m.getSecond();//y creamos la fecha y hpra actual con formato HH:ss y YYYY-mm-dd

            if (m.getHour() <= 0) {
                m = LocalDateTime.now();
                System.out.println(hora + "    " + m.getSecond() + "      " + m.getMinute() + "     " + m.getHour());
                hora = (m.getHour()) + ":" + m.getMinute() + ":" + m.getSecond();//y creamos la fecha y hpra actual con formato HH:ss y YYYY-mm-dd
                System.out.println(hora + "    " + m.getSecond() + "      " + m.getMinute() + "     " + m.getHour());

            }
            if (bandera == true) {//entrada
                int idMaximo = nominaDiarioDao.idMaxEmpleado(empleado.getId());
                List<NominaDiario> lsNomina = nominaDiarioDao.listarNominas();

                if (idMaximo == 0) {
                    NominaDiario nominaDiario = new NominaDiario();
                    Empleados e = emple.seleccionarEmpleado(txtNombreRecibe.getSelectedItem().toString(), 0);
                    nominaDiario.setCodigoEmpleado(e.getId());
                    nominaDiario.setHoraInicio(hora);
                    nominaDiario.setHoraSalida("00:00:00");

                    nominaDiario.setFecha(nfecha);
                    nominaDiario.setNombre(txtNombreRecibe.getSelectedItem().toString());
                    nominaDiarioDao.registrarNomina(nominaDiario);
                } else {
                    for (int i = 0; lsNomina.size() > i; i++) {
                        if (lsNomina.get(i).getCodigo() == idMaximo) {
                            if (!"00:00:00".equals(lsNomina.get(i).getHoraSalida())) {
                                NominaDiario nominaDiario = new NominaDiario();
                                Empleados e = emple.seleccionarEmpleado(txtNombreRecibe.getSelectedItem().toString(), 0);
                                nominaDiario.setCodigoEmpleado(e.getId());
                                nominaDiario.setHoraInicio(hora);
                                nominaDiario.setHoraSalida("00:00:00");
                                nominaDiario.setFecha(nfecha);
                                nominaDiario.setNombre(txtNombreRecibe.getSelectedItem().toString());
                                nominaDiarioDao.registrarNomina(nominaDiario);
                            } else {
                                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Ya cuenta con una ");
                                panel.showNotification();

                            }

                        }
                    }
                }
                System.out.println(hora + "    " + m.getSecond() + "      " + m.getMinute() + "     " + m.getHour());

            } else {//salida

                int idMaximo = nominaDiarioDao.idMaxEmpleado(empleado.getId());
                List<NominaDiario> lsNomina = nominaDiarioDao.listarNominas();
                int contador = 0;
                for (int i = 0; lsNomina.size() > i; i++) {
                    if (lsNomina.get(i).getCodigo() == idMaximo) {
                        if ("00:00:00".equals(lsNomina.get(i).getHoraSalida())) {
                            contador = 1;
                            nominaDiarioDao.registrarSalida(idMaximo, hora);
                        }
                    }
                }
                if (contador == 0) {
                    Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe crear una entrada antes");
                    panel.showNotification();
                }

            }

            accion = true;
            dispose();

        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Contraseña incorrecta");
            panel.showNotification();
        }

    }

    public void eventoF1() {
        txtContraseña.setText("");
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
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        panelRegistrarNota1 = new javax.swing.JPanel();
        jButton15 = new javax.swing.JLabel();
        panelRegistrarNota3 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JLabel();
        panelRegistrarNota8 = new javax.swing.JPanel();
        jButton22 = new javax.swing.JLabel();
        txtNombreRecibe = new combobox.Combobox();
        txtFecga = new textfield.TextField();
        txtHoraEntregada = new textfield.TextField();
        txtContraseña = new textfield.PasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setBackground(new java.awt.Color(0, 0, 204));
        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 255));
        jLabel23.setText("¿Está seguro de ejecutar esta acción? ");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));

        jLabel25.setBackground(new java.awt.Color(0, 0, 204));
        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 255));
        jLabel25.setText("-");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 150, -1));

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
        jButton15.setToolTipText("ENTER - Realizar accion");
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

        jPanel1.add(panelRegistrarNota1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, 50, 50));

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
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar nota 22.png"))); // NOI18N
        jButton17.setToolTipText("F1 - Limpiar todos los campos");
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

        jPanel1.add(panelRegistrarNota3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 270, 50, 50));

        panelRegistrarNota8.setBackground(new java.awt.Color(255, 255, 255));
        panelRegistrarNota8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelRegistrarNota8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRegistrarNota8MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelRegistrarNota8MouseEntered(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(255, 255, 255));
        jButton22.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jButton22.setForeground(new java.awt.Color(255, 255, 255));
        jButton22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imagen5.png"))); // NOI18N
        jButton22.setToolTipText("ESCAPE - Cancelar accion y salir");
        jButton22.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton22MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton22MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton22MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelRegistrarNota8Layout = new javax.swing.GroupLayout(panelRegistrarNota8);
        panelRegistrarNota8.setLayout(panelRegistrarNota8Layout);
        panelRegistrarNota8Layout.setHorizontalGroup(
            panelRegistrarNota8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelRegistrarNota8Layout.setVerticalGroup(
            panelRegistrarNota8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelRegistrarNota8, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 270, 50, 50));

        txtNombreRecibe.setToolTipText("");
        txtNombreRecibe.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNombreRecibe.setLabeText("Selecciona quien eres");
        txtNombreRecibe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreRecibeActionPerformed(evt);
            }
        });
        txtNombreRecibe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreRecibeKeyPressed(evt);
            }
        });
        jPanel1.add(txtNombreRecibe, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 210, -1));

        txtFecga.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        txtFecga.setEnabled(false);
        txtFecga.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtFecga.setLabelText("Fecha");
        txtFecga.setSelectionColor(new java.awt.Color(153, 153, 255));
        txtFecga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFecgaKeyPressed(evt);
            }
        });
        jPanel1.add(txtFecga, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 210, 40));

        txtHoraEntregada.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        txtHoraEntregada.setEnabled(false);
        txtHoraEntregada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtHoraEntregada.setLabelText("Hora actual");
        txtHoraEntregada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoraEntregadaActionPerformed(evt);
            }
        });
        txtHoraEntregada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHoraEntregadaKeyPressed(evt);
            }
        });
        jPanel1.add(txtHoraEntregada, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 210, -1));

        txtContraseña.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtContraseña.setLabelText("Contraseña");
        txtContraseña.setSelectionColor(new java.awt.Color(102, 102, 255));
        txtContraseña.setShowAndHide(true);
        txtContraseña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtContraseñaKeyPressed(evt);
            }
        });
        jPanel1.add(txtContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 210, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseClicked
        eventoEnter();
    }//GEN-LAST:event_jButton15MouseClicked

    private void jButton15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseEntered
        panelRegistrarNota1.setBackground(new Color(153, 204, 255));
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
        eventoF1();
    }//GEN-LAST:event_jButton17MouseClicked

    private void jButton17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseEntered

        panelRegistrarNota3.setBackground(new Color(153, 204, 255));
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

    private void jButton22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseClicked
        dispose();
    }//GEN-LAST:event_jButton22MouseClicked

    private void jButton22MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseEntered
        panelRegistrarNota8.setBackground(new Color(153, 204, 255));
    }//GEN-LAST:event_jButton22MouseEntered

    private void jButton22MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseExited
        panelRegistrarNota8.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton22MouseExited

    private void panelRegistrarNota8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota8MouseClicked

    private void panelRegistrarNota8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota8MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota8MouseEntered

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_formKeyPressed

    private void txtNombreRecibeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreRecibeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreRecibeActionPerformed

    private void txtHoraEntregadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoraEntregadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoraEntregadaActionPerformed

    private void txtFecgaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFecgaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case java.awt.event.KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case java.awt.event.KeyEvent.VK_F1:
                eventoF1();
                break;

            case java.awt.event.KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_txtFecgaKeyPressed

    private void txtHoraEntregadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoraEntregadaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case java.awt.event.KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case java.awt.event.KeyEvent.VK_F1:
                eventoF1();
                break;

            case java.awt.event.KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_txtHoraEntregadaKeyPressed

    private void txtNombreRecibeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreRecibeKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case java.awt.event.KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case java.awt.event.KeyEvent.VK_F1:
                eventoF1();
                break;

            case java.awt.event.KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }


    }//GEN-LAST:event_txtNombreRecibeKeyPressed

    private void txtContraseñaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContraseñaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case java.awt.event.KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case java.awt.event.KeyEvent.VK_F1:
                eventoF1();
                break;

            case java.awt.event.KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_txtContraseñaKeyPressed

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
            java.util.logging.Logger.getLogger(NominaEntradaSalida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NominaEntradaSalida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NominaEntradaSalida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NominaEntradaSalida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NominaEntradaSalida dialog = new NominaEntradaSalida(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jButton15;
    private javax.swing.JLabel jButton17;
    private javax.swing.JLabel jButton22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelRegistrarNota1;
    private javax.swing.JPanel panelRegistrarNota3;
    private javax.swing.JPanel panelRegistrarNota8;
    private textfield.PasswordField txtContraseña;
    private textfield.TextField txtFecga;
    private textfield.TextField txtHoraEntregada;
    private combobox.Combobox txtNombreRecibe;
    // End of variables declaration//GEN-END:variables

    private void Seticon() {
    setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
    }

    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == hi) {
            calcula();
            hora = String.valueOf(Integer.parseInt(hora));
            txtHoraEntregada.setText(hora + ":" + min);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }

    private void calcula() {
        Calendar calendario = new GregorianCalendar();
          SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        establecerFecha();
        Date fechaHoraactual = new Date();
        try {
            // Parsear el String para obtener un objeto Date
            fechaHoraactual = sdf.parse(horaBien);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendario.setTime(fechaHoraactual);
        ampm = calendario.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

        hora = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);

        min = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
        seg = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND);
    }

}
