package controladoras;

import basededatos.Conexion;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ControladoraLogin implements Initializable {

	@FXML
	JFXTextField usuario;

	@FXML
	JFXPasswordField pass;

	@FXML
	Label labelRegistro;

	@FXML
	CheckBox checkbox;

	@FXML
	Button botonIniciar;
	

	Conexion acc;
	String us, pw, cus, cpw;
	
	public static int idProf;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		acc = new Conexion();
		acciones();
		leerFichero();

	}

	private void leerFichero() {
		try {

			FileReader fr = new FileReader("src/sesiones/ficherologin.txt");
			BufferedReader br = new BufferedReader(fr);
			us = br.readLine();
			pw = br.readLine();
			usuario.setText(us);
			pass.setText(pw);
			br.close();
		} catch (IOException e) {

		}
	}

	private void acciones() {
		labelRegistro.setOnMouseEntered(new ManejoRaton());
		labelRegistro.setOnMouseExited(new ManejoRaton());
		labelRegistro.setOnMousePressed(new ManejoRaton());
		labelRegistro.setOnMouseReleased(new ManejoRaton());

		botonIniciar.setOnAction(new ManejoAction());
		checkbox.setOnAction(new ManejoAction());
	}

	class ManejoRaton implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			if (event.getSource() == labelRegistro) {
				if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
					labelRegistro.getScene().setCursor(Cursor.OPEN_HAND);
					labelRegistro.setTextFill(Color.BLUE);
				} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
					labelRegistro.getScene().setCursor(Cursor.DEFAULT);
					labelRegistro.setTextFill(Color.BLACK);
				} else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					labelRegistro.getScene().setCursor(Cursor.OPEN_HAND);
					try {
						Parent root = FXMLLoader.load(getClass().getResource("../vistas/registro.fxml"));
						Scene scene = new Scene(root, 600, 400);
						Stage stage = (Stage) labelRegistro.getScene().getWindow();
						stage.setScene(scene);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					labelRegistro.getScene().setCursor(Cursor.DEFAULT);
				}
			}
		}
	}

	class ManejoAction implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			File f = new File("src/sesiones/ficherologin.txt");
			if (event.getSource() == botonIniciar) {
				if (usuario.getText().isEmpty() || pass.getText().isEmpty()) {
					Alert dialogoError = new Alert(Alert.AlertType.ERROR);
					dialogoError.setTitle("ERROR");
					dialogoError.setHeaderText("Introduzca todos los campos");
					dialogoError.initStyle(StageStyle.DECORATED);
					java.awt.Toolkit.getDefaultToolkit().beep();
					dialogoError.showAndWait();
				} else if (!(usuario.getText().isEmpty() || pass.getText().isEmpty())) {
					
					Connection conn;
					PreparedStatement pstmt;
					ResultSet rs;
					
					 try {
						 
						conn = acc.getConexion();
						String query = "SELECT * FROM Profesores WHERE CorreoProf = ? AND ContProf = ?";
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, usuario.getText().toString());
						pstmt.setString(2, pass.getText().toString());
						rs = pstmt.executeQuery();
						
						if(rs.next()) {
							idProf = rs.getInt(1);
							Parent root = FXMLLoader.load(getClass().getResource("../vistas/tabla.fxml"));
                            Scene scene = new Scene(root, 600, 400);
                            Stage stage = (Stage) botonIniciar.getScene().getWindow();
                            stage.setScene(scene);
						}
						else {
							Alert dialogoError = new Alert(Alert.AlertType.ERROR);
							 dialogoError.setTitle("ERROR");
							 dialogoError.setHeaderText("Campos incorrectos");
							 dialogoError.initStyle(StageStyle.DECORATED);
							 java.awt.Toolkit.getDefaultToolkit().beep(); dialogoError.showAndWait();
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} else if (checkbox.isSelected()) {
				try {
					FileWriter fw = new FileWriter(f);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(usuario.getText());
					bw.newLine();
					bw.write(pass.getText());
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (!(checkbox.isSelected())) {
				f.delete();

			}
		}
	}
}