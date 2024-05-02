/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;


import Modelo.Cliente;
import Modelo.ClienteDao;
import Modelo.Detalle;
import Modelo.Empleados;
import Modelo.EmpleadosDao;
import Modelo.GastosDao;
import Modelo.Nota;
import Modelo.NotaDao;
import Modelo.anticipo;
import Modelo.anticipoDao;
import Modelo.config;
import Modelo.corteDiaDao;
import Modelo.entrega;
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
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
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

/**
 *
 * @author Jonathan Gil
 */
public class DatosReporte extends javax.swing.JDialog {

            EmpleadosDao emple = new EmpleadosDao();
         ClienteDao client = new ClienteDao();
     anticipoDao anticip = new anticipoDao();
          GastosDao gastos = new GastosDao();
     corteDiaDao corte = new corteDiaDao();
      DefaultTableModel modelo = new DefaultTableModel();
     NotaDao notaDao = new NotaDao();    
     List<Cliente> lsClientes = client.ListarCliente();
     List<Empleados> lsEmpleados = emple.listarEmpleados(); 
     boolean ventas=false;
     boolean entregas=false;
     boolean canceladas=false;
     boolean abonos = false;
         DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
             DecimalFormat df = new DecimalFormat("$ #,##0.00;($ #,##0.00)");

     
    
    public DatosReporte(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        txtPDF.setBackground(Color.WHITE);
        txtExcel.setBackground(Color.WHITE);
        ClientesTodos.setSelected(true);
        EmpleadosTodos.setSelected(true);
        ClientesTodos.setBackground(Color.WHITE);
        ClientesUno.setBackground(Color.WHITE);
        EmpleadosTodos.setBackground(Color.WHITE);
        EmpleadosUno.setBackground(Color.WHITE);
        txtEmpleado.setBackground(Color.WHITE);
         txtCliente.setBackground(Color.WHITE);
         txtEmpleado.setEnabled(false);
         txtCliente.setEnabled(false);
         this.setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Seticon();
                
        listarClientes();
        listarEmpleados();
         SetImagenLabel(cargando, "/Imagenes/loading.gif");       
         cargando.setVisible(false);
    }
    
     public void SetImagenLabel(JLabel labelName, String root){
        labelName.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(root)).getImage().getScaledInstance(labelName.getWidth(), labelName.getHeight(), java.awt.Image.SCALE_DEFAULT)));
    }
     
      public void proceso(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoginN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void paraVentas(){
        ventas=true;
        this.setTitle("Software Lavandería - Reporte de ventas");
        abonos=false;
    }
    
    public void paraAbonos(){
        abonos=true;
        this.setTitle("Software Lavandería - Reporte de abonos");
    }
    
     public void paraEntregas(){
        entregas=true;
        this.setTitle("Software Lavandería - Reporte de entregas");
    }
     
      public void paraCanceladas(){
        canceladas=true;
        this.setTitle("Software Lavandería - Reporte de notas canceladas");
    }
    
     public void listarEmpleados() {
        //Se obtienen los empleados de la base de datos (para generar nota
        txtEmpleado.removeAllItems();//Se borran los datos del ComboBox para evitar que se sobreencimen
        for (int i = 0; i < lsEmpleados.size(); i++) {//vaciamos los datos
            if(lsEmpleados.get(i).getEstado()==1)
            txtEmpleado.addItem(String.format("%0" + 2 + "d", Integer.valueOf(lsEmpleados.get(i).getId()))+"-"+lsEmpleados.get(i).getNombre());
        }
    } 
     
      public void listarClientes() {
        //Se obtienen los empleados de la base de datos (para generar nota
        txtCliente.removeAllItems();//Se borran los datos del ComboBox para evitar que se sobreencimen
        for (int i = 0; i < lsClientes.size(); i++) {//vaciamos los datos
            if(lsClientes.get(i).getEstado()==1)
            txtCliente.addItem(String.format("%0" + 2 + "d", Integer.valueOf(lsClientes.get(i).getId()))+"-"+lsClientes.get(i).getNombre()+" "+lsClientes.get(i).getApellido());
        }
    }
      
      public void eventoEnter(){
        new Thread(){
        public void run(){
                cargando.setVisible(true);
                proceso();
                if (jDateChooser4.getDate() != null && (txtPDF.isSelected()==true || txtExcel.isSelected()==true) && (ClientesTodos.isSelected()==true || ClientesUno.isSelected()==true) && (EmpleadosTodos.isSelected()==true || EmpleadosUno.isSelected()==true)) {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Generando su reporte");
                               panel.showNotification();
                      
         }else{
               Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Rellene todos los campos");
                               panel.showNotification();
           }
                        cargando.setVisible(false);
}
        }.start();
           
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooser4 = new com.raven.datechooser.DateChooser();
        jDateChooser5 = new com.raven.datechooser.DateChooser();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        ClientesTodos = new javax.swing.JCheckBox();
        ClientesUno = new javax.swing.JCheckBox();
        jLabel28 = new javax.swing.JLabel();
        EmpleadosTodos = new javax.swing.JCheckBox();
        jLabel29 = new javax.swing.JLabel();
        EmpleadosUno = new javax.swing.JCheckBox();
        jLabel30 = new javax.swing.JLabel();
        txtPDF = new javax.swing.JCheckBox();
        txtExcel = new javax.swing.JCheckBox();
        cargando = new javax.swing.JLabel();
        txtEmpleado = new combobox.Combobox();
        txtCliente = new combobox.Combobox();
        segundaFecha = new textfield.TextField();
        primeraFecha = new textfield.TextField();
        panelGuardar = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JLabel();
        panelCancelar = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JLabel();

        jDateChooser4.setForeground(new java.awt.Color(51, 102, 255));
        jDateChooser4.setTextRefernce(primeraFecha);
        jDateChooser4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser4PropertyChange(evt);
            }
        });
        jDateChooser4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jDateChooser4KeyPressed(evt);
            }
        });

        jDateChooser5.setForeground(new java.awt.Color(51, 102, 255));
        jDateChooser5.setTextRefernce(segundaFecha);
        jDateChooser5.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser5PropertyChange(evt);
            }
        });
        jDateChooser5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jDateChooser5KeyPressed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 255));
        jLabel8.setText("¿Está seguro de ejecutar esta acción? ");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 255));
        jLabel9.setText("Generar reporte");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, -1, -1));

        ClientesTodos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ClientesTodos.setText("Todos los clientes");
        ClientesTodos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ClientesTodosMouseClicked(evt);
            }
        });
        ClientesTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClientesTodosActionPerformed(evt);
            }
        });
        ClientesTodos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ClientesTodosKeyPressed(evt);
            }
        });
        jPanel4.add(ClientesTodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        ClientesUno.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ClientesUno.setText("Cliente en específico");
        ClientesUno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ClientesUnoMouseClicked(evt);
            }
        });
        ClientesUno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClientesUnoActionPerformed(evt);
            }
        });
        ClientesUno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ClientesUnoKeyPressed(evt);
            }
        });
        jPanel4.add(ClientesUno, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setText("Elija el tipo de reporte");
        jLabel28.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel4.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, 150, 20));

        EmpleadosTodos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        EmpleadosTodos.setText("Todos los empleados");
        EmpleadosTodos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EmpleadosTodosMouseClicked(evt);
            }
        });
        EmpleadosTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmpleadosTodosActionPerformed(evt);
            }
        });
        EmpleadosTodos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EmpleadosTodosKeyPressed(evt);
            }
        });
        jPanel4.add(EmpleadosTodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, -1, -1));

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setText("Empleados");
        jLabel29.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel4.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 70, 20));

        EmpleadosUno.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        EmpleadosUno.setText("Empleado en específico");
        EmpleadosUno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EmpleadosUnoMouseClicked(evt);
            }
        });
        EmpleadosUno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmpleadosUnoActionPerformed(evt);
            }
        });
        EmpleadosUno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EmpleadosUnoKeyPressed(evt);
            }
        });
        jPanel4.add(EmpleadosUno, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, -1, -1));

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setText("Clientes");
        jLabel30.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel4.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 50, 20));

        txtPDF.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPDF.setText("Reporte pdf");
        txtPDF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPDFMouseClicked(evt);
            }
        });
        txtPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPDFActionPerformed(evt);
            }
        });
        txtPDF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPDFKeyPressed(evt);
            }
        });
        jPanel4.add(txtPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, -1, -1));

        txtExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtExcel.setText("Reporte Excel");
        txtExcel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtExcelMouseClicked(evt);
            }
        });
        txtExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExcelActionPerformed(evt);
            }
        });
        txtExcel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExcelKeyPressed(evt);
            }
        });
        jPanel4.add(txtExcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, -1, -1));

        cargando.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.add(cargando, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 400, 80, 50));

        txtEmpleado.setToolTipText("");
        txtEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtEmpleado.setLabeText("Elige empleado");
        txtEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpleadoActionPerformed(evt);
            }
        });
        txtEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpleadoKeyPressed(evt);
            }
        });
        jPanel4.add(txtEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 150, -1));

        txtCliente.setToolTipText("");
        txtCliente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCliente.setLabeText("Elige cliente");
        txtCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteActionPerformed(evt);
            }
        });
        txtCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClienteKeyPressed(evt);
            }
        });
        jPanel4.add(txtCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 140, -1));

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
        jPanel4.add(segundaFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 130, 40));

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
        jPanel4.add(primeraFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 130, 40));

        panelGuardar.setBackground(new java.awt.Color(255, 255, 255));
        panelGuardar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelGuardarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelGuardarMouseEntered(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(153, 204, 255));
        btnGuardar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar nota 2.png"))); // NOI18N
        btnGuardar.setToolTipText("ENTER - Realizar acción");
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardarMouseExited(evt);
            }
        });
        btnGuardar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGuardarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelGuardarLayout = new javax.swing.GroupLayout(panelGuardar);
        panelGuardar.setLayout(panelGuardarLayout);
        panelGuardarLayout.setHorizontalGroup(
            panelGuardarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelGuardarLayout.setVerticalGroup(
            panelGuardarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel4.add(panelGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 50, 50));

        panelCancelar.setBackground(new java.awt.Color(255, 255, 255));
        panelCancelar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelCancelarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelCancelarMouseEntered(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imagen5.png"))); // NOI18N
        btnCancelar.setToolTipText("ESCAPE - Cancelar acción y salir");
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelarMouseExited(evt);
            }
        });
        btnCancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCancelarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelCancelarLayout = new javax.swing.GroupLayout(panelCancelar);
        panelCancelar.setLayout(panelCancelarLayout);
        panelCancelarLayout.setHorizontalGroup(
            panelCancelarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelCancelarLayout.setVerticalGroup(
            panelCancelarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel4.add(panelCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 330, 50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ClientesTodosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClientesTodosMouseClicked
       if (ClientesTodos.isSelected() == true) {
            ClientesUno.setSelected(false);
            txtCliente.setEnabled(false);
        }
    }//GEN-LAST:event_ClientesTodosMouseClicked

    private void ClientesTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClientesTodosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ClientesTodosActionPerformed

    private void ClientesTodosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClientesTodosKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;


            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_ClientesTodosKeyPressed

    private void ClientesUnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClientesUnoMouseClicked
      if (ClientesUno.isSelected() == true) {
            ClientesTodos.setSelected(false);
            txtCliente.setEnabled(true);
            
        }
    }//GEN-LAST:event_ClientesUnoMouseClicked

    private void ClientesUnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClientesUnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ClientesUnoActionPerformed

    private void ClientesUnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClientesUnoKeyPressed
       int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;


            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_ClientesUnoKeyPressed

    private void EmpleadosTodosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmpleadosTodosMouseClicked
      if (EmpleadosTodos.isSelected() == true) {
            EmpleadosUno.setSelected(false);
            txtEmpleado.setEnabled(false);
        }
    }//GEN-LAST:event_EmpleadosTodosMouseClicked

    private void EmpleadosTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmpleadosTodosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmpleadosTodosActionPerformed

    private void EmpleadosTodosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmpleadosTodosKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;


            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_EmpleadosTodosKeyPressed

    private void EmpleadosUnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmpleadosUnoMouseClicked
      if (EmpleadosUno.isSelected() == true) {
            EmpleadosTodos.setSelected(false);
            txtEmpleado.setEnabled(true);
        }
    }//GEN-LAST:event_EmpleadosUnoMouseClicked

    private void EmpleadosUnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmpleadosUnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmpleadosUnoActionPerformed

    private void EmpleadosUnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmpleadosUnoKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;


            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_EmpleadosUnoKeyPressed

    private void txtPDFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPDFMouseClicked
        if (txtPDF.isSelected() == true) {
            txtExcel.setSelected(false);
        }
    }//GEN-LAST:event_txtPDFMouseClicked

    private void txtPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPDFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPDFActionPerformed

    private void txtPDFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPDFKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;

            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_txtPDFKeyPressed

    private void txtExcelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtExcelMouseClicked
        if (txtExcel.isSelected() == true) {
            txtPDF.setSelected(false);
        }
    }//GEN-LAST:event_txtExcelMouseClicked

    private void txtExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExcelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtExcelActionPerformed

    private void txtExcelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExcelKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;

            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_txtExcelKeyPressed

    private void jDateChooser4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser4PropertyChange
        jDateChooser5.setMinSelectableDate(jDateChooser4.getDate());
        jDateChooser5.setSelectedDate(jDateChooser4.getDate());
    }//GEN-LAST:event_jDateChooser4PropertyChange

    private void jDateChooser4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser4KeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;

            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_jDateChooser4KeyPressed

    private void jDateChooser5PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser5PropertyChange

    }//GEN-LAST:event_jDateChooser5PropertyChange

    private void jDateChooser5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser5KeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;

            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_jDateChooser5KeyPressed

    private void txtEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpleadoActionPerformed

    private void txtEmpleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpleadoKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;


            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_txtEmpleadoKeyPressed

    private void txtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClienteActionPerformed

    private void txtClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClienteKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;


            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_txtClienteKeyPressed

    private void primeraFechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_primeraFechaKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
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
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;


            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_segundaFechaKeyPressed

    private void segundaFechaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_segundaFechaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_segundaFechaKeyReleased

    private void btnGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseClicked
        eventoEnter();
    }//GEN-LAST:event_btnGuardarMouseClicked

    private void btnGuardarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseEntered
        panelGuardar.setBackground(new Color(153,204,255));
    }//GEN-LAST:event_btnGuardarMouseEntered

    private void btnGuardarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseExited
        panelGuardar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnGuardarMouseExited

    private void btnGuardarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGuardarKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;

            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_btnGuardarKeyPressed

    private void panelGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelGuardarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelGuardarMouseClicked

    private void panelGuardarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelGuardarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelGuardarMouseEntered

    private void btnCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseClicked
        dispose();
    }//GEN-LAST:event_btnCancelarMouseClicked

    private void btnCancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseEntered
        panelCancelar.setBackground(new Color(153,204,255));
    }//GEN-LAST:event_btnCancelarMouseEntered

    private void btnCancelarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseExited
        panelCancelar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelarMouseExited

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        int codigo=evt.getKeyCode();
        switch(codigo){
            case KeyEvent.VK_ENTER:
            eventoEnter();
            break;

            case KeyEvent.VK_ESCAPE:
            dispose();
            break;

        }
    }//GEN-LAST:event_btnCancelarKeyPressed

    private void panelCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelCancelarMouseClicked

    }//GEN-LAST:event_panelCancelarMouseClicked

    private void panelCancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelCancelarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelCancelarMouseEntered

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
            java.util.logging.Logger.getLogger(DatosReporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DatosReporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DatosReporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DatosReporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DatosReporte dialog = new DatosReporte(new javax.swing.JFrame(), true);
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
    private javax.swing.JCheckBox ClientesTodos;
    private javax.swing.JCheckBox ClientesUno;
    private javax.swing.JCheckBox EmpleadosTodos;
    private javax.swing.JCheckBox EmpleadosUno;
    private javax.swing.JLabel btnCancelar;
    private javax.swing.JLabel btnGuardar;
    private javax.swing.JLabel cargando;
    private com.raven.datechooser.DateChooser jDateChooser4;
    private com.raven.datechooser.DateChooser jDateChooser5;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel panelCancelar;
    private javax.swing.JPanel panelGuardar;
    private textfield.TextField primeraFecha;
    private textfield.TextField segundaFecha;
    private combobox.Combobox txtCliente;
    private combobox.Combobox txtEmpleado;
    private javax.swing.JCheckBox txtExcel;
    private javax.swing.JCheckBox txtPDF;
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
    
 private static CellStyle getContabilidadCellStyle(Workbook workbook, DecimalFormat df) {
    CellStyle style = workbook.createCellStyle();
    style.setDataFormat(workbook.createDataFormat().getFormat(df.format(0)));
    return style;
  }    
       
   public void notasEnTienda(){
        try {

              Formatter obj = new Formatter();
                      Formatter obj2 = new Formatter();

            LocalDateTime m = LocalDateTime.now(); //Obtenemos la fecha actual
        String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
        String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
        String DiagHoy =dia+"-"+ mes + "-" +m.getYear();
        String fechaHoyResta = m.getYear()+"-"+mes + "-"+dia;
        
            
                config configura = new config();
            configura = gastos.buscarDatos();
                        Font letra = new Font(Font.FontFamily.TIMES_ROMAN, 11);
                        Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            FileOutputStream archivo;
            //File file = new File("corte" + fechaHoy + ".pdf");
                        File file = new File("ReportesPDF\\reporteNotasEnTienda.pdf");
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
            String tituloEncabezado ="";
                tituloEncabezado="RESUMEN";

            cell = new PdfPCell(new Phrase(configura.getNomnbre()+"\nNOTAS EN TIENDA\n"+tituloEncabezado+"\nFECHA DE EMISIÓN "+DiagHoy,letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            encabezado.addCell(img);
            doc.add(encabezado);

            PdfPTable tablapro = new PdfPTable(9);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] columnapro = new float[]{15f, 10f, 20f, 15f,12f,12f,12f,8f,7f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1 = new PdfPCell(new Phrase("Fecha",letra));
            PdfPCell pro2 = new PdfPCell(new Phrase("Folio",letra));
            PdfPCell pro3 = new PdfPCell(new Phrase("Cliente",letra));
            PdfPCell pro4 = new PdfPCell(new Phrase("Concepto",letra));
            PdfPCell pro5 = new PdfPCell(new Phrase("Importe",letra));
            PdfPCell pro6 = new PdfPCell(new Phrase("Cobrado",letra));
            PdfPCell pro7 = new PdfPCell(new Phrase("Pago",letra));
            PdfPCell pro8 = new PdfPCell(new Phrase("Recibe",letra));
            PdfPCell pro9 = new PdfPCell(new Phrase("Días",letra));

            pro1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro4.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro5.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro6.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro7.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro8.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro9.setHorizontalAlignment(Element.ALIGN_CENTER);




            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
             tablapro.addCell(pro5);
            tablapro.addCell(pro6);
            tablapro.addCell(pro7);
            tablapro.addCell(pro8);
              tablapro.addCell(pro9);
            
            double totalVendido=0;
            double totalCobrado=0;
            
            List<Nota> ListaCl = notaDao.listarNotasSinEntregar();

             for (int i = 0; i < ListaCl.size(); i++) { 
                 if(ListaCl.get(i).getEstado()==0){
                     String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
                     cell = new PdfPCell();
                     cell = new PdfPCell(new Phrase(fechaF,letra));
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setBorder(0);
                     tablapro.addCell(cell);
                     
                     String folioF = String.valueOf(ListaCl.get(i).getFolio());
                     cell = new PdfPCell();
                     cell = new PdfPCell(new Phrase(folioF,letra));
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setBorder(0);
                     tablapro.addCell(cell);
                     
                     String clienteF =ListaCl.get(i).getNombre()+" "+ListaCl.get(i).getApellido();
                     cell = new PdfPCell();
                     cell = new PdfPCell(new Phrase(clienteF,letra));
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setBorder(0);
                     tablapro.addCell(cell);
                     
                     List<Detalle> lsDetalle = notaDao.regresarDetalles(ListaCl.get(i).getFolio());
                     List<Integer> repetidos=new ArrayList<Integer>();
                     for(int j=0;j<lsDetalle.size();j++){
                         boolean checando=false;
                         for(int h=0;h<repetidos.size();h++){
                             if(repetidos.get(h)==lsDetalle.get(j).getCodigoPrecio()){
                              checando=true;   
                             }
                         }
                         if(checando==false){
                         repetidos.add(lsDetalle.get(j).getCodigoPrecio());

                         }
                     }
                     String conceptoF="";
                     for(int j=0;j<repetidos.size();j++){
                         conceptoF+= String.format("%0" + 2 + "d", Integer.valueOf(repetidos.get(j)))+"-";
                     }
                     conceptoF=conceptoF.replaceFirst(".$", "");
                     cell = new PdfPCell();
                     cell = new PdfPCell(new Phrase(conceptoF,letra));
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setBorder(0);
                     tablapro.addCell(cell);
                     
                     String totalF =""+"$"+formateador.format(ListaCl.get(i).getVentaTotal());
                     cell = new PdfPCell();
                     cell = new PdfPCell(new Phrase(totalF,letra));
                     cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                     cell.setBorder(0);
                     tablapro.addCell(cell);
                     totalVendido=totalVendido+ListaCl.get(i).getVentaTotal();
                     
                                List<anticipo> lsAnti = anticip.listarAnticipo();
                   double anticiposAcumulados=0;
                   for(int j=0;j<lsAnti.size();j++){
                       if(lsAnti.get(j).getFolio()==ListaCl.get(i).getFolio()){
                          anticiposAcumulados=anticiposAcumulados+lsAnti.get(j).getCantidad();
                       }
                   }
                     double totalPagado=anticiposAcumulados+ListaCl.get(i).getAnticipo();
                     String cobradoF =""+"$"+formateador.format(totalPagado);;
                     cell = new PdfPCell();
                     cell = new PdfPCell(new Phrase(cobradoF,letra));
                     cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                     cell.setBorder(0);
                     tablapro.addCell(cell);
                     totalCobrado=totalCobrado+totalPagado;
                     
                     int pagoR=0;
                     int pagoA=0;
                     int pagoE=0;
                     if(ListaCl.get(i).getAnticipo()>0){
                         pagoR=ListaCl.get(i).getFormaPago();
                     }
                     List<entrega> ListaEntregas = notaDao.listarNotasEntregas();
                     for(int k=0;k<ListaEntregas.size();k++){
                         if(ListaEntregas.get(k).getIdnota()==ListaCl.get(i).getFolio()){
                           pagoE= ListaEntregas.get(k).getFormaPago();
                         }
                     }
                     
                     List<Integer> repetidos2=new ArrayList<Integer>();
                     for(int j=0;j<lsAnti.size();j++){
                       if(lsAnti.get(j).getFolio()==ListaCl.get(i).getFolio()){
                           boolean rep =false;
                             for(int h=0;h<repetidos2.size();h++){
                             if(repetidos2.get(h)!=lsAnti.get(j).getFormaPago() && lsAnti.get(j).getFormaPago()!=pagoE && lsAnti.get(j).getFormaPago()!=pagoR){
                              
                             }else{
                                 rep=true;
                             }
                         }
                       if(rep==false){
                       repetidos2.add(lsAnti.get(j).getFormaPago());
                       }     
                       
                       }
                       }
                     if(pagoR>0) repetidos2.add(pagoR);
                     if(pagoE>0) repetidos2.add(pagoE);
                     String formaPagoF="";
                     for(int j=0;j<repetidos2.size();j++){
                         formaPagoF+= String.format("%0" + 2 + "d", Integer.valueOf(repetidos2.get(j)))+"-";
                     }
                     formaPagoF=formaPagoF.replaceFirst(".$", "");
                     cell = new PdfPCell();
                     cell = new PdfPCell(new Phrase(formaPagoF,letra));
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setBorder(0);
                     tablapro.addCell(cell);
                     
                      String recibeF =String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getIdRecibe()));
                     cell = new PdfPCell();
                     cell = new PdfPCell(new Phrase(recibeF,letra));
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setBorder(0);
                     tablapro.addCell(cell);
                     
                     Date fechaRecibida=StringADate(ListaCl.get(i).getFecha());
                     Date fechaActualE=StringADate(fechaHoyResta);
                    LocalDate a1= fechaRecibida.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate a2= fechaActualE.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                   long diff = ChronoUnit.DAYS.between(a1, a2);
                     cell = new PdfPCell();
                     cell = new PdfPCell(new Phrase(""+diff,letra));
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setBorder(0);
                     tablapro.addCell(cell);
             }
             }
             
             
              for(int i=0;i<3;i++){
                 cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(""));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell); 
             }
            
            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Total",negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);
             
             cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(""+"$"+formateador.format(totalVendido),negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);
            
             cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(""+"$"+formateador.format(totalCobrado),negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);
            
             for(int i=0;i<3;i++){
                 cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(""));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell); 
             }
            
             doc.add(tablapro);
             
             
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
   
      public void notasEnTiendaExcel(){
            Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Notas en tienda");
          
        
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
        String DiagHoy =dia+"-"+ mes + "-" +m.getYear();
         String fechaHoyResta = m.getYear()+"-"+mes + "-"+dia;
        
            
                config configura = new config();
            configura = gastos.buscarDatos();
            Row filaTitulo = sheet.createRow(1);
            Cell celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue(configura.getNomnbre()); 
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(1,1,1,7));
            
             filaTitulo = sheet.createRow(2);
             celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("NOTAS EN TIENDA"); 
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(2,2,1,7));
            
         String tituloEncabezado ="";
                tituloEncabezado="LISTADO DE NOTAS EN TIENDA";
             filaTitulo = sheet.createRow(3);
             celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue(tituloEncabezado); 
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(3,3,1,7));
            
            filaTitulo = sheet.createRow(4);
            celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("FECHA DE EMISIÓN "+DiagHoy); 
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(4,4,1,7));
            
            String [] cabecera = new String[]{"Fecha","Folio","Cliente","Concepto","Importe","Cobrado","Pago","Recibe","Días en tienda"};
            
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
            for(int i=0;i<cabecera.length;i++){
                Cell celdaEncabezado = filaEncabezados.createCell(i);
                celdaEncabezado.setCellStyle(headerStyle);
                celdaEncabezado.setCellValue(cabecera[i]);
            }
            
            int numFilaDatos =7;
            
             CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.NONE);
            datosEstilo.setBorderRight(BorderStyle.NONE);
            datosEstilo.setBorderLeft(BorderStyle.NONE);
             datosEstilo.setBorderTop(BorderStyle.NONE);
             datosEstilo.setAlignment(HorizontalAlignment.CENTER);
            datosEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
            
           List<Nota> ListaCl = notaDao.listarNotasSinEntregar();
            
            int numCol = ListaCl.size();
            
            
            double totalVendido =0;
            double totalCobrado=0;
            for(int i = 0;i<ListaCl.size();i++){
                                 if(ListaCl.get(i).getEstado()==0){
                Row filaDatos = sheet.createRow(numFilaDatos);
                
                List<String> datosEscribir = new ArrayList<String>();
                
                 String fechaF = fechaFormatoCorrecto(ListaCl.get(i).getFecha());
                    datosEscribir.add(fechaF);
                     
                     String folioF = String.valueOf(ListaCl.get(i).getFolio());
                     datosEscribir.add(folioF);
                     
                     String clienteF =ListaCl.get(i).getNombre()+" "+ListaCl.get(i).getApellido();
                     datosEscribir.add(clienteF);
                     
                     List<Detalle> lsDetalle = notaDao.regresarDetalles(ListaCl.get(i).getFolio());
                     List<Integer> repetidos=new ArrayList<Integer>();
                     for(int j=0;j<lsDetalle.size();j++){
                         boolean checando=false;
                         for(int h=0;h<repetidos.size();h++){
                             if(repetidos.get(h)==lsDetalle.get(j).getCodigoPrecio()){
                              checando=true;   
                             }
                         }
                         if(checando==false){
                         repetidos.add(lsDetalle.get(j).getCodigoPrecio());

                         }
                     }
                     String conceptoF="";
                     for(int j=0;j<repetidos.size();j++){
                         conceptoF+= String.format("%0" + 2 + "d", Integer.valueOf(repetidos.get(j)))+"-";
                     }
                     conceptoF=conceptoF.replaceFirst(".$", "");
                     datosEscribir.add(conceptoF);
                     
                     String totalF =""+ListaCl.get(i).getVentaTotal();
                    datosEscribir.add(totalF);
                    totalVendido=totalVendido+ListaCl.get(i).getVentaTotal();
                     
                                List<anticipo> lsAnti = anticip.listarAnticipo();
                   double anticiposAcumulados=0;
                   for(int j=0;j<lsAnti.size();j++){
                       if(lsAnti.get(j).getFolio()==ListaCl.get(i).getFolio()){
                          anticiposAcumulados=anticiposAcumulados+lsAnti.get(j).getCantidad();
                       }
                   }
                     double totalPagado=anticiposAcumulados+ListaCl.get(i).getAnticipo();
                     String cobradoF =""+totalPagado;
                    datosEscribir.add(cobradoF);
                    totalCobrado=totalCobrado+totalPagado;
                     
                     int pagoR=0;
                     int pagoA=0;
                     int pagoE=0;
                     if(ListaCl.get(i).getAnticipo()>0){
                         pagoR=ListaCl.get(i).getFormaPago();
                     }
                     List<entrega> ListaEntregas = notaDao.listarNotasEntregas();
                     for(int k=0;k<ListaEntregas.size();k++){
                         if(ListaEntregas.get(k).getIdnota()==ListaCl.get(i).getFolio()){
                           pagoE= ListaEntregas.get(k).getFormaPago();
                         }
                     }
                     
                     List<Integer> repetidos2=new ArrayList<Integer>();
                     for(int j=0;j<lsAnti.size();j++){
                       if(lsAnti.get(j).getFolio()==ListaCl.get(i).getFolio()){
                           boolean rep =false;
                             for(int h=0;h<repetidos2.size();h++){
                             if(repetidos2.get(h)!=lsAnti.get(j).getFormaPago() && lsAnti.get(j).getFormaPago()!=pagoE && lsAnti.get(j).getFormaPago()!=pagoR){
                              
                             }else{
                                 rep=true;
                             }
                         }
                       if(rep==false){
                       repetidos2.add(lsAnti.get(j).getFormaPago());
                       }     
                       
                       }
                       }
                     if(pagoR>0) repetidos2.add(pagoR);
                     if(pagoE>0) repetidos2.add(pagoE);
                     String formaPagoF="";
                     for(int j=0;j<repetidos2.size();j++){
                         formaPagoF+= String.format("%0" + 2 + "d", Integer.valueOf(repetidos2.get(j)))+"-";
                     }
                     formaPagoF=formaPagoF.replaceFirst(".$", "");
                    datosEscribir.add(formaPagoF);
                     
                      String recibeF =String.format("%0" + 2 + "d", Integer.valueOf(ListaCl.get(i).getIdRecibe()));
                    datosEscribir.add(recibeF);
                     
                     Date fechaRecibida=StringADate(ListaCl.get(i).getFecha());
                     Date fechaActualE=StringADate(fechaHoyResta);
                    LocalDate a1= fechaRecibida.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate a2= fechaActualE.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                   long diff = ChronoUnit.DAYS.between(a1, a2);
                     datosEscribir.add(""+diff);
                     
                   for(int j=0;j<datosEscribir.size();j++){
                       if(j==4 || j==5){
                         Cell celdaDatos = filaDatos.createCell(j, CellType.NUMERIC);
                        celdaDatos.setCellValue(Double.parseDouble(datosEscribir.get(j)));
                        celdaDatos.setCellStyle(getContabilidadCellStyle(book,df));
                       }else{
                       Cell celdaDatos = filaDatos.createCell(j);
                       celdaDatos.setCellStyle(datosEstilo);
                       celdaDatos.setCellValue(datosEscribir.get(j));
                       }
                   }
                 
                 numFilaDatos++;  
            }
            }
                 

            
            
             Row filaDatos = sheet.createRow(numFilaDatos);
              Cell celdaDatos = filaDatos.createCell(3);
                       celdaDatos.setCellStyle(datosEstilo);
                       celdaDatos.setCellValue("TOTAL");
                       
                       
                       celdaDatos = filaDatos.createCell(4, CellType.NUMERIC);
                        celdaDatos.setCellValue(totalVendido);
                        celdaDatos.setCellStyle(getContabilidadCellStyle(book,df));
                        
                        celdaDatos = filaDatos.createCell(5, CellType.NUMERIC);
                        celdaDatos.setCellValue(totalCobrado);
                        celdaDatos.setCellStyle(getContabilidadCellStyle(book,df)); 

            
            //reajustamos al tamaño
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
             sheet.autoSizeColumn(7);
              sheet.autoSizeColumn(8);
   
            
            //hacemos zoom
            sheet.setZoom(150);
            
            FileOutputStream fileOut = new FileOutputStream("ReportesExcel\\reporteNotasEnTienda.xlsx");
            book.write(fileOut);
            fileOut.close();
             File file = new File("ReportesExcel\\reporteNotasEnTienda.xlsx");
                       Desktop.getDesktop().open(file);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(vistaPrecios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(vistaPrecios.class.getName()).log(Level.SEVERE, null, ex);
        }
     }                            
                     }     



