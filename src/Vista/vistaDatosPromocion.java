/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Eventos;
import Modelo.GastosDao;
import Modelo.PrecioDao;
import Modelo.Precios;
import Modelo.PromocionDao;
import Modelo.datosPromocion;
import com.raven.swing.Table;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.util.List;
import javaswingdev.Notification;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Jonathan Gil
 */
public class vistaDatosPromocion extends javax.swing.JDialog {

    Eventos event = new Eventos();
    GastosDao gastos = new GastosDao();
    PrecioDao preciosDao = new PrecioDao();
    List<Precios> lsPrecios = preciosDao.getPrecios();
    Object[][] dataVentas = new Object[lsPrecios.size()][2];
    PromocionDao promocionDao = new PromocionDao();
    DefaultTableModel modelo = new DefaultTableModel();
    datosPromocion datos = promocionDao.obtenerDatos();
    DecimalFormat formato = new DecimalFormat("0.00");

    public vistaDatosPromocion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Seticon();
        this.setTitle("Software Lavandería - Información de promoción");

        for (int i = 0; i < lsPrecios.size(); i++) {
            dataVentas[i][0] = lsPrecios.get(i).getNombre();
            dataVentas[i][1] = false;
        }
        String[] columnNames = {"Concepto", "Válido"};
        modelo = new DefaultTableModel(dataVentas, columnNames);
        tablaConceptos.setModel(modelo);
        TableColumn column = tablaConceptos.getColumnModel().getColumn(1);
        column.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        column.setCellRenderer(tablaConceptos.getDefaultRenderer(Boolean.class));
        TableColumn column0 = tablaConceptos.getColumnModel().getColumn(0);
        TableColumn column1 = tablaConceptos.getColumnModel().getColumn(1);
        column0.setPreferredWidth(120); // Asignar un ancho de 100 píxeles a la columna 0
        column1.setPreferredWidth(20); //
        tablaConceptos.setDefaultRenderer(Object.class, new CustomCellRenderer());

        listarConfig();
    }

    public boolean hayAlMenosUnaFilaConTrue(DefaultTableModel modelo) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Boolean valor = (Boolean) modelo.getValueAt(i, 1);
            if (valor != null && valor) {
                return true;
            }
        }
        return false;
    }

    public void bloqueando(boolean bandera) {
        checkRifa.setSelected(bandera);
        checkReiniciar.setEnabled(bandera);
        txtNombre.setEnabled(bandera);
        txtMensaje.setEnabled(bandera);
        txtMontoMinimo.setEnabled(bandera);
        jLabel5.setEnabled(bandera);
        contenerdorMensaje.setEnabled(bandera);
        tablaConceptos.setEnabled(bandera);
        jButton1.setSelected(bandera);
        if (!bandera) {
            checkReiniciar.setSelected(bandera);
            jButton1.setBackground(new Color(240, 240, 240));
            checkRifa.setBackground(new Color(240, 240, 240));
            checkReiniciar.setBackground(new Color(240, 240, 240));
            checkReiniciar.setBackground(new Color(240, 240, 240));
            txtNombre.setBackground(new Color(240, 240, 240));
            txtMontoMinimo.setBackground(new Color(240, 240, 240));
            contenerdorMensaje.setBackground(new Color(240, 240, 240));
            tablaConceptos.setBackground(new Color(240, 240, 240));
        } else {
            jButton1.setBackground(new Color(255, 255, 255));
            checkRifa.setBackground(new Color(255, 255, 255));
            checkReiniciar.setBackground(new Color(255, 255, 255));
            checkReiniciar.setBackground(new Color(255, 255, 255));
            txtNombre.setBackground(new Color(255, 255, 255));
            txtMontoMinimo.setBackground(new Color(255, 255, 255));
            contenerdorMensaje.setBackground(new Color(255, 255, 255));
            tablaConceptos.setBackground(new Color(255, 255, 255));
        }
    }

    public void listarConfig() {
        if (datos.getIndicador() == 1) {
            checkRifa.setSelected(true);
        } else {
            bloqueando(false);
        }
        txtNombre.setText(datos.getNombre());
        txtMensaje.setText(datos.getMensaje());
        txtMontoMinimo.setText(formato.format(datos.getMontoMinimo()));
        String[] conceptosValidos = datos.getConceptos().split("-");
        for (String nombre : conceptosValidos) {
            int indice = tablaConceptos.getRowCount() - 1;
            while (indice >= 0) {
                if (tablaConceptos.getValueAt(indice, 0).equals(nombre)) {
                    tablaConceptos.setValueAt(true, indice, 1);
                    break;
                }
                indice--;
            }
        }

    }

    public void eventoEnter() {
        if (!checkRifa.isSelected()) {
            datos.setIndicador(0);
            promocionDao.actualizarDatosRifa(datos);
            dispose();
        } else {
            if (!"".equals(txtNombre.getText()) && !"".equals(txtMensaje.getText()) && !"".equals(txtMontoMinimo.getText())) {
                if (!txtMontoMinimo.getText().endsWith(".")) {
                    if (hayAlMenosUnaFilaConTrue((DefaultTableModel) tablaConceptos.getModel())) {
                        datos.setNombre(txtNombre.getText());
                        datos.setMensaje(txtMensaje.getText());
                        datos.setMontoMinimo(Double.parseDouble(txtMontoMinimo.getText()));
                        String nuevosConceptos = "";
                        for (int i = 0; i < tablaConceptos.getRowCount(); i++) {
                            if ((boolean) tablaConceptos.getValueAt(i, 1) == true) {
                                nuevosConceptos = nuevosConceptos + tablaConceptos.getValueAt(i, 0).toString() + "-";
                            }
                        }
                        datos.setConceptos(nuevosConceptos);
                        datos.setIndicador(1);
                        promocionDao.actualizarDatosRifa(datos);
                        if (checkReiniciar.isSelected()) {
                            promocionDao.reiniciarPromocion();
                        }
                        Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Datos guardados exitosamente");
                        panel.showNotification();
                        dispose();
                    } else {
                        Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe seleccionar almenos un concepto");
                        panel.showNotification();
                    }
                } else {
                    Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Cantidad miníma inválida");
                    panel.showNotification();
                }
            } else {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Rellene todos los campos");
                panel.showNotification();
            }
        }
    }

    public void cambiandoValoresTabla(boolean bandera) {
        if (checkRifa.isSelected()) {
            for (int i = 0; i < dataVentas.length; i++) {
                dataVentas[i][1] = bandera;
                tablaConceptos.setValueAt(dataVentas[i][1], i, 1);
            }
        }
    }

    public void eventoF1() {
        txtNombre.setText("");
        txtMontoMinimo.setText("");
        txtMensaje.setText("");
        cambiandoValoresTabla(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtNombre = new textfield.TextField();
        txtMontoMinimo = new textfield.TextField();
        checkRifa = new javax.swing.JCheckBox();
        checkReiniciar = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        contenerdorMensaje = new javax.swing.JScrollPane();
        txtMensaje = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaConceptos = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        panelAceptar = new javax.swing.JPanel();
        btnAceptar = new javax.swing.JLabel();
        panelLimpiar = new javax.swing.JPanel();
        btnLimpiar = new javax.swing.JLabel();
        panelEliminar = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
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

        jLabel25.setBackground(new java.awt.Color(0, 0, 204));
        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 255));
        jLabel25.setText("Datos de la promoción");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 170, -1));

        txtNombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNombre.setLabelText("Nombre de la promoción");
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
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 85, 250, 40));

        txtMontoMinimo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMontoMinimo.setLabelText("Monto minímo");
        txtMontoMinimo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMontoMinimoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMontoMinimoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMontoMinimoKeyTyped(evt);
            }
        });
        jPanel1.add(txtMontoMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 250, 40));

        checkRifa.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        checkRifa.setText("Habilitar rifa");
        checkRifa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkRifaMouseClicked(evt);
            }
        });
        checkRifa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkRifaActionPerformed(evt);
            }
        });
        checkRifa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkRifaKeyPressed(evt);
            }
        });
        jPanel1.add(checkRifa, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        checkReiniciar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        checkReiniciar.setText("Reiniciar folios");
        checkReiniciar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkReiniciarMouseClicked(evt);
            }
        });
        checkReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkReiniciarActionPerformed(evt);
            }
        });
        checkReiniciar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkReiniciarKeyPressed(evt);
            }
        });
        jPanel1.add(checkReiniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Mensaje para ticket");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 150, 10));

        txtMensaje.setColumns(20);
        txtMensaje.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtMensaje.setRows(5);
        txtMensaje.setText("\n\n");
        contenerdorMensaje.setViewportView(txtMensaje);

        jPanel1.add(contenerdorMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 250, 140));

        jTabbedPane1.addTab("Datos", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaConceptos = new Table();
        tablaConceptos.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        tablaConceptos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Área", "Acceso"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaConceptos.setToolTipText("Determine los permisos para el área de Ventas ");
        tablaConceptos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tablaConceptos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaConceptosMouseClicked(evt);
            }
        });
        tablaConceptos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaConceptosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablaConceptosKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tablaConceptos);
        if (tablaConceptos.getColumnModel().getColumnCount() > 0) {
            tablaConceptos.getColumnModel().getColumn(0).setPreferredWidth(120);
            tablaConceptos.getColumnModel().getColumn(1).setPreferredWidth(20);
        }

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 270, 180));

        jLabel23.setBackground(new java.awt.Color(0, 0, 204));
        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 255));
        jLabel23.setText("¿Está seguro de ejecutar esta acción? ");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

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

        jPanel2.add(panelAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 50, 50));

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

        jPanel2.add(panelLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 280, 50, 50));

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

        jPanel2.add(panelEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, 50, 50));

        jLabel26.setBackground(new java.awt.Color(0, 0, 204));
        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 255));
        jLabel26.setText("Conceptos válidos");
        jPanel2.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 120, -1));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Seleccionar todos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, -1, -1));

        jTabbedPane1.addTab("Conceptos", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtMontoMinimoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoMinimoKeyPressed
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
    }//GEN-LAST:event_txtMontoMinimoKeyPressed

    private void txtMontoMinimoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoMinimoKeyReleased

    }//GEN-LAST:event_txtMontoMinimoKeyReleased

    private void txtMontoMinimoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoMinimoKeyTyped
        event.numberDecimalKeyPress(evt, txtMontoMinimo);
    }//GEN-LAST:event_txtMontoMinimoKeyTyped

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

    private void checkRifaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkRifaMouseClicked
        bloqueando(checkRifa.isSelected());
    }//GEN-LAST:event_checkRifaMouseClicked

    private void checkRifaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkRifaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkRifaActionPerformed

    private void checkRifaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkRifaKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_checkRifaKeyPressed

    private void checkReiniciarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkReiniciarMouseClicked

    }//GEN-LAST:event_checkReiniciarMouseClicked

    private void checkReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkReiniciarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkReiniciarActionPerformed

    private void checkReiniciarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkReiniciarKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_ENTER:
                eventoEnter();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_checkReiniciarKeyPressed

    private void tablaConceptosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaConceptosMouseClicked

    }//GEN-LAST:event_tablaConceptosMouseClicked

    private void tablaConceptosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaConceptosKeyPressed

        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_tablaConceptosKeyPressed

    private void tablaConceptosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaConceptosKeyReleased
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
    }//GEN-LAST:event_tablaConceptosKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cambiandoValoresTabla(true);
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
            java.util.logging.Logger.getLogger(vistaDatosPromocion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(vistaDatosPromocion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(vistaDatosPromocion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(vistaDatosPromocion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                vistaDatosPromocion dialog = new vistaDatosPromocion(new javax.swing.JFrame(), true);
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
    private javax.swing.JCheckBox checkReiniciar;
    private javax.swing.JCheckBox checkRifa;
    private javax.swing.JScrollPane contenerdorMensaje;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel panelAceptar;
    private javax.swing.JPanel panelEliminar;
    private javax.swing.JPanel panelLimpiar;
    private javax.swing.JTable tablaConceptos;
    private javax.swing.JTextArea txtMensaje;
    private textfield.TextField txtMontoMinimo;
    private textfield.TextField txtNombre;
    // End of variables declaration//GEN-END:variables

    private void Seticon() {
    setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
    }

}

class CustomCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cellComponent.setForeground(Color.BLACK);
        return cellComponent;
    }
}
