package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class NotaDao {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    int r;
    int id;
    ClienteDao client = new ClienteDao();
    entrega ent = new entrega();

    public List listarNotasPorDia(String fecha) {
        List<Nota> lsNotas = new ArrayList();
        String sql = "SELECT * FROM notas WHERE diaRecibido=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setDiaEntrega(rs.getString("diaEntrega"));
                n.setHoraEntrega(rs.getString("horaEntrega"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setTotalPagar(rs.getDouble("totalPagar"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setEntrega(rs.getInt("entrega"));
                n.setSaldo(rs.getInt("saldo"));
                n.setSaldo(rs.getInt("estado"));
                n.setEstado(rs.getInt("estado"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));
                n.setFormaPago(rs.getInt("formaPago"));
                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "1");
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
        return lsNotas;
    }

    public List listarNotasPorDiaHora(String fechaInicial, String fechaFinal) {
        List<Nota> lsNotas = new ArrayList();
        String sql = "SELECT * FROM notas WHERE CONCAT(diaRecibido, ' ', horaRecibido) between ? and ?;";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicial);
            ps.setString(2, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setDiaEntrega(rs.getString("diaEntrega"));
                n.setHoraEntrega(rs.getString("horaEntrega"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setTotalPagar(rs.getDouble("totalPagar"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setEntrega(rs.getInt("entrega"));
                n.setSaldo(rs.getInt("saldo"));
                n.setSaldo(rs.getInt("estado"));
                n.setEstado(rs.getInt("estado"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));
                n.setFormaPago(rs.getInt("formaPago"));
                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "1");
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
        return lsNotas;
    }

    public void meterAnticipo(Nota n) {
        String sql = "UPDATE notas set anticipo=?, totalPagar=?, saldo=? where folio=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, n.getAnticipo());
            ps.setDouble(2, n.getTotalPagar());
            System.out.println(n.getTotalPagar());
            ps.setInt(3, n.getSaldo());
            ps.setInt(4, n.getFolio());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "2");
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

    public List listarNotasEntregaPorDia(String fecha) {
        List<Nota> lsNotas = new ArrayList();
        String sql = "SELECT * FROM notas WHERE entregaDia=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setDiaEntrega(rs.getString("diaEntrega"));
                n.setHoraEntrega(rs.getString("horaEntrega"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setTotalPagar(rs.getDouble("totalPagar"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setEntrega(rs.getInt("entrega"));
                n.setSaldo(rs.getInt("saldo"));
                n.setSaldo(rs.getInt("estado"));
                n.setEstado(rs.getInt("estado"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));

                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "3");
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
        return lsNotas;
    }

    public List listarNotasEntregaPorDiaHora(String fechaInicial, String fechaFinal) {
        List<Nota> lsNotas = new ArrayList();
        String sql = "SELECT * FROM notas WHERE CONCAT(entregaDia, ' ', entregaHora) between ? and ?;";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicial);
            ps.setString(2, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setDiaEntrega(rs.getString("diaEntrega"));
                n.setHoraEntrega(rs.getString("horaEntrega"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setTotalPagar(rs.getDouble("totalPagar"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setEntrega(rs.getInt("entrega"));
                n.setSaldo(rs.getInt("saldo"));
                n.setSaldo(rs.getInt("estado"));
                n.setEstado(rs.getInt("estado"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));

                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "3");
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
        return lsNotas;
    }

    public List listarNotasSinEntregar() {
        List<Nota> lsNotas = new ArrayList();
        String sql = "SELECT * FROM notas WHERE entrega=0";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setDiaEntrega(rs.getString("diaEntrega"));
                n.setHoraEntrega(rs.getString("horaEntrega"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setTotalPagar(rs.getDouble("totalPagar"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setEntrega(rs.getInt("entrega"));
                n.setSaldo(rs.getInt("saldo"));
                n.setSaldo(rs.getInt("estado"));
                n.setEstado(rs.getInt("estado"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));
                n.setFormaPago(rs.getInt("formaPago"));

                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "4");
        }
        return lsNotas;
    }

    public boolean entregarNota(int a, double pagar, String fecha, String hora, int idEntrega) {
        boolean entrega = false;
        Nota n = buscarPorFolio(a);
        String sql = "UPDATE notas SET entrega=1, totalPagar=?, saldo=?, entregaDia=?, entregaHora=?, folioEntrega=? WHERE folio=? AND entrega=0";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            double saldoFinal = n.getTotalPagar() - pagar;
            if (saldoFinal <= 0) {
                ps.setDouble(1, 0);
                ps.setInt(2, 1);
            } else {
                ps.setDouble(1, saldoFinal);
                ps.setInt(2, n.getSaldo());
            }
            ps.setString(3, fecha);
            ps.setString(4, hora);
            ps.setInt(5, idEntrega);
            ps.setInt(6, n.getFolio());
            ps.execute();
            entrega = true;
            JOptionPane.showMessageDialog(null, "OPERACION REALIZADA");
        } catch (SQLException e) {
            System.out.println(e.toString() + "5");
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

        return entrega;
    }

    public List listarNotas() {
        List<Nota> lsNotas = new ArrayList();
        String sql = "SELECT * FROM notas";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setDiaEntrega(rs.getString("diaEntrega"));
                n.setHoraEntrega(rs.getString("horaEntrega"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setTotalPagar(rs.getDouble("totalPagar"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setEntrega(rs.getInt("entrega"));
                n.setSaldo(rs.getInt("saldo"));
                n.setSaldo(rs.getInt("estado"));
                n.setEstado(rs.getInt("estado"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));
                n.setEntregaDia(rs.getString("entregaDia"));
                n.setHoraEntrega(rs.getString("entregaHora"));
                n.setIva(rs.getDouble("iva"));
                n.setSubtotal(rs.getDouble("subTotal"));

                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "7");
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
        return lsNotas;

    }

    public List listarNotasEntregas() {
        List<entrega> lsNotas = new ArrayList();
        String sql = "SELECT * FROM entregas";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                entrega ent = new entrega();
                ent.setId(rs.getInt("identregas"));
                ent.setIdnota(rs.getInt("idNota"));
                ent.setCantidadAPagar(rs.getDouble("CantidadAEntregar"));
                ent.setPago(rs.getDouble("Pago"));
                ent.setFechaEntrega(rs.getString("fechaEntrega"));
                ent.setHoraEntrega("horaEntrega");
                ent.setComentario(rs.getString("comentario"));
                ent.setIdEmpleado(rs.getInt("idEmpleado"));
                ent.setFormaPago(rs.getInt("formaPago"));
                lsNotas.add(ent);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "8");
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
        return lsNotas;

    }

    public entrega listarNotasEntregasFolio(int folio) {
        String sql = "SELECT * FROM entregas where idNota = ?";
        entrega ent = new entrega();
        try {

            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, folio);
            rs = ps.executeQuery();

            if (rs.next()) {
                ent.setId(rs.getInt("identregas"));
                ent.setIdnota(rs.getInt("idNota"));
                ent.setCantidadAPagar(rs.getDouble("CantidadAEntregar"));
                ent.setPago(rs.getDouble("Pago"));
                ent.setFechaEntrega(rs.getString("fechaEntrega"));
                ent.setHoraEntrega("horaEntrega");
                ent.setComentario(rs.getString("comentario"));
                ent.setIdEmpleado(rs.getInt("idEmpleado"));
                ent.setFormaPago(rs.getInt("formaPago"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "9");
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
        return ent;

    }

    public List listarNotasEntregasPeriodo(String fechaInicio, String fechaFinal) {
        List<entrega> lsNotas = new ArrayList();
        String sql = "SELECT * FROM entregas WHERE fechaEntrega between ? and ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                entrega ent = new entrega();
                ent.setId(rs.getInt("identregas"));
                ent.setIdnota(rs.getInt("idNota"));
                ent.setCantidadAPagar(rs.getDouble("CantidadAEntregar"));
                ent.setPago(rs.getDouble("Pago"));
                ent.setFechaEntrega(rs.getString("fechaEntrega"));
                ent.setHoraEntrega(rs.getString("horaEntrega"));
                ent.setComentario(rs.getString("comentario"));
                ent.setIdEmpleado(rs.getInt("idEmpleado"));
                ent.setFormaPago(rs.getInt("formaPago"));
                lsNotas.add(ent);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "10");
        }
        return lsNotas;

    }

    public List listarPorFecha(String fechaInicio, String fechaFinal) {
        List<Nota> lsNotas = new ArrayList();
        String sql = "SELECT * FROM notas WHERE diaRecibido between ? and ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setDiaEntrega(rs.getString("diaEntrega"));
                n.setHoraEntrega(rs.getString("horaEntrega"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setTotalPagar(rs.getDouble("totalPagar"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setEntrega(rs.getInt("entrega"));
                n.setSaldo(rs.getInt("saldo"));
                n.setSaldo(rs.getInt("estado"));
                n.setEstado(rs.getInt("estado"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));
                n.setFormaPago(rs.getInt("formaPago"));
                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "11");
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
        return lsNotas;

    }

    public List listarPorFechaYCliente(String fechaInicio, String fechaFinal, int id) {
        List<Nota> lsNotas = new ArrayList();
        String sql = "SELECT * FROM notas WHERE codigoCliente=? and diaRecibido between ? and ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, fechaInicio);
            ps.setString(3, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setDiaEntrega(rs.getString("diaEntrega"));
                n.setHoraEntrega(rs.getString("horaEntrega"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setTotalPagar(rs.getDouble("totalPagar"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setEntrega(rs.getInt("entrega"));
                n.setSaldo(rs.getInt("saldo"));
                n.setSaldo(rs.getInt("estado"));
                n.setEstado(rs.getInt("estado"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));
                n.setFormaPago(rs.getInt("formaPago"));
                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "12");
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
        return lsNotas;

    }

    public Nota buscarPorFolio(int a) {
        String sql = "SELECT * FROM notas WHERE folio = ? ";
        Nota n = new Nota();
        boolean bandera = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, a);
            rs = ps.executeQuery();
            if (rs.next()) {
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setDiaEntrega(rs.getString("diaEntrega"));
                n.setHoraEntrega(rs.getString("horaEntrega"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setTotalPagar(rs.getDouble("totalPagar"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setEntrega(rs.getInt("entrega"));
                n.setSaldo(rs.getInt("saldo"));
                n.setEstado(rs.getInt("estado"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));
                n.setIva(rs.getDouble("iva"));
                n.setSubtotal(rs.getDouble("subTotal"));
                n.setEntregaDia(rs.getString("entregaDia"));
                n.setEntregaHora(rs.getString("entregaHora"));
                bandera = true;
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "13");
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
        if (bandera == false) {
            n.setNombre("fallo");
        }
        return n;
    }

    public int idVenta() {

        String sql = "SELECT MAX(folio) FROM notas";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            if (id == 0) {
                id = 11611;
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "14");
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
        return id;
    }

    public void anotarEntrega(int folio, double pago, String fecha, String hora, String comentario, int idEmpleado, int formaPago) {
        String sql = "INSERT INTO entregas (idNota, CantidadAEntregar, Pago, fechaEntrega, horaEntrega, comentario,idEmpleado,formaPago) VALUES (?,?,?,?,?,?,?,?)";
        try {
            Nota not = buscarPorFolio(folio);
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, not.getFolio());
            ps.setDouble(2, not.getTotalPagar());
            ps.setDouble(3, pago);
            ps.setString(4, fecha);
            ps.setString(5, hora);
            ps.setString(6, comentario);
            ps.setInt(7, idEmpleado);
            ps.setInt(8, formaPago);
            ps.execute();

        } catch (SQLException e) {
            System.out.println(e.toString() + "Aqui es JEJEJJE 15");
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

    public int registrarVenta(Nota n, String hora, String fecha) {
        String sql = "INSERT INTO notas (codigoCliente, anticipo, diaEntrega, horaEntrega, nombreCliente, apellidoCliente, idRecibe, totalPagar, totalVenta, entrega, saldo,estado,entregaDia,entregaHora,diaRecibido,horaRecibido, iva, subTotal,formaPago) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, n.getCodigoCLiente());
            ps.setDouble(2, n.getAnticipo());
            ps.setString(3, n.getDiaEntrega());
            ps.setString(4, n.getHoraEntrega());
            ps.setString(5, n.getNombre());
            ps.setString(6, n.getApellido());
            ps.setInt(7, n.getIdRecibe());
            ps.setDouble(8, n.getTotalPagar());
            ps.setDouble(9, n.getVentaTotal());
            ps.setInt(10, 0);
            if (n.getSaldo() == 1) {
                ps.setInt(11, 1);
            } else {
                ps.setInt(11, 0);
            }
            ps.setInt(12, 0);
            ps.setString(13, "0001-01-01");
            ps.setString(14, "00:01");
            ps.setString(15, fecha);
            ps.setString(16, hora);
            ps.setDouble(17, n.getIva());
            ps.setDouble(18, n.getSubtotal());
            ps.setInt(19, n.getFormaPago());
            ps.execute();
            Cliente c = client.BuscarPorCodigo(n.getCodigoCLiente());
            client.aumentarVecesAsistidas(c);

        } catch (SQLException e) {
            System.out.println(e.toString() + "Aqui es JEJEJJE 16");
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
        return r;
    }

    public List listarDetalles() {
        List<Detalle> lsNotas = new ArrayList();
        String sql = "SELECT * FROM detalle";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Detalle ent = new Detalle();
                ent.setCantidad(rs.getDouble("cantidad"));
                ent.setCodigoPrecio(rs.getInt("codigoPrecio"));
                ent.setComentario(rs.getString("detalle"));
                ent.setDescripcion(rs.getString("descripcion"));
                ent.setIdVenta(rs.getInt("idVenta"));
                ent.setPrecioFinal(rs.getDouble("precioFinal"));
                ent.setPrecioUnitario(rs.getDouble("precioUnitario"));
                lsNotas.add(ent);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "ph yea 18");
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
        return lsNotas;

    }

    public List regresarDetalles(int id) {
        List<Detalle> listaDetalle = new ArrayList();
        String sql = "SELECT * FROM detalle WHERE idVenta = ? ";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            int a = 0;
            while (rs.next()) {
                Detalle detalle = new Detalle();
                detalle.setIdVenta(rs.getInt("idVenta"));
                detalle.setCantidad(rs.getDouble("cantidad"));
                detalle.setDescripcion(rs.getString("descripcion"));
                detalle.setPrecioUnitario(rs.getDouble("precioUnitario"));
                detalle.setPrecioFinal(rs.getDouble("precioFinal"));
                detalle.setComentario(rs.getString("detalle"));
                detalle.setCodigoPrecio(rs.getInt("codigoPrecio"));
                listaDetalle.add(detalle);
            }

        } catch (SQLException e) {
            System.out.println(e.toString() + "19");
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

        return listaDetalle;
    }

    public int registrarDetalle(Detalle dv) {
        String sql = "INSERT INTO detalle (cantidad, descripcion, precioUnitario, precioFinal, idVenta, detalle,codigoPrecio) VALUES (?,?,?,?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, dv.getCantidad());
            ps.setString(2, dv.getDescripcion());
            ps.setDouble(3, dv.getPrecioUnitario());
            ps.setDouble(4, dv.getPrecioFinal());
            ps.setInt(5, dv.getId());
            ps.setString(6, dv.getComentario());
            ps.setDouble(7, dv.getCodigoPrecio());
            ps.execute();
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
        return r;
    }

    public int regresarFolioCancelacion(int id) {
        String sql = "SELECT * FROM cancelar WHERE folio = ? ";
        int folioRegresar = 0;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                folioRegresar = rs.getInt("idcancelar");

            }

        } catch (SQLException e) {
            System.out.println(e.toString() + "desde aqi  21o");
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

        return folioRegresar;
    }

    public List listarNotasCanceladas(String fechaInicio, String fechaFinal) {
        String sql = "SELECT * FROM cancelar WHERE fecha between ? and ?";
        List<Cancelar> listaCancelar = new ArrayList();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cancelar c = new Cancelar();
                c.setId(rs.getInt("idcancelar"));
                c.setFolio(rs.getInt("folio"));
                c.setFecha(rs.getString("fecha"));
                c.setComentario(rs.getString("comentario"));
                c.setIdEmpleada(rs.getInt("idEmpleada"));
                c.setHora(rs.getString("hora"));
                c.setCantidad(rs.getDouble("cantidad"));
                listaCancelar.add(c);
            }

        } catch (SQLException e) {
            System.out.println(e.toString() + "desde aqio 22");
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

        return listaCancelar;
    }

    public boolean cancelarNotaTablaNota(int folio, int cancelado) {
        String sql = "UPDATE notas SET estado=1, anticipo=0, totalVenta=0, totalPagar=0, idCancelacion=" + cancelado + " WHERE folio=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, folio);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString() + "falla aqui");
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

    public boolean soloCancelar(int folio) {
        String sql = "UPDATE notas SET estado=1 WHERE folio=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, folio);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString() + "falla aqui");
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

    public List listarPorFechaAnti(String fechaInicio, String fechaFinal) {
        List<Nota> lsNotas = new ArrayList();
        String sql = "SELECT * FROM antinotas WHERE diaRecibido between ? and ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));
                n.setComentario(rs.getString("comentario"));
                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "11");
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
        return lsNotas;

    }

    public List listarAntiNotasPorDia(String fecha) {
        List<Nota> lsNotas = new ArrayList();
        String sql = "SELECT * FROM antinotas WHERE diaRecibido=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));
                n.setComentario(rs.getString("comentario"));
                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "1");
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
        return lsNotas;
    }

        public List listarAntiNotasPorDiaHora(String fechaInicial, String fechaFinal) {
        List<Nota> lsNotas = new ArrayList();
          String sql = "SELECT * FROM antinotas WHERE CONCAT(diaRecibido, ' ', horaRecibido) between ? and ?;";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicial);
            ps.setString(2, fechaFinal);
            rs = ps.executeQuery();
            while (rs.next()) {
                Nota n = new Nota();
                n.setFolio(rs.getInt("folio"));
                n.setCodigoCLiente(rs.getInt("codigoCliente"));
                n.setAnticipo(rs.getDouble("anticipo"));
                n.setNombre(rs.getString("nombreCliente"));
                n.setApellido(rs.getString("apellidoCliente"));
                n.setIdRecibe(rs.getInt("idRecibe"));
                n.setVentaTotal(rs.getDouble("totalVenta"));
                n.setFecha(rs.getString("diaRecibido"));
                n.setHora(rs.getString("horaRecibido"));
                n.setComentario(rs.getString("comentario"));
                lsNotas.add(n);
            }
        } catch (SQLException e) {
            System.out.println(e.toString() + "1");
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
        return lsNotas;
    }

    
    public int registrarAntiVenta(Nota n, String hora, String fecha, String comentario) {
        String sql = "INSERT INTO antinotas (codigoCliente, anticipo, nombreCliente, apellidoCliente, idRecibe, totalVenta, diaRecibido, horaRecibido, folio, comentario) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, n.getCodigoCLiente());
            ps.setDouble(2, n.getAnticipo() * (-1));
            ps.setString(3, n.getNombre());
            ps.setString(4, n.getApellido());
            ps.setInt(5, n.getIdRecibe());
            ps.setDouble(6, n.getVentaTotal() * (-1));
            ps.setString(7, fecha);
            ps.setString(8, hora);
            ps.setInt(9, n.getFolio());
            ps.setString(10, comentario);
            ps.execute();

        } catch (SQLException e) {
            System.out.println(e.toString() + "Aqui es JEJEJJE 16");
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
        return r;
    }

    public void cancelarNoysTablaCancelar(int folio, String fecha, String comentario, int idEmpleado, String hora, double cantidad) {
        String sql = "INSERT INTO cancelar (folio, fecha, comentario, idEmpleada, hora, cantidad) VALUES (?,?,?,?,?,?)";
        try {
            Nota not = buscarPorFolio(folio);
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, not.getFolio());
            ps.setString(2, fecha);
            ps.setString(3, comentario);
            ps.setInt(4, idEmpleado);
            ps.setString(5, hora);
            ps.setDouble(6, cantidad);
            ps.execute();

        } catch (SQLException e) {
            System.out.println(e.toString() + "Aqui es 24");
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
}
