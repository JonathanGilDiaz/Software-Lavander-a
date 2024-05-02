
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//En este clase, se realiza la conexion con la base de datos
public class Conexion {
                Connection con;
                
                //en este metodo se reaiza la conexion
    public Connection getConnection(){

      try{
          
          //indicamos la instruccion, nos uniremos al puerto 3303, el nombre de la base de datos es softwarelavanderia
        String myBD = "jdbc:mysql://localhost:3306/softwarelavanderia?serverTimezone=UTC";
        //Se indica el usuario y contraseña
        con = DriverManager.getConnection(myBD, "root","takerwells");
        
        //con = DriverManager.getConnection(myBD, "root","takerwells");
       
        
      }catch(SQLException e){
          System.out.println(e.toString());
          return null;
      }
           return con;//se retorna la conexion
    }
    
    private static String us ="root";
private static String pas = "";
private static String bd = "jdbc:mysql://localhost:3306/softwarelavanderia";
//mas de lo mismo
//los metodos de abajo fueronn de prueba para realizar el respaldo sin exito

public static String getUs(){
return us;
}

public static String getPas(){
return pas;
}

public static String getBd(){
    return bd;
}
    
//metodo main para comprobar rapidamente si la conexion se ha realizado exitosamente
    public static void main (String[]args){
        Conexion c = new Conexion();
        if(c.getConnection()!=null){
            System.out.println("FUNCIONA");
        }else{
                       System.out.println("mierda");
 
        }
    }
    
    
     
}
