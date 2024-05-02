//Son los metodos de la clase Ciente con la base de datos
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**seleccionarEmpleado
 *
 * @author Jonathan Gil
 */
public class EmpleadosDao {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    
    
    public int idEmpleado(){
        
        String sql = "SELECT MAX(idempleados) FROM empleados";
        int id=0;
        try{
            con =cn.getConnection();
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){
                id = rs.getInt(1);
            }
        }catch(SQLException e){
            System.out.println(e.toString());
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
        return id;
    }
    
    //Metodo para eliminar cliente
    public boolean EliminarEmpleado(int id){
      String sql = "DELETE FROM empleados WHERE idempleados = ?"; //sentencia sql
      try{
                      con =cn.getConnection();
         ps = con.prepareStatement(sql); //se prepara la instruccion
         ps.setInt(1,id); // se vacian los datos a la instruccion
         ps.execute();//se ejecuta la instruccion
         return true;
      }catch(SQLException e){
        System.out.println(e.toString());
        return false;
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
      
    }
    
    
    //Metodo para actualizar empleado
     public boolean actualizarEmpleado(Empleados cl){
     String sql = "UPDATE empleados SET nombre=?, contraseña=?, nivel=? WHERE idempleados=?";//sentencia sql
     try{
                     con =cn.getConnection();
          ps=con.prepareStatement(sql);//se prepara la isntruccion
          ps.setString(1, cl.getNombre()); //se vacian los datos
          ps.setString(2,cl.getContraseña());
          ps.setInt(3, cl.getNivel());
          ps.setInt(4,cl.getId());
          ps.execute(); // se ejecuta la instruccion
          return true;
     }catch(SQLException e){
         System.out.println(e.toString());
           return false;
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
     }
    
     //Registrar Empleado
    public boolean RegistrarEmpleado(Empleados p){ 
      String sql = "INSERT INTO empleados (nombre, contraseña, fechaCreacion, nivel) VALUES (?,?,?,?)";
        try{
          con = cn.getConnection();
          ps = con.prepareStatement(sql);
          ps.setString(1,p.getNombre());
          ps.setString(2,p.getContraseña());
          ps.setString(3, p.getFechaCreacion());
          ps.setInt(4, p.getNivel());
          ps.execute();
          return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.toString());
            return false;
         } finally{
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
      
    }
    
    //EL metodo regresa un empleado con su nombre o id
    public Empleados seleccionarEmpleado(String nombre, int id){
        String sql = "SELECT * FROM empleados WHERE nombre=? OR idempleados=?";
        Empleados cl = new Empleados();
        try{
          con = cn.getConnection();
           ps = con.prepareStatement(sql);
         ps.setString(1, nombre);  
         ps.setInt(2, id);
         rs=ps.executeQuery();
         if(rs.next()){
             cl.setNombre(rs.getString("nombre"));
             cl.setContraseña(rs.getString("contraseña"));
             cl.setId(rs.getInt("idempleados"));         
         }
        }catch(SQLException e){
         System.out.println(e.toString());
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
        return cl;
    }
    
    //metodo para verificar si se ha repetido algun cliente 
    public boolean empleadoRepetido(Empleados cl){
        String sql = "SELECT * FROM empleados WHERE nombre=?";
        boolean bandera=false;
        
        try{
          con = cn.getConnection();
           ps = con.prepareStatement(sql);
         ps.setString(1, cl.getNombre());  
         rs=ps.executeQuery();
         if(rs.next()){
             bandera=true;
         }
        }catch(SQLException e){
         System.out.println(e.toString());
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
        return bandera;
    }
    
    //Este me parece que no sirve
    public boolean promoRepetidaActualizar(Empleados cl){
        String sql = "SELECT * FROM empleados WHERE nombre=?";
        boolean bandera=false;
        int p;
        try{
          con = cn.getConnection();
           ps = con.prepareStatement(sql);
         ps.setString(1, cl.getNombre());  
         rs=ps.executeQuery();
            while(rs.next()){
             p=rs.getInt("idempleados");
             if(cl.getId()!=p){
                 bandera=true;
             }
             
         }
        }catch(SQLException e){
         System.out.println(e.toString());
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
        return bandera;
    }
    
    //Metodo para istar todos los empleados
    public List listarEmpleados(){
            List <Empleados> lsEmpleados = new ArrayList();
       String sql = "SELECT * FROM empleados";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs=ps.executeQuery();
        while(rs.next()){
         Empleados e = new Empleados();
         e.setNombre(rs.getString("nombre"));
         e.setContraseña(rs.getString("contraseña"));
         e.setId(rs.getInt("idempleados"));
         e.setFechaCreacion(rs.getString("fechaCreacion"));
         e.setEstado(rs.getInt("estado"));
         e.setNivel(rs.getInt("nivel"));
         lsEmpleados.add(e); //Agregar al arreglo
        }
       }catch(SQLException e){
           System.out.println(e.toString()+"ph yea");
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
       return lsEmpleados;
    }
    
     public Empleados BuscarPorCodigo(int a){
          String sql = "SELECT * FROM empleados WHERE idempleados = ?";
          Empleados e = new Empleados();
                  boolean bandera = false;   

          try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, a);
        rs=ps.executeQuery();
         if(rs.next()){
         e.setContraseña(rs.getString("contraseña"));
         e.setEstado(rs.getInt("estado"));
         e.setFechaCreacion(rs.getString("fechaCreacion"));
         e.setId(rs.getInt("idempleados"));
         e.setNivel(rs.getInt("nivel"));
         e.setNombre(rs.getString("nombre"));
         bandera=true;
            }
       }catch(SQLException ex){
           System.out.println(ex.toString());
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
       if(bandera==false) {
       e.setNombre("");
       } 
       return e;

    }
     
      public List buscarLetra(String buscar){ //este metodo permite el buscar algun cliente con ciertas letras
        List <Empleados> ListaCl = new ArrayList(); //se declara un arreglo cliente para guardar los datos
        String filtro=""+buscar+"%"; //se guarda la variable con la cual se buscara 
       String sql = "SELECT * FROM empleados WHERE nombre LIKE"+'"'+filtro+'"';//Sentencia sql
       try{
        con = cn.getConnection(); //Se prepara la conexion
        ps = con.prepareStatement(sql); //se prepara la sentencia sql
        rs=ps.executeQuery(); //se ejecuta la instruccion sql
        while(rs.next()){ // si hay algun dato, entra en el ciclo
         Empleados e = new Empleados(); //Se crea el cliente con el cual se guardaran los datos para posteriormente agregarlos a la lista
        e.setContraseña(rs.getString("contraseña"));
         e.setEstado(rs.getInt("estado"));
         e.setFechaCreacion(rs.getString("fechaCreacion"));
         e.setId(rs.getInt("idempleados"));
         e.setNivel(rs.getInt("nivel"));
         e.setNombre(rs.getString("nombre"));
         ListaCl.add(e); //se agrega el cliente al arreglo
        }
       }catch(SQLException e){
           System.out.println(e.toString());
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
       return ListaCl; //se retorna la lista
        
    }
      
       public boolean ModificarEstado(int codigo, int indicador){
     String sql = "UPDATE empleados SET estado=? WHERE idempleados=?";
     try{
                     con =cn.getConnection();

          ps=con.prepareStatement(sql);
          ps.setInt(1, indicador);
          ps.setInt(2,codigo);
  
          ps.execute();
          return true;
     }catch(SQLException e){
         System.out.println(e.toString());
           return false;
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
    }
    
    //mmetodo para verificar si se ha repetido algunn cliente
     public boolean EmpleRepetidaActualizar(Empleados cl){
        String sql = "SELECT * FROM empleados WHERE nombre=?";
        boolean bandera=false;
        int p;
        try{
          con = cn.getConnection();
           ps = con.prepareStatement(sql);
         ps.setString(1, cl.getNombre());  
         rs=ps.executeQuery();
            while(rs.next()){
             p=rs.getInt("idempleados");
             if(cl.getId()!=p){
                 bandera=true;
             }
             
         }
        }catch(SQLException e){
         System.out.println(e.toString());
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
        return bandera;
    }
    
}
