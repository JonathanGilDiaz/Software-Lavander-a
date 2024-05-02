/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 *
 * @author Jonathan Gil
 */
public class establecerFecha {
    
        corteDiaDao corte = new corteDiaDao();
            String fecha, hora;
                String hoy = corte.getDia();
    GastosDao gastos = new GastosDao();

    
         public List establecerFecha() { //Establecemos la fecha a usar, esto se hace ya que hasta que no se cierre caja, no se puede avanzar de dia
           config configura = gastos.buscarDatos();
             List<String> datos = new ArrayList<>();  
                   LocalTime horaActual = LocalTime.now();
        LocalTime horaDesdeString = LocalTime.parse(configura.getHora());
        DateTimeFormatter formatterHoraSQL = DateTimeFormatter.ofPattern("HH:mm:ss");


        if ("mas".equals(configura.getIndicadorHora())) {
            horaActual = horaActual.plusHours(horaDesdeString.getHour())
                    .plusMinutes(horaDesdeString.getMinute())
                    .plusSeconds(horaDesdeString.getSecond());

        } else {
            horaActual = horaActual.minusHours(horaDesdeString.getHour())
                    .minusMinutes(horaDesdeString.getMinute())
                    .minusSeconds(horaDesdeString.getSecond());

        }      
        
 Formatter obj = new Formatter();
        Formatter obj2 = new Formatter();
        LocalDateTime m = LocalDateTime.now(); //Obtenemos la fecha actual
        String mes = String.valueOf(obj.format("%02d", m.getMonthValue()));//Modificamos la fecha al formato que queremos 
        String dia = String.valueOf(obj2.format("%02d", m.getDayOfMonth()));
        fecha = m.getYear() + "-" + mes + "-" + dia;
        
        datos.add(fecha);
        datos.add(horaActual.format(formatterHoraSQL));
        return datos;
        
    }
    
}
