/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

/**
 *
 * @author Jonathan Gil
 */
public class imprimiendo {

    private final static Logger LOGGER = Logger.getLogger("mx.hash.impresionpdf.Impresor");

    /**
     * *
     * Manda un documento a imprimir a la impresora que se indique en el dialogo
     *
     * @throws PrinterException
     * @throws IOException
     */
    public void imprimir(File file) throws PrinterException, IOException {
        PDDocument document = PDDocument.load(file);
        PrinterJob job = PrinterJob.getPrinterJob();

        PageFormat pf = job.defaultPage();
        Paper paper = new Paper();
        double margin = 0; // half inch
        paper.setSize(140.76f, 350.90f);
        paper.setImageableArea(0, 0, 140.76f, 500f);

        pf.setPaper(paper);

        job.setPrintable(new PDFPrintable(document, Scaling.SHRINK_TO_FIT), pf);

        LOGGER.log(Level.INFO, "Mostrando el dialogo de impresion");

        LOGGER.log(Level.INFO, "Imprimiendo documento");

        job.print();

    }

    public void imprimirCorte() throws PrinterException, IOException {
        // Indicamos el nombre del archivo Pdf que deseamos imprimir

        File file = new File("corteticket.pdf");

        PDDocument document = PDDocument.load(file);

        PrinterJob job = PrinterJob.getPrinterJob();

        PageFormat pf = job.defaultPage();
        Paper paper = new Paper();
        double margin = 0; // half inch
        paper.setSize(140.76f, 600.90f);
        paper.setImageableArea(1f, 1f, 140.76f, 600.90f);

        pf.setPaper(paper);

        job.setPrintable(new PDFPrintable(document, Scaling.ACTUAL_SIZE), pf);

        LOGGER.log(Level.INFO, "Mostrando el dialogo de impresion");

        LOGGER.log(Level.INFO, "Imprimiendo documento");

        job.print();

    }
}
