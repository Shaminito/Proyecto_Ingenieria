package controladoras;

import basededatos.Conexion;

import basededatos.DatosBD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import utils.Asignaturas;
import utils.Nombres;
import utils.Profesor;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ControladoraInicio implements Initializable {
    @FXML
    JFXComboBox ComboProfesor;

    @FXML
    TableView Tabla;
    @FXML
    TableColumn cLunes, cMartes, cMiercoles,cJueves,cViernes;
    @FXML
    JFXButton btnSalir;


    ObservableList<Nombres> listaCombo;
    ObservableList<Asignaturas> tablass;

    Connection connection = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instancias();
        configurarTabla();
        rellenarCombo();
        rellenarTabla();
        acciones();
    }


    private void configurarTabla() {
        cLunes.setCellValueFactory(new PropertyValueFactory<>("Lunes"));
        cMartes.setCellValueFactory(new PropertyValueFactory<>("Martes"));
        cMiercoles.setCellValueFactory(new PropertyValueFactory<>("Miércoles"));
        cJueves.setCellValueFactory(new PropertyValueFactory<>("Jueves"));
        cViernes.setCellValueFactory(new PropertyValueFactory<>("Viernes"));
    }

    private void rellenarCombo() {
        listaCombo.add(new Nombres("José Alberto Aijon"));
        listaCombo.add(new Nombres("Asuncion Herreros"));
        listaCombo.add(new Nombres("Darío Gallach"));
        listaCombo.add(new Nombres("Fernando Aparicio"));
        listaCombo.add(new Nombres("Juan Jose Martín"));
        listaCombo.add(new Nombres("Christian Sucuzhanay"));
        listaCombo.add(new Nombres("Jose Javier Medina"));

        ComboProfesor.setItems(listaCombo);
    }

    private void rellenarTabla() {

        Thread thread=new Thread(){
            @Override
            public void run() {
                try {

                    String url = "jdbc:sqlite:C:/sqlite/ProyectoIngenieria.db";
                    String username = "root";
                    String password = "";
                    Connection conn = DriverManager.getConnection(url, username, password);
                    Statement stmt = conn.createStatement();
                    ResultSet rs;
                    connection= Conexion.connect();
                    rs = stmt.executeQuery("SELECT * FROM Asignaturas");
                    while (rs.next()){

                        //El select obtiene el nombre de las asignaturas pero no las muestra
                        String nombre=rs.getString(DatosBD.TABLA_ASIGNATURA_NOMBRE);
                        Asignaturas a=new Asignaturas(nombre);
                      tablass.add(a);
                     Tabla.setItems(tablass);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
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
