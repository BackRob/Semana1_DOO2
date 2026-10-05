package dao;

import model.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD de la tabla entregas (relacion entre pedidos y repartidores).
 * Todos los metodos lanzan SQLException con un mensaje claro para mostrarlo en la ventana.
 */
public class EntregaDAO {

    /** Registra la entrega y le deja puesto el id que genero la BD. */
    public void create(Entrega entrega) throws SQLException {
        String sql = "INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet claves = null;
        try {
            conexion = ConexionDB.conectar();
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
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("El pedido o el repartidor elegido ya no existe en la base de datos.", e);
        } catch (SQLException e) {
            throw new SQLException("Error al registrar la entrega: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, claves);
        }
    }

    /** Lee todas las entregas. */
    public List<Entrega> readAll() throws SQLException {
        return readFiltrado(null, null);
    }

    /** Lee las entregas de un pedido. */
    public List<Entrega> readByPedido(int idPedido) throws SQLException {
        return readFiltrado(idPedido, null);
    }

    /** Lee las entregas de un repartidor. */
    public List<Entrega> readByRepartidor(int idRepartidor) throws SQLException {
        return readFiltrado(null, idRepartidor);
    }

    /**
     * Lee las entregas filtrando por pedido y/o repartidor (null = sin filtro).
     * Usa JOIN para traer la direccion del pedido y el nombre del repartidor.
     */
    public List<Entrega> readFiltrado(Integer idPedido, Integer idRepartidor) throws SQLException {
        String sql = "SELECT e.id, e.id_pedido, e.id_repartidor, e.fecha, e.hora, p.direccion, r.nombre "
                + "FROM entregas e "
                + "LEFT JOIN pedidos p ON e.id_pedido = p.id "
                + "LEFT JOIN repartidores r ON e.id_repartidor = r.id "
                + "WHERE 1=1";
        if (idPedido != null) sql += " AND e.id_pedido = ?";
        if (idRepartidor != null) sql += " AND e.id_repartidor = ?";
        sql += " ORDER BY e.id";

        List<Entrega> entregas = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql);
            int posicion = 1;
            if (idPedido != null) sentencia.setInt(posicion++, idPedido);
            if (idRepartidor != null) sentencia.setInt(posicion, idRepartidor);
            resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Date fecha = resultado.getDate("fecha");
                Time hora = resultado.getTime("hora");
                Entrega entrega = new Entrega(resultado.getInt("id"),
                        resultado.getInt("id_pedido"),
                        resultado.getInt("id_repartidor"),
                        fecha != null ? fecha.toLocalDate() : null,
                        hora != null ? hora.toLocalTime() : null);
                entrega.setDireccionPedido(resultado.getString("direccion"));
                entrega.setNombreRepartidor(resultado.getString("nombre"));
                entregas.add(entrega);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las entregas: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, resultado);
        }
        return entregas;
    }

    /** Actualiza pedido, repartidor, fecha y hora de la entrega con ese id. */
    public void update(Entrega entrega) throws SQLException {
        String sql = "UPDATE entregas SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, entrega.getIdPedido());
            sentencia.setInt(2, entrega.getIdRepartidor());
            sentencia.setDate(3, Date.valueOf(entrega.getFecha()));
            sentencia.setTime(4, Time.valueOf(entrega.getHora()));
            sentencia.setInt(5, entrega.getId());
            if (sentencia.executeUpdate() == 0) {
                throw new SQLException("No existe una entrega con id " + entrega.getId());
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("El pedido o el repartidor elegido ya no existe en la base de datos.", e);
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la entrega: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, null);
        }
    }

    /** Elimina la entrega con ese id. */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM entregas WHERE id = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, id);
            if (sentencia.executeUpdate() == 0) {
                throw new SQLException("No existe una entrega con id " + id);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la entrega: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, null);
        }
    }
}
