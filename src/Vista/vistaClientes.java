/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Cliente;
import Modelo.ClienteDao;
import Modelo.Detalle;
import Modelo.Gastos;
import Modelo.GastosDao;
import Modelo.Nota;
import Modelo.NotaDao;
import Modelo.anticipo;
import Modelo.anticipoDao;
import Modelo.config;
import Modelo.corteDiaDao;
import Modelo.entrega;
import Modelo.establecerFecha;
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
import com.raven.model.StatusType;
import com.raven.swing.Table2;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javaswingdev.Notification;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
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

public class vistaClientes extends javax.swing.JDialog {

    ClienteDao client = new ClienteDao();
    anticipoDao anticip = new anticipoDao();
    GastosDao gastos = new GastosDao();
    corteDiaDao corte = new corteDiaDao();
    DefaultTableModel modelo = new DefaultTableModel();
    NotaDao notaDao = new NotaDao();
    establecerFecha establecer = new establecerFecha();
    String fecha, hora;
    DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
    DecimalFormat df = new DecimalFormat("$ #,##0.00;($ #,##0.00)");

    int fila = -1;

    public vistaClientes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(200, 50);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Software Lavandería - Clientes");
        Seticon();
        establecerFecha();
        listarCliente();

        jScrollPane2.getViewport().setBackground(new Color(204, 204, 204));
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        TableClientes.getTableHeader().setBackground(Color.blue);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        TableClientes.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TableClientes.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TableClientes.getColumnModel().getColumn(2).setCellRenderer(tcr);
        TableClientes.getColumnModel().getColumn(3).setCellRenderer(tcr);
        TableClientes.getColumnModel().getColumn(4).setCellRenderer(tcr);
        TableClientes.getColumnModel().getColumn(6).setCellRenderer(tcr);
        ((DefaultTableCellRenderer) TableClientes.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

    }

    public void LimpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    private static boolean isSubstring(String s, String seq) {
        return Pattern.compile(Pattern.quote(seq), Pattern.CASE_INSENSITIVE)
                .matcher(s).find();
    }

    public void establecerFecha() { //Establecemos la fecha a usar, esto se hace ya que hasta que no se cierre caja, no se puede avanzar de dia
        List<String> datos = establecer.establecerFecha();
        fecha = datos.get(0);
        hora = datos.get(1);

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

    public void listarCliente() {
        List<Cliente> ListaCl = client.ListarCliente();
        modelo = (DefaultTableModel) TableClientes.getModel();
        Object[] ob = new Object[7];
        for (int i = 0; i < ListaCl.size(); i++) {
            ob[0] = ListaCl.get(i).getId();
            ob[1] = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
            ob[2] = ListaCl.get(i).getCorreo();
            ob[3] = ListaCl.get(i).getTelefono();
            ob[4] = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
            if (ListaCl.get(i).getEstado() == 1) {
                ob[5] = StatusType.ACTIVE;
            } else {
                ob[5] = StatusType.INACTIVE;
            }
            ob[6] = ListaCl.get(i).getDomicilio();

            modelo.addRow(ob);
        }
        TableClientes.setModel(modelo);
    }

    public void crearCliente() {
        CrearModificarCliente cc = new CrearModificarCliente(new javax.swing.JFrame(), true);
        Cliente cp = new Cliente();
        cc.vaciarDatos(fecha, cp, true);
        cc.setVisible(true);
        if (cc.accionCompletada == true) {
            LimpiarTabla();
            listarCliente();
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Cliente creado correctamente");
            panel.showNotification();
        }
    }

    public void reporteCliente() {
        if (fila != -1) {
            vistaReportesPDFExcel a = new vistaReportesPDFExcel(new javax.swing.JFrame(), true);
            a.paraCliente();
            a.setVisible(true);
            if (a.accionRealizadaCliente == 1) {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Generando reporte");
                panel.showNotification();
                if (a.tipo == true) {
                    reporteCliente(a.fechaInicial, a.fechaFinal, Integer.parseInt(TableClientes.getValueAt(fila, 0).toString()));
                } else {
                    reporteClienteExcel(a.fechaInicial, a.fechaFinal, Integer.parseInt(TableClientes.getValueAt(fila, 0).toString()));
                }
            }
        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe seleccionar una fila antes");
            panel.showNotification();
        }

    }

    public void modificarCliente() {
        if (fila != -1) {
            CrearModificarCliente cc = new CrearModificarCliente(new javax.swing.JFrame(), true);
            Cliente cp = client.BuscarPorCodigo(Integer.parseInt(TableClientes.getValueAt(fila, 0).toString()));
            cc.vaciarDatos(fecha, cp, false);
            cc.setVisible(true);
            if (cc.accionCompletada == true) {
                LimpiarTabla();
                listarCliente();
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Cliente modificado correctamente");
                panel.showNotification();
            }
        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe seleccionar una fila antes");
            panel.showNotification();
        }
    }

    public void bloquearCliente() {
        if (fila != -1) {
            ContraseñaConUsuario cc = new ContraseñaConUsuario(new javax.swing.JFrame(), true);
            cc.cambiandoEncabezado(1);
            cc.setVisible(true);
            if (cc.contraseñaAceptada == true) {

                Cliente abloquear = client.BuscarPorCodigo(Integer.parseInt(TableClientes.getValueAt(fila, 0).toString()));
                if (abloquear.getEstado() == 1) {
                    client.ModificarEstado(Integer.parseInt(TableClientes.getValueAt(fila, 0).toString()), 0);
                } else {
                    client.ModificarEstado(Integer.parseInt(TableClientes.getValueAt(fila, 0).toString()), 1);
                }
                LimpiarTabla();
                listarCliente();
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Se ha modificado el estado del cliente");
                panel.showNotification();
            }
        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe seleccionar una fila antes");
            panel.showNotification();
        }
    }

    public void eliminarCliente() {
        if (fila != -1) {

            List<Nota> lsNotas = notaDao.listarNotas();
            boolean tieneNotas = false;
            for (int i = 0; i < lsNotas.size(); i++) {
                if (lsNotas.get(i).getCodigoCLiente() == Integer.parseInt(TableClientes.getValueAt(fila, 0).toString())) {
                    tieneNotas = true;
                }
            }
            if (tieneNotas == false) {//si se va a eliminar
                ContraseñaConUsuario cc = new ContraseñaConUsuario(new javax.swing.JFrame(), true);
                cc.cambiandoEncabezado(2);
                cc.setVisible(true);
                if (cc.contraseñaAceptada == true) {
                    client.EliminarCliente(Integer.parseInt(TableClientes.getValueAt(fila, 0).toString()));
                    LimpiarTabla();
                    listarCliente();
                    Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Cliente eliminado");
                    panel.showNotification();
                }
            } else {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Ya cuenta con notas registradas");
                panel.showNotification();
            }
        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe seleccionar una fila antes");
            panel.showNotification();
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
        jScrollPane2 = new javax.swing.JScrollPane();
        TableClientes = new javax.swing.JTable();
        panelModificar = new javax.swing.JPanel();
        btnModificar = new javax.swing.JLabel();
        panelReporte = new javax.swing.JPanel();
        btnReporte = new javax.swing.JLabel();
        panelNuevo = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JLabel();
        panelEliminar = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JLabel();
        panelEstado = new javax.swing.JPanel();
        btnEstado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CLIENTES");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(667, 667, 667))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1023, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField1.setLabelText("Buscar cliente");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 440, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 106, 620, 60));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableClientes = new Table2();
        TableClientes.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        TableClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Correo eléctronico", "Teléfono", "Creación", "Estatus", "Dirección"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableClientes.setToolTipText("Haz doble click para poder hacer modificaciones al cliente seleccionado");
        TableClientes.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TableClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableClientesMouseClicked(evt);
            }
        });
        TableClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableClientesKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(TableClientes);
        if (TableClientes.getColumnModel().getColumnCount() > 0) {
            TableClientes.getColumnModel().getColumn(0).setResizable(false);
            TableClientes.getColumnModel().getColumn(0).setPreferredWidth(5);
            TableClientes.getColumnModel().getColumn(1).setResizable(false);
            TableClientes.getColumnModel().getColumn(1).setPreferredWidth(150);
            TableClientes.getColumnModel().getColumn(2).setPreferredWidth(120);
            TableClientes.getColumnModel().getColumn(3).setResizable(false);
            TableClientes.getColumnModel().getColumn(3).setPreferredWidth(20);
            TableClientes.getColumnModel().getColumn(4).setPreferredWidth(20);
            TableClientes.getColumnModel().getColumn(5).setResizable(false);
            TableClientes.getColumnModel().getColumn(5).setPreferredWidth(60);
            TableClientes.getColumnModel().getColumn(6).setPreferredWidth(100);
        }

        jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 980, 430));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 1003, 450));

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
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/MODIFICAR.png"))); // NOI18N
        btnModificar.setToolTipText("F3 - Modificar cliente");
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

        jPanel1.add(panelModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 50, 50));

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

        jPanel1.add(panelReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 50, 50));

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
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar nota 22.png"))); // NOI18N
        btnNuevo.setToolTipText("F1 - Nuevo cliente");
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
        btnEliminar.setToolTipText("F5 - Eliminar cliente");
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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

        jPanel1.add(panelEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 50, 50));

        panelEstado.setBackground(new java.awt.Color(255, 255, 255));
        panelEstado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelEstado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelEstadoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelEstadoMouseEntered(evt);
            }
        });

        btnEstado.setBackground(new java.awt.Color(255, 255, 255));
        btnEstado.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        btnEstado.setForeground(new java.awt.Color(255, 255, 255));
        btnEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ESTADO.png"))); // NOI18N
        btnEstado.setToolTipText("F4 - Modificar estado");
        btnEstado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEstado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEstadoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEstadoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEstadoMouseExited(evt);
            }
        });
        btnEstado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEstadoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelEstadoLayout = new javax.swing.GroupLayout(panelEstado);
        panelEstado.setLayout(panelEstadoLayout);
        panelEstadoLayout.setHorizontalGroup(
            panelEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelEstadoLayout.setVerticalGroup(
            panelEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnEstado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TableClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableClientesMouseClicked
        if (evt.getClickCount() == 2) {
            fila = TableClientes.rowAtPoint(evt.getPoint());
        }
    }//GEN-LAST:event_TableClientesMouseClicked

    private void TableClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableClientesKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                crearCliente();
                break;

            case KeyEvent.VK_F2:
                reporteCliente();
                break;

            case KeyEvent.VK_F3:
                modificarCliente();
                break;

            case KeyEvent.VK_F4:
                bloquearCliente();
                break;

            case KeyEvent.VK_F5:
                eliminarCliente();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_TableClientesKeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                crearCliente();
                break;

            case KeyEvent.VK_F2:
                reporteCliente();
                break;

            case KeyEvent.VK_F3:
                modificarCliente();
                break;

            case KeyEvent.VK_F4:
                bloquearCliente();
                break;

            case KeyEvent.VK_F5:
                eliminarCliente();
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
                Cliente alistar = client.BuscarPorCodigo(Integer.parseInt(jTextField1.getText()));
                if (alistar.getId() == 0) {
                    modelo = (DefaultTableModel) TableClientes.getModel();
                    TableClientes.setModel(modelo);
                } else {
                    modelo = (DefaultTableModel) TableClientes.getModel();
                    Object[] ob = new Object[7];
                    ob[0] = alistar.getId();
                    ob[1] = alistar.getNombre() + " " + alistar.getApellido();
                    ob[2] = alistar.getCorreo();
                    ob[3] = alistar.getTelefono();
                    ob[4] = fechaFormatoCorrecto(alistar.getFecha());
                    if (alistar.getEstado() == 1) {
                        ob[5] = StatusType.ACTIVE;
                    } else {
                        ob[5] = StatusType.INACTIVE;
                    }
                    ob[6] = alistar.getDomicilio();
                    modelo.addRow(ob);

                    TableClientes.setModel(modelo);
                }
            } else {
                modelo = (DefaultTableModel) TableClientes.getModel();
                Object[] ob = new Object[7];
                List<Cliente> lista = client.buscarLetra(jTextField1.getText());
                if (lista.size() == 0) {

                    List<Cliente> todosClientes = client.ListarCliente();
                    for (int i = 0; i < todosClientes.size(); i++) {
                        String nombreCompleto = todosClientes.get(i).getNombre() + " " + todosClientes.get(i).getApellido();
                        if (isSubstring(nombreCompleto, jTextField1.getText())) {
                            ob[0] = todosClientes.get(i).getId();
                            ob[1] = todosClientes.get(i).getNombre() + " " + todosClientes.get(i).getApellido();
                            ob[2] = todosClientes.get(i).getCorreo();
                            ob[3] = todosClientes.get(i).getTelefono();
                            ob[4] = fechaFormatoCorrecto(todosClientes.get(i).getFecha());
                            if (todosClientes.get(i).getEstado() == 1) {
                                ob[5] = StatusType.ACTIVE;
                            } else {
                                ob[5] = StatusType.INACTIVE;
                            }
                            ob[6] = todosClientes.get(i).getDomicilio();
                            modelo.addRow(ob);
                        }
                    }
                    TableClientes.setModel(modelo);

                }

                for (int i = 0; i < lista.size(); i++) {
                    ob[0] = lista.get(i).getId();
                    ob[1] = lista.get(i).getNombre() + " " + lista.get(i).getApellido();
                    ob[2] = lista.get(i).getCorreo();
                    ob[3] = lista.get(i).getTelefono();
                    ob[4] = fechaFormatoCorrecto(lista.get(i).getFecha());
                    if (lista.get(i).getEstado() == 1) {
                        ob[5] = StatusType.ACTIVE;
                    } else {
                        ob[5] = StatusType.INACTIVE;
                    }
                    ob[6] = lista.get(i).getDomicilio();
                    modelo.addRow(ob);
                }
                TableClientes.setModel(modelo);
            }
        } else {
            listarCliente();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                crearCliente();
                break;

            case KeyEvent.VK_F2:
                reporteCliente();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void btnModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseClicked
        modificarCliente();
    }//GEN-LAST:event_btnModificarMouseClicked

    private void btnModificarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseEntered
        if (fila != -1) {
            panelModificar.setBackground(new Color(153, 204, 255));
            btnModificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }//GEN-LAST:event_btnModificarMouseEntered

    private void btnModificarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseExited
        panelModificar.setBackground(new Color(255, 255, 255));
        btnModificar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnModificarMouseExited

    private void btnModificarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnModificarKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                crearCliente();
                break;

            case KeyEvent.VK_F2:
                reporteCliente();
                break;

            case KeyEvent.VK_F3:
                modificarCliente();
                break;

            case KeyEvent.VK_F4:
                bloquearCliente();
                break;

            case KeyEvent.VK_F5:
                eliminarCliente();
                break;

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

    private void btnReporteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReporteMouseClicked
        reporteCliente();

    }//GEN-LAST:event_btnReporteMouseClicked

    private void btnReporteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReporteMouseEntered
        if (fila != -1) {
            btnReporte.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panelReporte.setBackground(new Color(153, 204, 255));
        }
    }//GEN-LAST:event_btnReporteMouseEntered

    private void btnReporteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReporteMouseExited
        panelReporte.setBackground(new Color(255, 255, 255));
        btnReporte.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnReporteMouseExited

    private void btnReporteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnReporteKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                crearCliente();
                break;

            case KeyEvent.VK_F2:
                reporteCliente();
                break;

            case KeyEvent.VK_F3:
                modificarCliente();
                break;

            case KeyEvent.VK_F4:
                bloquearCliente();
                break;

            case KeyEvent.VK_F5:
                eliminarCliente();
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

    private void btnNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseClicked
        crearCliente();
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
            case KeyEvent.VK_F1:
                crearCliente();
                break;

            case KeyEvent.VK_F2:
                reporteCliente();
                break;

            case KeyEvent.VK_F3:
                modificarCliente();
                break;

            case KeyEvent.VK_F4:
                bloquearCliente();
                break;

            case KeyEvent.VK_F5:
                eliminarCliente();
                break;

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

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        eliminarCliente();
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void btnEliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarMouseEntered

    private void btnEliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseExited
        panelEliminar.setBackground(new Color(255, 255, 255));
        btnEliminar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnEliminarMouseExited

    private void btnEliminarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEliminarKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                crearCliente();
                break;

            case KeyEvent.VK_F2:
                reporteCliente();
                break;

            case KeyEvent.VK_F3:
                modificarCliente();
                break;

            case KeyEvent.VK_F4:
                bloquearCliente();
                break;

            case KeyEvent.VK_F5:
                eliminarCliente();
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

    private void btnEstadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstadoMouseClicked
        bloquearCliente();
    }//GEN-LAST:event_btnEstadoMouseClicked

    private void btnEstadoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstadoMouseEntered
        if (fila != -1) {
            panelEstado.setBackground(new Color(153, 204, 255));
            btnEstado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }//GEN-LAST:event_btnEstadoMouseEntered

    private void btnEstadoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstadoMouseExited
        panelEstado.setBackground(new Color(255, 255, 255));
        btnEstado.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnEstadoMouseExited

    private void btnEstadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEstadoKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                crearCliente();
                break;

            case KeyEvent.VK_F2:
                reporteCliente();
                break;

            case KeyEvent.VK_F3:
                modificarCliente();
                break;

            case KeyEvent.VK_F4:
                bloquearCliente();
                break;

            case KeyEvent.VK_F5:
                eliminarCliente();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_btnEstadoKeyPressed

    private void panelEstadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelEstadoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelEstadoMouseClicked

    private void panelEstadoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelEstadoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelEstadoMouseEntered

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
            java.util.logging.Logger.getLogger(vistaClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(vistaClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(vistaClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(vistaClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                vistaClientes dialog = new vistaClientes(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable TableClientes;
    private javax.swing.JLabel btnEliminar;
    private javax.swing.JLabel btnEstado;
    private javax.swing.JLabel btnModificar;
    private javax.swing.JLabel btnNuevo;
    private javax.swing.JLabel btnReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private textfield.TextField jTextField1;
    private javax.swing.JPanel panelEliminar;
    private javax.swing.JPanel panelEstado;
    private javax.swing.JPanel panelModificar;
    private javax.swing.JPanel panelNuevo;
    private javax.swing.JPanel panelReporte;
    // End of variables declaration//GEN-END:variables

    private void Seticon() {
    setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
}

    private static CellStyle getContabilidadCellStyle(Workbook workbook, DecimalFormat df) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat(df.format(0)));
        return style;
    }

    public void reporteCliente(String fechaInicial, String fechaFinal, int codigo) {
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
            Cliente cliente = client.BuscarPorCodigo(codigo);
            //File file = new File("corte" + fechaHoy + ".pdf");
            File file = new File("ReportesPDF\\reporteCliente " + cliente.getNombre() + " " + cliente.getApellido() + ".pdf");
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
            cell = new PdfPCell(new Phrase(configura.getNomnbre() + "\nREPORTE DE VENTAS\n" + cliente.getNombre().toUpperCase() + " " + cliente.getApellido().toUpperCase() + " DEL " + fechaInicialBien + " AL " + fechaFInalBien + "\nFECHA DE EMISIÓN " + DiagHoy, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            encabezado.addCell(img);
            doc.add(encabezado);

            PdfPTable tablapro = new PdfPTable(8);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] columnapro = new float[]{15f, 10f, 20f, 15f, 8f, 8f, 12f, 8f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1 = new PdfPCell(new Phrase("Fecha", letra));
            PdfPCell pro2 = new PdfPCell(new Phrase("Folio", letra));
            PdfPCell pro3 = new PdfPCell(new Phrase("Cliente", letra));
            PdfPCell pro4 = new PdfPCell(new Phrase("Concepto", letra));
            PdfPCell pro5 = new PdfPCell(new Phrase("Importe", letra));
            PdfPCell pro6 = new PdfPCell(new Phrase("Cobrado", letra));
            PdfPCell pro7 = new PdfPCell(new Phrase("Pago", letra));
            PdfPCell pro8 = new PdfPCell(new Phrase("Recibe", letra));

            pro1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro4.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro5.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro6.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro7.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro8.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            tablapro.addCell(pro5);
            tablapro.addCell(pro6);
            tablapro.addCell(pro7);
            tablapro.addCell(pro8);

            double totalVendido = 0;
            double totalCobrado = 0;

            List<Nota> ListaCl = notaDao.listarPorFecha(fechaInicial, fechaFinal);

            for (int i = 0; i < ListaCl.size(); i++) {
                if (ListaCl.get(i).getCodigoCLiente() == codigo) {

                    String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
                    cell = new PdfPCell();
                    cell = new PdfPCell(new Phrase(fechaF, letra));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(0);
                    tablapro.addCell(cell);

                    String folioF = String.valueOf(ListaCl.get(i).getFolio());
                    cell = new PdfPCell();
                    cell = new PdfPCell(new Phrase(folioF, letra));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(0);
                    tablapro.addCell(cell);

                    String clienteF = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
                    cell = new PdfPCell();
                    cell = new PdfPCell(new Phrase(clienteF, letra));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(0);
                    tablapro.addCell(cell);

                    List<Detalle> lsDetalle = notaDao.regresarDetalles(ListaCl.get(i).getFolio());
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
                    cell = new PdfPCell();
                    cell = new PdfPCell(new Phrase(conceptoF, letra));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(0);
                    tablapro.addCell(cell);

                    String totalF = "$" + formateador.format(ListaCl.get(i).getVentaTotal());
                    cell = new PdfPCell();
                    cell = new PdfPCell(new Phrase(totalF, letra));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setBorder(0);
                    tablapro.addCell(cell);
                    totalVendido = totalVendido + ListaCl.get(i).getVentaTotal();

                    List<anticipo> lsAnti = anticip.listarAnticipo();
                    double anticiposAcumulados = 0;
                    for (int j = 0; j < lsAnti.size(); j++) {
                        if (lsAnti.get(j).getFolio() == ListaCl.get(i).getFolio()) {
                            anticiposAcumulados = anticiposAcumulados + lsAnti.get(j).getCantidad();
                        }
                    }
                    double totalPagado = anticiposAcumulados + ListaCl.get(i).getAnticipo();
                    String cobradoF = "$" + formateador.format(totalPagado);;
                    cell = new PdfPCell();
                    cell = new PdfPCell(new Phrase(cobradoF, letra));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setBorder(0);
                    tablapro.addCell(cell);
                    totalCobrado = totalCobrado + totalPagado;

                    int pagoR = 0;
                    int pagoA = 0;
                    int pagoE = 0;
                    if (ListaCl.get(i).getAnticipo() > 0) {
                        pagoR = ListaCl.get(i).getFormaPago();
                    }
                    List<entrega> ListaEntregas = notaDao.listarNotasEntregas();
                    for (int k = 0; k < ListaEntregas.size(); k++) {
                        if (ListaEntregas.get(k).getIdnota() == ListaCl.get(i).getFolio()) {
                            pagoE = ListaEntregas.get(k).getFormaPago();
                        }
                    }

                    List<Integer> repetidos2 = new ArrayList<Integer>();
                    for (int j = 0; j < lsAnti.size(); j++) {
                        if (lsAnti.get(j).getFolio() == ListaCl.get(i).getFolio()) {
                            boolean rep = false;
                            for (int h = 0; h < repetidos2.size(); h++) {
                                if (repetidos2.get(h) != lsAnti.get(j).getFormaPago() && lsAnti.get(j).getFormaPago() != pagoE && lsAnti.get(j).getFormaPago() != pagoR) {

                                } else {
                                    rep = true;
                                }
                            }
                            if (rep == false) {
                                repetidos2.add(lsAnti.get(j).getFormaPago());
                            }

                        }
                    }
                    if (pagoR > 0) {
                        repetidos2.add(pagoR);
                    }
                    if (pagoE > 0) {
                        repetidos2.add(pagoE);
                    }
                    String formaPagoF = "";
                    for (int j = 0; j < repetidos2.size(); j++) {
                        formaPagoF += String.format("%0" + 2 + "d", Integer.valueOf(repetidos2.get(j))) + "-";
                    }
                    formaPagoF = formaPagoF.replaceFirst(".$", "");
                    cell = new PdfPCell();
                    cell = new PdfPCell(new Phrase(formaPagoF, letra));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(0);
                    tablapro.addCell(cell);

                    String recibeF = String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getIdRecibe()));
                    cell = new PdfPCell();
                    cell = new PdfPCell(new Phrase(recibeF, letra));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(0);
                    tablapro.addCell(cell);
                }
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
            cell = new PdfPCell(new Phrase("$" + formateador.format(totalVendido), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(totalCobrado), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
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

    public void reporteClienteExcel(String fechaInicial, String fechaFinal, int codigo) {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Gastos");
        String fechaInicialBien = fechaFormatoCorrecto(fechaInicial);
        String fechaFInalBien = fechaFormatoCorrecto(fechaFinal);
        Cliente cliente = client.BuscarPorCodigo(codigo);

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
            celdaTitulo.setCellValue("REPORTE DE VENTAS");
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 7));

            String tituloEncabezado = cliente.getNombre().toUpperCase() + " " + cliente.getApellido().toUpperCase() + " DEL " + fechaInicialBien + " AL " + fechaFInalBien;
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
            celdaTitulo.setCellValue("FECHA DE EMISION " + DiagHoy);
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 7));

            String[] cabecera = new String[]{"Fecha", "Folio", "Cliente", "Concepto", "Importe", "Cobrado", "Pago", "Recibe"};

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

            List<Nota> ListaCl = notaDao.listarPorFechaYCliente(fechaInicial, fechaFinal, codigo);
            int numCol = ListaCl.size();

            double totalVendido = 0;
            double totalCobrado = 0;

            for (int i = 0; i < ListaCl.size(); i++) {
                Row filaDatos = sheet.createRow(numFilaDatos);

                List<String> datosEscribir = new ArrayList<String>();

                String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
                datosEscribir.add(fechaF);

                String folioF = String.valueOf(ListaCl.get(i).getFolio());
                datosEscribir.add(folioF);

                String clienteF = ListaCl.get(i).getNombre() + " " + ListaCl.get(i).getApellido();
                datosEscribir.add(clienteF);

                List<Detalle> lsDetalle = notaDao.regresarDetalles(ListaCl.get(i).getFolio());
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
                datosEscribir.add(conceptoF);

                String totalF = "" + ListaCl.get(i).getVentaTotal();
                datosEscribir.add(totalF);

                List<anticipo> lsAnti = anticip.listarAnticipo();
                double anticiposAcumulados = 0;
                for (int j = 0; j < lsAnti.size(); j++) {
                    if (lsAnti.get(j).getFolio() == ListaCl.get(i).getFolio()) {
                        anticiposAcumulados = anticiposAcumulados + lsAnti.get(j).getCantidad();
                    }
                }
                double totalPagado = anticiposAcumulados + ListaCl.get(i).getAnticipo();
                String cobradoF = "" + totalPagado;
                datosEscribir.add(cobradoF);

                int pagoR = 0;
                int pagoA = 0;
                int pagoE = 0;
                if (ListaCl.get(i).getAnticipo() > 0) {
                    pagoR = ListaCl.get(i).getFormaPago();
                }
                List<entrega> ListaEntregas = notaDao.listarNotasEntregas();
                for (int k = 0; k < ListaEntregas.size(); k++) {
                    if (ListaEntregas.get(k).getIdnota() == ListaCl.get(i).getFolio()) {
                        pagoE = ListaEntregas.get(k).getFormaPago();
                    }
                }

                List<Integer> repetidos2 = new ArrayList<Integer>();
                for (int j = 0; j < lsAnti.size(); j++) {
                    if (lsAnti.get(j).getFolio() == ListaCl.get(i).getFolio()) {
                        boolean rep = false;
                        for (int h = 0; h < repetidos2.size(); h++) {
                            if (repetidos2.get(h) != lsAnti.get(j).getFormaPago() && lsAnti.get(j).getFormaPago() != pagoE && lsAnti.get(j).getFormaPago() != pagoR) {

                            } else {
                                rep = true;
                            }
                        }
                        if (rep == false) {
                            repetidos2.add(lsAnti.get(j).getFormaPago());
                        }

                    }
                }
                if (pagoR > 0) {
                    repetidos2.add(pagoR);
                }
                if (pagoE > 0) {
                    repetidos2.add(pagoE);
                }
                String formaPagoF = "";
                for (int j = 0; j < repetidos2.size(); j++) {
                    formaPagoF += String.format("%0" + 2 + "d", Integer.valueOf(repetidos2.get(j))) + "-";
                }
                formaPagoF = formaPagoF.replaceFirst(".$", "");
                datosEscribir.add(formaPagoF);

                String recibeF = String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getIdRecibe()));
                datosEscribir.add(recibeF);

                for (int j = 0; j < datosEscribir.size(); j++) {
                    if (j == 4 || j == 5) {
                        Cell celdaDatos = filaDatos.createCell(j, CellType.NUMERIC);
                        celdaDatos.setCellValue(Double.parseDouble(datosEscribir.get(j)));
                        celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));
                    } else {
                        Cell celdaDatos = filaDatos.createCell(j);
                        celdaDatos.setCellStyle(datosEstilo);
                        celdaDatos.setCellValue(datosEscribir.get(j));
                    }
                }
                totalCobrado = totalCobrado + totalPagado;
                totalVendido = totalVendido + ListaCl.get(i).getVentaTotal();
                numFilaDatos++;

            }

            Row filaDatos = sheet.createRow(numFilaDatos);
            Cell celdaDatos = filaDatos.createCell(3);
            celdaDatos.setCellStyle(datosEstilo);
            celdaDatos.setCellValue("TOTAL");

            celdaDatos = filaDatos.createCell(4, CellType.NUMERIC);
            celdaDatos.setCellValue(totalVendido);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            celdaDatos = filaDatos.createCell(5, CellType.NUMERIC);
            celdaDatos.setCellValue(totalCobrado);
            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));

            //reajustamos al tamaño
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);

            //hacemos zoom
            sheet.setZoom(150);

            FileOutputStream fileOut = new FileOutputStream("ReportesExcel\\reporteCliente.xlsx");
            book.write(fileOut);
            fileOut.close();
            File file = new File("ReportesExcel\\reporteCliente.xlsx");
            Desktop.getDesktop().open(file);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(vistaPrecios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(vistaPrecios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
