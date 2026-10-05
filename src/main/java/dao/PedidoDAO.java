package dao;

import model.EstadoPedido;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD de la tabla pedidos.
 * Todos los metodos lanzan SQLException con un mensaje claro para mostrarlo en la ventana.
 */
public class PedidoDAO {

    /** Inserta el pedido y le deja puesto el id que genero la BD. */
    public void create(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet claves = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, pedido.getDireccionEntrega());
            sentencia.setString(2, pedido.getTipo());
            sentencia.setString(3, pedido.getEstado().name());
            sentencia.executeUpdate();

            //id AUTO_INCREMENT
            claves = sentencia.getGeneratedKeys();
            if (claves.next()) {
                pedido.setIdPedido(claves.getInt(1));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al registrar el pedido: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, claves);
        }
    }

    /** Lee todos los pedidos. */
    public List<Pedido> readAll() throws SQLException {
        return readFiltrado(null, null);
    }

    /**
     * Lee los pedidos filtrando por estado y/o tipo.
     * Si un filtro viene null no se usa (null, null = todos).
     */
    public List<Pedido> readFiltrado(String estado, String tipo) throws SQLException {
        //se arma el WHERE segun los filtros que vengan
        String sql = "SELECT id, direccion, tipo, estado FROM pedidos WHERE 1=1";
        if (estado != null) sql += " AND estado = ?";
        if (tipo != null) sql += " AND tipo = ?";
        sql += " ORDER BY id";

        List<Pedido> pedidos = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql);
            int posicion = 1;
            if (estado != null) sentencia.setString(posicion++, estado);
            if (tipo != null) sentencia.setString(posicion, tipo);
            resultado = sentencia.executeQuery();
            while (resultado.next()) {
                //arma el objeto segun la columna tipo (la distancia no se guarda en la BD)
                Pedido pedido = Pedido.crear(resultado.getString("tipo"),
                        resultado.getInt("id"),
                        resultado.getString("direccion"));
                if (pedido != null) {
                    pedido.setEstado(resultado.getString("estado"));
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los pedidos: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, resultado);
        }
        return pedidos;
    }

    /** Lee solo los pedidos con el estado indicado (lo usa la simulacion de entregas). */
    public List<Pedido> readByEstado(EstadoPedido estado) throws SQLException {
        return readFiltrado(estado.name(), null);
    }

    /** Actualiza direccion, tipo y estado del pedido con ese id. */
    public void update(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, pedido.getDireccionEntrega());
            sentencia.setString(2, pedido.getTipo());
            sentencia.setString(3, pedido.getEstado().name());
            sentencia.setInt(4, pedido.getIdPedido());
            if (sentencia.executeUpdate() == 0) {
                throw new SQLException("No existe un pedido con id " + pedido.getIdPedido());
            }
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el pedido: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, null);
        }
    }

    /** Cambia solo el estado (lo usa el repartidor mientras entrega). */
    public void updateEstado(int idPedido, EstadoPedido estado) throws SQLException {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, estado.name());
            sentencia.setInt(2, idPedido);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el estado del pedido " + idPedido + ": " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, null);
        }
    }

    /** Elimina el pedido. Si tiene entregas, la llave foranea no lo deja borrar. */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = ConexionDB.conectar();
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, id);
            if (sentencia.executeUpdate() == 0) {
                throw new SQLException("No existe un pedido con id " + id);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("No se puede eliminar el pedido " + id + " porque tiene entregas asociadas. Elimina primero sus entregas.", e);
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el pedido: " + e.getMessage(), e);
        } finally {
            ConexionDB.cerrar(conexion, sentencia, null);
        }
    }

}
