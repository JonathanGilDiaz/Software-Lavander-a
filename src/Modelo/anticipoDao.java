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

/**
 *
 * @author Jonathan Gil
 */
public class anticipoDao {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public void registrarAnticipo(anticipo a){
        String sql = "INSERT INTO anticipo (folio, fecha, cantidad, nombre, apellido, codigoC,Observaciones,formaPago,hora,idEmpleado) values (?,?,?,?,?,?,?,?,?,?)";
         try{
            con =cn.getConnection();
            ps=con.prepareStatement(sql);
            ps.setInt(1, a.getFolio());
            ps.setString(2, a.getFecha());
            ps.setDouble(3, a.getCantidad());
            ps.setString(4, a.getNombre());
            ps.setString(5, a.getApellido());
            ps.setInt(6, a.getCodigoC());
            ps.setString(7, a.getObservaciones());
            ps.setInt(8, a.getFormaPago());
             ps.setString(9, a.getHora());
             ps.setInt(10, a.getIdEmpleado());
                        ps.execute();
          }catch(SQLException e){
            System.out.println(e.toString()+"Aqui es JEJEJJE");
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
    
    public List listarAnticipo(){
        List <anticipo> lsAntic = new ArrayList();
       String sql = "SELECT * FROM anticipo";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
         rs=ps.executeQuery();
        while(rs.next()){
            anticipo a = new anticipo();
            a.setApellido(rs.getString("apellido"));
            a.setCantidad(rs.getDouble("cantidad"));
            a.setCodigoC(rs.getInt("codigoC"));
            a.setFecha(rs.getString("fecha"));
            a.setFolio(rs.getInt("folio"));
            a.setId(rs.getInt("id"));
            a.setNombre(rs.getString("nombre"));
            a.setFormaPago(rs.getInt("formaPago"));
            a.setHora(rs.getString("hora"));
            a.setObservaciones(rs.getString("Observaciones"));
                        a.setIdEmpleado(rs.getInt("idEmpleado"));

            lsAntic.add(a);
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
       return lsAntic;
    }
    
        public List listarAnticipoPorDiaHora(String fechaInicial, String fechaFinal) {
        List <anticipo> lsAntic = new ArrayList();
        String sql = "SELECT * FROM anticipo WHERE CONCAT(fecha, ' ', hora) between ? and ?;";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
         ps.setString(1, fechaInicial);
            ps.setString(2, fechaFinal);
         rs=ps.executeQuery();
        while(rs.next()){
            anticipo a = new anticipo();
            a.setApellido(rs.getString("apellido"));
            a.setCantidad(rs.getDouble("cantidad"));
            a.setCodigoC(rs.getInt("codigoC"));
            a.setFecha(rs.getString("fecha"));
            a.setFolio(rs.getInt("folio"));
            a.setId(rs.getInt("id"));
            a.setNombre(rs.getString("nombre"));
            a.setFormaPago(rs.getInt("formaPago"));
            a.setHora(rs.getString("hora"));
            a.setObservaciones(rs.getString("Observaciones"));
                        a.setIdEmpleado(rs.getInt("idEmpleado"));

            lsAntic.add(a);
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
       return lsAntic;
    }

    
     public List listarAnticipoPeriodo(String fechaInicio, String fechaFinal){
        List <anticipo> lsAntic = new ArrayList();
       String sql = "SELECT * FROM anticipo where fecha between ? and ?";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
         ps.setString(1, fechaInicio);
         ps.setString(2, fechaFinal);   
         rs=ps.executeQuery();
        while(rs.next()){
            anticipo a = new anticipo();
            a.setApellido(rs.getString("apellido"));
            a.setCantidad(rs.getDouble("cantidad"));
            a.setCodigoC(rs.getInt("codigoC"));
            a.setFecha(rs.getString("fecha"));
            a.setFolio(rs.getInt("folio"));
            a.setId(rs.getInt("id"));
            a.setNombre(rs.getString("nombre"));
            a.setFormaPago(rs.getInt("formaPago"));
            a.setHora(rs.getString("hora"));
                        a.setObservaciones(rs.getString("Observaciones"));
            a.setIdEmpleado(rs.getInt("idEmpleado"));
            lsAntic.add(a);
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
       return lsAntic;
    }
     
          public double listarAbonosPorNota(int folio){
        List <anticipo> lsAntic = new ArrayList();
        double total=0;
       String sql = "SELECT * FROM anticipo where folio=?";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
         ps.setInt(1, folio);
         rs=ps.executeQuery();
        while(rs.next()){
            
            total=total+rs.getDouble("cantidad");

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
       return total;
    }
          
                   public double listarAbonosPorNotaSinFolioActual(int folio, int folioAnticipo){
        List <anticipo> lsAntic = new ArrayList();
        double total=0;
       String sql = "SELECT * FROM anticipo where folio=?";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
         ps.setInt(1, folio);
         rs=ps.executeQuery();
        while(rs.next()){
            if(folioAnticipo != rs.getInt("id"))
            total=total+rs.getDouble("cantidad");

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
       return total;
    }
                   
        public boolean EliminarAnticipos(int id){ 
      String sql = "DELETE FROM anticipo WHERE folio = ?";
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


}
