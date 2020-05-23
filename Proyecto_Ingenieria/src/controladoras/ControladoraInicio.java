package controladoras;

import basededatos.Conexion;
import controladoras.ControladoraInicio.ManejoAction;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
	JFXComboBox<String> comboPersonal;
	
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
	Button btnSalir;

	private int idProf;

	ObservableList<String> listaProfesores;
	ObservableList<String> listaSemestres;
	ObservableList<String> listaComboPersonal;
	ObservableList<String> listaCursos;

	Conexion connection = null;
	ArrayList<Asignaturas> asignaturas;
	ArrayList<Profesores> profesores;
	ArrayList<Integer> semestres;
	ArrayList<Integer> cursos;
	
	int cursoAMostrar = 0;
	int semestreAMostrar = 0;

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
		obtenerAsignaturas(idProf);
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

	private void obtenerAsignaturas(int idProf) {
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
				
				System.out.println("------------------------------");
				System.out.println(idAsignatura+" - "+nombre+" - "+curso+" - "+semestre);
				System.out.println("------------------------------");

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
	
	private int obtenerIdProf(String nomProf) {
		int idProf = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rslt = null;

		try {

			conn = connection.getConexion();
			String query = "SELECT * FROM Profesores WHERE NombreProf = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, nomProf);
			rslt = pstmt.executeQuery();
			
			while (rslt.next()) {

				idProf = rslt.getInt(Profesores.COLUMNA_ID);
				System.out.println(idProf);
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
		return idProf;
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
		
		/*
		 * listaCombo.add("JosÃ© Alberto Aijon"); listaCombo.add("Asuncion Herreros");
		 * listaCombo.add("DarÃ­o Gallach"); listaCombo.add("Fernando Aparicio");
		 * listaCombo.add("Juan Jose MartÃ­n"); listaCombo.add("Christian Sucuzhanay");
		 * listaCombo.add("Jose Javier Medina");
		 */
		
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
		
		//Combo personal
		for (int i = 0; i < cursos.size(); i++) {
			listaComboPersonal.add(cursos.get(i)+"º curso");
		}
			
		comboPersonal.setItems(listaComboPersonal);
	}

	private void rellenarTabla() {
		System.out.println("Rellenando tabla...");
		

		for (int i = 0; i < asignaturas.size(); i++) {
			
			Asignaturas asignatura = asignaturas.get(i);
			
			for(int j = 0; j < asignatura.getHorario().length ; j++) {
				
				if(asignatura.getCurso() == cursoAMostrar ) { //Aquï¿½ hay que introducir la variable de la selecciï¿½n del combo
					
					if(asignatura.getSemestre() == semestreAMostrar) {
						
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

	}

	private void instancias() {
		listaComboPersonal = FXCollections.observableArrayList();
		listaProfesores = FXCollections.observableArrayList();
		listaSemestres = FXCollections.observableArrayList();
		listaCursos = FXCollections.observableArrayList();
	}

	private void acciones() {
		//Añadir eventos de los botones
		btnSalir.setOnAction(new ManejoAction());
		btnProfesores.setOnAction(new ManejoAction());
		btnVolver.setOnAction(new ManejoAction());
		comboPersonal.setOnAction(new ManejoAction());
		ComboProfesor.setOnAction(new ManejoAction());
		cbSemestre.setOnAction(new ManejoAction());
		cbCurso.setOnAction(new ManejoAction());
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
				comboPersonal.setVisible(false);
				ComboProfesor.setVisible(true);
				cbSemestre.setVisible(true);
				cbCurso.setVisible(true);
				btnVolver.setVisible(true);
			}
			//Para volver a lo que pertenece al profesor
			else if(event.getSource() == btnVolver) {
				btnProfesores.setVisible(true);
				comboPersonal.setVisible(true);
				ComboProfesor.setVisible(false);
				cbSemestre.setVisible(false);
				cbCurso.setVisible(false);
				btnVolver.setVisible(false);
				borrarTabla();
				obtenerAsignaturas(idProf);
				obtenerHoraAsignatura();
				cursoAMostrar = 1;
				semestreAMostrar = 1;
				rellenarTabla();
			}
			else if (event.getSource() == comboPersonal) {
				semestreAMostrar = 1;
				cursoAMostrar = comboPersonal.getSelectionModel().getSelectedIndex()+1;
				borrarTabla();
				rellenarTabla();
				semestreAMostrar = 0;
			}
			else if(event.getSource() == ComboProfesor) {
				String profesor = ComboProfesor.getSelectionModel().getSelectedItem();
				borrarTabla();
				obtenerAsignaturas(obtenerIdProf(profesor));
				obtenerHoraAsignatura();
				rellenarTabla();
			}
			else if(event.getSource() == cbSemestre) {
				semestreAMostrar = cbSemestre.getSelectionModel().getSelectedIndex()+1;
				borrarTabla();
				rellenarTabla();
			}
			else if(event.getSource() == cbCurso) {
				cursoAMostrar = cbCurso.getSelectionModel().getSelectedIndex()+1;
				borrarTabla();
				rellenarTabla();
			}
		}
	}
	
	private void borrarTabla() {
		
//		for(int i=1;i<=15;i++) {
//			(zona+i).setText("");
//		}
			zona1.setText("");
			zona2.setText("");	
			zona3.setText("");	
			zona4.setText("");	
			zona5.setText("");	
			zona6.setText("");	
			zona7.setText("");	
			zona8.setText("");					
			zona9.setText("");	
			zona10.setText("");	
			zona11.setText("");	
			zona12.setText("");	
			zona13.setText("");	
			zona14.setText("");	
			zona15.setText("");	
	}
}
