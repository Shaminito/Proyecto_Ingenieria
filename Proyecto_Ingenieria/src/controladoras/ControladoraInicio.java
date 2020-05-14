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
import javafx.scene.control.Label;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.Asignaturas;
import utils.Horario;
import utils.Profesores;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladoraInicio implements Initializable {
	
	@FXML
	JFXButton btnProfesores;
	
	@FXML
	JFXComboBox<String> ComboProfesor;
	
	@FXML
	JFXComboBox<String> cbSemestre;
	
	@FXML
	JFXComboBox<String> cbCurso;

	@FXML
	JFXButton btnVolver;
	
	@FXML
	Label horario1, horario2, horario3;

	@FXML
	Label zona1, zona2, zona3, zona4, zona5, zona6, zona7, zona8, zona9, zona10, zona11, zona12, zona13, zona14, zona15;

	@FXML
	JFXButton btnSalir;

	private int idProf;

	ObservableList<String> listaProfesores;
	ObservableList<String> listaSemestres;
	ObservableList<String> listaCursos;

	Conexion connection = null;
	ArrayList<Asignaturas> asignaturas;
	ArrayList<Profesores> profesores;
	ArrayList<Integer> semestres;
	ArrayList<Integer> cursos;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		connection = new Conexion();
		idProf = ControladoraLogin.idProf;
		instancias();
		// configurarTabla();
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
		obtenerHoraAsignatura();
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
				if (rslt != null) {
					rslt.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
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

				int idAsignatura = rslt.getInt(Asignaturas.COLUMNA_IDASIG);
				String nombre = rslt.getString(Asignaturas.COLUMNA_NOMBRE);
				int curso = rslt.getInt(Asignaturas.COLUMNA_CURSO);
				int semestre = rslt.getInt(Asignaturas.COLUMNA_SEMESTRE);

				asignaturas.add(new Asignaturas(idAsignatura, nombre, semestre, curso));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rslt != null) {
					rslt.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void obtenerSemestres() {
		semestres = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rslt = null;

		try {

			conn = connection.getConexion();
			String query = "SELECT DISTINCT semestre FROM Asignaturas";
			stmt = conn.createStatement();
			rslt = stmt.executeQuery(query);

			while (rslt.next()) {
				semestres.add(rslt.getInt(Asignaturas.COLUMNA_SEMESTRE));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rslt != null) {
					rslt.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void obtenerCursos() {
		cursos = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rslt = null;

		try {

			conn = connection.getConexion();
			String query = "SELECT DISTINCT curso FROM Asignaturas ORDER BY curso";
			stmt = conn.createStatement();
			rslt = stmt.executeQuery(query);

			while (rslt.next()) {
				cursos.add(rslt.getInt(Asignaturas.COLUMNA_CURSO));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rslt != null) {
					rslt.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void obtenerHoraAsignatura() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rslt = null;

		try {
			for (int i = 0; i < asignaturas.size(); i++) {

				conn = connection.getConexion();
				String query = "SELECT * FROM Horario WHERE Id_Asignatura = ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, asignaturas.get(i).getIdAsignaturas());
				rslt = pstmt.executeQuery();

				while (rslt.next()) {

					asignaturas.get(i).addHorario(Integer.parseInt(rslt.getString(Horario.COLUMNA_HORA1)),
							rslt.getString(Horario.COLUMNA_CLASE1), Integer.parseInt(rslt.getString(Horario.COLUMNA_HORA2)),
							rslt.getString(Horario.COLUMNA_CLASE2));
				}

			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rslt != null) {
					rslt.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void rellenarCombo() {
		//Combo de los profesores
		for (int i = 0; i < profesores.size(); i++) {
			listaProfesores.add(profesores.get(i).getNombre());
		}

		ComboProfesor.setItems(listaProfesores);
		
		//Combo de los semestres
		obtenerSemestres();
		for (int i = 0; i < semestres.size(); i++) {
			listaSemestres.add(semestres.get(i)+"º semestre");
		}
		
		cbSemestre.setItems(listaSemestres);
		
		//Combo de los cursos
		obtenerCursos();
		for (int i = 0; i < cursos.size(); i++) {
			listaCursos.add(cursos.get(i)+"º curso");
		}
		
		cbCurso.setItems(listaCursos);
	}

	private void rellenarTabla() {

		for (int i = 0; i < asignaturas.size(); i++) {
			
			Asignaturas asignatura = asignaturas.get(i);
			
			for(int j = 0; j < asignatura.getHorario().length ; j++) {
				
				if(asignatura.getCurso() == 1) {
					
					if(asignatura.getHorario(j).getHora() == 1) {
						
						zona1.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 2) {
						zona2.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 3) {
						zona3.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 4) {
						zona4.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 5) {
						zona5.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 6) {
						zona6.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 7) {
						zona7.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 8) {
						zona8.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 9) {
						zona9.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 10) {
						zona10.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 11) {
						zona11.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 12) {
						zona12.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 13) {
						zona13.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 14) {
						zona14.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					else if(asignatura.getHorario(j).getHora() == 15) {
						zona15.setText(asignatura.getNomAsignatura()+"\n - Clase: "+asignatura.getHorario(j).getClase()+"\n - Curso: "+asignatura.getCurso());
					}
					
				}
			}
		}
	}

	private void instancias() {
		listaProfesores = FXCollections.observableArrayList();
		listaSemestres = FXCollections.observableArrayList();
		listaCursos = FXCollections.observableArrayList();
	}

	private void acciones() {
		//Añadir eventos de los botones
		btnSalir.setOnAction(new ManejoAction());
		btnProfesores.setOnAction(new ManejoAction());
		btnVolver.setOnAction(new ManejoAction());
	}

	class ManejoAction implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//Botón para finalizar el programa
			if (event.getSource() == btnSalir) {
				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.close();
			}
			//Para acceder a los profesores
			else if(event.getSource() == btnProfesores) {
				btnProfesores.setVisible(false);
				ComboProfesor.setVisible(true);
				cbSemestre.setVisible(true);
				cbCurso.setVisible(true);
				btnVolver.setVisible(true);
			}
			//Para volver a lo que pertenece al profesor
			else if(event.getSource() == btnVolver) {
				btnProfesores.setVisible(true);
				ComboProfesor.setVisible(false);
				cbSemestre.setVisible(false);
				cbCurso.setVisible(false);
				btnVolver.setVisible(false);
			}
		}
	}
}
