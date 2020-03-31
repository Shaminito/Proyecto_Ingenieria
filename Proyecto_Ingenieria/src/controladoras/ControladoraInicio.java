package controladoras;

import basededatos.Conexion;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.Asignaturas;
import utils.Profesores;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladoraInicio implements Initializable {
	@FXML
	JFXComboBox<String> ComboProfesor;

	@FXML
	TableView<Asignaturas> Tabla;
	@FXML
	TableColumn cLunes, cMartes, cMiercoles, cJueves, cViernes;
	@FXML
	JFXButton btnSalir;

	private int idProf;

	ObservableList<String> listaCombo;
	ObservableList<Asignaturas> tablass;

	Conexion connection = null;
	ArrayList<Asignaturas> asignaturas;
	ArrayList<Profesores> profesores;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		connection = new Conexion();
		idProf = ControladoraLogin.idProf;
		instancias();
		configurarTabla();
		llamadaBBDD();
		rellenarCombo();
		rellenarTabla();
		acciones();
	}

	/*
	 * Se crea el método para obtener el nombre del profesor para rellenar el combo
	 * Tambien debe obtener los datos de las asignaturas que corresponde al profesor
	 */
	private void llamadaBBDD() {
		obtenerAsignaturas();
		obtenerProfesores();
	}

	private void obtenerProfesores() {
		profesores = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rslt = null;
		
		try {
			
			conn = connection.getConexion();
			String query = "SELECT * FROM Profesores";
			stmt = conn.createStatement();
			rslt = stmt.executeQuery(query);

			while (rslt.next()) {

				String nombre = rslt.getString(Profesores.COLUMNA_NOMBRE);
				String correo = rslt.getString(Profesores.COLUMNA_CORREO);
				
				profesores.add(new Profesores(nombre, correo));
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
			if(rslt != null) {rslt.close();}
			if(stmt != null) {stmt.close();}
			if(conn != null) {conn.close();}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void obtenerAsignaturas() {
		asignaturas = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rslt = null;
		
		try {
			
			conn = connection.getConexion();
			String query = "SELECT * FROM Asignaturas WHERE idProfesor = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, idProf);
			rslt = pstmt.executeQuery();

			while (rslt.next()) {

				String nombre = rslt.getString(Asignaturas.COLUMNA_NOMBRE);
				int curso = rslt.getInt(Asignaturas.COLUMNA_CURSO);
				int semestre = rslt.getInt(Asignaturas.COLUMNA_SEMESTRE);
				
				asignaturas.add(new Asignaturas(nombre, semestre, curso));
				tablass.add(new Asignaturas(nombre, semestre, curso));
				//System.out.println(nombre+" | "+curso+" | "+semestre);
			}
			Tabla.setItems(tablass);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
			if(rslt != null) {rslt.close();}
			if(pstmt != null) {pstmt.close();}
			if(conn != null) {conn.close();}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void configurarTabla() {
		cLunes.setCellValueFactory(new PropertyValueFactory<>("Lunes"));
		cMartes.setCellValueFactory(new PropertyValueFactory<>("Martes"));
		cMiercoles.setCellValueFactory(new PropertyValueFactory<>("Miércoles"));
		cJueves.setCellValueFactory(new PropertyValueFactory<>("Jueves"));
		cViernes.setCellValueFactory(new PropertyValueFactory<>("Viernes"));
	}

	private void rellenarCombo() {
		for(int i = 0; i < profesores.size(); i++) {
			listaCombo.add(profesores.get(i).getNombre());
		}
		/*
		 * listaCombo.add("JosÃ© Alberto Aijon");
		listaCombo.add("Asuncion Herreros");
		listaCombo.add("DarÃ­o Gallach");
		listaCombo.add("Fernando Aparicio");
		listaCombo.add("Juan Jose MartÃ­n");
		listaCombo.add("Christian Sucuzhanay");
		listaCombo.add("Jose Javier Medina");
		 */

		ComboProfesor.setItems(listaCombo);
	}

	private void rellenarTabla() {

		/*
		 * Thread thread = new Thread() {
		 * @Override
			public void run() {
				try {
					Connection conn;
					PreparedStatement pstmt;
					ResultSet rs;

					conn = connection.getConexion();
					String query = "Select * FROM ASIGNATURAS WHERE idProfesor = ?";
					pstmt = conn.prepareStatement(query);
					pstmt.setInt(idProf, 1);
					rs = pstmt.executeQuery();

					while (rs.next()) {

						//String nombre = rs.getString(DatosBD.TABLA_ASIGNATURA_NOMBRE);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				Tabla.setItems(tablass);
			}
		};
		thread.start();
		*/
	}
			

	private void instancias() {
		listaCombo = FXCollections.observableArrayList();
		tablass = FXCollections.observableArrayList();
	}

	private void acciones() {
		btnSalir.setOnAction(new ManejoAction());
	}

	class ManejoAction implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (event.getSource() == btnSalir) {
				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.close();

			}
		}
	}
}
