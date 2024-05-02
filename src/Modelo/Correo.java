//En esta clase se encuentra el metodo para enviar la nota al respectivo cliente medinte correo electronico

package Modelo;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

/**
 *
 * @author Jonathan Gil
 */
public class Correo {
    private static String emailFrom = "takerwells@gmail.com"; //se indica el correo emisor
    private static String passwordFrom = "dnpwvskbgfproagt"; // su contraseña de acceso
    private String emailTo; //correo destinatario
    private String subject; //encabezado del correo
    private String content;//cuepro del correo

    private Properties mProperties;
    private Session mSession;
    private MimeMessage mCorreo;
    Cliente c = new Cliente();
    int folioNota;
    
    public Correo(Cliente c, int folioNota) throws MessagingException { //metodo para enviar el correo
        mProperties = new Properties();
        this.folioNota=folioNota;//Se detecta el folio de la nota a enviar
        this.c=c;
        if(!"".equals(c.getCorreo() )){
                    createEmail();//aqui se crea el correo
                    sendEmail();//se envia el correo
        }else{
            JOptionPane.showMessageDialog(null, "NO HAY NINGUN CORREO REGISTRADO");
        }
        
    }

    private void createEmail() throws MessagingException { //creando correo
        emailTo = c.getCorreo(); //a quien se va a mandar
        subject = "COMPROBANTE DE NOTA LAVANDERIA GL"; //encabezado
        content = "Buen dia "+c.getNombre()+" "+c.getApellido()+" , le mandamos un cordial saludo\n Le adjuntamos su nota."; // mensaje
        
         // Simple mail transfer protocol
         
         //enviando documento
         BodyPart texto = new MimeBodyPart();
         texto.setContent(content, "text/html");
         
         BodyPart adjunto = new MimeBodyPart();
         adjunto.setDataHandler(new DataHandler(new FileDataSource("Iconos\\venta"+ folioNota+".pdf"))); //Aqui se carga el pdf de la nota
         adjunto.setFileName("NotaLvanderiaGL.pdf"); // se le asigna un nombre al pdf
         
         MimeMultipart miltiParte = new MimeMultipart();
         //Se adjunta el cuerpo del mensaje y el pdf
         miltiParte.addBodyPart(texto);
         miltiParte.addBodyPart(adjunto);
         
         //se establecen los datos el remitente
          mCorreo = new MimeMessage(mSession);
         mCorreo.setFrom(new InternetAddress(emailFrom));
         
         
         
         
        mProperties.put("mail.smtp.host", "smtp.gmail.com");
        mProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mProperties.setProperty("mail.smtp.starttls.enable", "true");
        mProperties.setProperty("mail.smtp.port", "587");
        mProperties.setProperty("mail.smtp.user",emailFrom);
        mProperties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        mProperties.setProperty("mail.smtp.auth", "true");
        
        mSession = Session.getDefaultInstance(mProperties);
        
        
        try {
            //se crea el correo
            System.out.println(emailTo);
            mCorreo = new MimeMessage(mSession);
            mCorreo.setFrom(new InternetAddress(emailFrom));
            mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            mCorreo.setSubject(subject);
            mCorreo.setContent(miltiParte);
                     
            
        } catch (AddressException ex) {
            System.out.println("Error1");
        } catch (MessagingException ex) {
            System.out.println("Error2");
        }
    }

    private void sendEmail() {//envindo correo
        try {
            
            //se cargan los datos y se envian
            Transport mTransport = mSession.getTransport("smtp");
            mTransport.connect(emailFrom, passwordFrom);
            mTransport.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
            mTransport.close();
            
            JOptionPane.showMessageDialog(null, "Correo enviado");
        } catch (NoSuchProviderException ex) {
            System.out.println("Error11");
        } catch (MessagingException ex) {
            System.out.println("Error22"+ex);
        }
    }
    
    
}

