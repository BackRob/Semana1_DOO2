package dao;

import model.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD de la tabla repartidores.
 * Todos los metodos lanzan SQLException con un mensaje claro para mostrarlo en la ventana.
 */
public class RepartidorDAO {

    /** Inserta un repartidor y le deja puesto el id que genero la BD. */
    public void create(Repartidor repartidor) throws SQLException {
        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet claves = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, repartidor.getNombre());
            sentencia.executeUpdate();

            //id AUTO_INCREMENT
            claves = sentencia.getGeneratedKeys();
            if (claves.next()) {
                repartidor.setId(claves.getInt(1));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al registrar el repartidor: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, claves);
        }
    }

    /** Lee todos los repartidores ordenados por id. */
    public List<Repartidor> readAll() throws SQLException {
        String sql = "SELECT id, nombre FROM repartidores ORDER BY id";
        List<Repartidor> repartidores = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql);
            resultado = sentencia.executeQuery();
            while (resultado.next()) {
                repartidores.add(new Repartidor(resultado.getInt("id"), resultado.getString("nombre")));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los repartidores: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, resultado);
        }
        return repartidores;
    }

    /** Cambia el nombre del repartidor con ese id. */
    public void update(Repartidor repartidor) throws SQLException {
        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, repartidor.getNombre());
            sentencia.setInt(2, repartidor.getId());
            if (sentencia.executeUpdate() == 0) {
                throw new SQLException("No existe un repartidor con id " + repartidor.getId());
            }
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el repartidor: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, null);
        }
    }

    /** Elimina el repartidor. Si tiene entregas, la llave foranea no lo deja borrar. */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM repartidores WHERE id = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, id);
            if (sentencia.executeUpdate() == 0) {
                throw new SQLException("No existe un repartidor con id " + id);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("No se puede eliminar el repartidor " + id + " porque tiene entregas asociadas. Elimina primero sus entregas.", e);
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el repartidor: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, null);
        }
    }
}
