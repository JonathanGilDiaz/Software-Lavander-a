/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Eventos;
import Modelo.GastosDao;
import Modelo.config;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Jonathan Gil
 */
public class DatosEmpresa extends javax.swing.JDialog {

    Eventos event = new Eventos();
    config configura = new config();
    GastosDao gastos = new GastosDao();
    boolean accionRealizada = false;

    public DatosEmpresa(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Seticon();
        this.setTitle("Software Lavandería - Datos de la empresa");
        listarConfig();
        selectorHora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                SimpleDateFormat formato12 = new SimpleDateFormat("h:mm a");
                SimpleDateFormat formato24 = new SimpleDateFormat("HH:mm");

                try {
                    Date date = formato12.parse(selectorHora.getSelectedTime());
                    String hora24 = formato24.format(date);
                    txtHora.setText(hora24);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void listarConfig() {
        configura = gastos.buscarDatos();
        txtRFC.setText(configura.getRfc());
        txtRazonSocial.setText(configura.getRazonSocial());
        txtTelefono.setText(String.valueOf(configura.getTelefono()));
        txtNombre.setText(configura.getNomnbre());
        txtDireccion.setText(configura.getDireccion());
        txtHorario.setText(configura.getHorario());
        txtEncargado.setText(configura.getEncargado());
        txtLetra1.setText("" + configura.getLetraGrande());
        txtLetra2.setText("" + configura.getLetraChica());
        txtMensaje.setText(configura.getMensaje());
        LocalTime horaActual = LocalTime.now();
        LocalTime horaDesdeString = LocalTime.parse(configura.getHora());
        System.out.println(configura.getIndicadorHora());

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

        // Formatear la hora resultante en el formato hh:mm
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
        txtHora.setText(horaActual.format(formato));

    }

    public void eventoEnter() {
        if (!"".equals(txtHora.getText()) && !"".equals(txtNombre.getText()) && !"".equals(txtDireccion.getText()) && !"".equals(txtRFC.getText()) && !"".equals(txtRazonSocial.getText()) && !"".equals(txtTelefono.getText())) {
            configura.setDireccion(txtDireccion.getText());
            configura.setNomnbre(txtNombre.getText());
            configura.setRazonSocial(txtRazonSocial.getText());
            configura.setRfc(txtRFC.getText());
            configura.setTelefono(txtTelefono.getText());
            configura.setEncargado(txtEncargado.getText());
            configura.setHorario(txtHorario.getText());
            configura.setLetraGrande(Integer.parseInt(txtLetra1.getText()));
            configura.setLetraChica(Integer.parseInt(txtLetra2.getText()));
            configura.setMensaje(txtMensaje.getText());
            int valor = 0;
            Date horaActual = new Date(); //hora actual

            SimpleDateFormat formato12 = new SimpleDateFormat("h:mm a");
            SimpleDateFormat formato24 = new SimpleDateFormat("HH:mm");

            try {
                Date horaSeleccionada = formato12.parse(selectorHora.getSelectedTime());
                String hora24 = formato24.format(horaSeleccionada);
                if (horaSeleccionada.after(horaActual)) {
                    valor = 1;
                } else {
                    valor = 0;
                }
                // Calcular la diferencia de tiempo en milisegundos
                long diferenciaMillis = Math.abs(horaSeleccionada.getTime() - horaActual.getTime());

                // Convertir la diferencia de tiempo a un objeto Duration
                Duration duracion = Duration.ofMillis(diferenciaMillis);

                // Calcular las horas, minutos y segundos de la duración
                long segundosTotales = duracion.getSeconds();

                long horas;
                if (segundosTotales >= 86400) { // Si hay más de un día en segundos (86400 segundos en un día)
                    long dias = segundosTotales / 86400;
                    horas = (segundosTotales % 86400) / 3600;
                } else {
                    horas = segundosTotales / 3600;
                }

                long minutos = (segundosTotales % 3600) / 60;
                long segundos = segundosTotales % 60;

                // Formatear la diferencia de tiempo en formato LocalTime
                LocalTime diferenciaHorasSQL = LocalTime.of((int) horas, (int) minutos, (int) segundos);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                configura.setHora(diferenciaHorasSQL.format(formatter));
            } catch (ParseException ex) {
                Logger.getLogger(DatosEmpresa.class.getName()).log(Level.SEVERE, null, ex);
            }

            gastos.actualizarDatos(configura, valor);
            accionRealizada = true;
            dispose();
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Información  registrada exitosamente");
            panel.showNotification();
        }
    }

    public void eventoF1() {
        txtNombre.setText("");
        txtDireccion.setText("");
        txtRFC.setText("");
        txtTelefono.setText("");
        txtRazonSocial.setText("");
        txtLetra2.setText("");
        txtLetra1.setText("");
        txtEncargado.setText("");
        txtHorario.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectorHora = new com.raven.swing.TimePicker();
        jPanel1 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtNombre = new textfield.TextField();
        txtDireccion = new textfield.TextField();
        txtRFC = new textfield.TextField();
        txtRazonSocial = new textfield.TextField();
        txtTelefono = new textfield.TextField();
        txtHorario = new textfield.TextField();
        txtEncargado = new textfield.TextField();
        txtLetra1 = new textfield.TextField();
        txtLetra2 = new textfield.TextField();
        panelAceptar = new javax.swing.JPanel();
        btnAceptar = new javax.swing.JLabel();
        panelLimpiar = new javax.swing.JPanel();
        btnLimpiar = new javax.swing.JLabel();
        panelEliminar = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMensaje = new javax.swing.JTextArea();
        txtHora = new textfield.TextField();
        jButton1 = new javax.swing.JButton();

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
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 370, -1, -1));

        jLabel25.setBackground(new java.awt.Color(0, 0, 204));
        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 255));
        jLabel25.setText("Datos de la empresa");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, 170, -1));

        txtNombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNombre.setLabelText("Nombre de la empresa");
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 250, 40));

        txtDireccion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtDireccion.setLabelText("Dirección");
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionKeyTyped(evt);
            }
        });
        jPanel1.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 85, 250, 40));

        txtRFC.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtRFC.setLabelText("RFC");
        txtRFC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRFCKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRFCKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRFCKeyTyped(evt);
            }
        });
        jPanel1.add(txtRFC, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 250, 40));

        txtRazonSocial.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtRazonSocial.setLabelText("Razón social");
        txtRazonSocial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRazonSocialKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRazonSocialKeyReleased(evt);
            }
        });
        jPanel1.add(txtRazonSocial, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 175, 250, 40));

        txtTelefono.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTelefono.setLabelText("Teléfono");
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
            }
        });
        jPanel1.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 250, 40));

        txtHorario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtHorario.setLabelText("Horario");
        txtHorario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHorarioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHorarioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHorarioKeyTyped(evt);
            }
        });
        jPanel1.add(txtHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 250, 40));

        txtEncargado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtEncargado.setLabelText("Encargado");
        txtEncargado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEncargadoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEncargadoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEncargadoKeyTyped(evt);
            }
        });
        jPanel1.add(txtEncargado, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 85, 250, 40));

        txtLetra1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtLetra1.setLabelText("Letra para ticket 1");
        txtLetra1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLetra1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLetra1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLetra1KeyTyped(evt);
            }
        });
        jPanel1.add(txtLetra1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, 250, 40));

        txtLetra2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtLetra2.setLabelText("Letra para ticket 2");
        txtLetra2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLetra2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLetra2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLetra2KeyTyped(evt);
            }
        });
        jPanel1.add(txtLetra2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 175, 250, 40));

        panelAceptar.setBackground(new java.awt.Color(255, 255, 255));
        panelAceptar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelAceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelAceptarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelAceptarMouseEntered(evt);
            }
        });

        btnAceptar.setBackground(new java.awt.Color(153, 204, 255));
        btnAceptar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        btnAceptar.setForeground(new java.awt.Color(255, 255, 255));
        btnAceptar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar nota 2.png"))); // NOI18N
        btnAceptar.setToolTipText("ENTER - Guardar empleado");
        btnAceptar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAceptarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAceptarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAceptarMouseExited(evt);
            }
        });
        btnAceptar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAceptarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelAceptarLayout = new javax.swing.GroupLayout(panelAceptar);
        panelAceptar.setLayout(panelAceptarLayout);
        panelAceptarLayout.setHorizontalGroup(
            panelAceptarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelAceptarLayout.setVerticalGroup(
            panelAceptarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAceptar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 390, 50, 50));

        panelLimpiar.setBackground(new java.awt.Color(255, 255, 255));
        panelLimpiar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelLimpiarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelLimpiarMouseEntered(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar nota 22.png"))); // NOI18N
        btnLimpiar.setToolTipText("F1 - Limpiar todos los campos");
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseExited(evt);
            }
        });
        btnLimpiar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLimpiarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelLimpiarLayout = new javax.swing.GroupLayout(panelLimpiar);
        panelLimpiar.setLayout(panelLimpiarLayout);
        panelLimpiarLayout.setHorizontalGroup(
            panelLimpiarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelLimpiarLayout.setVerticalGroup(
            panelLimpiarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLimpiar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 390, 50, 50));

        panelEliminar.setBackground(new java.awt.Color(255, 255, 255));
        panelEliminar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelEliminarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelEliminarMouseEntered(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(255, 255, 255));
        btnEliminar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imagen5.png"))); // NOI18N
        btnEliminar.setToolTipText("ESCAPE - Cancelar accion y salir");
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEliminarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEliminarMouseExited(evt);
            }
        });
        btnEliminar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEliminarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelEliminarLayout = new javax.swing.GroupLayout(panelEliminar);
        panelEliminar.setLayout(panelEliminarLayout);
        panelEliminarLayout.setHorizontalGroup(
            panelEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelEliminarLayout.setVerticalGroup(
            panelEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 390, 50, 50));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Mensaje final del ticket");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 240, 150, 10));

        txtMensaje.setColumns(20);
        txtMensaje.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtMensaje.setRows(5);
        txtMensaje.setText("\n\n");
        jScrollPane1.setViewportView(txtMensaje);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 260, 270, 100));

        txtHora.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtHora.setLabelText("Hora");
        txtHora.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtHoraMouseClicked(evt);
            }
        });
        txtHora.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHoraKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHoraKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHoraKeyTyped(evt);
            }
        });
        jPanel1.add(txtHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 265, 250, 40));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("Modificar logo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 140, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void txtNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtNombreKeyPressed

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        event.textKeyPress(evt);
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtDireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtDireccionKeyPressed

    private void txtDireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyReleased

    }//GEN-LAST:event_txtDireccionKeyReleased

    private void txtDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyTyped

    }//GEN-LAST:event_txtDireccionKeyTyped

    private void txtRFCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRFCKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtRFCKeyPressed

    private void txtRFCKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRFCKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRFCKeyReleased

    private void txtRFCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRFCKeyTyped
        // event.numberKeyPress(evt);
    }//GEN-LAST:event_txtRFCKeyTyped

    private void txtRazonSocialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRazonSocialKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtRazonSocialKeyPressed

    private void txtRazonSocialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRazonSocialKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRazonSocialKeyReleased

    private void txtTelefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtTelefonoKeyPressed

    private void txtTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoKeyReleased

    private void txtHorarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHorarioKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtHorarioKeyPressed

    private void txtHorarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHorarioKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHorarioKeyReleased

    private void txtHorarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHorarioKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHorarioKeyTyped

    private void txtEncargadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEncargadoKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtEncargadoKeyPressed

    private void txtEncargadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEncargadoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEncargadoKeyReleased

    private void txtEncargadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEncargadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEncargadoKeyTyped

    private void txtLetra1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLetra1KeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtLetra1KeyPressed

    private void txtLetra1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLetra1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLetra1KeyReleased

    private void txtLetra1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLetra1KeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtLetra1KeyTyped

    private void txtLetra2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLetra2KeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtLetra2KeyPressed

    private void txtLetra2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLetra2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLetra2KeyReleased

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void txtLetra2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLetra2KeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtLetra2KeyTyped

    private void btnAceptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAceptarMouseClicked
        eventoEnter();
    }//GEN-LAST:event_btnAceptarMouseClicked

    private void btnAceptarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAceptarMouseEntered
        panelAceptar.setBackground(new Color(153, 204, 255));
    }//GEN-LAST:event_btnAceptarMouseEntered

    private void btnAceptarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAceptarMouseExited
        panelAceptar.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnAceptarMouseExited

    private void btnAceptarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAceptarKeyPressed
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
    }//GEN-LAST:event_btnAceptarKeyPressed

    private void panelAceptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAceptarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelAceptarMouseClicked

    private void panelAceptarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAceptarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelAceptarMouseEntered

    private void btnLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseClicked
        eventoF1();

    }//GEN-LAST:event_btnLimpiarMouseClicked

    private void btnLimpiarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseEntered

        panelLimpiar.setBackground(new Color(153, 204, 255));
    }//GEN-LAST:event_btnLimpiarMouseEntered

    private void btnLimpiarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseExited
        panelLimpiar.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnLimpiarMouseExited

    private void btnLimpiarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLimpiarKeyPressed
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
    }//GEN-LAST:event_btnLimpiarKeyPressed

    private void panelLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelLimpiarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelLimpiarMouseClicked

    private void panelLimpiarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelLimpiarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelLimpiarMouseEntered

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        dispose();
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void btnEliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseEntered
        panelEliminar.setBackground(new Color(153, 204, 255));
    }//GEN-LAST:event_btnEliminarMouseEntered

    private void btnEliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseExited
        panelEliminar.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnEliminarMouseExited

    private void btnEliminarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEliminarKeyPressed
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
    }//GEN-LAST:event_btnEliminarKeyPressed

    private void panelEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelEliminarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelEliminarMouseClicked

    private void panelEliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelEliminarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelEliminarMouseEntered

    private void txtHoraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoraKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoraKeyPressed

    private void txtHoraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoraKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoraKeyReleased

    private void txtHoraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoraKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoraKeyTyped

    private void txtHoraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHoraMouseClicked
        selectorHora.showPopup(this, 90, 250);

    }//GEN-LAST:event_txtHoraMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Crear un cuadro de diálogo para seleccionar archivos
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(null);

        // Verificar si el usuario ha seleccionado un archivo
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                // Obtener la imagen seleccionada por el usuario
                File imagenSeleccionada = fileChooser.getSelectedFile();

                // Leer la imagen como un objeto BufferedImage
                BufferedImage imagen = ImageIO.read(imagenSeleccionada);

                // Definir la ruta y nombre de archivo para guardar la imagen
                String rutaDestino = "Iconos\\logo 100x100.jpg";

                // Sobrescribir el archivo existente
                Path rutaArchivoDestino = Paths.get(rutaDestino);
                Files.copy(imagenSeleccionada.toPath(), rutaArchivoDestino, StandardCopyOption.REPLACE_EXISTING);

                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Logo modificado correctamente");
                panel.showNotification();
            } catch (IOException e) {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Error al cargar la imagen");
                panel.showNotification();
            }
        } else {
            System.out.println("Operación cancelada por el usuario.");
        }

    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(DatosEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DatosEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DatosEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DatosEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DatosEmpresa dialog = new DatosEmpresa(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel btnAceptar;
    private javax.swing.JLabel btnEliminar;
    private javax.swing.JLabel btnLimpiar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelAceptar;
    private javax.swing.JPanel panelEliminar;
    private javax.swing.JPanel panelLimpiar;
    private com.raven.swing.TimePicker selectorHora;
    private textfield.TextField txtDireccion;
    private textfield.TextField txtEncargado;
    private textfield.TextField txtHora;
    private textfield.TextField txtHorario;
    private textfield.TextField txtLetra1;
    private textfield.TextField txtLetra2;
    private javax.swing.JTextArea txtMensaje;
    private textfield.TextField txtNombre;
    private textfield.TextField txtRFC;
    private textfield.TextField txtRazonSocial;
    private textfield.TextField txtTelefono;
    // End of variables declaration//GEN-END:variables

    private void Seticon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
    }

}
