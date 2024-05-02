/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.BoletoPromocion;
import Modelo.Cliente;
import Modelo.ClienteDao;
import Modelo.Detalle;
import Modelo.Empleados;
import Modelo.EmpleadosDao;
import Modelo.Eventos;
import Modelo.GastosDao;
import Modelo.Nota;
import Modelo.NotaDao;
import Modelo.PrecioDao;
import Modelo.Precios;
import Modelo.PromocionDao;
import Modelo.TextPrompt;
import Modelo.anticipo;
import Modelo.anticipoDao;
import Modelo.config;
import Modelo.corteDiaDao;
import Modelo.datosPromocion;
import Modelo.entrega;
import Modelo.establecerFecha;
import Modelo.imprimiendo;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.raven.model.StatusType;
import com.raven.swing.ScrollBar;
import com.raven.swing.Table;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javaswingdev.Notification;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.raven.swing.Table2;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class vistaNota extends javax.swing.JDialog {

    String horas, min, seg, ampm;

    Calendar calendario;
    Thread hi;

    Nota nota = new Nota();
    corteDiaDao corte = new corteDiaDao();
    entrega entreg = new entrega();
    anticipoDao anticip = new anticipoDao();
    NotaDao notaDao = new NotaDao();
    Detalle dv = new Detalle();
    config configura = new config();
    Cliente cl = new Cliente();
    ClienteDao client = new ClienteDao();
    EmpleadosDao emple = new EmpleadosDao();
    PromocionDao promocionDao = new PromocionDao();
    Eventos event = new Eventos();
    PrecioDao precio = new PrecioDao();
    DefaultListModel model;
    DefaultTableModel tmp;
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel modelo1 = new DefaultTableModel();
    String hoy = corte.getDia();
    String fecha, hora;
    double totalAPagar = 0.00;
    double totalAPagarNota = 0.00;
    boolean indicador = false;
    boolean guardar, ver, imprimir, entregar, abonar, cancelar, agregar, btnEliminar, btnModificar;
    boolean limpiar = true;
    int folioMostrar;
    GastosDao gastos = new GastosDao();
    List<Integer> listaFolios = new ArrayList<Integer>();
    DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
    datosPromocion datos = promocionDao.obtenerDatos();
        establecerFecha establecer = new establecerFecha();

    public vistaNota(java.awt.Frame parent, boolean modal) {

        super(parent, modal);
        initComponents();
        selectorHora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                SimpleDateFormat formato12 = new SimpleDateFormat("h:mm a");
                SimpleDateFormat formato24 = new SimpleDateFormat("HH:mm");

                try {
                    Date date = formato12.parse(selectorHora.getSelectedTime());
                    String hora24 = formato24.format(date);
                    txtHoraEntrega.setText(hora24);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setLocation(250, 80);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);

        Date fechaRestada = calendar.getTime();
        selectorHora.setSelectedTime(fechaRestada);
        txtHoraEntrega.setText("19:00");
        txtHoraEntrega.setBackground(new Color(240, 240, 240));

    }

    public void LimpiarTabla() {
        for (int i = 0; i < modelo1.getRowCount(); i++) {
            modelo1.removeRow(i);
            i = i - 1;
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

    public void listarNotasPorNombre(int idCliente) {
        List<Nota> lsnotas = notaDao.listarNotas();
        modelo1 = (DefaultTableModel) TableNotasCliente.getModel();
        Object[] ob = new Object[6];
        for (int i = lsnotas.size() - 1; i >= 0; i--) {
            if (lsnotas.get(i).getCodigoCLiente() == idCliente) {
                ob[0] = fechaFormatoCorrecto(lsnotas.get(i).getFecha());
                ob[1] = lsnotas.get(i).getFolio();
                ob[2] = "$" + formateador.format(lsnotas.get(i).getVentaTotal());

                double abonos = lsnotas.get(i).getVentaTotal() - lsnotas.get(i).getTotalPagar();

                ob[3] = "$" + formateador.format(abonos);
                ob[4] = "$" + formateador.format(lsnotas.get(i).getTotalPagar());
                if (lsnotas.get(i).getEstado() == 1) { //SI esta cancelada
                    ob[5] = StatusType.REJECT;
                } else {
                    if (lsnotas.get(i).getEntrega() == 1) { //Si ya fue entregada
                        ob[5] = StatusType.APPROVED;
                    } else {
                        ob[5] = StatusType.PENDING;
                    }
                }
                modelo1.addRow(ob);
            }
        }
        TableNotasCliente.setModel(modelo1);
    }

    public void validandoDatos(int folioMostrar, boolean indicador) {
        this.indicador = indicador;
        this.folioMostrar = folioMostrar;
        Seticon();
        jButton15.setToolTipText("F1 - Nueva nota");
        jButton17.setToolTipText("F2 - Registrar nota");
        jButton18.setToolTipText("F3 - Visualizar pdf de la nota");
        jButton19.setToolTipText("F4 - Imprimir nota");
        jButton20.setToolTipText("F5 - Entregar nota");
        jButton21.setToolTipText("F6 - Abonar a nota");
        jButton22.setToolTipText("F7 - Cancelar nota");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Software Lavandería - Notas");
        int i = notaDao.idVenta() + 1; //Se obtiene el numero actual que contendra la siguiente nota
        txtFolio.setText(String.valueOf(i));
        establecerFecha(); //se busca estblecer la fecha
        controladorLista.setVisible(false);
        TableVenta.setSelectionForeground(Color.WHITE);

        jList1.setVisible(false);
        Date diaActual = StringADate(fecha);
        DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
        String fechaUsar;
        Date fechaNueva;
        Calendar c = Calendar.getInstance();
        c.setTime(diaActual);
        fechaUsar = cam.format(diaActual);
        long miliseconds = System.currentTimeMillis(); //El tiempo actual
        Date date = new Date(miliseconds);
        txtDiaEntrega.setMinSelectableDate(date);
        APOYOPARAOBTNEREMPELADO.setVisible(false);
        txtApoyoeliminar.setVisible(false);
        jLabel63.setText(fechaUsar);
        listarEmpleados();
        rellenarPrecios();
        tmp = (DefaultTableModel) TableVenta.getModel();

        txtNombreRecibe.setBackground(Color.WHITE);
        precios.setBackground(Color.WHITE);
        TableVenta.setBackground(Color.WHITE);
        jScrollPane1.getViewport().setBackground(new Color(204, 204, 204));

        TableVenta.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        DefaultTableCellRenderer tcrDerecha = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        TableVenta.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TableVenta.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TableVenta.getColumnModel().getColumn(2).setCellRenderer(tcrDerecha);
        TableVenta.getColumnModel().getColumn(3).setCellRenderer(tcrDerecha);
        TableVenta.getColumnModel().getColumn(4).setCellRenderer(tcr);
        ((DefaultTableCellRenderer) TableVenta.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        if (indicador == true) {//generar nota
            ver = imprimir = entregar = abonar = cancelar = false;
            guardar = agregar = btnEliminar = btnModificar = true;
            txtDiaEntrega.setTextRefernce(txtParaFecha);
            hi = new Thread();
            hi.start();
        } else {
            txtDiaEntrega.setTextRefernce(apoyoFecha);
            bloquearDatos(false);
            vaciarDatos(folioMostrar);
        }
        txtCodigoCliente1.setDisabledTextColor(Color.black);
        txtNombreCliente.setDisabledTextColor(Color.black);
        txtcorreoI.setDisabledTextColor(Color.black);
        txtTelefonoI1.setDisabledTextColor(Color.black);
        txtfechaEntregada.setDisabledTextColor(Color.black);
        txtHoraEntregada.setDisabledTextColor(Color.black);
        txtPrecioUnitario.setDisabledTextColor(Color.black);
        LabelTotal1.setDisabledTextColor(Color.black);
        txtAbonos.setDisabledTextColor(Color.black);
        LabelTotal.setDisabledTextColor(Color.black);

        cargando.setVisible(false);
        SetImagenLabel(cargando, "/Imagenes/loading.gif");
        TextPrompt fondo = new TextPrompt("Introduce el nombre o código del cliente", jTextField1);

        tcr = new DefaultTableCellRenderer();
        tcrDerecha = new DefaultTableCellRenderer();
        tcrDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        TableNotasCliente.getColumnModel().getColumn(0).setCellRenderer(tcr);
        TableNotasCliente.getColumnModel().getColumn(1).setCellRenderer(tcr);
        TableNotasCliente.getColumnModel().getColumn(2).setCellRenderer(tcrDerecha);
        TableNotasCliente.getColumnModel().getColumn(3).setCellRenderer(tcrDerecha);
        TableNotasCliente.getColumnModel().getColumn(4).setCellRenderer(tcrDerecha);
        spTable.setVerticalScrollBar(new ScrollBar());
        spTable.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);

    }

    public void vaciarDatos(int folioBuscar) {

        Nota not = notaDao.buscarPorFolio(folioBuscar);//Obtenemos los datos de la nota de la BD
        txtFolio.setText(String.valueOf(not.getFolio())); //Colocamos la fecha de recepcion de la nota y su folio
        Date diaActual = StringADate(not.getFecha());
        DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
        String fechaUsar;
        Date fechaNueva;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(diaActual);
        fechaUsar = cam.format(diaActual);
        agregar = btnEliminar = btnModificar = false;
        jLabel63.setText(fechaUsar);

        Date horaFecha = StringADateHora(not.getHora());
        Formatter obj2 = new Formatter();
        String horaModificada = horaFecha.getHours() + ":" + String.valueOf(obj2.format("%02d", horaFecha.getMinutes()));
        jLabel7.setText(horaModificada);

        //Dependiendo del estado de la nota, se habilitaran ciertos botones
        if (not.getEstado() == 1) { //SI esta cancelada
            espacioFecha.setText("CANCELADA");
            ver = imprimir = true;
            guardar = entregar = cancelar = abonar = false;

        } else {
            if (not.getEntrega() == 1) { //Si ya fue entregada
                espacioFecha.setText("ENTREGADA");
                ver = imprimir = true;
                abonar = guardar = entregar = cancelar = false;
                diaActual = StringADate(not.getEntregaDia());
                cam = new SimpleDateFormat("dd-MM-yyyy");

                c1 = Calendar.getInstance();
                c1.setTime(diaActual);
                fechaUsar = cam.format(diaActual);
                txtfechaEntregada.setText(fechaUsar);
                Date horaDeEntrega = StringADateHora2(not.getEntregaHora());
                Formatter obj = new Formatter();
                String nhora = horaDeEntrega.getHours() + ":" + String.valueOf(obj.format("%02d", horaDeEntrega.getMinutes()));
                txtHoraEntregada.setText(nhora);

            } else {
                espacioFecha.setText("EN TIENDA"); //Entonces sigue en tienda
                ver = imprimir = cancelar = entregar = abonar = true;
                guardar = false;
            }
        }
        if (not.getSaldo() == 1 && not.getEstado() == 0) { //Checamos si ya fue pagada
            espacioFecha.setText(espacioFecha.getText() + "-PAGADA");
            abonar = false;

        }
        if (not.getSaldo() == 0 && not.getEstado() == 0) { // o bien, no se ha pagado totalmente
            espacioFecha.setText(espacioFecha.getText() + "-SIN COMPLETAR PAGO");
            abonar = true;

        }
        if (datos.getIndicador() == 1 && promocionDao.tieneBoleto(not.getFolio())) {
            espacioFecha.setText(espacioFecha.getText() + "-BOLETO");
        }

        //vaciamos cada uno de los datos de la nota como si se acabara de generar la nota, bloqueando los espacios de texto para que no se puedan modificar
        txtCodigoCliente1.setText(String.valueOf(not.getCodigoCLiente()));
        txtNombreCliente.setText(not.getNombre() + " " + not.getApellido());
        txtParaFecha.setText(not.getDiaEntrega());
        txtDiaEntrega.setMinSelectableDate(StringADate(not.getDiaEntrega()));
        txtDiaEntrega.setSelectedDate(StringADate(not.getDiaEntrega()));
        txtParaFecha.setEnabled(false);

        txtHoraEntrega.setText(not.getHoraEntrega());
        LabelTotal.setText("$" + formateador.format(not.getTotalPagar()));
        txtAnticipo.setText("$" + formateador.format(not.getAnticipo()));
        Empleados empleado = emple.seleccionarEmpleado("", not.getIdRecibe());
        txtNombreRecibe.setSelectedItem(empleado.getNombre());
        Cliente c = client.BuscarPorCodigo(not.getCodigoCLiente());
        listarNotasPorNombre(Integer.parseInt(txtCodigoCliente1.getText()));
        txtTelefonoI1.setText(c.getTelefono());
        txtcorreoI.setText(c.getCorreo());
        LabelTotal1.setText("$" + formateador.format(not.getVentaTotal()));
        List<Detalle> ListaDetalles = notaDao.regresarDetalles(not.getFolio());
        tmp = (DefaultTableModel) TableVenta.getModel();
        //vaciamos cada concepto en la tabla 
        for (int i = 0; i < ListaDetalles.size(); i++) {
            ArrayList lista = new ArrayList();
            lista.add(ListaDetalles.get(i).getCantidad());
            System.out.println("De la nota: " + ListaDetalles.get(i).getCantidad());
            lista.add(ListaDetalles.get(i).getDescripcion());
            lista.add(ListaDetalles.get(i).getPrecioUnitario());
            lista.add(ListaDetalles.get(i).getPrecioFinal());
            lista.add(ListaDetalles.get(i).getComentario());
            Object[] o = new Object[5];
            o[0] = lista.get(0);
            o[1] = lista.get(1);
            o[2] = "$" + formateador.format(lista.get(2));
            o[3] = "$" + formateador.format(lista.get(3));
            o[4] = lista.get(4);
            tmp.addRow(o);
        }
        TableVenta.setModel(tmp);

        List<anticipo> lsAnti = anticip.listarAnticipo();
        double anticiposAcumulados = 0;
        for (int i = 0; i < lsAnti.size(); i++) {
            if (lsAnti.get(i).getFolio() == folioMostrar) {
                anticiposAcumulados = anticiposAcumulados + lsAnti.get(i).getCantidad();
            }
        }
        if (anticiposAcumulados != 0) {
            txtAbonos.setText("$" + formateador.format(anticiposAcumulados));
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

    public java.util.Date StringADateHora2(String cambiar) {//Este metodo te transforma un String a date (hora)
        SimpleDateFormat formato_del_Texto = new SimpleDateFormat("HH:mm");
        Date fechaE = null;
        try {
            fechaE = formato_del_Texto.parse(cambiar);
            return fechaE;
        } catch (ParseException ex) {
            return null;
        }
    }

 public void establecerFecha() { //Establecemos la fecha a usar, esto se hace ya que hasta que no se cierre caja, no se puede avanzar de dia
        List<String> datos = establecer.establecerFecha();
        fecha = datos.get(0);
        hora = datos.get(1);

    }
    public static boolean isNumeric(String s) {
        if (s == null || s.equals("")) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private void totalPagar() {
        try {
            totalAPagar = 0.00;
            int numFIla = TableVenta.getRowCount();
            for (int i = 0; i < numFIla; i++) {
                Number setTotalVentas;
                setTotalVentas = formateador.parse(removefirstChar(TableVenta.getModel().getValueAt(i, 3).toString()));
                double cal = setTotalVentas.doubleValue();

                totalAPagar = totalAPagar + cal;
            }
            LabelTotal1.setText("$" + formateador.format(totalAPagar));
        } catch (ParseException ex) {
            Logger.getLogger(vistaNota.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void totalPagarNota() {
        if (!"-----".equals(LabelTotal1.getText())) {
            totalAPagarNota = 0.00;
            double b;
            Number cambiando;
            try {
                cambiando = formateador.parse(removefirstChar(LabelTotal1.getText()));
                double a = cambiando.doubleValue();

                if ("".equals(txtAnticipo.getText())) {
                    b = 0;
                } else {
                    b = Double.parseDouble(txtAnticipo.getText());
                }

                totalAPagarNota = a - b;
                LabelTotal.setText("$" + formateador.format(totalAPagarNota));
            } catch (ParseException ex) {
                Logger.getLogger(vistaNota.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Rellene todos los campos");
            panel.showNotification();
        }
    }

    public static String removefirstChar(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(1);
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

    public List rellenarPrecios() { //Metodo para vaciar los precios en el combo box
        List<Precios> lsNotas = precio.getPrecios(); //obtenemos los precios de la base de datos y la guardamos en una lista
        precios.removeAllItems();//Borramos los valores que tenga la lista para evitar que se sobrencimen
        for (int i = 0; i < lsNotas.size(); i++) {//Con este ciclo, vamos vaciando los precios en el ComboBox
            if (lsNotas.get(i).getEstado() == 0) {
                precios.addItem(lsNotas.get(i).getNombre());
            }
        }
        return lsNotas;
    }

    public void bloquearDatos(boolean cambiar) {
        txtCantidad.setEnabled(cambiar);
        precios.setEnabled(cambiar);
        jTextField1.setEnabled(cambiar);
        jList1.setEnabled(cambiar);
        txtAnticipo.setEnabled(cambiar);
        txtDiaEntrega.setEnabled(cambiar);

        jTextField3.setEnabled(cambiar);
    }

    public void limpiarDatosNota() {
        txtTelefonoI1.setText("");
        txtAnticipo.setText("");
        txtHoraEntrega.setText("");
        txtNombreCliente.setText("");
        LabelTotal1.setText("----");
        LabelTotal.setText("----");
        txtNombreRecibe.setSelectedIndex(0);
        espacioFecha.setText("-");
        txtCodigoCliente1.setText("");
        txtcorreoI.setText("");
        LimpiarTabla();
        jLabel7.setText("-");
        txtfechaEntregada.setText("");
        txtHoraEntregada.setText("");
    }

    public void LimpiarTablaNota() {
        TableVenta.setModel(tmp);
        for (int i = 0; i < tmp.getRowCount(); i++) {
            tmp.removeRow(i);
            i = i - 1;
        }
    }

    private void registrarNota(int formaPago) throws ParseException {

        boolean x = false;
        establecerFecha();
        try {
            nota.setCodigoCLiente(Integer.parseInt(txtCodigoCliente1.getText()));
            nota.setAnticipo(Double.parseDouble(txtAnticipo.getText()));
            if (Double.parseDouble(txtAnticipo.getText()) > 0) {
                nota.setFormaPago(formaPago);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaE = sdf.format(txtDiaEntrega.getDateBD());
            nota.setDiaEntrega(fechaE);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            String horaE = txtHoraEntrega.getText();
            nota.setHoraEntrega(horaE);
            Cliente clienteU = client.BuscarPorCodigo(Integer.parseInt(txtCodigoCliente1.getText()));
            nota.setNombre(clienteU.getNombre());
            nota.setApellido(clienteU.getApellido());
            Number tomandoNumero = 0;
            Number cambiando = formateador.parse(removefirstChar(LabelTotal1.getText()));
            nota.setVentaTotal(cambiando.doubleValue());
            if (datos.getIndicador() == 1) {
                String[] conceptosValidos = datos.getConceptos().split("-");
                double totalConceptos = 0;
                for (int i = 0; i < conceptosValidos.length; i++) {
                    for (int tabla = 0; tabla < TableVenta.getRowCount(); tabla++) {
                        if (TableVenta.getValueAt(tabla, 1).toString().equals(conceptosValidos[i])) {
                            tomandoNumero = formateador.parse(removefirstChar(TableVenta.getValueAt(tabla, 3).toString()));
                            totalConceptos = totalConceptos + tomandoNumero.doubleValue();
                            System.out.println(totalConceptos + "--------" + tomandoNumero.doubleValue());
                        }
                    }
                }
                if (totalConceptos >= datos.getMontoMinimo()) {
                    BoletoPromocion promo = new BoletoPromocion();
                    promo.setFecha(fecha);
                    promo.setHora(hora);
                    promo.setFolioNota(notaDao.idVenta() + 1);
                    promocionDao.introducirFolio(promo);
                }
            }
            cambiando = formateador.parse(removefirstChar(LabelTotal.getText()));
            nota.setTotalPagar(cambiando.doubleValue());

            Empleados empleado = emple.seleccionarEmpleado(txtNombreRecibe.getSelectedItem().toString(), 0);
            nota.setIdRecibe(empleado.getId());
            nota.setEntrega(0); //por entregar

            if (cambiando.doubleValue() == 0) {
                nota.setSaldo(1); //pagado
            } else {
                nota.setSaldo(2); //no pagado
            }
        notaDao.registrarVenta(nota, hora, fecha);
            registrarDetalle();
            x = true;

        } catch (NullPointerException e) {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Verifique la fecha de entrega");
            panel.showNotification();
            x = false;
        }
    }

    private void registrarDetalle() throws ParseException {
        int id = notaDao.idVenta();
        for (int i = 0; i < TableVenta.getRowCount(); i++) {
            double cantidad = Double.parseDouble(TableVenta.getValueAt(i, 0).toString());
            String descripcion = TableVenta.getValueAt(i, 1).toString();
            Number cambiando = formateador.parse(removefirstChar(TableVenta.getValueAt(i, 2).toString()));
            double precioUnitario = cambiando.doubleValue();
            cambiando = formateador.parse(removefirstChar(TableVenta.getValueAt(i, 3).toString()));
            double precioFinal = cambiando.doubleValue();

            String comentario = TableVenta.getValueAt(i, 4).toString();
            List<Precios> ls = precio.getPrecios();
            for (int j = 0; j < ls.size(); j++) {
                if (ls.get(j).getNombre().equals(descripcion)) {
                    dv.setCodigoPrecio(ls.get(j).getId());
                }
            }
            dv.setCantidad(cantidad);
            dv.setDescripcion(descripcion);
            dv.setPrecioUnitario(precioUnitario);
            dv.setPrecioFinal(precioFinal);
            dv.setId(id);
            dv.setComentario(comentario);
            notaDao.registrarDetalle(dv);
        }
    }

    public boolean comprobarContraseña() { //Aqui validaremos la contraseña del empleado
        boolean ind = false;
        List<Empleados> lista = emple.listarEmpleados(); //Obtenemos los empleados de la BD
        String nombreEmpleados[] = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            nombreEmpleados[i] = lista.get(i).getNombre();
        }
        JComboBox cb = new JComboBox(nombreEmpleados);
        int input = JOptionPane.showConfirmDialog(this, cb, "SELECCIONA", JOptionPane.DEFAULT_OPTION);
        if (input == JOptionPane.OK_OPTION) {
            String nombreElegido = cb.getSelectedItem().toString();
            APOYOPARAOBTNEREMPELADO.setText(nombreElegido);
            Empleados empleado = emple.seleccionarEmpleado(nombreElegido, 0);
            String comprobandoContraseña = JOptionPane.showInputDialog("INTRODUZCA SU CONTRASEÑA");
            if (comprobandoContraseña.equals(empleado.getContraseña())) {
                ind = true;
            } else {
                JOptionPane.showMessageDialog(null, "CONTRASEÑA INCORRECTA");
            }
        }
        return ind;
    }

    public void registrandoNota(int formaPago) throws ParseException {
        registrarNota(formaPago);
        bloquearDatos(false);
        txtDiaEntrega.setTextRefernce(apoyoFecha);
        txtParaFecha.setEnabled(false);
        guardar = btnEliminar = agregar = btnModificar = false;
        ver = imprimir = abonar = entregar = cancelar = cancelar = true;

        Number cambiando = formateador.parse(removefirstChar(LabelTotal.getText()));
        nota.setVentaTotal(cambiando.doubleValue());
        LimpiarTablaNota();
        LimpiarTabla();
        vaciarDatos(Integer.parseInt(txtFolio.getText()));
        hi.stop();
    }

    public void eventoF1() {
        txtParaFecha.setEnabled(true);
        txtDiaEntrega.setTextRefernce(txtParaFecha);
        long miliseconds = System.currentTimeMillis(); //El tiempo actual
        Date date = new Date(miliseconds);
        txtDiaEntrega.setMinSelectableDate(date);
        txtDiaEntrega.setSelectedDate(date);
        bloquearDatos(true);
        limpiarDatosNota();
        LimpiarTablaNota();
        LimpiarTabla();
        jLabel7.setText("-");
        int i = notaDao.idVenta() + 1;
        txtFolio.setText(String.valueOf(i));
        Date diaActual = StringADate(fecha);
        DateFormat cam = new SimpleDateFormat("dd-MM-yyyy");
        String fechaUsar;
        Date fechaNueva;
        Calendar c = Calendar.getInstance();
        c.setTime(diaActual);
        fechaUsar = cam.format(diaActual);
        jLabel63.setText(fechaUsar);
        ver = imprimir = entregar = abonar = cancelar = false;
        guardar = agregar = btnEliminar = btnModificar = true;
        hi = new Thread();
        hi.start();
        date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);

        Date fechaRestada = calendar.getTime();
        selectorHora.setSelectedTime(fechaRestada);
        txtHoraEntrega.setText("19:00");
    }

    public void SetImagenLabel(JLabel labelName, String root) {
        labelName.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(root)).getImage().getScaledInstance(labelName.getWidth(), labelName.getHeight(), java.awt.Image.SCALE_DEFAULT)));
    }

    public void proceso() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoginN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eventoF2() {
        if (guardar == true) {
            try {
                if (!"".equals(txtCodigoCliente1.getText()) && !"".equals(txtAnticipo.getText()) && !"".equals(txtDiaEntrega.getDateBD().toString())
                        && !"".equals(txtHoraEntrega.getText()) && !"".equals(txtNombreCliente.getText()) && !"".equals(LabelTotal1.getText()) && !"".equals(txtNombreRecibe.getSelectedItem().toString()) && !"".equals(LabelTotal.getText())) {
                    Number totalNota = formateador.parse(removefirstChar(LabelTotal1.getText()));
                    if (Double.parseDouble(txtAnticipo.getText()) <= totalNota.doubleValue()) {
                        Empleados empleado = emple.seleccionarEmpleado(txtNombreRecibe.getSelectedItem().toString(), 0);
                        ContraseñaConfirmacion cc = new ContraseñaConfirmacion(new javax.swing.JFrame(), true);
                        cc.determinarContraseña(empleado);
                        if (Double.parseDouble(txtAnticipo.getText()) > 0) {
                            cc.checandoAnticipo(true);
                        } else {
                            cc.checandoAnticipo(false);
                        }
                        cc.setVisible(true);
                        if (cc.contraseñaAceptada == true) {
                            new Thread() {
                                public void run() {
                                    cargando.setVisible(true);
                                    proceso();
                                    try {
                                        registrandoNota(cc.FormaPago);

                                    } catch (ParseException ex) {
                                        Logger.getLogger(vistaNota.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    cargando.setVisible(false);
                                }
                            }.start();
                            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Nota registrada exitosamente");
                            panel.showNotification();
                        }
                    } else {
                        Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "El anticipo es mayor al total");
                        panel.showNotification();
                    }
                } else {
                    Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Rellene todos los campos");
                    panel.showNotification();
                }
            } catch (Exception e) {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Algo salio mal. pruebe nuevamente " + e.getMessage());
                panel.showNotification();
            }
        }
    }

    public void eventoF3() {

        if (ver == true) {
            try {
                int id = Integer.parseInt(txtFolio.getText());
                elegirTipoTicket tt = new elegirTipoTicket(new javax.swing.JFrame(), true);
                tt.setVisible(true);
                if (tt.accionRealizada == true) {
                    File file;
                    if (tt.tipo == true) {
                        file = new File("C:\\Program Files (x86)\\AppLavanderia\\Tickets\\venta" + txtFolio.getText() + " cliente.pdf");
                        ticket(id);
                    } else {
                        file = new File("C:\\Program Files (x86)\\AppLavanderia\\Tickets\\venta" + txtFolio.getText() + " tienda.pdf");
                        ticketTienda(id);
                    }
                    Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Abriendo ticket");
                    panel.showNotification();
                    Desktop.getDesktop().open(file);
                }
            } catch (IOException ex) {

            } catch (ParseException ex) {
                Logger.getLogger(vistaNota.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void eventoF4() {
        if (imprimir == true) {
            establecerFecha();

            try {
                int id = Integer.parseInt(txtFolio.getText());
                elegirTipoTicket tt = new elegirTipoTicket(new javax.swing.JFrame(), true);
                tt.setVisible(true);
                if (tt.accionRealizada == true) {
                    File file;
                    if (tt.tipo == true) {
                        file = new File("C:\\Program Files (x86)\\AppLavanderia\\Tickets\\venta" + txtFolio.getText() + " cliente.pdf");
                        ticket(id);
                    } else {
                        file = new File("C:\\Program Files (x86)\\AppLavanderia\\Tickets\\venta" + txtFolio.getText() + " tienda.pdf");
                        ticketTienda(id);
                    }
                    Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Imprimiendo nota");
                    panel.showNotification();
                    imprimiendo m = new imprimiendo();
                    m.imprimir(file);

                }

            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null, "Error de impresion", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());

            } catch (ParseException ex) {
                Logger.getLogger(vistaNota.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void eventoF5() {
        if (entregar == true) {
            ventanaEntrega cc = new ventanaEntrega(new javax.swing.JFrame(), true);
            cc.obtenerYRellenar(Integer.parseInt(txtFolio.getText()), Integer.parseInt(txtCodigoCliente1.getText()), fecha);
            cc.setVisible(true);
            if (cc.bandera == true) {

                bloquearDatos(false);
                limpiarDatosNota();
                LimpiarTablaNota();
                vaciarDatos(Integer.parseInt(txtFolio.getText()));
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Nota entregada exitosamente");
                panel.showNotification();
            }

        }
    }

    public void eventoF6() {
        if (abonar == true) {
            VentanaAbono cc = new VentanaAbono(new javax.swing.JFrame(), true);
            cc.obtenerYRellenar(Integer.parseInt(txtFolio.getText()), Integer.parseInt(txtCodigoCliente1.getText()), fecha);
            cc.setVisible(true);
            if (cc.bandera == true) {
                bloquearDatos(false);
                limpiarDatosNota();
                LimpiarTablaNota();
                vaciarDatos(Integer.parseInt(txtFolio.getText()));
            }
        }
    }

    public void eventoF7() {
        if (cancelar == true) {
            establecerFecha();
            vistaCancelar cc = new vistaCancelar(new javax.swing.JFrame(), true);
            cc.vaciarDatos(fecha, Integer.parseInt(txtFolio.getText()));
            cc.setVisible(true);
            if (cc.accionRealizada == true) {
                bloquearDatos(false);
                limpiarDatosNota();
                LimpiarTablaNota();
                vaciarDatos(Integer.parseInt(txtFolio.getText()));
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Nota cancelada exitosamente");
                panel.showNotification();

            }
        }

    }

    public void eventoEnterAgregar() {
        if (agregar == true) {
            if (!"".equals(txtCantidad.getText()) && !"".equals(LabelTotal1.getText())) {
                double cantidad = Double.parseDouble(txtCantidad.getText());
                String descripcion = precios.getSelectedItem().toString();
                float kaka = Float.parseFloat(txtPrecioUnitario.getText()); // convertir a float
                int precioUnitario = (int) kaka;
                double totalParcial = Double.parseDouble(txtCantidad.getText()) * Double.parseDouble(txtPrecioUnitario.getText());
                int total = (int) Math.round(totalParcial);
                String comentario = jTextField3.getText();
                tmp = (DefaultTableModel) TableVenta.getModel();
                ArrayList lista = new ArrayList();
                lista.add(cantidad);
                lista.add(descripcion);
                lista.add(precioUnitario);
                lista.add(total);
                lista.add(comentario);

                Object[] o = new Object[5];
                o[0] = lista.get(0);
                o[1] = lista.get(1);
                o[2] = "$" + formateador.format(lista.get(2));
                o[3] = "$" + formateador.format(lista.get(3));
                o[4] = lista.get(4);
                tmp.addRow(o);
                TableVenta.setModel(tmp);
                txtCantidad.setText("");
                txtAnticipo.setText("");

                totalPagar();
                if ("".equals(txtAnticipo.getText())) {
                    Number cambiando;
                    try {
                        cambiando = formateador.parse(removefirstChar(LabelTotal1.getText()));
                        double f = cambiando.doubleValue();
                        LabelTotal.setText("$" + formateador.format(f));
                    } catch (ParseException ex) {
                        Logger.getLogger(vistaNota.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    double antic = Double.parseDouble(txtAnticipo.getText());

                    Number cambiando;
                    try {
                        cambiando = formateador.parse(removefirstChar(LabelTotal1.getText()));
                        double mien = cambiando.doubleValue();

                        LabelTotal.setText("$" + formateador.format(String.valueOf(mien - antic)));
                    } catch (ParseException ex) {
                        Logger.getLogger(vistaNota.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                precios.setSelectedIndex(0);
                jTextField3.setText("");
                txtCantidad.requestFocus();

            } else {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Rellene todos los campos");
                panel.showNotification();
            }
        }
    }

    private static boolean isSubstring(String s, String seq) {
        return Pattern.compile(Pattern.quote(seq), Pattern.CASE_INSENSITIVE)
                .matcher(s).find();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtDiaEntrega = new com.raven.datechooser.DateChooser();
        apoyoFecha = new javax.swing.JTextField();
        selectorHora = new com.raven.swing.TimePicker();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        panelRegistrarNota1 = new javax.swing.JPanel();
        jButton15 = new javax.swing.JLabel();
        panelRegistrarNota3 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JLabel();
        panelRegistrarNota4 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JLabel();
        panelRegistrarNota5 = new javax.swing.JPanel();
        jButton19 = new javax.swing.JLabel();
        panelRegistrarNota6 = new javax.swing.JPanel();
        jButton20 = new javax.swing.JLabel();
        panelRegistrarNota7 = new javax.swing.JPanel();
        jButton21 = new javax.swing.JLabel();
        panelRegistrarNota8 = new javax.swing.JPanel();
        jButton22 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        controladorLista = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        txtCodigoCliente1 = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        txtTelefonoI1 = new javax.swing.JTextField();
        txtcorreoI = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtNombreRecibe = new javax.swing.JComboBox<>();
        txtfechaEntregada = new javax.swing.JTextField();
        txtHoraEntregada = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtParaFecha = new javax.swing.JTextField();
        txtHoraEntrega = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        precios = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        txtPrecioUnitario = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        panelAgregar = new javax.swing.JPanel();
        agregarBoton = new javax.swing.JLabel();
        panelEliminar = new javax.swing.JPanel();
        eliminarBoton = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableVenta = new javax.swing.JTable();
        panelModificar = new javax.swing.JPanel();
        modificarBoton = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        APOYOPARAOBTNEREMPELADO = new javax.swing.JLabel();
        txtApoyoeliminar = new javax.swing.JTextField();
        spTable = new javax.swing.JScrollPane();
        TableNotasCliente = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        txtAnticipo = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        LabelTotal1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtAbonos = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        LabelTotal = new javax.swing.JTextField();
        espacioFecha = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cargando = new javax.swing.JLabel();

        txtDiaEntrega.setForeground(new java.awt.Color(51, 102, 255));
        txtDiaEntrega.setDateFormat("yyyy-MM-dd");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("NOTA DE VENTA");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FOLIO");

        txtFolio.setFont(new java.awt.Font("Times New Roman", 1, 28)); // NOI18N
        txtFolio.setForeground(new java.awt.Color(255, 255, 255));
        txtFolio.setText("-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 365, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFolio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-5, 0, 870, 50));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("FECHA:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 50, -1, 20));

        jLabel63.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel63.setText("-");
        jPanel1.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 50, 100, 20));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setText("-");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, 80, 20));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setText("ESTATUS:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 70, 110, 30));

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
        jButton15.setToolTipText("");
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

        jPanel1.add(panelRegistrarNota1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 50, 50));

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
        jButton17.setToolTipText("");
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

        jPanel1.add(panelRegistrarNota3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 50, 50));

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
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imagen1.png"))); // NOI18N
        jButton18.setToolTipText("");
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

        jPanel1.add(panelRegistrarNota4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 50, 50));

        panelRegistrarNota5.setBackground(new java.awt.Color(255, 255, 255));
        panelRegistrarNota5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelRegistrarNota5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRegistrarNota5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelRegistrarNota5MouseEntered(evt);
            }
        });

        jButton19.setBackground(new java.awt.Color(255, 255, 255));
        jButton19.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jButton19.setForeground(new java.awt.Color(255, 255, 255));
        jButton19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imagen2.png"))); // NOI18N
        jButton19.setToolTipText("");
        jButton19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton19MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton19MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton19MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelRegistrarNota5Layout = new javax.swing.GroupLayout(panelRegistrarNota5);
        panelRegistrarNota5.setLayout(panelRegistrarNota5Layout);
        panelRegistrarNota5Layout.setHorizontalGroup(
            panelRegistrarNota5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelRegistrarNota5Layout.setVerticalGroup(
            panelRegistrarNota5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelRegistrarNota5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 50, 50));

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
        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imagen3.png"))); // NOI18N
        jButton20.setToolTipText("");
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

        jPanel1.add(panelRegistrarNota6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 50, 50));

        panelRegistrarNota7.setBackground(new java.awt.Color(255, 255, 255));
        panelRegistrarNota7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelRegistrarNota7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRegistrarNota7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelRegistrarNota7MouseEntered(evt);
            }
        });

        jButton21.setBackground(new java.awt.Color(255, 255, 255));
        jButton21.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jButton21.setForeground(new java.awt.Color(255, 255, 255));
        jButton21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imagen4.png"))); // NOI18N
        jButton21.setToolTipText("");
        jButton21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton21MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton21MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton21MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelRegistrarNota7Layout = new javax.swing.GroupLayout(panelRegistrarNota7);
        panelRegistrarNota7.setLayout(panelRegistrarNota7Layout);
        panelRegistrarNota7Layout.setHorizontalGroup(
            panelRegistrarNota7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelRegistrarNota7Layout.setVerticalGroup(
            panelRegistrarNota7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel1.add(panelRegistrarNota7, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 50, 50));

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
        jButton22.setToolTipText("");
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

        jPanel1.add(panelRegistrarNota8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 50, 50));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        controladorLista.setViewportView(jList1);

        jPanel4.add(controladorLista, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 430, 10));
        jPanel4.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 430, 10));

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setText("Información cliente");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 12, -1, -1));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel9.setText("  Elegir cliente");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 31, 82, 20));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel10.setText("Código de cliente ");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 61, -1, -1));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel11.setText("Nombre de cliente ");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 83, -1, -1));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel12.setText("Correo electrónico ");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 101, -1, 20));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel13.setText("Número de teléfono ");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 123, -1, 20));

        jTextField1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jTextField1.setToolTipText("Introduzca el nombre o codigo del cliente a buscar");
        jTextField1.setBorder(null);
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
        jPanel4.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 430, 20));

        txtCodigoCliente1.setBackground(new java.awt.Color(204, 204, 204));
        txtCodigoCliente1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtCodigoCliente1.setEnabled(false);
        jPanel4.add(txtCodigoCliente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 430, -1));

        txtNombreCliente.setBackground(new java.awt.Color(204, 204, 204));
        txtNombreCliente.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtNombreCliente.setEnabled(false);
        jPanel4.add(txtNombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 430, -1));

        txtTelefonoI1.setBackground(new java.awt.Color(204, 204, 204));
        txtTelefonoI1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtTelefonoI1.setEnabled(false);
        jPanel4.add(txtTelefonoI1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 430, -1));

        txtcorreoI.setBackground(new java.awt.Color(204, 204, 204));
        txtcorreoI.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtcorreoI.setEnabled(false);
        txtcorreoI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcorreoIActionPerformed(evt);
            }
        });
        jPanel4.add(txtcorreoI, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 430, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 555, 150));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel20.setText("    Hora entregada ");
        jLabel20.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 100, 20));

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel21.setText("Hora de entrega ");
        jLabel21.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 20));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel22.setText(" Fecha de entrega ");
        jLabel22.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 20));

        jLabel23.setBackground(new java.awt.Color(0, 0, 204));
        jLabel23.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 10)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 255));
        jLabel23.setText("Información de entrega");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        txtNombreRecibe.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtNombreRecibe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        txtNombreRecibe.setToolTipText("Selecciona quien eres");
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
        jPanel6.add(txtNombreRecibe, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 170, 23));

        txtfechaEntregada.setBackground(new java.awt.Color(204, 204, 204));
        txtfechaEntregada.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtfechaEntregada.setEnabled(false);
        jPanel6.add(txtfechaEntregada, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 170, -1));

        txtHoraEntregada.setBackground(new java.awt.Color(204, 204, 204));
        txtHoraEntregada.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtHoraEntregada.setEnabled(false);
        jPanel6.add(txtHoraEntregada, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 170, -1));

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel24.setText("Recibe");
        jLabel24.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, -1, 20));

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel25.setText(" Fecha entregada  ");
        jLabel25.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel6.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 100, 100, 20));

        txtParaFecha.setBackground(new java.awt.Color(240, 240, 240));
        txtParaFecha.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtParaFecha.setToolTipText("Determine la fecha de entrega de la nota");
        txtParaFecha.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jPanel6.add(txtParaFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 62, 170, 20));

        txtHoraEntrega.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtHoraEntrega.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtHoraEntrega.setEnabled(false);
        txtHoraEntrega.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtHoraEntregaMouseClicked(evt);
            }
        });
        jPanel6.add(txtHoraEntrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 41, 170, -1));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 105, 290, 150));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel14.setText("Cantidad");
        jLabel14.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel5.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 31, 50, 20));

        txtCantidad.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        txtCantidad.setToolTipText("Introduzca la cantidad");
        txtCantidad.setBorder(null);
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantidadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadKeyTyped(evt);
            }
        });
        jPanel5.add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 130, 20));
        jPanel5.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 130, 10));

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel26.setText("  Concepto");
        jLabel26.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel5.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 54, 70, 20));

        precios.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        precios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        precios.setToolTipText("Seleccione concepto");
        precios.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                preciosItemStateChanged(evt);
            }
        });
        precios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                preciosMouseClicked(evt);
            }
        });
        precios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preciosActionPerformed(evt);
            }
        });
        precios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                preciosKeyPressed(evt);
            }
        });
        jPanel5.add(precios, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 54, 130, 23));

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel27.setText(" Precio unitario");
        jLabel27.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel5.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 78, 100, 20));

        txtPrecioUnitario.setBackground(new java.awt.Color(204, 204, 204));
        txtPrecioUnitario.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtPrecioUnitario.setEnabled(false);
        jPanel5.add(txtPrecioUnitario, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 75, 170, -1));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel15.setText("   Descripción");
        jLabel15.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel5.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 80, 20));

        jTextField3.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jTextField3.setToolTipText("Introduzca algun detalle o descripcion");
        jTextField3.setBorder(null);
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });
        jPanel5.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 130, 20));
        jPanel5.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 130, 10));

        panelAgregar.setBackground(new java.awt.Color(255, 255, 255));
        panelAgregar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelAgregarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelAgregarMouseEntered(evt);
            }
        });

        agregarBoton.setBackground(new java.awt.Color(255, 255, 255));
        agregarBoton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        agregarBoton.setForeground(new java.awt.Color(255, 255, 255));
        agregarBoton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        agregarBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar nota 2.png"))); // NOI18N
        agregarBoton.setToolTipText("Agregar");
        agregarBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        agregarBoton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarBotonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                agregarBotonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                agregarBotonMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelAgregarLayout = new javax.swing.GroupLayout(panelAgregar);
        panelAgregar.setLayout(panelAgregarLayout);
        panelAgregarLayout.setHorizontalGroup(
            panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(agregarBoton, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelAgregarLayout.setVerticalGroup(
            panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(agregarBoton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel5.add(panelAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 50, 50));

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

        eliminarBoton.setBackground(new java.awt.Color(255, 255, 255));
        eliminarBoton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        eliminarBoton.setForeground(new java.awt.Color(255, 255, 255));
        eliminarBoton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eliminarBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imagen5.png"))); // NOI18N
        eliminarBoton.setToolTipText("Seleccione una celda de la tabla para elimiarlo");
        eliminarBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eliminarBoton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eliminarBotonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                eliminarBotonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                eliminarBotonMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelEliminarLayout = new javax.swing.GroupLayout(panelEliminar);
        panelEliminar.setLayout(panelEliminarLayout);
        panelEliminarLayout.setHorizontalGroup(
            panelEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(eliminarBoton, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        panelEliminarLayout.setVerticalGroup(
            panelEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(eliminarBoton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel5.add(panelEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 50, 50));

        jLabel16.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 10)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 255));
        jLabel16.setText("Información de nota");
        jPanel5.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, -1, -1));

        TableVenta = new Table();
        TableVenta.setBackground(new java.awt.Color(204, 204, 255));
        TableVenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TableVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cantidad", "Descripción", "P. Unitario", "Importe", "Comentario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableVenta.setFocusable(false);
        TableVenta.setGridColor(new java.awt.Color(255, 255, 255));
        TableVenta.setOpaque(false);
        TableVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableVentaMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                TableVentaMouseExited(evt);
            }
        });
        TableVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableVentaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(TableVenta);
        if (TableVenta.getColumnModel().getColumnCount() > 0) {
            TableVenta.getColumnModel().getColumn(0).setResizable(false);
            TableVenta.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableVenta.getColumnModel().getColumn(1).setResizable(false);
            TableVenta.getColumnModel().getColumn(2).setResizable(false);
            TableVenta.getColumnModel().getColumn(2).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(3).setResizable(false);
            TableVenta.getColumnModel().getColumn(3).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 530, 210));

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

        modificarBoton.setBackground(new java.awt.Color(255, 255, 255));
        modificarBoton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        modificarBoton.setForeground(new java.awt.Color(255, 255, 255));
        modificarBoton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        modificarBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/MODIFICAR.png"))); // NOI18N
        modificarBoton.setToolTipText("Seleccione una celda de la tabla para modificar sus datos");
        modificarBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        modificarBoton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                modificarBotonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                modificarBotonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                modificarBotonMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelModificarLayout = new javax.swing.GroupLayout(panelModificar);
        panelModificar.setLayout(panelModificarLayout);
        panelModificarLayout.setHorizontalGroup(
            panelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modificarBoton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelModificarLayout.setVerticalGroup(
            panelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modificarBoton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel5.add(panelModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 50, 50));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 850, 250));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 10)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 255));
        jLabel18.setText("Notas anteriores");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 6, -1, -1));

        APOYOPARAOBTNEREMPELADO.setText("-");
        jPanel7.add(APOYOPARAOBTNEREMPELADO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        txtApoyoeliminar.setEditable(false);
        txtApoyoeliminar.setEnabled(false);
        jPanel7.add(txtApoyoeliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 20, 10));

        TableNotasCliente = new Table2();
        TableNotasCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Folio", "Total", "Pagado", "Adeudo", "Estatus"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableNotasCliente.setToolTipText("Haz doble click para poder hacer modificaciones al precio seleccionado");
        TableNotasCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableNotasClienteMouseClicked(evt);
            }
        });
        TableNotasCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableNotasClienteKeyPressed(evt);
            }
        });
        spTable.setViewportView(TableNotasCliente);
        if (TableNotasCliente.getColumnModel().getColumnCount() > 0) {
            TableNotasCliente.getColumnModel().getColumn(0).setPreferredWidth(30);
            TableNotasCliente.getColumnModel().getColumn(1).setPreferredWidth(20);
            TableNotasCliente.getColumnModel().getColumn(2).setPreferredWidth(30);
            TableNotasCliente.getColumnModel().getColumn(3).setPreferredWidth(30);
            TableNotasCliente.getColumnModel().getColumn(4).setPreferredWidth(30);
            TableNotasCliente.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        jPanel7.add(spTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 20, 504, 84));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 515, 515, 110));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 170, 10));

        txtAnticipo.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtAnticipo.setToolTipText("Para guardar el anticipo presione ENTER");
        txtAnticipo.setBorder(null);
        txtAnticipo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtAnticipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAnticipoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAnticipoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAnticipoKeyTyped(evt);
            }
        });
        jPanel2.add(txtAnticipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 34, 170, -1));

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel29.setText(" Total nota");
        jLabel29.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 7, 60, 20));

        LabelTotal1.setBackground(new java.awt.Color(204, 204, 204));
        LabelTotal1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        LabelTotal1.setText("-----");
        LabelTotal1.setEnabled(false);
        jPanel2.add(LabelTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 7, 170, -1));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel17.setText("Anticipo");
        jLabel17.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 50, 20));

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel30.setText("   Abonos realizados ");
        jLabel30.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 55, 120, 20));

        txtAbonos.setBackground(new java.awt.Color(204, 204, 204));
        txtAbonos.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txtAbonos.setEnabled(false);
        jPanel2.add(txtAbonos, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 55, 170, -1));

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel31.setText("  Pendiente de pagar");
        jLabel31.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 77, 120, 20));

        LabelTotal.setBackground(new java.awt.Color(204, 204, 204));
        LabelTotal.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        LabelTotal.setText("-----");
        LabelTotal.setEnabled(false);
        jPanel2.add(LabelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 77, 170, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 515, 330, 110));

        espacioFecha.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        espacioFecha.setForeground(new java.awt.Color(0, 0, 153));
        jPanel1.add(espacioFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 70, 320, 30));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel19.setText(" HORA:");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, -1, 20));

        cargando.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.add(cargando, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 70, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseClicked
        eventoF1();
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

        eventoF2();
    }//GEN-LAST:event_jButton17MouseClicked

    private void jButton17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseEntered
        if (guardar == true) {
            panelRegistrarNota3.setBackground(new Color(153, 204, 255));
            jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        } else {
            jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
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
        eventoF3();
    }//GEN-LAST:event_jButton18MouseClicked

    private void jButton18MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseEntered
        if (ver == true) {
            panelRegistrarNota4.setBackground(new Color(153, 204, 255));
            jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        } else {
            jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
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

    private void jButton19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseClicked
        eventoF4();
    }//GEN-LAST:event_jButton19MouseClicked

    private void jButton19MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseEntered
        if (ver == true) {
            panelRegistrarNota5.setBackground(new Color(153, 204, 255));
            jButton19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        } else {
            jButton19.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_jButton19MouseEntered

    private void jButton19MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseExited
        panelRegistrarNota5.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton19MouseExited

    private void panelRegistrarNota5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota5MouseClicked

    private void panelRegistrarNota5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota5MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota5MouseEntered

    private void jButton20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseClicked
        eventoF5();
    }//GEN-LAST:event_jButton20MouseClicked

    private void jButton20MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseEntered
        if (entregar == true) {
            panelRegistrarNota6.setBackground(new Color(153, 204, 255));
        } else {
            jButton20.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_jButton20MouseEntered

    private void jButton20MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseExited
        panelRegistrarNota6.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton20MouseExited

    private void panelRegistrarNota6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota6MouseClicked

    private void panelRegistrarNota6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota6MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota6MouseEntered

    private void jButton21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21MouseClicked
        eventoF6();
    }//GEN-LAST:event_jButton21MouseClicked

    private void jButton21MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21MouseEntered
        if (abonar == true) {
            panelRegistrarNota7.setBackground(new Color(153, 204, 255));
            jButton21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        } else {
            jButton21.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_jButton21MouseEntered

    private void jButton21MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21MouseExited
        panelRegistrarNota7.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton21MouseExited

    private void panelRegistrarNota7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota7MouseClicked

    private void panelRegistrarNota7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRegistrarNota7MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelRegistrarNota7MouseEntered

    private void jButton22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseClicked
        eventoF7();
    }//GEN-LAST:event_jButton22MouseClicked

    private void jButton22MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseEntered
        if (cancelar == true) {
            panelRegistrarNota8.setBackground(new Color(153, 204, 255));
        } else {
            jButton22.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
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

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (evt.getClickCount() == 2) {
            if (isNumeric(jTextField1.getText()) == true) {
                Cliente alistar = client.BuscarPorCodigo(Integer.parseInt(jTextField1.getText()));
                txtCodigoCliente1.setText(String.valueOf(alistar.getId()));
                txtNombreCliente.setText(alistar.getNombre() + " " + alistar.getApellido());
                txtTelefonoI1.setText(alistar.getTelefono());
                txtcorreoI.setText(alistar.getCorreo());
                jTextField1.setText("");
                jList1.setVisible(false);
                controladorLista.setVisible(false);
            } else {
                int k = jList1.getSelectedIndex();
                List<Cliente> lista = client.buscarLetra(jTextField1.getText());
                if (lista.size() == 0) {
                    Cliente clienteAVacir = client.BuscarPorCodigo(listaFolios.get(k));
                    txtCodigoCliente1.setText(String.valueOf(clienteAVacir.getId()));
                    txtNombreCliente.setText(clienteAVacir.getNombre() + " " + clienteAVacir.getApellido());
                    txtTelefonoI1.setText(clienteAVacir.getTelefono());
                    txtcorreoI.setText(clienteAVacir.getCorreo());
                    jTextField1.setText("");
                    jList1.setVisible(false);
                    controladorLista.setVisible(false);
                } else {

                    txtCodigoCliente1.setText(String.valueOf(lista.get(k).getId()));
                    txtNombreCliente.setText(lista.get(k).getNombre() + " " + lista.get(k).getApellido());
                    txtTelefonoI1.setText(lista.get(k).getTelefono());
                    txtcorreoI.setText(lista.get(k).getCorreo());
                    jTextField1.setText("");
                    jList1.setVisible(false);
                    controladorLista.setVisible(false);
                }
            }
            listarNotasPorNombre(Integer.parseInt(txtCodigoCliente1.getText()));
            if ("".equals(txtTelefonoI1.getText())) {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Necesita actualizar el teléfono del cliente");
                panel.showNotification();
                txtCodigoCliente1.setText("");
                txtNombreCliente.setText("");
                txtTelefonoI1.setText("");
                txtcorreoI.setText("");
                LimpiarTabla();
            }
        }

    }//GEN-LAST:event_jList1MouseClicked

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

            case KeyEvent.VK_F6:
                eventoF6();
                break;

            case KeyEvent.VK_F7:
                eventoF7();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        DefaultListModel model = new DefaultListModel();
        controladorLista.setVisible(true);
        jList1.setVisible(true);
        controladorLista.setBounds(controladorLista.getX(), controladorLista.getY(), controladorLista.getWidth(), 85);
        jList1.setBounds(jList1.getX(), jList1.getY(), jList1.getWidth(), 85);
        if (isNumeric(jTextField1.getText()) == true) {
            Cliente alistar = client.BuscarPorCodigo(Integer.parseInt(jTextField1.getText()));
            if (alistar.getEstado() == 1) {
                model.addElement(alistar.getNombre() + " " + alistar.getApellido());
                jList1.setModel(model);
                if ("".equals(alistar.getNombre())) {
                    controladorLista.setVisible(false);
                    jList1.setVisible(false);
                }
            }
        } else {
            List<Cliente> lista = client.buscarLetra(jTextField1.getText());
            if (lista.size() == 0) {
                listaFolios = new ArrayList<Integer>();
                List<Cliente> todosClientes = client.ListarCliente();
                for (int i = 0; i < todosClientes.size(); i++) {
                    String nombreCompleto = todosClientes.get(i).getNombre() + " " + todosClientes.get(i).getApellido();
                    if (isSubstring(nombreCompleto, jTextField1.getText())) {
                        model.addElement(nombreCompleto);
                        listaFolios.add(todosClientes.get(i).getId());
                    }

                }
            }
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getEstado() == 1) {
                    model.addElement(lista.get(i).getNombre() + " " + lista.get(i).getApellido());
                }
            }
            jList1.setModel(model);
        }

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped

    }//GEN-LAST:event_jTextField1KeyTyped

    private void txtcorreoIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcorreoIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcorreoIActionPerformed

    private void txtNombreRecibeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreRecibeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreRecibeActionPerformed

    private void txtNombreRecibeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreRecibeKeyPressed

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

            case KeyEvent.VK_F6:
                eventoF6();
                break;

            case KeyEvent.VK_F7:
                eventoF7();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_txtNombreRecibeKeyPressed

    private void txtCantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyPressed

        int codigo = evt.getKeyCode();
        switch (codigo) {

            case KeyEvent.VK_ENTER:
                eventoEnterAgregar();
                break;

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

            case KeyEvent.VK_F6:
                eventoF6();
                break;

            case KeyEvent.VK_F7:
                eventoF7();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_txtCantidadKeyPressed

    private void txtCantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadKeyReleased

    private void txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyTyped
        event.numberDecimalKeyPress(evt, txtCantidad);
    }//GEN-LAST:event_txtCantidadKeyTyped

    private void preciosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_preciosItemStateChanged
        List<Precios> lsNotas = precio.getPrecios();
        if (precios.getSelectedIndex() == -1) {

        } else {
            for (int i = 0; i < lsNotas.size(); i++) {
                String pa = "" + precios.getSelectedItem().toString();
                if (pa.equals(lsNotas.get(i).getNombre())) {

                    txtPrecioUnitario.setText(String.format("%.2f", lsNotas.get(i).getPrecioU()));
                }
            }
        }
    }//GEN-LAST:event_preciosItemStateChanged

    private void preciosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_preciosMouseClicked
        rellenarPrecios();
    }//GEN-LAST:event_preciosMouseClicked

    private void preciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preciosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_preciosActionPerformed

    private void preciosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_preciosKeyPressed

        int codigo = evt.getKeyCode();
        switch (codigo) {

            case KeyEvent.VK_ENTER:
                eventoEnterAgregar();
                break;

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

            case KeyEvent.VK_F6:
                eventoF6();
                break;

            case KeyEvent.VK_F7:
                eventoF7();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_preciosKeyPressed

    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyPressed

        int codigo = evt.getKeyCode();
        switch (codigo) {

            case KeyEvent.VK_ENTER:
                eventoEnterAgregar();
                break;

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

            case KeyEvent.VK_F6:
                eventoF6();
                break;

            case KeyEvent.VK_F7:
                eventoF7();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_jTextField3KeyPressed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3KeyTyped

    private void agregarBotonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarBotonMouseClicked
        eventoEnterAgregar();
    }//GEN-LAST:event_agregarBotonMouseClicked

    private void agregarBotonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarBotonMouseEntered
        if (agregar == true) {
            panelAgregar.setBackground(new Color(153, 204, 255));
            agregarBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        } else {
            agregarBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_agregarBotonMouseEntered

    private void agregarBotonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarBotonMouseExited
        panelAgregar.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_agregarBotonMouseExited

    private void panelAgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAgregarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelAgregarMouseClicked

    private void panelAgregarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAgregarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelAgregarMouseEntered

    private void eliminarBotonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarBotonMouseClicked
        if (btnEliminar == true) {
            if (!"".equals(txtApoyoeliminar.getText())) {
                modelo = (DefaultTableModel) TableVenta.getModel();
                modelo.removeRow(TableVenta.getSelectedRow());
                totalPagar();
                totalPagarNota();
                if (TableVenta.getRowCount() == 0) {
                    LabelTotal1.setText("----");
                    LabelTotal.setText("-----");
                }

            } else {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Seleccione una fila para eliminar");
                panel.showNotification();
            }
        }
    }//GEN-LAST:event_eliminarBotonMouseClicked

    private void eliminarBotonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarBotonMouseEntered
        if (btnEliminar == true) {
            panelEliminar.setBackground(new Color(153, 204, 255));
            eliminarBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        } else {
            eliminarBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_eliminarBotonMouseEntered

    private void eliminarBotonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarBotonMouseExited
        panelEliminar.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_eliminarBotonMouseExited

    private void panelEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelEliminarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelEliminarMouseClicked

    private void panelEliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelEliminarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelEliminarMouseEntered

    private void TableVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentaMouseClicked
        int fila = TableVenta.rowAtPoint(evt.getPoint());
        txtApoyoeliminar.setText(TableVenta.getValueAt(fila, 0).toString());
    }//GEN-LAST:event_TableVentaMouseClicked

    private void TableVentaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentaMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_TableVentaMouseExited

    private void TableVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableVentaKeyPressed

    }//GEN-LAST:event_TableVentaKeyPressed

    private void modificarBotonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modificarBotonMouseClicked
        if (btnModificar == true) {
            if (!"".equals(txtApoyoeliminar.getText())) {
                modelo = (DefaultTableModel) TableVenta.getModel();
                txtCantidad.setText(TableVenta.getValueAt(TableVenta.getSelectedRow(), 0).toString());
                jTextField3.setText(TableVenta.getValueAt(TableVenta.getSelectedRow(), 4).toString());
                precios.setSelectedItem(TableVenta.getValueAt(TableVenta.getSelectedRow(), 1).toString());
                modelo.removeRow(TableVenta.getSelectedRow());
                totalPagar();
                totalPagarNota();
                if (TableVenta.getRowCount() == 0) {
                    LabelTotal1.setText("----");
                    LabelTotal.setText("-----");
                }

            } else {
                Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Seleccione una fila para eliminar");
                panel.showNotification();
            }
        }
    }//GEN-LAST:event_modificarBotonMouseClicked

    private void modificarBotonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modificarBotonMouseEntered
        if (btnModificar == true) {
            panelModificar.setBackground(new Color(153, 204, 255));
            modificarBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        } else {
            modificarBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_modificarBotonMouseEntered

    private void modificarBotonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modificarBotonMouseExited
        panelModificar.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_modificarBotonMouseExited

    private void panelModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelModificarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelModificarMouseClicked

    private void panelModificarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelModificarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelModificarMouseEntered

    private void txtAnticipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnticipoKeyPressed

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

            case KeyEvent.VK_F6:
                eventoF6();
                break;

            case KeyEvent.VK_F7:
                eventoF7();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_txtAnticipoKeyPressed

    private void txtAnticipoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnticipoKeyReleased
        totalPagarNota();
    }//GEN-LAST:event_txtAnticipoKeyReleased

    private void txtAnticipoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnticipoKeyTyped
        char c = event.numberDecimalKeyPress(evt, txtAnticipo);

    }//GEN-LAST:event_txtAnticipoKeyTyped

    private void txtHoraEntregaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHoraEntregaMouseClicked
        if (ver == false) {

            selectorHora.showPopup(this, 605, 198);
        }


    }//GEN-LAST:event_txtHoraEntregaMouseClicked

    private void TableNotasClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableNotasClienteMouseClicked
        if (evt.getClickCount() == 2) {
            int fila = TableNotasCliente.rowAtPoint(evt.getPoint());
            int buscando = Integer.parseInt(TableNotasCliente.getValueAt(fila, 1).toString());
            vistaNota vN = new vistaNota(new javax.swing.JFrame(), true);
            vN.validandoDatos(buscando, false);
            vN.setVisible(true);

        }
    }//GEN-LAST:event_TableNotasClienteMouseClicked

    private void TableNotasClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableNotasClienteKeyPressed

        int codigo = evt.getKeyCode();
        switch (codigo) {

            case KeyEvent.VK_ENTER:
                eventoEnterAgregar();
                break;

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

            case KeyEvent.VK_F6:
                eventoF6();
                break;

            case KeyEvent.VK_F7:
                eventoF7();
                break;

            case KeyEvent.VK_ESCAPE:
                dispose();
                break;

        }
    }//GEN-LAST:event_TableNotasClienteKeyPressed

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
            java.util.logging.Logger.getLogger(vistaNota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(vistaNota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(vistaNota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(vistaNota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                vistaNota dialog = new vistaNota(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel APOYOPARAOBTNEREMPELADO;
    private javax.swing.JTextField LabelTotal;
    private javax.swing.JTextField LabelTotal1;
    private javax.swing.JTable TableNotasCliente;
    private javax.swing.JTable TableVenta;
    private javax.swing.JLabel agregarBoton;
    private javax.swing.JTextField apoyoFecha;
    private javax.swing.JLabel cargando;
    private javax.swing.JScrollPane controladorLista;
    private javax.swing.JLabel eliminarBoton;
    private javax.swing.JLabel espacioFecha;
    private javax.swing.JLabel jButton15;
    private javax.swing.JLabel jButton17;
    private javax.swing.JLabel jButton18;
    private javax.swing.JLabel jButton19;
    private javax.swing.JLabel jButton20;
    private javax.swing.JLabel jButton21;
    private javax.swing.JLabel jButton22;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel modificarBoton;
    private javax.swing.JPanel panelAgregar;
    private javax.swing.JPanel panelEliminar;
    private javax.swing.JPanel panelModificar;
    private javax.swing.JPanel panelRegistrarNota1;
    private javax.swing.JPanel panelRegistrarNota3;
    private javax.swing.JPanel panelRegistrarNota4;
    private javax.swing.JPanel panelRegistrarNota5;
    private javax.swing.JPanel panelRegistrarNota6;
    private javax.swing.JPanel panelRegistrarNota7;
    private javax.swing.JPanel panelRegistrarNota8;
    private javax.swing.JComboBox<String> precios;
    private com.raven.swing.TimePicker selectorHora;
    private javax.swing.JScrollPane spTable;
    private javax.swing.JTextField txtAbonos;
    private javax.swing.JTextField txtAnticipo;
    private javax.swing.JTextField txtApoyoeliminar;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCodigoCliente1;
    private com.raven.datechooser.DateChooser txtDiaEntrega;
    private javax.swing.JLabel txtFolio;
    private javax.swing.JTextField txtHoraEntrega;
    private javax.swing.JTextField txtHoraEntregada;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JComboBox<String> txtNombreRecibe;
    private javax.swing.JTextField txtParaFecha;
    private javax.swing.JTextField txtPrecioUnitario;
    private javax.swing.JTextField txtTelefonoI1;
    private javax.swing.JTextField txtcorreoI;
    private javax.swing.JTextField txtfechaEntregada;
    // End of variables declaration//GEN-END:variables

    private void Seticon() {
    setIconImage(Toolkit.getDefaultToolkit().getImage("Iconos\\logo 100x100.jpg"));
    }

    private void ticket(int id) throws ParseException {
        try {
            FileOutputStream archivo;
            File file = new File("C:\\Program Files (x86)\\AppLavanderia\\Tickets\\venta" + id + " cliente.pdf");

            //File file = new File("src/pdf/ticket" + id + ".pdf");
            //File file = new File("ticket" + id + ".pdf");
            archivo = new FileOutputStream(file);
            Rectangle pageSize = new Rectangle(140.76f, 500f); //ancho y alto
            Document doc = new Document(pageSize, 0, 0, 0, 0);
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            //Image img = Image.getInstance("src/Imagenes/logo 100x100.jpg");
            Image img = Image.getInstance("C:\\Program Files (x86)\\AppLavanderia\\Iconos\\logo 100x100.jpg");
            configura = gastos.buscarDatos();

            Font negrita = new Font(Font.FontFamily.HELVETICA, configura.getLetraGrande(), Font.UNDERLINE | Font.ITALIC | Font.BOLD);
            Font letra2 = new Font(Font.FontFamily.HELVETICA, configura.getLetraChica(), Font.BOLD);

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
            doc.add(logo);

            String rfc = configura.getRfc();
            String nombre = configura.getNomnbre();
            String tel = configura.getTelefono();
            String direccion = configura.getDireccion();
            String encargado = configura.getEncargado();
            String razonSocial = configura.getRazonSocial();
            String horario = configura.getHorario();

            PdfPTable encabezado = new PdfPTable(1);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{100f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Chunk.ALIGN_MIDDLE);
            PdfPCell cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(nombre, letra2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            cell = new PdfPCell(new Phrase(encargado, letra2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            cell = new PdfPCell(new Phrase("Rfc: " + rfc, letra2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            cell = new PdfPCell(new Phrase(razonSocial, letra2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            cell = new PdfPCell(new Phrase(horario, letra2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            cell = new PdfPCell(new Phrase(direccion, letra2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            cell = new PdfPCell(new Phrase(tel, letra2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);

            doc.add(encabezado);

            PdfPTable tablapro = new PdfPTable(2);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnaEncabezado = new float[]{60f, 40f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Recepción: " + jLabel63.getText() + " " + jLabel7.getText(), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Folio: " + txtFolio.getText(), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            doc.add(Chunk.NEWLINE);
            doc.add(tablapro);

            tablapro = new PdfPTable(1);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnaEncabezado = new float[]{100f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);

            Empleados emplead = emple.seleccionarEmpleado(txtNombreRecibe.getSelectedItem().toString(), -1);
            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Recibe: " + String.format("%0" + 2 + "d", Integer.valueOf(emplead.getId())) + " " + emplead.getNombre(), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Cliente: " + txtNombreCliente.getText(), new Font(Font.FontFamily.HELVETICA, configura.getLetraGrande() + 1, Font.UNDERLINE | Font.ITALIC | Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            doc.add(Chunk.NEWLINE);
            doc.add(tablapro);

            tablapro = new PdfPTable(4);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnaEncabezado = new float[]{18f, 40f, 20f, 20f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);

            PdfPCell pro1 = new PdfPCell(new Phrase("Cant", letra2));
            PdfPCell pro2 = new PdfPCell(new Phrase("Concepto", letra2));
            PdfPCell pro3 = new PdfPCell(new Phrase("Precio", letra2));
            PdfPCell pro4 = new PdfPCell(new Phrase("Importe", letra2));

            pro1.setBorder(0);
            pro2.setBorder(0);
            pro3.setBorder(0);
            pro4.setBorder(0);

            pro1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro4.setHorizontalAlignment(Element.ALIGN_CENTER);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("____", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("___________", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("_____", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("_____", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);
            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            doc.add(tablapro);

            for (int i = 0; i < TableVenta.getRowCount(); i++) {
                tablapro = new PdfPTable(4);
                tablapro.setWidthPercentage(100);
                tablapro.getDefaultCell().setBorder(0);
                columnaEncabezado = new float[]{18f, 35f, 20f, 25f};
                tablapro.setWidths(columnaEncabezado);
                tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(TableVenta.getValueAt(i, 0).toString(), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(TableVenta.getValueAt(i, 1).toString(), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(TableVenta.getValueAt(i, 2).toString(), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(TableVenta.getValueAt(i, 3).toString(), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                doc.add(tablapro);
                if (!TableVenta.getValueAt(i, 4).toString().equals("")) {
                    tablapro = new PdfPTable(3);
                    tablapro.setWidthPercentage(100);
                    tablapro.getDefaultCell().setBorder(0);
                    float[] columnapro = new float[]{18f, 60f, 20f};
                    tablapro.setWidths(columnapro);
                    tablapro.addCell("");
                    cell = new PdfPCell();
                    cell = new PdfPCell(new Phrase(TableVenta.getValueAt(i, 4).toString(), letra2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(0);
                    tablapro.addCell(cell);
                    tablapro.addCell("");
                    doc.add(tablapro);

                }

            }
            tablapro = new PdfPTable(4);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnaEncabezado = new float[]{18f, 40f, 20f, 20f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("____", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("___________", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("_____", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("_____", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            doc.add(tablapro);

            tablapro = new PdfPTable(3);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnaEncabezado = new float[]{50f, 20f, 30f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);
            
            Font letra3 = new Font(Font.FontFamily.HELVETICA, configura.getLetraChica()-1, Font.BOLD);
            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Total", letra3));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(LabelTotal1.getText(), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Datos de entrega", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Anticipo", letra3));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(txtAnticipo.getText(), letra2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String fechaE = sdf.format(txtDiaEntrega.getDateBD());

            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            String horaE = txtHoraEntrega.getText();

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(fechaE + " " + horaE, negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Pendiente", letra3));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();

            Number primera = formateador.parse(removefirstChar(LabelTotal1.getText()));
            double primeraCantidad = primera.doubleValue();
            Number segunda = formateador.parse(removefirstChar(txtAnticipo.getText()));
            double segundaCantidad = segunda.doubleValue();

            double sacandoPendiente = primeraCantidad - segundaCantidad;

            cell = new PdfPCell(new Phrase("$" + formateador.format((int) sacandoPendiente), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);
            tablapro.addCell(cell);

            doc.add(tablapro);

            if (promocionDao.promocionActiva() == 1 && promocionDao.tieneBoleto(id)) {
                Font letraRifa = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC | Font.BOLD);
                Paragraph textoRifa = new Paragraph(datos.getMensaje() + "\nFOLIO :" + promocionDao.getFolioPromocion(id), letraRifa);
                textoRifa.setAlignment(Element.ALIGN_CENTER);
                doc.add(textoRifa);
                doc.add(Chunk.NEWLINE);
                doc.add(Chunk.NEWLINE);
            }

            Paragraph notasCliente = new Paragraph(configura.getMensaje(), letra2);
            doc.add(notasCliente);

            doc.close();
            archivo.close();
        } catch (DocumentException | IOException e) {
            System.out.println(e.toString());
        }
    }

    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == hi) {
            calcula();
            horas = String.valueOf(Integer.parseInt(horas) - 1);
            jLabel7.setText(horas + ":" + min);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }

    private void calcula() {
        Calendar calendario = new GregorianCalendar();
        Date fechaHoraactual = new Date();
        calendario.setTime(fechaHoraactual);
        ampm = calendario.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

        horas = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);

        min = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
        seg = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND);
    }

    private void ticketTienda(int id) throws ParseException {
        try {
            FileOutputStream archivo;
            File file = new File("C:\\Program Files (x86)\\AppLavanderia\\Tickets\\venta" + id + " tienda.pdf");

            //File file = new File("src/pdf/ticket" + id + ".pdf");
            //File file = new File("ticket" + id + ".pdf");
            archivo = new FileOutputStream(file);
            Rectangle pageSize = new Rectangle(140.76f, 500f); //ancho y alto
            Document doc = new Document(pageSize, 0, 0, 0, 0);
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            //Image img = Image.getInstance("src/Imagenes/logo 100x100.jpg");

            Font negritaSubrayado = new Font(Font.FontFamily.HELVETICA, 13, Font.UNDERLINE | Font.ITALIC | Font.BOLD);
            Font negrita = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC | Font.BOLD);
            Font letra2 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
                        Font letra3 = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD);

            configura = gastos.buscarDatos();
            PdfPTable encabezado = new PdfPTable(1);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{100f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Chunk.ALIGN_MIDDLE);
            PdfPCell cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(configura.getNomnbre(), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Folio: " + txtFolio.getText(), negritaSubrayado));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            encabezado.addCell(cell);

            cell = new PdfPCell(new Phrase(txtNombreCliente.getText(), negritaSubrayado));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            encabezado.addCell(cell);

            cell = new PdfPCell(new Phrase("Recepción: " + jLabel63.getText() + " " + jLabel7.getText(), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            encabezado.addCell(cell);

            doc.add(encabezado);
            PdfPTable tablapro;
            Paragraph lineas = new Paragraph("-------------------------------------", letra2);

            for (int i = 0; i < TableVenta.getRowCount(); i++) {
                doc.add(lineas);
                tablapro = new PdfPTable(3);
                tablapro.setWidthPercentage(100);
                tablapro.getDefaultCell().setBorder(0);
                columnaEncabezado = new float[]{25f, 50f, 25f};
                tablapro.setWidths(columnaEncabezado);
                tablapro.setHorizontalAlignment(Chunk.ALIGN_CENTER);

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(TableVenta.getValueAt(i, 0).toString(), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(0);
                tablapro.addCell(cell);

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(TableVenta.getValueAt(i, 1).toString(), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                tablapro.addCell(cell);

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(TableVenta.getValueAt(i, 2).toString(), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                doc.add(tablapro);

                tablapro = new PdfPTable(2);
                tablapro.setWidthPercentage(100);
                tablapro.getDefaultCell().setBorder(0);
                columnaEncabezado = new float[]{60f,40f};
                tablapro.setWidths(columnaEncabezado);
                tablapro.setHorizontalAlignment(Chunk.ALIGN_CENTER);

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(TableVenta.getValueAt(i, 4).toString(), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(0);
                tablapro.addCell(cell);

                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(TableVenta.getValueAt(i, 3).toString(), letra2));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                tablapro.addCell(cell);
                doc.add(tablapro);

            }

            doc.add(lineas);

            tablapro = new PdfPTable(2);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnaEncabezado = new float[]{50f, 50f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_LEFT);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Total", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(LabelTotal1.getText(), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Anticipo", negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(txtAnticipo.getText(), negrita));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Pendiente", negritaSubrayado));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();

            Number primera = formateador.parse(removefirstChar(LabelTotal1.getText()));
            double primeraCantidad = primera.doubleValue();
            Number segunda = formateador.parse(removefirstChar(txtAnticipo.getText()));
            double segundaCantidad = segunda.doubleValue();

            double sacandoPendiente = primeraCantidad - segundaCantidad;

            cell = new PdfPCell(new Phrase("$" + formateador.format((int) sacandoPendiente), negritaSubrayado));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);
            tablapro.addCell(cell);

            doc.add(tablapro);

            doc.add(Chunk.NEWLINE);

            tablapro = new PdfPTable(1);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnaEncabezado = new float[]{100f};
            tablapro.setWidths(columnaEncabezado);
            tablapro.setHorizontalAlignment(Chunk.ALIGN_MIDDLE);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Datos de entrega", negritaSubrayado));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String fechaE = sdf.format(txtDiaEntrega.getDateBD());

            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            String horaE = txtHoraEntrega.getText();

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(fechaE + " " + horaE, negritaSubrayado));
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
