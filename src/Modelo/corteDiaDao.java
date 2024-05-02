package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class corteDiaDao {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    double saldoInicial;

    public void aumentarSaldoInicial(double saldo, int folio) {
        String sql = "UPDATE corte set saldoInicial=? WHERE id = ?";
        corteDia c = new corteDia();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, saldo);
            ps.setInt(2, folio);
            ps.execute();

        } catch (SQLException e) {
            System.out.println(e.toString() + "ALOMEJOR segundo");
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

    public double getSaldoInicial() {
        String sql = "SELECT saldoInicial FROM corte WHERE id = (SELECT MAX(id) from corte)";
        corteDia c = new corteDia();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                c.setSaldoInicial(rs.getDouble(1));
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
        return c.getSaldoInicial();
    }

    public void crearNuevoDia(corteDia c, String hoy, String hora) {
        String sql = "INSERT INTO corte (fechaInicio, horaInicio, saldoInicial) VALUES (?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, hoy);
            ps.setString(2, hora);
            ps.setDouble(3, c.getSaldoFinal());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
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

    public String getDia() {
        String sql = "SELECT fechaInicio FROM corte WHERE id = (SELECT MAX(id) from corte)";
        String fecha = "";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                fecha = (rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "O AQUI");
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

        return fecha;
    }

    public int idMax() {
        String sql = "SELECT MAX(id) from corte";
        int num = 0;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                num = (rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "O AQUI");
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
        return num;
    }

    public boolean terminarCorte(corteDia cl) {
        String sql = "UPDATE corte SET totalVentas=?, totalCobros=?, saldoEnCaja=?, totalGastos=?, efectivo=?, diferencia=?, retiro=?, saldoFinal=?, persona=?, fechaCierre=?, horaCierre=?, comentario=?, arqueo=? WHERE id = " + idMax();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, cl.getTotalVentas());
            ps.setDouble(2, cl.getTotalCobros());
            ps.setDouble(3, cl.getSaldoEnCaja());
            ps.setDouble(4, cl.getTotalGastos());
            ps.setDouble(5, cl.getEfectivo());
            ps.setDouble(6, cl.getDiferencia());
            ps.setDouble(7, cl.getRetiro());
            ps.setDouble(8, cl.getSaldoFinal());
            ps.setString(9, cl.getPersona());
            ps.setString(10, cl.getFechaCierre());
            ps.setString(12, cl.getComentario());
            ps.setString(11, cl.getHoraCierre());
            ps.setDouble(13, cl.getArqueos());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
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

    public corteDia regresarCorte(String fecha) {
        String sql = "SELECT * FROM corte where fechaCierre=?";
        corteDia corte = new corteDia();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();
            while (rs.next()) {
                corte.setTotalVentas(rs.getDouble("totalVentas"));
                corte.setTotalGastos(rs.getDouble("totalGastos"));
                corte.setSaldoEnCaja(rs.getDouble("saldoEnCaja"));
                corte.setSaldoFinal(rs.getDouble("saldoFinal"));
                corte.setRetiro(rs.getDouble("retiro"));
                corte.setPersona(rs.getString("persona"));
                corte.setTotalCobros(rs.getDouble("totalCobros"));
                corte.setEfectivo(rs.getDouble("efectivo"));
                corte.setDiferencia(rs.getDouble("diferencia"));
                corte.setSaldoInicial(rs.getDouble("saldoInicial"));
                corte.setComentario(rs.getString("comentario"));
                corte.setArqueos(rs.getDouble("arqueo"));

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
        return corte;

    }

    public corteDia regresarCorte(int id) {
        String sql = "SELECT * FROM corte where id=?";
        corteDia corte = new corteDia();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                corte.setId(rs.getInt("id"));
                corte.setFechaInicio(rs.getString("fechaInicio"));
                corte.setHoraInicio(rs.getString("horaInicio"));
                corte.setTotalVentas(rs.getDouble("totalVentas"));
                corte.setTotalGastos(rs.getDouble("totalGastos"));
                corte.setSaldoEnCaja(rs.getDouble("saldoEnCaja"));
                corte.setSaldoFinal(rs.getDouble("saldoFinal"));
                corte.setRetiro(rs.getDouble("retiro"));
                corte.setPersona(rs.getString("persona"));
                corte.setTotalCobros(rs.getDouble("totalCobros"));
                corte.setEfectivo(rs.getDouble("efectivo"));
                corte.setDiferencia(rs.getDouble("diferencia"));
                corte.setSaldoInicial(rs.getDouble("saldoInicial"));
                corte.setComentario(rs.getString("comentario"));
                corte.setFechaCierre(rs.getString("fechaCierre"));
                corte.setHoraCierre(rs.getString("horaCierre"));
                corte.setArqueos(rs.getDouble("arqueo"));

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
        return corte;

    }

    public List cortePorPeriodo(String fechaInicio, String fechaFinal) {
        String sql = "SELECT * FROM corte where fechaCierre between ? and ?";
        List<corteDia> listaCorte = new ArrayList();

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                corteDia corte = new corteDia();
                    corte.setId(rs.getInt("id"));
                corte.setFechaInicio(rs.getString("fechaInicio"));
                corte.setHoraInicio(rs.getString("horaInicio"));
                corte.setTotalVentas(rs.getDouble("totalVentas"));
                corte.setTotalGastos(rs.getDouble("totalGastos"));
                corte.setSaldoEnCaja(rs.getDouble("saldoEnCaja"));
                corte.setSaldoFinal(rs.getDouble("saldoFinal"));
                corte.setRetiro(rs.getDouble("retiro"));
                corte.setPersona(rs.getString("persona"));
                corte.setTotalCobros(rs.getDouble("totalCobros"));
                corte.setEfectivo(rs.getDouble("efectivo"));
                corte.setDiferencia(rs.getDouble("diferencia"));
                corte.setSaldoInicial(rs.getDouble("saldoInicial"));
                corte.setComentario(rs.getString("comentario"));
                corte.setFechaCierre(rs.getString("fechaCierre"));
                corte.setHoraCierre(rs.getString("horaCierre"));
                corte.setArqueos(rs.getDouble("arqueo"));
                
                listaCorte.add(corte);
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
        return listaCorte;

    }

}
