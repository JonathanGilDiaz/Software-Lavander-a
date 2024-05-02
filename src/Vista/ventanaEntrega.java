/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.ClienteDao;
import Modelo.Empleados;
import Modelo.EmpleadosDao;
import Modelo.Eventos;
import Modelo.Nota;
import Modelo.NotaDao;
import Modelo.establecerFecha;
import java.awt.Color;
import com.sun.glass.events.KeyEvent;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;

/**
 *
 * @author Jonathan Gil
 */
public class ventanaEntrega extends javax.swing.JDialog implements Runnable {
    
    String hora, min, seg, ampm, horaBien;
    int FormaPago = 0;
    Calendar calendario;
    Thread hi;
    
    EmpleadosDao emple = new EmpleadosDao();
    Nota nota = new Nota();
    NotaDao notaDao = new NotaDao();
    Eventos event = new Eventos();
    ClienteDao client = new ClienteDao();
    establecerFecha establecer = new establecerFecha();
    
    int folioBuscar;
    int folioCliente;
    String fecha;
    Nota n;
    boolean bandera = false;
    DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
    double cantidadInicial = 0;
    
    public ventanaEntrega(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Software Lavandería - Entrega");
        Seticon();
        listarEmpleados();
        txtNombreRecibe.setBackground(Color.WHITE);
        hi = new Thread(this);
        hi.start();
        txtFormaPago.setVisible(false);
        txtFechaEntregada.setBackground(new Color(240, 240, 240));
        txtHoraEntregada.setBackground(new Color(240, 240, 240));
        txtTotalNota.setBackground(new Color(240, 240, 240));
        txtPendiente.setBackground(new Color(240, 240, 240));
        txtCambio.setBackground(new Color(240, 240, 240));
        txtAdeudoRestante.setBackground(new Color(240, 240, 240));
        txtFechaEntregada.setEnabled(false);
    }
    
    public void establecerFecha() { //Establecemos la fecha a usar, esto se hace ya que hasta que no se cierre caja, no se puede avanzar de dia
        List<String> datos = establecer.establecerFecha();
        horaBien = datos.get(1);
        
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
    
    public void obtenerYRellenar(int folioBuscar, int folioCliente, String fecha) {
        this.folioBuscar = folioBuscar;
        this.folioCliente = folioCliente;
        this.fecha = fecha;
        txtNombreRecibe.setBackground(Color.WHITE);
        n = notaDao.buscarPorFolio(folioBuscar);
        txtTotalNota.setText("$" + formateador.format(n.getVentaTotal()));
        txtPendiente.setText("$" + formateador.format(n.getTotalPagar()));
        cantidadInicial = n.getTotalPagar();
        Date diaActual = StringADate(fecha);
        DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
        String fechaUsar;
        Date fechaNueva;
        Calendar c = Calendar.getInstance();
        c.setTime(diaActual);
        fechaUsar = cam.format(diaActual);
        txtFechaEntregada.setText(fechaUsar);
        txtFormaPago.requestFocus();
        txtFormaPago.setSelectedIndex(1);
                txtFormaPago.setSelectedIndex(0);
    }
    
    public void calcularAbono() {
        double b = 0;
        if (!"".equals(txtCantidadAbono.getText())) {
            
            b = Double.parseDouble(txtCantidadAbono.getText());
        }
        if (b > 0) {
            txtFormaPago.setVisible(true);
        } else {
            txtFormaPago.setVisible(false);
            FormaPago = 0;
        }
        double calculandoAdeudo = n.getTotalPagar() - b;
        if (b > n.getTotalPagar()) {
            txtAdeudoRestante.setText("0");
            calculandoAdeudo = calculandoAdeudo * -1;
            txtCambio.setText("$" + formateador.format(calculandoAdeudo));
        } else {
            txtAdeudoRestante.setText("$" + formateador.format(calculandoAdeudo));
            txtCambio.setText("");
        }
    }
    
    public static String removefirstChar(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(1);
    }
    
    public void eventoEnter() throws ParseException {
        if (!"".equals(txtContraseña.getText()) && !"".equals(txtCantidadAbono.getText())) {
            Empleados empleado = emple.seleccionarEmpleado(txtNombreRecibe.getSelectedItem().toString(), 0);
            double CantidadAbono = Double.parseDouble(txtCantidadAbono.getText());
Number Pendiente = n.getTotalPagar();

// Crear un objeto DecimalFormat con el formato deseado
DecimalFormat formato = new DecimalFormat("#.##");
double totalPagarRedondeado = Double.parseDouble(formato.format(Pendiente));

// Asignar el valor redondeado a Pendiente
Pendiente = totalPagarRedondeado;
if (CantidadAbono >= Pendiente.doubleValue()) {
                
                if (empleado.getContraseña().equals(txtContraseña.getText())) {
                    double entregando = CantidadAbono;
                    if (entregando > 0) {
                        switch (txtFormaPago.getSelectedIndex()) {
                            case 0:
                                FormaPago = 01;
                                break;
                            case 1:
                                FormaPago = 03;
                                break;
                            case 2:
                                FormaPago = 04;
                                break;
                        }
                    }
                    double totalPagar = Pendiente.doubleValue();
                    double resultante = entregando - totalPagar;
                    double pagar;
                    if (resultante > 0) {
                        pagar = totalPagar;
                    } else {
                        
                        if (resultante < 0) {

                            //JOptionPane.showMessageDialog(null, "NOTA ENTREGADA EXITOSAMENTE \nQUEDA ADEUDO DE:" + resultante * (-1));
                            pagar = entregando;
                        } else {
                            //JOptionPane.showMessageDialog(null, "NOTA ENTREGADA EXITOSAMENTE");
                            pagar = totalPagar;
                        }
                    }
                    establecerFecha();
                    String comentario = txtObservaciones.getText();
                    Empleados e = emple.seleccionarEmpleado(txtNombreRecibe.getSelectedItem().toString(), 0);
                    notaDao.entregarNota(n.getFolio(), pagar, fecha, horaBien, e.getId());
                    notaDao.anotarEntrega(n.getFolio(), pagar, fecha, horaBien, comentario, e.getId(), FormaPago);
                    bandera = true;
                    dispose();
                    
                } else {
                    Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Contraseña incorrecta");
                    panel.showNotification();
                }
            } else {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "El monto no puede ser inferior");
                panel.showNotification();
            }
        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Rellene todos los campos");
            panel.showNotification();
        }
    }
    
    public void eventoF1() {
        txtContraseña.setText("");
        txtCantidadAbono.setText("");
        txtObservaciones.setText("");
        txtCambio.setText("");
        txtAdeudoRestante.setText("");
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
        jLabel25 = new javax.swing.JLabel();
        txtNombreRecibe = new combobox.Combobox();
        txtContraseña = new textfield.PasswordField();
        txtTotalNota = new textfield.TextField();
        txtPendiente = new textfield.TextField();
        txtHoraEntregada = new textfield.TextField();
        txtCambio = new textfield.TextField();
        txtAdeudoRestante = new textfield.TextField();
        txtObservaciones = new textfield.TextField();
        txtFormaPago = new combobox.Combobox();
        jLabel26 = new javax.swing.JLabel();
        txtCantidadAbono = new textfield.TextField();
        txtFechaEntregada = new textfield.TextField();
        panelEliminar = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JLabel();
        panelLimpiar = new javax.swing.JPanel();
        btnLimpiar = new javax.swing.JLabel();
        panelAceptar = new javax.swing.JPanel();
        btnAceptar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setBackground(new java.awt.Color(0, 0, 204));
        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 255));
        jLabel25.setText("Entrega de nota");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, -1, -1));

        txtNombreRecibe.setToolTipText("");
        txtNombreRecibe.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNombreRecibe.setLabeText("Nombre de quien recibe");
        txtNombreRecibe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreRecibeActionPerformed(evt);
            }
        });
        txtNombreRecibe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreRecibeKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreRecibeKeyTyped(evt);
            }
        });
        jPanel1.add(txtNombreRecibe, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 250, -1));

        txtContraseña.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtContraseña.setLabelText("Contraseña");
        txtContraseña.setSelectionColor(new java.awt.Color(102, 102, 255));
        txtContraseña.setShowAndHide(true);
        txtContraseña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtContraseñaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtContraseñaKeyTyped(evt);
            }
        });
        jPanel1.add(txtContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 182, 250, -1));

        txtTotalNota.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTotalNota.setEnabled(false);
        txtTotalNota.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTotalNota.setLabelText("Total de la nota");
        txtTotalNota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalNotaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTotalNotaKeyReleased(evt);
            }
        });
        jPanel1.add(txtTotalNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 250, 40));

        txtPendiente.setText("$100.00 + $4.50 (com) = $104.50");
        txtPendiente.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtPendiente.setEnabled(false);
        txtPendiente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPendiente.setLabelText("Pendiente de pago");
        txtPendiente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPendienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPendienteKeyReleased(evt);
            }
        });
        jPanel1.add(txtPendiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 95, 250, 40));

        txtHoraEntregada.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtHoraEntregada.setEnabled(false);
        txtHoraEntregada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtHoraEntregada.setLabelText("Hora a entregar");
        txtHoraEntregada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHoraEntregadaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHoraEntregadaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHoraEntregadaKeyTyped(evt);
            }
        });
        jPanel1.add(txtHoraEntregada, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 95, 250, 40));

        txtCambio.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCambio.setEnabled(false);
        txtCambio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCambio.setLabelText("Cambio a entregar");
        txtCambio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCambioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCambioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCambioKeyTyped(evt);
            }
        });
        jPanel1.add(txtCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 182, 250, 40));

        txtAdeudoRestante.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtAdeudoRestante.setEnabled(false);
        txtAdeudoRestante.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtAdeudoRestante.setLabelText("Adeudo");
        txtAdeudoRestante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAdeudoRestanteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAdeudoRestanteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdeudoRestanteKeyTyped(evt);
            }
        });
        jPanel1.add(txtAdeudoRestante, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 233, 250, 40));

        txtObservaciones.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtObservaciones.setLabelText("Observaciones");
        txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacionesKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtObservacionesKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtObservacionesKeyTyped(evt);
            }
        });
        jPanel1.add(txtObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 233, 250, 40));

        txtFormaPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01-Efectivo", "03-Transferencia", "04-Tarjeta" }));
        txtFormaPago.setToolTipText("");
        txtFormaPago.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtFormaPago.setLabeText("Forma de pago");
        txtFormaPago.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                txtFormaPagoItemStateChanged(evt);
            }
        });
        txtFormaPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFormaPagoActionPerformed(evt);
            }
        });
        txtFormaPago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFormaPagoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFormaPagoKeyTyped(evt);
            }
        });
        jPanel1.add(txtFormaPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 250, -1));

        jLabel26.setBackground(new java.awt.Color(0, 0, 204));
        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 255));
        jLabel26.setText("¿Está seguro de ejecutar esta acción? ");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, -1, -1));

        txtCantidadAbono.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCantidadAbono.setLabelText("Cantidad pagada");
        txtCantidadAbono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadAbonoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantidadAbonoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadAbonoKeyTyped(evt);
            }
        });
        jPanel1.add(txtCantidadAbono, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 250, 40));

        txtFechaEntregada.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtFechaEntregada.setEnabled(false);
        txtFechaEntregada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtFechaEntregada.setLabelText("Fecha a entregar");
        txtFechaEntregada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFechaEntregadaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFechaEntregadaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFechaEntregadaKeyTyped(evt);
            }
        });
        jPanel1.add(txtFechaEntregada, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 250, 40));

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

        jPanel1.add(panelEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 390, 50, 50));

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

        jPanel1.add(panelLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 390, 50, 50));

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

        jPanel1.add(panelAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 390, 50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreRecibeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreRecibeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreRecibeActionPerformed

    private void txtNombreRecibeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreRecibeKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtNombreRecibeKeyPressed

    private void txtContraseñaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContraseñaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtContraseñaKeyPressed

    private void txtTotalNotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalNotaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtTotalNotaKeyPressed

    private void txtTotalNotaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalNotaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalNotaKeyReleased

    private void txtPendienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPendienteKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtPendienteKeyPressed

    private void txtPendienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPendienteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPendienteKeyReleased

    private void txtHoraEntregadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoraEntregadaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtHoraEntregadaKeyPressed

    private void txtHoraEntregadaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoraEntregadaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoraEntregadaKeyReleased

    private void txtHoraEntregadaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoraEntregadaKeyTyped
        char c = event.numberDecimalKeyPress(evt, txtCantidadAbono);
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtHoraEntregadaKeyTyped

    private void txtCambioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCambioKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtCambioKeyPressed

    private void txtCambioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCambioKeyReleased

    }//GEN-LAST:event_txtCambioKeyReleased

    private void txtCambioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCambioKeyTyped
        event.textKeyPress(evt);
    }//GEN-LAST:event_txtCambioKeyTyped

    private void txtAdeudoRestanteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdeudoRestanteKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtAdeudoRestanteKeyPressed

    private void txtAdeudoRestanteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdeudoRestanteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdeudoRestanteKeyReleased

    private void txtAdeudoRestanteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdeudoRestanteKeyTyped
        event.numberKeyPress(evt);
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtAdeudoRestanteKeyTyped

    private void txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtObservacionesKeyPressed

    private void txtObservacionesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtObservacionesKeyReleased

    private void txtFormaPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFormaPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFormaPagoActionPerformed

    private void txtFormaPagoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFormaPagoKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtFormaPagoKeyPressed

    private void txtCantidadAbonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadAbonoKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtCantidadAbonoKeyPressed

    private void txtCantidadAbonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadAbonoKeyReleased
        calcularAbono();
    }//GEN-LAST:event_txtCantidadAbonoKeyReleased

    private void txtCantidadAbonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadAbonoKeyTyped
        event.numberDecimalKeyPress(evt, txtCantidadAbono);
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtCantidadAbonoKeyTyped

    private void txtFechaEntregadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaEntregadaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtFechaEntregadaKeyPressed

    private void txtFechaEntregadaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaEntregadaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaEntregadaKeyReleased

    private void txtFechaEntregadaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaEntregadaKeyTyped
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtFechaEntregadaKeyTyped

    private void txtNombreRecibeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreRecibeKeyTyped
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtNombreRecibeKeyTyped

    private void txtContraseñaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContraseñaKeyTyped
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtContraseñaKeyTyped

    private void txtObservacionesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesKeyTyped
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtObservacionesKeyTyped

    private void txtFormaPagoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFormaPagoKeyTyped
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                dispose();
                break;
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            
            case KeyEvent.VK_F1:
                eventoF1();
                break;
        }
    }//GEN-LAST:event_txtFormaPagoKeyTyped

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
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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

    private void btnAceptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAceptarMouseClicked
        try {
            eventoEnter();
        } catch (ParseException ex) {
            Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            case KeyEvent.VK_ENTER: {
                try {
                    eventoEnter();
                } catch (ParseException ex) {
                    Logger.getLogger(ventanaEntrega.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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

    private void txtFormaPagoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_txtFormaPagoItemStateChanged
        if (txtFormaPago.getSelectedIndex() == 2) {
            n.setTotalPagar(cantidadInicial * 1.045);
            txtPendiente.setText("$" + formateador.format(cantidadInicial) + "+$" + formateador.format(cantidadInicial*0.045) + "(com)=$" + formateador.format(n.getTotalPagar()));
            
        } else {
            n.setTotalPagar(cantidadInicial);
            txtPendiente.setText("$" + formateador.format(n.getTotalPagar()));
            
        }
        calcularAbono();
    }//GEN-LAST:event_txtFormaPagoItemStateChanged

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
            java.util.logging.Logger.getLogger(ventanaEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ventanaEntrega dialog = new ventanaEntrega(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelAceptar;
    private javax.swing.JPanel panelEliminar;
    private javax.swing.JPanel panelLimpiar;
    private textfield.TextField txtAdeudoRestante;
    private textfield.TextField txtCambio;
    private textfield.TextField txtCantidadAbono;
    private textfield.PasswordField txtContraseña;
    private textfield.TextField txtFechaEntregada;
    private combobox.Combobox txtFormaPago;
    private textfield.TextField txtHoraEntregada;
    private combobox.Combobox txtNombreRecibe;
    private textfield.TextField txtObservaciones;
    private textfield.TextField txtPendiente;
    private textfield.TextField txtTotalNota;
    // End of variables declaration//GEN-END:variables

    private void Seticon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
    }
    
    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == hi) {
            calcula();
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
