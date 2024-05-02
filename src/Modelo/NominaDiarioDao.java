//En esta clase se encuentran los metodos de la la clase NominaDiario con la base de datos


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
public class NominaDiarioDao {
     Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public void registrarNomina(NominaDiario nd){
         String sql = "INSERT INTO nominadiario (nombreEmpleado, fecha, horaInicio, horaFinal, codigoEmpleado) VALUES (?,?,?,?,?)";
        try{
          con = cn.getConnection();
          ps = con.prepareStatement(sql);
          ps.setString(1,nd.getNombre());
          ps.setString(2,nd.getFecha());
          ps.setString(3, nd.getHoraInicio());
          ps.setString(4, nd.getHoraSalida());
          ps.setInt(5, nd.getCodigoEmpleado());

          ps.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.toString()+"aqui");
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
    
    
    
     public boolean registrarSalida(int codigo, String hora){
     String sql = "UPDATE nominadiario SET horaFinal=? WHERE codigo=?";
     try{
                   con = cn.getConnection();
          ps=con.prepareStatement(sql);
          ps.setString(1, hora);
          ps.setInt(2, codigo);
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
    
     public int idMaxEmpleado(int idEmpleado){
        int idMax=0;
        String sql = "SELECT MAX(codigo) FROM nominadiario where codigoEmpleado="+idEmpleado;
        try{
            con =cn.getConnection();
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){
                idMax = rs.getInt(1);
            }else{
                idMax=0;
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
        return idMax;
    }
     
      public NominaDiario buscarPorIdEmpleado(int a){
        String sql = "SELECT * FROM nominadiario WHERE codidoEmpleado = ? ";
        NominaDiario n = new NominaDiario();
       boolean bandera = false;
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, a);
        rs=ps.executeQuery();
        if(rs.next()){
         n.setCodigo(rs.getInt("codigo"));
         n.setCodigoEmpleado(rs.getInt("codigoEmpleado"));
         n.setFecha(rs.getString("fecha"));
         n.setHoraInicio(rs.getString("horaInicio"));
         n.setHoraSalida(rs.getString("horaFinal"));
         n.setNombre(rs.getString("nombreEmpleado"));
         
         bandera=true;
        }else{
            
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
       if(bandera==false) {
           JOptionPane.showMessageDialog(null, "NO SE ENCONTRO NINGUna DATO CON ESE CODIGO");
           n.setNombre("fallo");
       }
       return n;
    }
      
            public List listarNominasPorPeriodo(String fechaInicio, String fechaFinal){ 
         List <NominaDiario> lsNomina = new ArrayList();
       String sql = "SELECT * FROM nominadiario where fecha between '"+fechaInicio+"' AND '"+fechaFinal+"'";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs=ps.executeQuery();
        while(rs.next()){
             NominaDiario n = new NominaDiario();
         n.setCodigo(rs.getInt("codigo"));
         n.setCodigoEmpleado(rs.getInt("codigoEmpleado"));
         n.setFecha(rs.getString("fecha"));
         n.setHoraInicio(rs.getString("horaInicio"));
         n.setHoraSalida(rs.getString("horaFinal"));
         n.setNombre(rs.getString("nombreEmpleado"));
         
         lsNomina.add(n);
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
       return lsNomina;
        
    } 

      
      public List listarNominas(){ 
         List <NominaDiario> lsNomina = new ArrayList();
       String sql = "SELECT * FROM nominadiario";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs=ps.executeQuery();
        while(rs.next()){
             NominaDiario n = new NominaDiario();
             
         n.setCodigo(rs.getInt("codigo"));
         n.setCodigoEmpleado(rs.getInt("codigoEmpleado"));
         n.setFecha(rs.getString("fecha"));
         n.setHoraInicio(rs.getString("horaInicio"));
         n.setHoraSalida(rs.getString("horaFinal"));
         n.setNombre(rs.getString("nombreEmpleado"));
         
         lsNomina.add(n);
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
       return lsNomina;
        
    } 
     
}
