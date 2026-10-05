package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Maneja la conexion con MySQL (semana 8, antes se llamaba ConexionBD).
 * Los datos de conexion se leen del archivo db.properties, que esta en el .gitignore
 * para que la contraseña no se suba a GitHub.
 */
public class ConexionDB {
    private static final String ARCHIVO = "db.properties";

    /**
     * Abre una conexion nueva con la base de datos.
     * @throws SQLException con un mensaje claro si falta el archivo o MySQL no responde
     */
    public static Connection conectar() throws SQLException {
        Properties datos = new Properties();
        try (FileInputStream archivo = new FileInputStream(ARCHIVO)) {
            datos.load(archivo);
        } catch (IOException e) {
            throw new SQLException("No se encontro el archivo " + ARCHIVO + " (copia db.properties.example y pon tus datos)");
        }
        try {
            return DriverManager.getConnection(datos.getProperty("db.url"),
                    datos.getProperty("db.user"),
                    datos.getProperty("db.password"));
        } catch (SQLException e) {
            throw new SQLException("No se pudo conectar a la base de datos. Revisa que MySQL este encendido y los datos de db.properties.\nDetalle: " + e.getMessage(), e);
        }
    }

    /**
     * Cierra lo que se haya abierto, se llama desde el finally de los DAO.
     * Recibe null en lo que no se uso.
     */
    public static void cerrar(Connection conexion, Statement sentencia, ResultSet resultado) {
        try {
            if (resultado != null) resultado.close();
            if (sentencia != null) sentencia.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion: " + e.getMessage());
        }
    }
}
