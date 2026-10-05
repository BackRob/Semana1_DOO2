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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    //inserta el pedido y le deja puesto el id que genero la BD, devuelve true si se guardo
    public boolean guardar(Pedido pedido) {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet claves = null;
        try {
            conexion = ConexionBD.conectar();
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
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar el pedido: " + e.getMessage());
            return false;
        } finally {
            ConexionBD.cerrar(conexion, sentencia, claves);
        }
    }

    //lista todos los pedidos de la tabla pedido
    public List<Pedido> listarTodos() {
        return listar("SELECT id, direccion, tipo, estado FROM pedido ORDER BY id", null);
    }

    //lista solo los pedidos con el estado indicado (ej: PENDIENTE)
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        return listar("SELECT id, direccion, tipo, estado FROM pedido WHERE estado = ? ORDER BY id", estado.name());
    }

    //cambia el estado del pedido (lo usa el repartidor mientras entrega)
    public boolean actualizarEstado(int idPedido, EstadoPedido estado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = ConexionBD.conectar();
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, estado.name());
            sentencia.setInt(2, idPedido);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el pedido " + idPedido + ": " + e.getMessage());
            return false;
        } finally {
            ConexionBD.cerrar(conexion, sentencia, null);
        }
    }

    private List<Pedido> listar(String sql, String parametro) {
        List<Pedido> pedidos = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        try {
            conexion = ConexionBD.conectar();
            sentencia = conexion.prepareStatement(sql);
            if (parametro != null) {
                sentencia.setString(1, parametro);
            }
            resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Pedido pedido = crearPedido(resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("tipo"));
                if (pedido != null) {
                    pedido.setEstado(resultado.getString("estado"));
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pedidos: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(conexion, sentencia, resultado);
        }
        return pedidos;
    }

    //arma el objeto segun la columna tipo (la distancia no se guarda en la BD, queda en 0)
    private Pedido crearPedido(int id, String direccion, String tipo) {
        return switch (tipo.toUpperCase()) {
            case "COMIDA" -> new PedidoComida(id, direccion, 0);
            case "ENCOMIENDA" -> new PedidoEncomienda(id, direccion, 0);
            case "EXPRESS" -> new PedidoExpress(id, direccion, 0);
            default -> null;
        };
    }
}
