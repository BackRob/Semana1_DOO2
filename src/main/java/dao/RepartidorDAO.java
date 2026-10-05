package dao;

import model.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    //inserta un repartidor, devuelve true si se guardo
    public boolean guardar(Repartidor repartidor) {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet claves = null;
        try {
            conexion = ConexionBD.conectar();
            sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, repartidor.getNombre());
            sentencia.executeUpdate();

            claves = sentencia.getGeneratedKeys();
            if (claves.next()) {
                repartidor.setId(claves.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar el repartidor: " + e.getMessage());
            return false;
        } finally {
            ConexionBD.cerrar(conexion, sentencia, claves);
        }
    }

    //metodo pedido por la pauta, lee todos los repartidores con ResultSet
    public List<Repartidor> listarTodos() {
        String sql = "SELECT id, nombre FROM repartidor ORDER BY id";
        List<Repartidor> repartidores = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        try {
            conexion = ConexionBD.conectar();
            sentencia = conexion.prepareStatement(sql);
            resultado = sentencia.executeQuery();
            while (resultado.next()) {
                repartidores.add(new Repartidor(resultado.getInt("id"), resultado.getString("nombre")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar repartidores: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(conexion, sentencia, resultado);
        }
        return repartidores;
    }
}
