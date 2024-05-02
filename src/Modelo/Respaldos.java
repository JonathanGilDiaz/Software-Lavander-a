/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;
import javax.swing.JFileChooser;

/**
 *
 * @author Jonathan Gil
 */
public class Respaldos {
    
    public void respaldo(){
          try {
            /* - Datos de acceso a nuestra base de datos */
            /* Usa localhost si el servidor corre en la misma maquina, de lo 
            contrario utiliza la IP o dirección del sevidor*/
            String dbServer = "localhost";
            /* El usuario de tu base de datos*/
            String dbName = "softwarelavanderia";
            /* El usuario de tu base de datos*/
            String dbUser = "root";
            /* La contraseña de la base de datos (dejarla en texto plano puede 
            ser un problema)*/
            String dbPass = "takerwells";
            
            /*El nombre o ruta a donde se guardara el archivo de volcado .sql*/
          //  javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
        // String ruta = "";
    // try{
  //      if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION){
   //       ruta = jF1.getSelectedFile().getAbsolutePath();
        //Aqui ya tiens la ruta,,,ahora puedes crear un fichero n esa ruta y escribir lo k kieras...
   //     }
   //   }catch (Exception ex){
   //     ex.printStackTrace();
   //   }
            String sqlFile = "respaldo.sql";
      System.out.println(sqlFile);

            /* La linea de comando completa que ejecutara el programa*/
String command = "mysqldump --host " + dbServer + " -u " + dbUser+ " -p" + dbPass + " " + dbName + " -r " + sqlFile;                     
            /*Se crea un proceso que ejecuta el comando dado*/
            Process bck = Runtime.getRuntime().exec(command);
            
            /* Obtiene el flujo de datos del proceso, esto se hace para obtener 
            el texto del proceso*/
            InputStream stdout = bck.getErrorStream();
            
            /* Se obtiene el resultado de finalizacion del proceso*/
            int resultado = bck.waitFor();
            
            String line;
 
            /* Se crea un buffer de lectura con el flujo de datos outstd y ese mismo
            sera leido e impreso, esto mostrara el texto que muestre el programa
            mysqldump, de esta forma sabra cual es el error en su comando*/
            BufferedReader brCleanUp
                    = new BufferedReader(new InputStreamReader(stdout));
            while ((line = brCleanUp.readLine()) != null) {
                System.out.println(line);
            }
            brCleanUp.close();
 
            if (resultado == 0) {
                  Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Respaldo exitoso");
                                 panel.showNotification();
            } else {
                 Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Error al respaldar");
                                 panel.showNotification();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }
    
    
       
    
     public void restaurar() {
        try {
            /* - Datos de acceso a nuestra base de datos */
            /* Usa localhost si el servidor corre en la misma maquina, de lo 
            contrario utiliza la IP o dirección del sevidor*/
            String dbServer = "localhost";
            /* El nombre de tu base de datos*/
            String dbName = "softwarelavanderia";
            /* El usuario de tu base de datos*/
            String dbUser = "root";
            /* La contraseña de la base de datos (dejarla en texto plano puede 
            ser un problema)*/
            String dbPass = "takerwells";
            
            /*Nombre o ruta del archivo de volcado del cual se va a restaurar*/
             String ruta ="";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(fileChooser);
       
            ruta = fileChooser.getSelectedFile().getAbsolutePath();                                        

            String sqlFile = ruta;
 
            /* Comando a ejecutar, note que se utiliza un array, ya que una sola
            linea causaria un bloqueo de búfer*/
String command[] = new String[]{"mysql", dbName, "-u" + dbUser, "-p"
                + dbPass, "-e", " source " + sqlFile};
            
            /*Se crea un proceso que ejecuta el comando dado*/
            Process bck = Runtime.getRuntime().exec(command);
            
            /* Obtiene el flujo de datos del proceso, esto se hace para obtener 
            el texto del proceso*/
            InputStream stdout = bck.getErrorStream();
            
            /* Se obtiene el resultado de finalizacion del proceso*/
            int resultado = bck.waitFor();
            
            String line;
 
            /* Se crea un buffer de lectura con el flujo de datos outstd y ese mismo
            sera leido e impreso, esto mostrara el texto que muestre el programa
            mysqldump, de esta forma sabra cual es el error en su comando*/
            BufferedReader brCleanUp
                    = new BufferedReader(new InputStreamReader(stdout));
            while ((line = brCleanUp.readLine()) != null) {
                System.out.println(line);
            }
            brCleanUp.close();
 
            if (resultado == 0) {
                  Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Restauración exitosa");
                                 panel.showNotification();
            } else {
                    Notification panel = new Notification(new javax.swing.JFrame(), Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Error al restaurar");
                                 panel.showNotification();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

}
