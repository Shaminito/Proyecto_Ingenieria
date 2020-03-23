package basededatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {

    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:C:/sqlite/ProyectoIngenieria.db";
          //crear una conexion con la bdd
            conn = DriverManager.getConnection(url);

            System.out.println("Conexion establecida");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return conn;
    }
  /* public static void main(String[] args) {
        connect();
    }*/

}
