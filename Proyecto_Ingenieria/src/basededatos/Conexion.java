package basededatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;


public class Conexion {

    /*public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:dbSQLite/ProyectoIngenieria.db";
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
    */
	private String driver;
	private String url;
	
	
	public Conexion() {
		driver = "org.sqlite.JDBC";
		url = "jdbc:sqlite:dbSQLite/ProyectoIngenieria.db";
	}

	public Connection getConexion() throws ClassNotFoundException, SQLException {

		Class.forName(driver);

		SQLiteConfig config = new SQLiteConfig(); //objeto de configuracion de dicha conexion		
		config.enforceForeignKeys(true);  //controlar las forengkeys
		
		Connection con = DriverManager.getConnection(url,config.toProperties()); 
		System.out.println("Conexion establecida");
		return con;
	}
  /* public static void main(String[] args) {
        connect();
    }*/

}
