package dao;

import model.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

public class EntregaDAO {

    //registra la relacion entre pedido y repartidor, devuelve true si se guardo
    public boolean guardar(Entrega entrega) {
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet claves = null;
        try {
            conexion = ConexionBD.conectar();
            sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            sentencia.setInt(1, entrega.getIdPedido());
            sentencia.setInt(2, entrega.getIdRepartidor());
            sentencia.setDate(3, Date.valueOf(entrega.getFecha()));
            sentencia.setTime(4, Time.valueOf(entrega.getHora()));
            sentencia.executeUpdate();

            claves = sentencia.getGeneratedKeys();
            if (claves.next()) {
                entrega.setId(claves.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar la entrega: " + e.getMessage());
            return false;
        } finally {
            ConexionBD.cerrar(conexion, sentencia, claves);
        }
    }
}
