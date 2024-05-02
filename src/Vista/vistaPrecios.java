/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Detalle;
import Modelo.GastosDao;
import Modelo.NotaDao;
import Modelo.PrecioDao;
import Modelo.Precios;
import Modelo.anticipoDao;
import Modelo.config;
import Modelo.corteDiaDao;
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
import javaswingdev.Notification;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
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

public class vistaPrecios extends javax.swing.JDialog {

    corteDiaDao corte = new corteDiaDao();
    DefaultTableModel modelo = new DefaultTableModel();
    NotaDao notaDao = new NotaDao();
    static PrecioDao precio = new PrecioDao();
    anticipoDao antic = new anticipoDao();
    static GastosDao gastos = new GastosDao();
    establecerFecha establecer = new establecerFecha();

    int fila = -1;
    String fecha, hora;
    static DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
    static DecimalFormat df = new DecimalFormat("$ #,##0.00;($ #,##0.00)");

    public vistaPrecios(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(200, 50);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Software Lavandería - Precios");
        Seticon();
        TablePromociones.setBackground(Color.WHITE);
        TablePromociones.setSelectionForeground(Color.BLACK);

        jScrollPane7.getViewport().setBackground(new Color(204, 204, 204));
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        DefaultTableCellRenderer tcrDerecha = new DefaultTableCellRenderer();

        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        TablePromociones.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TablePromociones.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TablePromociones.getColumnModel().getColumn(2).setCellRenderer(tcrDerecha);
        TablePromociones.getColumnModel().getColumn(3).setCellRenderer(tcr);
        TablePromociones.getColumnModel().getColumn(4).setCellRenderer(tcr);
        ((DefaultTableCellRenderer) TablePromociones.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
        listarPromociones();
        establecerFecha();

    }

    public void listarPromociones() {
        List<Precios> ListaCl = precio.getPrecios();
        modelo = (DefaultTableModel) TablePromociones.getModel();
        Object[] ob = new Object[6];
        for (int i = 0; i < ListaCl.size(); i++) {
            ob[0] = ListaCl.get(i).getId();
            ob[1] = ListaCl.get(i).getNombre();
            ob[2] = "$" + formateador.format(ListaCl.get(i).getPrecioU());
            DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
            modelocentrar.setHorizontalAlignment(SwingConstants.RIGHT);
            TablePromociones.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
            if (0 == ListaCl.get(i).getEstado()) {
                ob[5] = StatusType.ACTIVE;
            } else {
                ob[5] = StatusType.INACTIVE;
            }
            ob[4] = ListaCl.get(i).getDescripcion();
            Date diaActual = StringADate(ListaCl.get(i).getFechaCreacion());
            DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
            String fechaUsar;
            Date fechaNueva;
            Calendar c = Calendar.getInstance();
            c.setTime(diaActual);
            fechaUsar = cam.format(diaActual);
            ob[3] = fechaUsar;
            modelo.addRow(ob);
        }
        TablePromociones.setModel(modelo);
    }

    public static java.util.Date StringADate(String cambiar) { //Este metodo te transforma un String a date (dia)
        SimpleDateFormat formato_del_Texto = new SimpleDateFormat("yyyy-MM-dd"); //El formato del string
        Date fechaE = null;
        try {
            fechaE = formato_del_Texto.parse(cambiar);
            return fechaE; //retornamos la fecha
        } catch (ParseException ex) {
            return null;
        }
    }

    public void establecerFecha() { //Establecemos la fecha a usar, esto se hace ya que hasta que no se cierre caja, no se puede avanzar de dia
        List<String> datos = establecer.establecerFecha();
        fecha = datos.get(0);
        hora = datos.get(1);
    }

    public void LimpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    public void eventoF1() {
        CrearModificarPrecio cc = new CrearModificarPrecio(new javax.swing.JFrame(), true);
        Precios cp = new Precios();
        cc.vaciarDatos(fecha, cp, true);
        cc.setVisible(true);
        if (cc.accionCompletada == true) {
            LimpiarTabla();
            listarPromociones();
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Precio registrado correctamente");
            panel.showNotification();
        }
    }

    public void eventoF2() {

        elegirPdfExcel cc = new elegirPdfExcel(new javax.swing.JFrame(), true);
        cc.setVisible(true);
        if (cc.accionRealizada == true) {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Generando reporte");
            panel.showNotification();
            if (cc.tipo == true) {
                reportePrecios();
            } else {
                reportePreciosExcel();
            }
        }
    }

    public void eventoF3() {
        if (fila != -1) {
            CrearModificarPrecio cc = new CrearModificarPrecio(new javax.swing.JFrame(), true);
            List<Precios> ls = precio.getPrecios();
            for (int i = 0; i < ls.size(); i++) {
                if (ls.get(i).getId() == Integer.parseInt(TablePromociones.getValueAt(fila, 0).toString())) {
                    cc.vaciarDatos(fecha, ls.get(i), false);
                    cc.setVisible(true);
                    if (cc.accionCompletada == true) {
                        LimpiarTabla();
                        listarPromociones();
                        Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Precio modificado correctamente");
                        panel.showNotification();
                    }
                }
            }
        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Seleccione una fila antes");
            panel.showNotification();
        }
    }

    public void eventoF4() {
        if (fila != 1) {
            ContraseñaConUsuario cc = new ContraseñaConUsuario(new javax.swing.JFrame(), true);
            cc.setVisible(true);
            if (cc.contraseñaAceptada == true) {
                Precios aCsmbiar = precio.BuscarPorCodigo(Integer.parseInt(TablePromociones.getValueAt(fila, 0).toString()));
                if (aCsmbiar.getEstado() == 0) {
                    precio.modificarEstado(Integer.parseInt(TablePromociones.getValueAt(fila, 0).toString()), true);
                } else {
                    precio.modificarEstado(Integer.parseInt(TablePromociones.getValueAt(fila, 0).toString()), false);
                }
                LimpiarTabla();
                listarPromociones();
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Estado de precio modificado correctamente");
                panel.showNotification();
            }
        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Seleccione una fila antes");
            panel.showNotification();
        }
    }

    public void eventoF5() {
        if (fila != -1) {
            List<Detalle> lsD = notaDao.listarDetalles();
            boolean tieneNotas = false;
            for (int i = 0; i < lsD.size(); i++) {
                if (lsD.get(i).getCodigoPrecio() == Integer.parseInt(TablePromociones.getValueAt(fila, 0).toString())) {
                    tieneNotas = true;
                }
            }
            if (tieneNotas == false) {//si se va a eliminar
                ContraseñaConUsuario cc = new ContraseñaConUsuario(new javax.swing.JFrame(), true);
                cc.setVisible(true);
                if (cc.contraseñaAceptada == true) {
                    precio.EliminarPromo(Integer.parseInt(TablePromociones.getValueAt(fila, 0).toString()));
                    LimpiarTabla();
                    listarPromociones();
                    Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Precio eliminado correctamente");
                    panel.showNotification();
                }
            } else {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Ya cuenta con notas registradas");
                panel.showNotification();
            }

        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Seleccione una fila antes");
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
        jScrollPane7 = new javax.swing.JScrollPane();
        TablePromociones = new javax.swing.JTable();
        panelEstado = new javax.swing.JPanel();
        btnEstado = new javax.swing.JLabel();
        panelEliminar = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JLabel();
        panelNuevo = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JLabel();
        panelReporte = new javax.swing.JPanel();
        btnReporte = new javax.swing.JLabel();
        panelModificar = new javax.swing.JPanel();
        btnModificar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Precios");

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
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 440, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 620, 60));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablePromociones = new Table2();
        TablePromociones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Concepto", "P/Unitario", "Creación", "Descripción", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablePromociones.setToolTipText("Haz doble click para poder hacer modificaciones al precio seleccionado");
        TablePromociones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablePromocionesMouseClicked(evt);
            }
        });
        TablePromociones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablePromocionesKeyPressed(evt);
            }
        });
        jScrollPane7.setViewportView(TablePromociones);
        if (TablePromociones.getColumnModel().getColumnCount() > 0) {
            TablePromociones.getColumnModel().getColumn(0).setResizable(false);
            TablePromociones.getColumnModel().getColumn(0).setPreferredWidth(4);
            TablePromociones.getColumnModel().getColumn(1).setResizable(false);
            TablePromociones.getColumnModel().getColumn(1).setPreferredWidth(160);
            TablePromociones.getColumnModel().getColumn(2).setResizable(false);
            TablePromociones.getColumnModel().getColumn(2).setPreferredWidth(15);
            TablePromociones.getColumnModel().getColumn(3).setResizable(false);
            TablePromociones.getColumnModel().getColumn(3).setPreferredWidth(20);
            TablePromociones.getColumnModel().getColumn(4).setResizable(false);
            TablePromociones.getColumnModel().getColumn(4).setPreferredWidth(120);
            TablePromociones.getColumnModel().getColumn(5).setResizable(false);
            TablePromociones.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        jPanel4.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 7, 920, 430));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 940, 450));

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
        btnEliminar.setToolTipText("F5 - Eliminar precio");
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
        btnNuevo.setToolTipText("F1 - Nuevo precio");
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
        btnModificar.setToolTipText("F3 - Modificar precio");
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

    private void TablePromocionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablePromocionesMouseClicked
        if (evt.getClickCount() == 2) {
            fila = TablePromociones.rowAtPoint(evt.getPoint());
        }
    }//GEN-LAST:event_TablePromocionesMouseClicked

    private void TablePromocionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablePromocionesKeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3:
                eventoF3();
                break;

            case KeyEvent.VK_F4:
                eventoF4();
                break;

            case KeyEvent.VK_F5:
                eventoF5();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_TablePromocionesKeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        int codigo = evt.getKeyCode();
        switch (codigo) {
            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3:
                eventoF3();
                break;

            case KeyEvent.VK_F4:
                eventoF4();
                break;

            case KeyEvent.VK_F5:
                eventoF5();
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
                Precios alistar = precio.BuscarPorCodigo(Integer.parseInt(jTextField1.getText()));
                if (alistar.getId() == 0) {
                    modelo = (DefaultTableModel) TablePromociones.getModel();
                    TablePromociones.setModel(modelo);
                } else {
                    modelo = (DefaultTableModel) TablePromociones.getModel();
                    Object[] ob = new Object[6];
                    ob[0] = alistar.getId();
                    ob[1] = alistar.getNombre();
                    ob[2] = String.format("%.2f", alistar.getPrecioU());
                    DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
                    modelocentrar.setHorizontalAlignment(SwingConstants.RIGHT);
                    TablePromociones.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
                    if (0 == alistar.getEstado()) {
                        ob[5] = StatusType.ACTIVE;
                    } else {
                        ob[5] = StatusType.INACTIVE;
                    }
                    ob[4] = alistar.getDescripcion();
                    Date diaActual = StringADate(alistar.getFechaCreacion());
                    DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
                    String fechaUsar;
                    Date fechaNueva;
                    Calendar c = Calendar.getInstance();
                    c.setTime(diaActual);
                    fechaUsar = cam.format(diaActual);
                    ob[3] = fechaUsar;
                    modelo.addRow(ob);

                    TablePromociones.setModel(modelo);
                }
            } else {
                List<Precios> ListaCl = precio.buscarLetra(jTextField1.getText());

                modelo = (DefaultTableModel) TablePromociones.getModel();
                Object[] ob = new Object[6];
                for (int i = 0; i < ListaCl.size(); i++) {
                    ob[0] = ListaCl.get(i).getId();
                    ob[1] = ListaCl.get(i).getNombre();
                    ob[2] = "$" + formateador.format(ListaCl.get(i).getPrecioU());
                    DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
                    modelocentrar.setHorizontalAlignment(SwingConstants.RIGHT);
                    TablePromociones.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
                    if (0 == ListaCl.get(i).getEstado()) {
                        ob[5] = StatusType.ACTIVE;
                    } else {
                        ob[5] = StatusType.INACTIVE;
                    }
                    ob[4] = ListaCl.get(i).getDescripcion();
                    Date diaActual = StringADate(ListaCl.get(i).getFechaCreacion());
                    DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
                    String fechaUsar;
                    Date fechaNueva;
                    Calendar c = Calendar.getInstance();
                    c.setTime(diaActual);
                    fechaUsar = cam.format(diaActual);
                    ob[3] = fechaUsar;
                    modelo.addRow(ob);
                }
                TablePromociones.setModel(modelo);
            }
        } else {
            listarPromociones();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped

    }//GEN-LAST:event_jTextField1KeyTyped

    private void btnEstadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstadoMouseClicked
        eventoF4();
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
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3:
                eventoF3();
                break;

            case KeyEvent.VK_F4:
                eventoF4();
                break;

            case KeyEvent.VK_F5:
                eventoF5();
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

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        eventoF5();
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
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3:
                eventoF3();
                break;

            case KeyEvent.VK_F4:
                eventoF4();
                break;

            case KeyEvent.VK_F5:
                eventoF5();
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

    private void btnNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseClicked
        eventoF1();
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
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3:
                eventoF3();
                break;

            case KeyEvent.VK_F4:
                eventoF4();
                break;

            case KeyEvent.VK_F5:
                eventoF5();
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
            case KeyEvent.VK_F1:
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3:
                eventoF3();
                break;

            case KeyEvent.VK_F4:
                eventoF4();
                break;

            case KeyEvent.VK_F5:
                eventoF5();
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

    private void btnModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseClicked
        eventoF3();
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
                eventoF1();
                break;

            case KeyEvent.VK_F2:
                eventoF2();
                break;

            case KeyEvent.VK_F3:
                eventoF3();
                break;

            case KeyEvent.VK_F4:
                eventoF4();
                break;

            case KeyEvent.VK_F5:
                eventoF5();
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
            java.util.logging.Logger.getLogger(vistaPrecios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(vistaPrecios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(vistaPrecios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(vistaPrecios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                vistaPrecios dialog = new vistaPrecios(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable TablePromociones;
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
    private javax.swing.JScrollPane jScrollPane7;
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

    public static String fechaFormatoCorrecto(String fechaHoy) {
        Date diaActual = StringADate(fechaHoy);
        DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
        String fechaUsar;
        Date fechaNueva;
        Calendar c = Calendar.getInstance();
        c.setTime(diaActual);
        fechaUsar = cam.format(diaActual);
        return fechaUsar;
    }

    private static CellStyle getContabilidadCellStyle(Workbook workbook, DecimalFormat df) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat(df.format(0)));
        return style;
    }

    public static void reportePrecios() {
        try {
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
            File file = new File("ReportesPDF\\reportePrecios.pdf");
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
            cell = new PdfPCell(new Phrase(configura.getNomnbre() + "\nCATÁLOGO DE CONCEPTOS Y SERVICIOS\nFECHA DE EMISIÓN " + DiagHoy, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            encabezado.addCell(img);
            doc.add(encabezado);

            PdfPTable tablapro = new PdfPTable(6);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] columnapro = new float[]{12f, 8f, 20f, 25f, 12f, 12f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1 = new PdfPCell(new Phrase("Fecha", letra));
            PdfPCell pro2 = new PdfPCell(new Phrase("Cóodigo", letra));
            PdfPCell pro3 = new PdfPCell(new Phrase("Concepto", letra));
            PdfPCell pro4 = new PdfPCell(new Phrase("Descripción", letra));
            PdfPCell pro5 = new PdfPCell(new Phrase("P/Unitario", letra));
            PdfPCell pro6 = new PdfPCell(new Phrase("Estado", letra));

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

            List<Precios> ListaCl = precio.getPrecios();

            for (int i = 0; i < ListaCl.size(); i++) {

                String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFechaCreacion());
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(fechaF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String folioF = String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getId()));
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(folioF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String clienteF = ListaCl.get(i).getNombre();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(clienteF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String conceptoF = ListaCl.get(i).getDescripcion();
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(conceptoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String totalF = "$" + formateador.format(ListaCl.get(i).getPrecioU());;
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(totalF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);

                String estadoF = "";
                if (0 == ListaCl.get(i).getEstado()) {
                    estadoF = "Habilitada";
                } else {
                    estadoF = "Inhabilitada";
                }
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(estadoF, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

            }

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
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe cerrar el documento antes");
            panel.showNotification();
        }
    }

    public static void reportePreciosExcel() {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Precios");

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
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 5));

            filaTitulo = sheet.createRow(2);
            celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("CATÁLOGO DE CONCEPTOS Y SERVICIOS");
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 5));

            filaTitulo = sheet.createRow(3);
            celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            //celdaTitulo.setCellValue("RESUMEN"); 
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 5));

            filaTitulo = sheet.createRow(4);
            celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("FECHA DE EMISIÓN " + DiagHoy);
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 5));

            String[] cabecera = new String[]{"Fecha", "Código", "Concepto", "Descripción", "P/Unitario", "Estado"};

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

            List<Precios> ListaCl = precio.getPrecios();

            int numCol = ListaCl.size();

            for (int i = 0; i < ListaCl.size(); i++) {
                Row filaDatos = sheet.createRow(numFilaDatos);

                List<String> datosEscribir = new ArrayList<String>();
                String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFechaCreacion());
                datosEscribir.add(fechaF);
                String folioF = String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getId()));
                datosEscribir.add(folioF);
                String clienteF = ListaCl.get(i).getNombre();
                datosEscribir.add(clienteF);
                String conceptoF = ListaCl.get(i).getDescripcion();
                datosEscribir.add(conceptoF);
                String totalF = "" + ListaCl.get(i).getPrecioU();
                datosEscribir.add(totalF);
                String estadoF = "";
                if (0 == ListaCl.get(i).getEstado()) {
                    estadoF = "Habilitada";
                } else {
                    estadoF = "Inhabilitada";
                }
                datosEscribir.add(estadoF);

                for (int j = 0; j < datosEscribir.size(); j++) {
                    if (j == 4) {
                        Cell celdaDatos = filaDatos.createCell(j, CellType.NUMERIC);
                        celdaDatos.setCellValue(Double.parseDouble(datosEscribir.get(j)));
                        celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));
                    } else {
                        Cell celdaDatos = filaDatos.createCell(j);
                        celdaDatos.setCellStyle(datosEstilo);
                        celdaDatos.setCellValue(datosEscribir.get(j));
                    }
                }

                numFilaDatos++;

            }

            //reajustamos al tamaño
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);

            //hacemos zoom
            sheet.setZoom(150);

            FileOutputStream fileOut = new FileOutputStream("ReportesExcel\\reportePrecios.xlsx");
            book.write(fileOut);
            fileOut.close();
            File file = new File("ReportesExcel\\reportePrecios.xlsx");
            Desktop.getDesktop().open(file);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(vistaPrecios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(vistaPrecios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
