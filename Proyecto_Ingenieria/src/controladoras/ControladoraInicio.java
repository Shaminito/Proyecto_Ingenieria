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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.Ano;
import utils.Asignaturas;
import utils.Horario;
import utils.Profesores;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladoraInicio implements Initializable {
	@FXML
	JFXComboBox<String> ComboProfesor;
	int cursoAMostrar=0;

	@FXML
	Label horario1, horario2, horario3;

	@FXML
	Label zona1, zona2, zona3, zona4, zona5, zona6, zona7, zona8, zona9, zona10, zona11, zona12, zona13, zona14, zona15;

	@FXML
	Button btnSalir;

	private int idProf;

	ObservableList<String> listaCombo;
	// ObservableList<Asignaturas> tablass;

	Conexion connection = null;
	ArrayList<Asignaturas> asignaturas;
	ArrayList<Profesores> profesores;
	ArrayList<Ano> Curso;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		connection = new Conexion();
		idProf = ControladoraLogin.idProf;
		instancias();
		llamadaBBDD();
		rellenarCombo();
		rellenarTabla();
		acciones();
	}






	/*
	 * Se crea el m�todo para obtener el nombre del profesor para rellenar el combo
	 * Tambien debe obtener los datos de las asignaturas que corresponde al profesor
	 */
	private void llamadaBBDD() {
		obtenerAsignaturas();
		obtenerHoraAsignatura();
		obtenerProfesores();
	}

	private void obtenerProfesores() {
		Curso = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rslt = null;

		try {

			conn = connection.getConexion();
			String query = "SELECT distinct Curso FROM Asignaturas order by Curso";
			stmt = conn.createStatement();
			rslt = stmt.executeQuery(query);

			while (rslt.next()) {

				String curso = rslt.getString(Ano.COLUMNA_CURSO);

				Curso.add(new Ano(curso));
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
				// tablass.add(new Asignaturas(nombre, semestre, curso));
				// System.out.println(nombre+" | "+curso+" | "+semestre);
			}
			// Tabla.setItems(tablass);

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

					/*
					 * System.out.println(asignaturas.get(i).getNomAsignatura());
					 * System.out.println("------------"); System.out.println("HORARIO1");
					 * System.out.println(asignaturas.get(i).getHorario1().getHora());
					 * System.out.println(asignaturas.get(i).getHorario1().getClase());
					 * System.out.println("HORARIO2");
					 * System.out.println(asignaturas.get(i).getHorario2().getHora());
					 * System.out.println(asignaturas.get(i).getHorario2().getClase());
					 * System.out.println("------------");
					 */
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

	/*
	 * private void configurarTabla() { //cLunes.setCellValueFactory(new
	 * PropertyValueFactory<>("Lunes")); //cMartes.setCellValueFactory(new
	 * PropertyValueFactory<>("Martes")); //cMiercoles.setCellValueFactory(new
	 * PropertyValueFactory<>("Mi�rcoles")); //cJueves.setCellValueFactory(new
	 * PropertyValueFactory<>("Jueves")); //cViernes.setCellValueFactory(new
	 * PropertyValueFactory<>("Viernes")); }
	 */

	private void rellenarCombo() {
		for (int i = 0; i < Curso.size(); i++) {
			listaCombo.add(Curso.get(i).getCurso());
		}
		/*
		 * listaCombo.add("José Alberto Aijon"); listaCombo.add("Asuncion Herreros");
		 * listaCombo.add("Darío Gallach"); listaCombo.add("Fernando Aparicio");
		 * listaCombo.add("Juan Jose Martín"); listaCombo.add("Christian Sucuzhanay");
		 * listaCombo.add("Jose Javier Medina");
		 */

		ComboProfesor.setItems(listaCombo);
	}

	private void rellenarTabla() {
		System.out.println("Rellenando tabla...");
		

		for (int i = 0; i < asignaturas.size(); i++) {
			
			Asignaturas asignatura = asignaturas.get(i);
			
			for(int j = 0; j < asignatura.getHorario().length ; j++) {
				
				if(asignatura.getCurso() == cursoAMostrar ) { //Aqu� hay que introducir la variable de la selecci�n del combo
					
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
		listaCombo = FXCollections.observableArrayList();
		// tablass = FXCollections.observableArrayList();
	}

	private void acciones() {
		btnSalir.setOnAction(new ManejoAction());
		ComboProfesor.setOnAction(new ManejoAction());
		
	}

	class ManejoAction implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (event.getSource() == btnSalir) {
				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.close();
			}
			else if (event.getSource() == ComboProfesor) {
				cursoAMostrar=ComboProfesor.getSelectionModel().getSelectedIndex()+1;
				borrarTabla();
				rellenarTabla();	
				}
				
			}
		}
		
		private void borrarTabla() {
			
//			for(int i=1;i<=15;i++) {
//				(zona+i).setText("");
//			}
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

