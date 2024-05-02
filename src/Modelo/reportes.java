/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

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
import java.awt.Desktop;
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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
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
public class reportes {

    GastosDao configuracionesDao = new GastosDao();
    DecimalFormat formateador = new DecimalFormat("#,###,##0.00");
    DecimalFormat df = new DecimalFormat("$ #,##0.00;($ #,##0.00)");
    NotaDao nota = new NotaDao();
    ClienteDao client = new ClienteDao();
    anticipoDao anticip = new anticipoDao();

    private static CellStyle getContabilidadCellStyle(Workbook workbook, DecimalFormat df) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat(df.format(0)));
        return style;
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

    public void reportePDF(String tipo, JTable tableLista, JTable tableTotales, String fechaInicial, String fechaFinal, String encabezadoTexto) throws DocumentException {
        try {
            String fechaInicialBien = fechaFormatoCorrecto(fechaInicial);
            String fechaFInalBien = fechaFormatoCorrecto(fechaFinal);

            config configura = configuracionesDao.buscarDatos();
            com.itextpdf.text.Font letra = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10);
            com.itextpdf.text.Font negrita = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD);
            FileOutputStream archivo;
            //File file = new File("corte" + fechaHoy + ".pdf");
            File file = new File("C:\\Program Files (x86)\\AppLavanderia\\ReportesPDF\\Reporte de " + tipo + ".pdf");
            archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            //Image img = Image.getInstance("Iconos\\logo 100x100.jpg");
            Image img = Image.getInstance("C:\\Program Files (x86)\\AppLavanderia\\Iconos\\logo 100x100.jpg");

            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{85f, 15f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell = new PdfPCell();
            String tituloEncabezado = " DEL " + fechaInicialBien + " AL " + fechaFInalBien;
            cell = new PdfPCell(new Phrase(configura.getNomnbre() + encabezadoTexto, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            encabezado.addCell(img);
            doc.add(encabezado);
            Paragraph dejarEspacio = new Paragraph();
            dejarEspacio.add(Chunk.NEWLINE);
            doc.add(dejarEspacio);

            String nombreEncabezado = "";
            TableColumnModel columnModel = tableLista.getColumnModel();
            float[] columnapro = new float[columnModel.getColumnCount()];
            
            List<String> encabezados = new ArrayList<>();
            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                //TableColumn column = columnModel.getColumn(i);
                //columnapro[i] = column.getWidth();
                nombreEncabezado = tableLista.getModel().getColumnName(i);
                encabezados.add(nombreEncabezado);
            }
            PdfPTable tablapro = new PdfPTable(columnModel.getColumnCount());
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            if (tipo.equals("Ventas")) {
                columnapro = new float[]{15f, 10f, 20f, 12f, 10f, 13f, 10f, 10f, 10f, 8f};
            }
            if (tipo.equals("Notas de crédito")) {
                columnapro = new float[]{25f, 13f, 13f, 55f, 30f, 20f, 20f, 20f, 20f};
            }
            if (tipo.equals("Cobranza")) {
                columnapro = new float[]{30f, 20f, 20f, 80f, 25f, 25f, 10f, 10f};
            }
            if (tipo.equals("Concentrado general")) {
                columnapro = new float[]{30f, 80f, 80f, 80f, 80f};
            }
            if (tipo.equals("Gastos")) {
                columnapro = new float[]{15f, 10f, 20f, 15f, 12f, 12f};
            }
            if (tipo.equals("Entregas")) {
                columnapro = new float[]{13f, 8f, 19f, 10f, 10f, 10f, 7f, 8f, 17f, 6f};
            }
            if (tipo.equals("Abonos")) {
                columnapro = new float[]{13f, 8f, 19f, 10f, 10f, 10f, 7f, 6f, 17f, 8f};
            }
            if (tipo.equals("Notas canceladas")) {
                columnapro = new float[]{12f, 8f, 17f, 12f, 12f, 12f, 9f, 20f};
            }
            tablapro.setWidths(columnapro);

            for (String encabezadop : encabezados) {
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(encabezadop, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablapro.addCell(cell);
            }

            DefaultTableModel modelo = (DefaultTableModel) tableLista.getModel();

// Agregar el contenido de las filas al PDF
            for (int fila = 0; fila < modelo.getRowCount(); fila++) {
                for (int columna = 0; columna < modelo.getColumnCount(); columna++) {
                    Object datoCelda = modelo.getValueAt(fila, columna);
                    cell = new PdfPCell();
                    if (datoCelda instanceof String && ((String) datoCelda).startsWith("$")) {
                        // Eliminar el primer caracter '$' de la cadena
                        String datoSinSignoDolar = ((String) datoCelda).substring(1);
                        cell = new PdfPCell(new Phrase(datoSinSignoDolar, letra)); // Usa tu fuente personalizada si es necesario
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    } else {
                        // Si no comienza con '$', simplemente agregar el contenido de la celda tal como está
                        cell = new PdfPCell(new Phrase(datoCelda.toString(), letra)); // Usa tu fuente personalizada si es necesario
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    }

                    cell.setBorder(0);
                    tablapro.addCell(cell);
                }
            }
           
            modelo = (DefaultTableModel) tableTotales.getModel();

// Agregar el contenido de las filas al PDF
            for (int fila = 0; fila < modelo.getRowCount(); fila++) {
                for (int columna = 0; columna < modelo.getColumnCount(); columna++) {
                    Object datoCelda = modelo.getValueAt(fila, columna);
                    cell = new PdfPCell();
                    if (datoCelda instanceof String && ((String) datoCelda).startsWith("$")) {
                        // Eliminar el primer caracter '$' de la cadena
                        String datoSinSignoDolar = ((String) datoCelda).substring(1);
                        cell = new PdfPCell(new Phrase(datoSinSignoDolar, negrita)); // Usa tu fuente personalizada si es necesario
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    } else {
                        // Si no comienza con '$', simplemente agregar el contenido de la celda tal como está
                        cell = new PdfPCell(new Phrase(datoCelda.toString(), negrita)); // Usa tu fuente personalizada si es necesario
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    }

                    cell.setBorder(0);
                    tablapro.addCell(cell);
                }
            }

            doc.add(tablapro);
            doc.add(dejarEspacio);
            doc.add(dejarEspacio);
            doc.add(dejarEspacio);
            doc.add(dejarEspacio);

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

            doc.add(tablaFinal);

            doc.close();
            archivo.close();
            Desktop.getDesktop().open(file);
        } catch (DocumentException | IOException e) {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe cerrar antes el documento");
            panel.showNotification();
        }
    }

    public void reportePDFConcentradoGeneral(String tipo, JTable tableLista, JTable tableTotales, String fechaInicial, String fechaFinal, String encabezadoTexto, double totalVendido, double totalCobrado, double totalGastado) throws DocumentException {
        try {
                        System.out.println(fechaInicial+"-----------"+fechaFinal);

            String fechaInicialBien = fechaFormatoCorrecto(fechaInicial);
            String fechaFInalBien = fechaFormatoCorrecto(fechaFinal);

            config configura = configuracionesDao.buscarDatos();
            com.itextpdf.text.Font letra = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10);
            com.itextpdf.text.Font negrita = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD);
            FileOutputStream archivo;
            File file = new File("C:\\Program Files (x86)\\AppLavanderia\\ReportesPDF\\Reporte de " + tipo + ".pdf");
            archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            //Image img = Image.getInstance("Iconos\\logo 100x100.jpg");
            Image img = Image.getInstance("C:\\Program Files (x86)\\AppLavanderia\\Iconos\\logo 100x100.jpg");

            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{85f, 15f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell = new PdfPCell();
            String tituloEncabezado = " DEL " + fechaInicialBien + " AL " + fechaFInalBien;
            cell = new PdfPCell(new Phrase(configura.getNomnbre() + encabezadoTexto, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            encabezado.addCell(cell);
            encabezado.addCell(img);
            doc.add(encabezado);
            Paragraph dejarEspacio = new Paragraph();
            dejarEspacio.add(Chunk.NEWLINE);
            doc.add(dejarEspacio);

            String nombreEncabezado = "";
            TableColumnModel columnModel = tableLista.getColumnModel();
            float[] columnapro = new float[columnModel.getColumnCount()];
            List<String> encabezados = new ArrayList<>();
            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                //TableColumn column = columnModel.getColumn(i);
                //columnapro[i] = column.getWidth();
                nombreEncabezado = tableLista.getModel().getColumnName(i);
                encabezados.add(nombreEncabezado);
            }
            PdfPTable tablapro = new PdfPTable(6);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnapro = new float[]{25f, 35f, 35f, 35f, 35f, 35f};
            tablapro.setWidths(columnapro);

            for (String encabezadop : encabezados) {
                cell = new PdfPCell();
                cell = new PdfPCell(new Phrase(encabezadop, letra));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablapro.addCell(cell);
            }

            DefaultTableModel modelo = (DefaultTableModel) tableLista.getModel();

// Agregar el contenido de las filas al PDF
            for (int fila = 0; fila < modelo.getRowCount(); fila++) {
                for (int columna = 0; columna < modelo.getColumnCount(); columna++) {
                    Object datoCelda = modelo.getValueAt(fila, columna);
                    cell = new PdfPCell();
                    if (datoCelda instanceof String && ((String) datoCelda).startsWith("$")) {
                        // Eliminar el primer caracter '$' de la cadena
                        String datoSinSignoDolar = ((String) datoCelda).substring(1);
                        cell = new PdfPCell(new Phrase(datoSinSignoDolar, letra)); // Usa tu fuente personalizada si es necesario
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    } else {
                        // Si no comienza con '$', simplemente agregar el contenido de la celda tal como está
                        cell = new PdfPCell(new Phrase(datoCelda.toString(), letra)); // Usa tu fuente personalizada si es necesario
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    }

                    cell.setBorder(0);
                    tablapro.addCell(cell);
                }
            }

            modelo = (DefaultTableModel) tableTotales.getModel();

// Agregar el contenido de las filas al PDF
            for (int fila = 0; fila < modelo.getRowCount(); fila++) {
                for (int columna = 0; columna < modelo.getColumnCount(); columna++) {
                    Object datoCelda = modelo.getValueAt(fila, columna);
                    cell = new PdfPCell();
                    if (datoCelda instanceof String && ((String) datoCelda).startsWith("$")) {
                        // Eliminar el primer caracter '$' de la cadena
                        String datoSinSignoDolar = ((String) datoCelda).substring(1);
                        cell = new PdfPCell(new Phrase(datoSinSignoDolar, negrita)); // Usa tu fuente personalizada si es necesario
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    } else {
                        // Si no comienza con '$', simplemente agregar el contenido de la celda tal como está
                        cell = new PdfPCell(new Phrase(datoCelda.toString(), negrita)); // Usa tu fuente personalizada si es necesario
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    }

                    cell.setBorder(0);
                    tablapro.addCell(cell);
                }
            }

            doc.add(tablapro);

            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            //comienza la segunda tabla
            tablapro = new PdfPTable(4);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            columnapro = new float[]{25, 15f, 15f, 15f};
            tablapro.setWidths(columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1 = new PdfPCell(new Phrase("Concepto", letra));
            PdfPCell pro2 = new PdfPCell(new Phrase("Cantidad", letra));
            PdfPCell pro3 = new PdfPCell(new Phrase("Concepto", letra));
            PdfPCell pro4 = new PdfPCell(new Phrase("Cantidad", letra));

            pro1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pro4.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Ventas promedio al dia", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = dateFormat.parse(fechaInicial);
            Date date2 = dateFormat.parse(fechaFinal);
            long differenceInMilliseconds = Math.abs(date1.getTime() - date2.getTime());
            long differenceInDays = differenceInMilliseconds / (24 * 60 * 60 * 1000);
            differenceInDays = tableLista.getRowCount();

            double promedioventas = totalVendido / differenceInDays;
            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(promedioventas), letra));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Notas generadas", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            List<Nota> lsNotas = nota.listarPorFecha(fechaInicial, fechaFinal);
            cell = new PdfPCell();
            String notasG = String.format("%0" + 2 + "d", Integer.valueOf(lsNotas.size()));
            cell = new PdfPCell(new Phrase(notasG, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Cobros promedio al dia", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            double promedioCobros = totalCobrado / differenceInDays;
            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(promedioCobros), letra));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Notas entregadas", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            lsNotas = nota.listarNotasEntregasPeriodo(fechaInicial, fechaFinal);
            cell = new PdfPCell();
            String entregasG = String.format("%0" + 2 + "d", Integer.valueOf(lsNotas.size()));
            cell = new PdfPCell(new Phrase(entregasG, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Gastos promedio al dia", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            double promedioGastos = totalGastado / differenceInDays;
            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("$" + formateador.format(promedioGastos), letra));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Notas canceladas", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            lsNotas = nota.listarNotasCanceladas(fechaInicial, fechaFinal);
            cell = new PdfPCell();
            String canceldasG = String.format("%0" + 2 + "d", Integer.valueOf(lsNotas.size()));
            cell = new PdfPCell(new Phrase(canceldasG, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Clientes nuevos", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            List<Cliente> lsClientes = client.listarClientePeriodoCreacion(fechaInicial, fechaFinal);
            String clientesN = String.format("%0" + 2 + "d", Integer.valueOf(lsClientes.size()));
            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(clientesN, letra));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            tablapro.addCell(cell);

            cell = new PdfPCell();
            cell = new PdfPCell(new Phrase("Abonos realizados", letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);

            List<anticipo> lsAnticipo = anticip.listarAnticipoPeriodo(fechaInicial, fechaFinal);
            cell = new PdfPCell();
            String abonosG = String.format("%0" + 2 + "d", Integer.valueOf(lsAnticipo.size()));
            cell = new PdfPCell(new Phrase(abonosG, letra));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            tablapro.addCell(cell);
            Paragraph primero = new Paragraph();
            primero.setFont(negrita);
            primero.add(Chunk.NEWLINE);
            primero.add(Chunk.NEWLINE);
            doc.add(primero);
            doc.add(tablapro);
            doc.add(dejarEspacio);
            doc.add(dejarEspacio);
            doc.add(dejarEspacio);
            doc.add(dejarEspacio);

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

            doc.add(tablaFinal);

            doc.close();
            archivo.close();
            Desktop.getDesktop().open(file);
        } catch (DocumentException | IOException e) {
            Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Debe cerrar antes el documento");
            panel.showNotification();
        } catch (ParseException ex) {
            Logger.getLogger(reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reporteExcel(String tipo, String fechaInicial, String fechaFinal, JTable TableList, JTable tableTotales) {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet(tipo);
        String fechaInicialBien = fechaFormatoCorrecto(fechaInicial);
        String fechaFInalBien = fechaFormatoCorrecto(fechaFinal);

        try {
            InputStream is = new FileInputStream("C:\\Program Files (x86)\\AppLavanderia\\Iconos\\logo 100x100.jpg");
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
            configura = configuracionesDao.buscarDatos();
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
            celdaTitulo.setCellValue("Reporte de " + tipo);
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 7));

            String tituloEncabezado = " DEL " + fechaInicialBien + " AL " + fechaFInalBien;
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
            celdaTitulo.setCellValue("FECHA DE EMISIÓN " + DiagHoy);
            //aqui lo que hacemos es hacer que ocupe varias filas las combine
            //la primera es en que fila va a empezar, la segunda donde va a termianr 
            //la tercera la primera columna que va a utilizar, ultimo la ultima coliumna
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 7));

            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.NONE);
            datosEstilo.setBorderRight(BorderStyle.NONE);
            datosEstilo.setBorderLeft(BorderStyle.NONE);
            datosEstilo.setBorderTop(BorderStyle.NONE);
            datosEstilo.setAlignment(HorizontalAlignment.CENTER);
            datosEstilo.setVerticalAlignment(VerticalAlignment.CENTER);

            org.apache.poi.ss.usermodel.Font font = book.createFont();
            font.setFontName(Font.FontFamily.TIMES_ROMAN.name());
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);

            DefaultTableModel model = (DefaultTableModel) TableList.getModel();
            int rowCount = model.getRowCount();
            int columnCount = model.getColumnCount();

            // Crear el encabezado en el archivo Excel
            Row headerRow = sheet.createRow(6);
            for (int col = 0; col < columnCount; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(model.getColumnName(col));
            }

            int numFilaDatos = 7;

            // Agregar los datos de las filas al archivo Excel
            for (int row = 0; row < rowCount; row++) {
                Row excelRow = sheet.createRow(numFilaDatos);
                for (int col = 0; col < columnCount; col++) {
                    Object data = model.getValueAt(row, col);
                    if (data != null) {
                        String cellValue = data.toString();
                        // Si el valor comienza con '$', eliminar el caracter '$' antes de escribir en el archivo Excel
                        if (cellValue.startsWith("$")) {
                            Cell celdaDatos = excelRow.createCell(col, CellType.NUMERIC);
                            Number cantidad = formateador.parse(cellValue.substring(1));
                            celdaDatos.setCellValue(cantidad.doubleValue());
                            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));
                        } else {
                            Cell celdaDatos = excelRow.createCell(col);
                            celdaDatos.setCellStyle(datosEstilo);
                            celdaDatos.setCellValue(cellValue);
                        }
                    }
                }
                numFilaDatos++;
            }

            model = (DefaultTableModel) tableTotales.getModel();
            rowCount = model.getRowCount();
            columnCount = model.getColumnCount();
            numFilaDatos++;
            // Crear el encabezado en el archivo Excel
            headerRow = sheet.createRow(numFilaDatos++);

            // Agregar los datos de las filas al archivo Excel
            for (int row = 0; row < rowCount; row++) {
                Row excelRow = sheet.createRow(numFilaDatos);
                for (int col = 0; col < columnCount; col++) {
                    Object data = model.getValueAt(row, col);
                    if (data != null) {
                        String cellValue = data.toString();
                        // Si el valor comienza con '$', eliminar el caracter '$' antes de escribir en el archivo Excel
                        if (cellValue.startsWith("$")) {
                            Cell celdaDatos = excelRow.createCell(col, CellType.NUMERIC);
                            Number cantidad = formateador.parse(cellValue.substring(1));
                            celdaDatos.setCellValue(cantidad.doubleValue());
                            celdaDatos.setCellStyle(getContabilidadCellStyle(book, df));
                        } else {
                            Cell celdaDatos = excelRow.createCell(col);
                            celdaDatos.setCellStyle(datosEstilo);
                            celdaDatos.setCellValue(cellValue);
                        }
                    }
                }
                numFilaDatos++;
            }

            for (int col = 0; col < columnCount; col++) {
                sheet.autoSizeColumn(col);
            }
            //hacemos zoom
            sheet.setZoom(150);

            FileOutputStream fileOut = new FileOutputStream("C:\\Program Files (x86)\\AppLavanderia\\ReportesExcel\\Reporte de " + tipo + ".xlsx");
            book.write(fileOut);
            fileOut.close();
            File file = new File("C:\\Program Files (x86)\\AppLavanderia\\ReportesExcel\\Reporte de " + tipo + ".xlsx");
            Desktop.getDesktop().open(file);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(reportes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(reportes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
