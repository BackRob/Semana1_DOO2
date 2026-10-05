package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConexionBD {
    //los datos de conexion ya no van en el codigo, se leen del archivo db.properties
    //ese archivo esta en el .gitignore, asi la contraseña no se sube a GitHub
    private static final String ARCHIVO = "db.properties";

    public static Connection conectar() throws SQLException {
        Properties datos = new Properties();
        try (FileInputStream archivo = new FileInputStream(ARCHIVO)) {
            datos.load(archivo);
        } catch (IOException e) {
            throw new SQLException("No se encontro el archivo " + ARCHIVO + " (copia db.properties.example y pon tus datos)");
        }
        String url = datos.getProperty("db.url");
        String user = datos.getProperty("db.user");
        String password = datos.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    //cierra lo que se haya abierto, se llama desde el finally de los DAO
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
