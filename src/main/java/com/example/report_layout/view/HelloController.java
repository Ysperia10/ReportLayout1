package com.example.report_layout.view;

import com.example.report_layout.HelloApplication;
import com.example.report_layout.model.Conexion;
import com.example.report_layout.model.Registro;
import com.example.report_layout.model.Report_Layout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private TextField pantalla;
    @FXML
    private TableView<Registro> tabla;
    @FXML
    private TableColumn<Registro, Integer> idCol;
    @FXML
    private TableColumn<Registro, Integer> sequenceCol;
    @FXML
    private TableColumn<Registro, LocalDate> writeDateCol;
    @FXML
    private TableColumn<Registro, LocalDate> createDateCol;
    @FXML
    private TableColumn<Registro, Character> nameCol;

    private ObservableList<Registro> registros = FXCollections.observableArrayList();

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        sequenceCol.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        writeDateCol.setCellValueFactory(new PropertyValueFactory<>("writeDate"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        tabla.setItems(registros);
    }

    public void cargarRegistro(ActionEvent actionEvent) {
        Task<Void> cargarTarea = new Task<>() {
            @Override
            protected Void call() {
                List<Registro> datos = new ArrayList<>();
                try (Connection conexion = Conexion.conectar();
                     Statement sentencia = conexion.createStatement();
                     ResultSet resultado = sentencia.executeQuery("SELECT ID, SEQUENCE, WRITE_DATE, CREATE_DATE, NAME FROM report_layout")) {

                    while (resultado.next()) {
                        int id = resultado.getInt("id");
                        int sequence = resultado.getInt("sequence");
                        LocalDate writeDate = resultado.getDate("write_date").toLocalDate();
                        LocalDate createDate = resultado.getDate("create_date").toLocalDate();
                        String nameStr = resultado.getString("name");
                        Character name = (nameStr != null && !nameStr.isEmpty()) ? nameStr.charAt(0) : ' ';

                        datos.add(new Registro(id, sequence, writeDate, createDate, name));
                    }
                    Platform.runLater(() -> registros.setAll(datos));
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void failed() {
                System.out.println("Error en la carga de registros");
                this.getException().printStackTrace();
            }
        };

        new Thread(cargarTarea).start();
    }

    public void añadirRegistro(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("añadiroeditar.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setTitle("Añadir Registro");
        stage.setScene(new Scene(root, 320, 240));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.showAndWait();
    }

    public void editarRegistro(ActionEvent actionEvent) throws IOException {
        Registro registroSeleccionado = tabla.getSelectionModel().getSelectedItem();
        if (registroSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No hay registro seleccionado");
            alert.setContentText("Por favor, selecciona un registro para editar.");
            alert.showAndWait();
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("añadiroeditar.fxml"));
        Parent root = fxmlLoader.load();

        RegistroControler controlador = fxmlLoader.getController();
        controlador.cargarDatos(registroSeleccionado); // Método que deberías implementar en RegistroControler para cargar datos

        Stage stage = new Stage();
        stage.setTitle("Editar Registro");
        stage.setScene(new Scene(root, 320, 240));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.showAndWait();
    }


    public void borrarRegistro(ActionEvent actionEvent) throws IOException {
        Registro registroSeleccionado = tabla.getSelectionModel().getSelectedItem();
        if (registroSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No se ha seleccionado ningún registro");
            alert.setContentText("Por favor, selecciona un registro para eliminar.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de que desea eliminar este registro?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Report_Layout.eliminarRegistro(registroSeleccionado.getId());
                    cargarRegistro(actionEvent);  // Recargar los registros
                } catch (SQLException e) {
                    System.err.println("Error al eliminar el registro: " + e.getMessage());
                }
            }
        });
    }
    @FXML
    public void buscarRegistro(ActionEvent actionEvent) {
        String textoBusqueda = pantalla.getText().trim(); // Asegúrate de que pantalla esté inicializada
        if (!textoBusqueda.isEmpty()) {
            try {
                int idBusqueda = Integer.parseInt(textoBusqueda); // Convierte el texto a un entero
                // Busca el registro
                Registro registroEncontrado = registros.stream()
                        .filter(reg -> reg.getId() == idBusqueda) // Comparación de enteros
                        .findFirst()
                        .orElse(null);

                if (registroEncontrado != null) {
                    tabla.getSelectionModel().select(registroEncontrado);
                    tabla.scrollTo(registroEncontrado);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No encontrado");
                    alert.setHeaderText("Registro no encontrado");
                    alert.setContentText("No existe un registro con el ID ingresado.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error de formato");
                alert.setHeaderText("ID no válido");
                alert.setContentText("Por favor, ingresa un ID numérico.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campo vacío");
            alert.setHeaderText("ID vacío");
            alert.setContentText("Por favor, ingresa un ID para buscar.");
            alert.showAndWait();
        }
    }



}






