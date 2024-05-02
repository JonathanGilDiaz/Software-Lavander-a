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
public class ArqueoDao {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public double getUltimoRetiro() {
        String sql = "SELECT retiro FROM arqueos WHERE id = (SELECT MAX(id) from arqueos)";
        double retiro = 0;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                retiro = rs.getDouble("retiro");
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "ALOMEJOR pobando");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString() + "6");
            }
        }
        return retiro;
    }

    public boolean registrarArqueo(Arqueo ar) {
        String sql = "INSERT INTO arqueos (idCorte,idEmpleado,fecha,hora,retiro,saldo,saldoFinal,comentario) VALUES (?,?,?,?,?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, ar.getIdCorte());
            ps.setInt(2, ar.getIdEmpleado());
            ps.setString(3, ar.getFecha());
            ps.setString(4, ar.getHora());
            ps.setDouble(5, ar.getRetiro());
            ps.setDouble(6, ar.getSaldo());
            ps.setDouble(7, ar.getSaldoFinal());
            ps.setString(8, ar.getComentario());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString() + "6");
            }
        }

    }

    public double totalArqueos(int codigo) {
        String sql = "SELECT SUM(retiro) AS total_retiro FROM arqueos WHERE idCorte = ?";
        double total = 0;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, codigo);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total_retiro");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString() + "6");
            }
        }
        return total;
    }

    public List listarArqueos() {
        List<Arqueo> lsArqueo = new ArrayList();
        String sql = "SELECT arqueos.*, empleados.nombre from arqueos INNER JOIN empleados ON arqueos.idEmpleado = empleados.idEmpleados";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Arqueo arqueo = new Arqueo();
                arqueo.setId(rs.getInt("id"));
                arqueo.setIdCorte(rs.getInt("idCorte"));
                arqueo.setIdEmpleado(rs.getInt("idEmpleado"));
                arqueo.setEmpleado(rs.getString("nombre"));
                arqueo.setFecha(rs.getString("fecha"));
                arqueo.setHora(rs.getString("hora"));
                arqueo.setRetiro(rs.getDouble("retiro"));
                arqueo.setSaldo(rs.getDouble("saldo"));
                arqueo.setSaldoFinal(rs.getDouble("saldoFinal"));
                arqueo.setComentario(rs.getString("comentario"));
                lsArqueo.add(arqueo); //Agregar al arreglo
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "ph yea");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString() + "6");
            }
        }
        return lsArqueo;
    }

    public List listarArqueosPeriodo(String fechaInicial, String fechaFinal) {
        List<Arqueo> lsArqueo = new ArrayList();
        String sql = "SELECT arqueos.*, empleados.nombre from arqueos INNER JOIN empleados ON arqueos.idEmpleado = empleados.idEmpleados WHERE CONCAT(fecha, ' ', hora) between ? and ?;";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicial);
            ps.setString(2, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                Arqueo arqueo = new Arqueo();
                arqueo.setId(rs.getInt("id"));
                arqueo.setIdCorte(rs.getInt("idCorte"));
                arqueo.setIdEmpleado(rs.getInt("idEmpleado"));
                arqueo.setEmpleado(rs.getString("nombre"));
                arqueo.setFecha(rs.getString("fecha"));
                arqueo.setHora(rs.getString("hora"));
                arqueo.setRetiro(rs.getDouble("retiro"));
                arqueo.setSaldo(rs.getDouble("saldo"));
                arqueo.setSaldoFinal(rs.getDouble("saldoFinal"));
                arqueo.setComentario(rs.getString("comentario"));
                lsArqueo.add(arqueo); //Agregar al arreglo
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "ph yea");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString() + "6");
            }
        }
        return lsArqueo;
    }
    
        public List listarArqueosPeriodoFecha(String fechaInicial, String fechaFinal) {
        List<Arqueo> lsArqueo = new ArrayList();
        String sql = "SELECT arqueos.*, empleados.nombre from arqueos INNER JOIN empleados ON arqueos.idEmpleado = empleados.idEmpleados WHERE fecha between ? and ?;";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicial);
            ps.setString(2, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                Arqueo arqueo = new Arqueo();
                arqueo.setId(rs.getInt("id"));
                arqueo.setIdCorte(rs.getInt("idCorte"));
                arqueo.setIdEmpleado(rs.getInt("idEmpleado"));
                arqueo.setEmpleado(rs.getString("nombre"));
                arqueo.setFecha(rs.getString("fecha"));
                arqueo.setHora(rs.getString("hora"));
                arqueo.setRetiro(rs.getDouble("retiro"));
                arqueo.setSaldo(rs.getDouble("saldo"));
                arqueo.setSaldoFinal(rs.getDouble("saldoFinal"));
                arqueo.setComentario(rs.getString("comentario"));
                lsArqueo.add(arqueo); //Agregar al arreglo
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "ph yea");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString() + "6");
            }
        }
        return lsArqueo;
    }


    public List listarArqueosCorte(int id) {
        List<Arqueo> lsArqueo = new ArrayList();
        String sql = "SELECT arqueos.*, empleados.nombre from arqueos INNER JOIN empleados ON arqueos.idEmpleado = empleados.idEmpleados where idCorte=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Arqueo arqueo = new Arqueo();
                arqueo.setId(rs.getInt("id"));
                arqueo.setIdCorte(rs.getInt("idCorte"));
                arqueo.setIdEmpleado(rs.getInt("idEmpleado"));
                arqueo.setEmpleado(rs.getString("nombre"));
                arqueo.setFecha(rs.getString("fecha"));
                arqueo.setHora(rs.getString("hora"));
                arqueo.setRetiro(rs.getDouble("retiro"));
                arqueo.setSaldo(rs.getDouble("saldo"));
                arqueo.setSaldoFinal(rs.getDouble("saldoFinal"));
                arqueo.setComentario(rs.getString("comentario"));
                lsArqueo.add(arqueo); //Agregar al arreglo
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "ph yea");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString() + "6");
            }
        }
        return lsArqueo;
    }

    public Arqueo buscarPorFolio(int id) {
        Arqueo arqueo = new Arqueo();
        String sql = "SELECT arqueos.*, empleados.nombre from arqueos INNER JOIN empleados ON arqueos.idEmpleado = empleados.idEmpleados where id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                arqueo.setId(rs.getInt("id"));
                arqueo.setIdCorte(rs.getInt("idCorte"));
                arqueo.setIdEmpleado(rs.getInt("idEmpleado"));
                arqueo.setEmpleado(rs.getString("nombre"));
                arqueo.setFecha(rs.getString("fecha"));
                arqueo.setHora(rs.getString("hora"));
                arqueo.setRetiro(rs.getDouble("retiro"));
                arqueo.setSaldo(rs.getDouble("saldo"));
                arqueo.setSaldoFinal(rs.getDouble("saldoFinal"));
                arqueo.setComentario(rs.getString("comentario"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "ph yea");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString() + "6");
            }
        }
        return arqueo;
    }

}
