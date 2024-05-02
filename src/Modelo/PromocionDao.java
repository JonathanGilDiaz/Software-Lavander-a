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
public class PromocionDao {
        Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    
     public void actualizarDatosRifa(datosPromocion datos){
     String sql = "UPDATE datospromocion SET nombre=?, montoMinimo=?, mensaje=?, conceptos=?, indicador=? WHERE id=1";
     try{
                 con = cn.getConnection();
          ps=con.prepareStatement(sql);
          ps.setString(1, datos.getNombre());
          ps.setDouble(2, datos.getMontoMinimo());
          ps.setString(3, datos.getMensaje());
          ps.setString(4, datos.getConceptos());
          ps.setInt(5, datos.getIndicador());
          ps.execute();
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
    }

         public datosPromocion obtenerDatos(){
        String sql = "SELECT * FROM datospromocion where id=1";
        datosPromocion datos = new datosPromocion();
        try{
            con =cn.getConnection();
            ps=con.prepareStatement(sql);
             rs=ps.executeQuery();
           if(rs.next()){
            datos.setNombre(rs.getString("nombre"));
            datos.setMontoMinimo(rs.getDouble("montoMinimo"));
            datos.setMensaje(rs.getString("mensaje"));
            datos.setIndicador(rs.getInt("indicador"));
            datos.setConceptos(rs.getString("conceptos"));
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
        return datos;
    }
     
         public int promocionActiva(){
        String sql = "SELECT * FROM datospromocion where id=1";
        int id=0;
        try{
            con =cn.getConnection();
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){
                id = rs.getInt("indicador");
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
         
        public void reiniciarPromocion(){
     try{
                     con =cn.getConnection();
                // Cambiamos el modo de seguridad de actualizaciones a 0
           ps = con.prepareStatement("SET SQL_SAFE_UPDATES=0");
           ps.executeUpdate();

           // Eliminamos todos los registros de la tabla rifa
           ps = con.prepareStatement("DELETE FROM boletopromocion");
           ps.executeUpdate();

           // Reiniciamos el valor del autoincremento de la tabla rifa
           ps = con.prepareStatement("ALTER TABLE boletopromocion AUTO_INCREMENT=0");
           ps.executeUpdate();
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
    }

              public boolean tieneBoleto(int folio){
        String sql = "SELECT * FROM boletopromocion where folioNota="+folio;
        boolean bandera=false;
        try{
            con =cn.getConnection();
            ps=con.prepareStatement(sql);
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
              
        public int getFolioPromocion(int folio){
        String sql = "SELECT * FROM boletopromocion where folioNota="+folio;
        int folioRifa=0;
        try{
            con =cn.getConnection();
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){
                folioRifa=rs.getInt("id");
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
        return folioRifa;
    }
     
             public boolean introducirFolio(BoletoPromocion rifa){ //para ello, se necesita un cliente para vaciar los datos
      String sql = "INSERT INTO boletopromocion (fecha, folioNota, hora) VALUES (?,?,?)"; //sentencia sql
        try{
          con = cn.getConnection(); //se establece la conexion
          ps = con.prepareStatement(sql); //se prepara la instruccion
          ps.setString(1,rifa.getFecha()); // se vacian los datos del cliente a la instrucciones
          ps.setInt(2,rifa.getFolioNota());
          ps.setString(3, rifa.getHora());
         
          ps.execute(); // se ejecutar la instrruccion
          return true; //bandera para checar si se ejecuto correctamnete la instruccion
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
             
          public List listarFolios(){
          List<BoletoPromocion> lsCredito = new ArrayList<BoletoPromocion>();
           String sql = "select n.totalVenta, CONCAT(cl.nombre, ' ', cl.apellido) as nombreCompleto, r.* from boletopromocion as r join notas as n on r.folioNota=n.folio join clientes as cl on r.folioNota=n.folio && n.codigoCliente=cl.id;";          
          try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs=ps.executeQuery();
         while(rs.next()){
          BoletoPromocion rifa = new BoletoPromocion();
             rifa.setId(rs.getInt("id"));
             rifa.setNombre(rs.getString("nombreCompleto"));
             rifa.setTotal(rs.getDouble("totalVenta"));
             rifa.setFecha(rs.getString("fecha"));
             rifa.setFolioNota(rs.getInt("folioNota"));
         
         lsCredito.add(rifa);
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
       return lsCredito;

    }
          
          public BoletoPromocion listarSoloUnFolio(int folio){
          BoletoPromocion rifa = new BoletoPromocion();
           String sql = "select n.totalVenta, CONCAT(cl.nombre, ' ', cl.apellido) as nombreCompleto, r.* from boletopromocion as r join notas as n on r.folioNota=n.folio join clientes as cl on r.folioNota=n.folio && n.codigoCliente=cl.id where r.id=?;";          
          try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, folio);
        rs=ps.executeQuery();
         if(rs.next()){
             rifa.setId(rs.getInt("id"));
             rifa.setNombre(rs.getString("nombreCompleto"));
             rifa.setTotal(rs.getDouble("totalVenta"));
             rifa.setFecha(rs.getString("fecha"));
             rifa.setFolioNota(rs.getInt("folioNota"));
         
            }else rifa.setId(0);
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
       return rifa;

    }

        public List buscarPorLetra(String buscar){
          List<BoletoPromocion> lsCredito = new ArrayList<BoletoPromocion>();
               String filtro=""+buscar+"%";
           String sql = "select n.totalVenta, CONCAT(cl.nombre, ' ', cl.apellido) as nombreCompleto, r.* from boletopromocion as r join notas as n on r.folioNota=n.folio join clientes as cl on r.folioNota=n.folio && n.codigoCliente=cl.id where CONCAT(cl.nombre, ' ', cl.apellido) LIKE"+'"'+filtro+'"';        
          try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs=ps.executeQuery();
         while(rs.next()){
          BoletoPromocion rifa = new BoletoPromocion();
             rifa.setId(rs.getInt("id"));
             rifa.setNombre(rs.getString("nombreCompleto"));
             rifa.setTotal(rs.getDouble("totalVenta"));
             rifa.setFecha(rs.getString("fecha"));
             rifa.setFolioNota(rs.getInt("folioNota"));
         
         lsCredito.add(rifa);
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
       return lsCredito;

    }




}
