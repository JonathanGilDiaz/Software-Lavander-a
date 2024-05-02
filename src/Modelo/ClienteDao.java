// la siguiente clase contiene los metodos de la clase cliente con la base de datos
package Modelo;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ClienteDao {
    //se declaran las variables necesarias
    Conexion cn = new Conexion(); //con este hacemos la conexion a base de datos
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

     public int idCliente(){
        
        String sql = "SELECT MAX(id) FROM clientes";
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
    
    
     public boolean ClienteRepetidaActualizar(Cliente cl){ //el siguiente metodo verifica que al modificar un cliente, no se repita
        String sql = "SELECT * FROM clientes"; //sentencia sql
        boolean bandera=false; //variable para indicar si efectivamente se duplico o no. Si es true se ha repetido, false no se repitio
        
        try{
          con = cn.getConnection(); //se establece la conexion
           ps = con.prepareStatement(sql); //se prepara la sentencia sql
         rs=ps.executeQuery(); //se ejecuta la sentencia sql
            while(rs.next()){
             String nombre=rs.getString("nombre"); //recibimos los valores de cada cliente para verificar si hay algun repetido
             String apellido = rs.getString("apellido");
             int codigo = rs.getInt("id");
             //ahora, verificamos si se ha duplicado algun cliente
             if(cl.getNombre().equals(nombre) && cl.getApellido().equals(apellido) && cl.getId()!= codigo){
                 bandera=true; //Si se duplico, se cambia la bandera a true
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
        return bandera; //se retorna la variable boolean
    }
    
    public List buscarLetra(String buscar){ //este metodo permite el buscar algun cliente con ciertas letras
        List <Cliente> ListaCl = new ArrayList(); //se declara un arreglo cliente para guardar los datos
        String filtro=""+buscar+"%"; //se guarda la variable con la cual se buscara 
       String sql = "SELECT * FROM clientes WHERE nombre LIKE"+'"'+filtro+'"';//Sentencia sql
       try{
        con = cn.getConnection(); //Se prepara la conexion
        ps = con.prepareStatement(sql); //se prepara la sentencia sql
        rs=ps.executeQuery(); //se ejecuta la instruccion sql
        while(rs.next()){ // si hay algun dato, entra en el ciclo
         Cliente cl = new Cliente(); //Se crea el cliente con el cual se guardaran los datos para posteriormente agregarlos a la lista
         cl.setId(rs.getInt("id"));// se guardan los datos traidos por la base de datos
         cl.setNombre(rs.getString("nombre"));
         cl.setApellido(rs.getString("apellido"));
         cl.setTelefono(rs.getString("telefono"));
         cl.setAdeudo(rs.getDouble("adeudo"));
         cl.setVecesAsistidas(rs.getInt("vecesAsistidas"));
         cl.setCorreo(rs.getString("correo"));
         cl.setDomicilio(rs.getString("direccion"));
         cl.setEstado(rs.getInt("estado"));
         cl.setFecha(rs.getString("fechaCreacion"));
         ListaCl.add(cl); //se agrega el cliente al arreglo
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
    
    //este metodo no se esta usando en estos momentos, lo que hace es reiniciar el identificador autoincrement del cliente
    public boolean ReiniciarContador(){ 
              String sql = "SET @num:= 0;";
              String sql1 = "UPDATE clientes SET id = @num:= (@num + 1);";
             String sql2 = "ALTER TABLE clientes AUTO_INCREMENT = 1;";
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
    
    //metodo para registrar un cliente en la base de datos
    public boolean RegistrarCliente(Cliente cl){ //para ello, se necesita un cliente para vaciar los datos
      String sql = "INSERT INTO clientes (nombre, apellido, telefono, adeudo, vecesAsistidas, correo, direccion, fechaCreacion) VALUES (?,?,?,?,?,?,?,?)"; //sentencia sql
        try{
          con = cn.getConnection(); //se establece la conexion
          ps = con.prepareStatement(sql); //se prepara la instruccion
          ps.setString(1,cl.getNombre()); // se vacian los datos del cliente a la instrucciones
          ps.setString(2,cl.getApellido());
          ps.setString(3, cl.getTelefono());
          ps.setDouble(4, 0);
          ps.setInt(5,0);
          ps.setString(6, cl.getCorreo());
          ps.setString(7, cl.getDomicilio());
          ps.setString(8, cl.getFecha());      
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
    
    public List ListarCliente(){ // metodo para regresar la informacion de todos los datos de la base de datos cliente
       List <Cliente> ListaCl = new ArrayList();
       String sql = "SELECT * FROM clientes";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs=ps.executeQuery();
        while(rs.next()){
         Cliente cl = new Cliente();
         cl.setId(rs.getInt("id"));
         cl.setNombre(rs.getString("nombre"));
         cl.setApellido(rs.getString("apellido"));
         cl.setTelefono(rs.getString("telefono"));
         cl.setAdeudo(rs.getDouble("adeudo"));
         cl.setVecesAsistidas(rs.getInt("vecesAsistidas"));
         cl.setDomicilio(rs.getString("direccion"));
         cl.setCorreo(rs.getString("correo"));
         cl.setEstado(rs.getInt("estado"));
         cl.setFecha(rs.getString("fechaCreacion"));
         ListaCl.add(cl);
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
       return ListaCl;
        
    }
    
     public List listarClientePeriodoCreacion(String fechaInicio, String fechaFinal){
       List <Cliente> ListaCl = new ArrayList();
       String sql = "SELECT * FROM clientes where fechaCreacion between ? and ?";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
         ps.setString(1, fechaInicio);
         ps.setString(2, fechaFinal); 
        rs=ps.executeQuery();
        while(rs.next()){
         Cliente cl = new Cliente();
         cl.setId(rs.getInt("id"));
         cl.setNombre(rs.getString("nombre"));
         cl.setApellido(rs.getString("apellido"));
         cl.setTelefono(rs.getString("telefono"));
         cl.setAdeudo(rs.getDouble("adeudo"));
         cl.setVecesAsistidas(rs.getInt("vecesAsistidas"));
         cl.setDomicilio(rs.getString("direccion"));
         cl.setCorreo(rs.getString("correo"));
         cl.setEstado(rs.getInt("estado"));
         cl.setFecha(rs.getString("fechaCreacion"));
         ListaCl.add(cl);
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
       return ListaCl;
        
    }
    
    
    public List ListarClienteConAdeudos(){ //se van a listar los clientes que tengan adeudo
       List <Cliente> ListaCl = new ArrayList();
       String sql = "SELECT * FROM clientes";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs=ps.executeQuery();
        while(rs.next()){
            if(rs.getDouble("adeudo") > 0){ //aqui verificamos la variable adeudo del cliente para agregarlo a la lista o no
         Cliente cl = new Cliente();
         cl.setId(rs.getInt("id"));
         cl.setNombre(rs.getString("nombre"));
         cl.setApellido(rs.getString("apellido"));
         cl.setTelefono(rs.getString("telefono"));
         cl.setAdeudo(rs.getDouble("adeudo"));
         cl.setVecesAsistidas(rs.getInt("vecesAsistidas"));
                  cl.setCorreo(rs.getString("correo"));
         ListaCl.add(cl);}
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
       return ListaCl;
        
    }
    
    //Con la varuable int id, indicaremos que cliente se va a eliminar
    public boolean EliminarCliente(int id){ 
      String sql = "DELETE FROM clientes WHERE id = ?";
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
    
    //metodo para modificar un cliente a base de su id, para lo cual necesitamos un cliente
    public boolean ModificarCliente(Cliente cl){
     String sql = "UPDATE clientes SET nombre=?, apellido=?, telefono=?, correo=?, direccion=? WHERE id=?";
     try{
                 con = cn.getConnection();
          ps=con.prepareStatement(sql);
          ps.setString(1, cl.getNombre());
          ps.setString(2,cl.getApellido());
          ps.setString(3,cl.getTelefono());
          ps.setString(4, cl.getCorreo());
          ps.setString(5, cl.getDomicilio());
          ps.setInt(6,cl.getId());
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
    
    public boolean ModificarEstado(int codigo, int indicador){
     String sql = "UPDATE clientes SET estado=? WHERE id=?";
     try{
                 con = cn.getConnection();
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
    
    //Se retornaran los clientes a base de su apellido
       public List buscarPorApellido(String a){ 
       List <Cliente> ListaCl = new ArrayList();
       String sql = "SELECT * FROM clientes";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs=ps.executeQuery();
        int i = 0;//variable para verificar si hay algun cliente con ese apellido
        while(rs.next()){
            if(a.equals(rs.getString("apellido"))){
         Cliente cl = new Cliente();
         cl.setId(rs.getInt("id"));
         cl.setNombre(rs.getString("nombre"));
         cl.setApellido(rs.getString("apellido"));
         cl.setTelefono(rs.getString("telefono"));
         cl.setAdeudo(rs.getDouble("adeudo"));
         cl.setVecesAsistidas(rs.getInt("vecesAsistidas"));
                  cl.setCorreo(rs.getString("correo"));
         ListaCl.add(cl);
         i++; //se suma 1 unidad al contador
            }
            
        } 
        if(i==0)    JOptionPane.showMessageDialog(null, "NO SE ENCONTRO NINGUN CLIENTE"); // aqui verificamos si se detecto algun cliente con ese apellido
        // si esto fue correcto, mostramos un mensaje
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
       return ListaCl;
        
    }
       
       public List buscarPorNombre(String a){ //lo mismo que el anterior, pero ahora con el nombre
       List <Cliente> ListaCl = new ArrayList();
       String sql = "SELECT * FROM clientes";
       try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs=ps.executeQuery();
        int i = 0;
        while(rs.next()){
            if(a.equals(rs.getString("nombre"))){
         Cliente cl = new Cliente();
         cl.setId(rs.getInt("id"));
         cl.setNombre(rs.getString("nombre"));
         cl.setApellido(rs.getString("apellido"));
         cl.setTelefono(rs.getString("telefono"));
         cl.setAdeudo(rs.getDouble("adeudo"));
         cl.setVecesAsistidas(rs.getInt("vecesAsistidas"));
                  cl.setCorreo(rs.getString("correo"));
         ListaCl.add(cl);
         i++;
            }
            
        } 
        if(i==0)    JOptionPane.showMessageDialog(null, "NO SE ENCONTRO NINGUN CLIENTE");
       }catch(SQLException e){
           System.out.println(e.toString());
       }
       return ListaCl;
        
    }
       
       //ahora, se retornara un cliente con ayuda de su id
    public Cliente BuscarPorCodigo(int a){
          String sql = "SELECT * FROM clientes WHERE id = ?";
          Cliente cl = new Cliente();
                  boolean bandera = false;   

          try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, a);
        rs=ps.executeQuery();
         if(rs.next()){
         cl.setId(rs.getInt("id"));
         cl.setNombre(rs.getString("nombre"));
         cl.setApellido(rs.getString("apellido"));
         cl.setTelefono(rs.getString("telefono"));
         cl.setAdeudo(rs.getDouble("adeudo"));
         cl.setVecesAsistidas(rs.getInt("vecesAsistidas"));
                  cl.setCorreo(rs.getString("correo"));
                  cl.setDomicilio(rs.getString("direccion"));
                  cl.setEstado(rs.getInt("estado"));
                  cl.setFecha(rs.getString("fechaCreacion"));
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
       if(bandera==false) {
       cl.setNombre("");
       cl.setApellido("");
       } 
       return cl;

    }
    
    //este metodo sera para que cuando un cliente asista, lo detecte y aumente su contador
    public void aumentarVecesAsistidas(Cliente cl){
         String sql = "UPDATE clientes SET vecesAsistidas=? WHERE id=?";
     try{
                 con = cn.getConnection();
          ps=con.prepareStatement(sql);
          ps.setInt(1,cl.getVecesAsistidas()+1);//se agrega 1 unidad a la variable Veces Asistida
          ps.setInt(2,cl.getId());
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
    
    //verifica si se ha duplicado algun cliente, similar al anterior, solo uno de los 2 funciona correctmente
    public boolean clienteRepetido(Cliente cl){
        String sql = "SELECT * FROM clientes WHERE nombre=? AND apellido=?";
        boolean bandera=false;
        try{
          con = cn.getConnection();
           ps = con.prepareStatement(sql);
         ps.setString(1, cl.getNombre());  
         ps.setString(2, cl.getApellido());
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
    
    
    
}
