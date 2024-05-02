/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Jonathan Gil
 */
public class PrecioDao {
     Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public int idPrecio(){
        
        
        
        String sql = "SELECT MAX(idprecios) FROM precios";
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
    
    public boolean clienteRepetido(Precios cl){
        String sql = "SELECT * FROM precios WHERE nombre=?";
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
    
    public boolean promoRepetida(Precios cl){
        String sql = "SELECT * FROM precios WHERE nombre=?";
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
    
    public boolean promoRepetidaActualizar(Precios cl){
        String sql = "SELECT * FROM precios WHERE nombre=?";
        boolean bandera=false;
        int p;
        try{
          con = cn.getConnection();
           ps = con.prepareStatement(sql);
         ps.setString(1, cl.getNombre());  
         rs=ps.executeQuery();
            while(rs.next()){
             p=rs.getInt("idprecios");
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
    
     public boolean RegistrarPromo(Precios p){
      String sql = "INSERT INTO precios (nombre, precioU, fechaCreacion,especificaciones) VALUES (?,?,?,?)";
        try{
          con = cn.getConnection();
          ps = con.prepareStatement(sql);
          ps.setString(1,p.getNombre());
          ps.setDouble(2,p.getPrecioU());
          ps.setString(3, p.getFechaCreacion());
          ps.setString(4, p.getDescripcion());
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
     
     public boolean modificarPrecio(Precios p){
     String sql = "UPDATE precios SET nombre=?, precioU=?, especificaciones=? WHERE idprecios=?";
     try{
                   con = cn.getConnection();
          ps=con.prepareStatement(sql);
          ps.setString(1, p.getNombre());
          ps.setDouble(2,p.getPrecioU());
          ps.setInt(4,p.getId());
          ps.setString(3, p.getDescripcion());
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
     
     public boolean modificarEstado(int p, boolean m){
     String sql = "UPDATE precios SET estado=? WHERE idprecios=?";
     try{
                            con = cn.getConnection();
          ps=con.prepareStatement(sql);
                   if(m==true){
          ps.setInt(1, 1);
          ps.setInt(2,p);
                   }
                   else{
                   ps.setInt(1, 0);
          ps.setInt(2,p);
                   } 
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
     
      public boolean ReiniciarContador(){
              String sql = "SET @num:= 0;";
              String sql1 = "UPDATE precios SET idprecios = @num:= (@num + 1);";
             String sql2 = "ALTER TABLE precios AUTO_INCREMENT = 1;";
              try{
               con = cn.getConnection();
            ps = con.prepareStatement(sql); 
             ps.execute();
             ps = con.prepareStatement(sql1); 
             ps.execute();
             ps = con.prepareStatement(sql2); 
             ps.execute();
             return true;
            } catch(SQLException e){
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
    
     public boolean EliminarPromo(int id){
      String sql = "DELETE FROM precios WHERE idprecios = ?";
      try{
                  con = cn.getConnection();
         ps = con.prepareStatement(sql);
         ps.setInt(1,id);
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
     
    public List getPrecios(){
        List <Precios> lsNotas = new ArrayList();
       String sql = "SELECT * FROM precios";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs=ps.executeQuery();
        while(rs.next()){
         Precios p = new Precios();
         p.setFechaCreacion(rs.getString("fechaCreacion"));
         p.setId(rs.getInt("idprecios"));
         p.setNombre(rs.getString("nombre"));
         p.setPrecioU(rs.getDouble("precioU"));
         p.setEstado(rs.getInt("usada"));
         p.setEstado(rs.getInt("estado"));
         p.setDescripcion(rs.getString("especificaciones"));
         lsNotas.add(p);
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
       return lsNotas;
    }
    
     public Precios BuscarPorCodigo(int a){
          String sql = "SELECT * FROM precios WHERE idprecios = ?";
          Precios p = new Precios();
                  boolean bandera = false;   

          try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, a);
        rs=ps.executeQuery();
         if(rs.next()){
         p.setFechaCreacion(rs.getString("fechaCreacion"));
         p.setId(rs.getInt("idprecios"));
         p.setNombre(rs.getString("nombre"));
         p.setPrecioU(rs.getDouble("precioU"));
         p.setEstado(rs.getInt("usada"));
         p.setEstado(rs.getInt("estado"));
         p.setDescripcion(rs.getString("especificaciones"));
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
       p.setNombre("");
       } 
       return p;

    }
     
       public List buscarLetra(String buscar){ //este metodo permite el buscar algun cliente con ciertas letras
        List <Precios> ListaCl = new ArrayList(); //se declara un arreglo cliente para guardar los datos
        String filtro=""+buscar+"%"; //se guarda la variable con la cual se buscara 
       String sql = "SELECT * FROM precios WHERE nombre LIKE"+'"'+filtro+'"';//Sentencia sql
       try{
        con = cn.getConnection(); //Se prepara la conexion
        ps = con.prepareStatement(sql); //se prepara la sentencia sql
        rs=ps.executeQuery(); //se ejecuta la instruccion sql
        while(rs.next()){ // si hay algun dato, entra en el ciclo
         Precios p = new Precios(); //Se crea el cliente con el cual se guardaran los datos para posteriormente agregarlos a la lista
        p.setFechaCreacion(rs.getString("fechaCreacion"));
         p.setId(rs.getInt("idprecios"));
         p.setNombre(rs.getString("nombre"));
         p.setPrecioU(rs.getDouble("precioU"));
         p.setEstado(rs.getInt("usada"));
         p.setEstado(rs.getInt("estado"));
         p.setDescripcion(rs.getString("especificaciones"));
         ListaCl.add(p); //se agrega el cliente al arreglo
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
    
}