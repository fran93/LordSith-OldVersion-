package es.ogamebot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author fran
 */
public class ManageDB {

    private Connection connection;

    public ManageDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.1.102:3306/ogame", "ogame", "Ogame123");
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    /**
     *
     * Método utilizado para realizar las instrucciones: INSERT, DELETE y UPDATE
     *
     * @param sql Cadena que contiene la instrucción SQL a ejecutar
     * @return estado regresa el estado de la ejecución, true(éxito) o
     * false(error)
     *
     */
    public boolean ejecutarSQL(String sql) {
        try {
            Statement sentencia = connection.createStatement();
            sentencia.executeUpdate(sql);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return false;
        }

        return true;
    }

    /**
     *
     * Método utilizado para realizar la instrucción SELECT
     *
     * @param sql Cadena que contiene la instrucción SQL a ejecutar
     * @return resultado regresa los registros generados por la consulta
     *
     */
    public ResultSet ejecutarSQLSelect(String sql) {
        ResultSet resultado;
        try {
            Statement sentencia = connection.createStatement();
            resultado = sentencia.executeQuery(sql);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return null;
        }

        return resultado;
    }
    
    /**
     * Mata la conexión
     */
    public void killConnection(){
        try {
            connection.close();
        } catch (SQLException ex) {
             System.err.println(ex.getMessage());
        }
    }
}
