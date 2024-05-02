//En esta clase de va a verificar las credenciales de acceso al programa
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class LoginDAO {
    
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion (); 
    
    //Metodo para obtener los datos de acceso a la base de datos 
    public login log(String usuario, String pass){
     login l = new login();
     String sql = "SELECT * FROM usuarios WHERE usuario = ? AND pass = ?"; //Sectencia sql para obtener el usuario y contraseña
       try{
         con = cn.getConnection();//Se establece la conexion con la base de datos
         ps = con.prepareStatement(sql);//Se prepara la sentencia
         ps.setString(1, usuario);//se agregando los datos que coloco el usuario en el login para verificar si son correctos
         ps.setString(2, pass);
         rs = ps.executeQuery();
          if(rs.next()){//si es asi, se guarda la informacion
            l.setId(rs.getInt("id"));
            l.setNombre(rs.getString("nombre"));
            l.setUsuario(rs.getString("usuario"));
            l.setPass(rs.getString("pass"));
          }
       }catch(SQLException e){
           JOptionPane.showMessageDialog(null, e);
       }finally{
         try{
         if(ps != null)
                ps.close();
            if(rs != null)
                rs.close();
            if(con != null)
                con.close();
         }catch(SQLException ex){
                   System.out.println(ex.toString()+"6");
         }
          } 
       
       System.out.println(l.getPass()+l.getUsuario());
       //Se retorna la informacion, si no se encontro ningun dato, se retorna null
       return l;
    }
  
}
