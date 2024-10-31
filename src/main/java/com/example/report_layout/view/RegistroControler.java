package com.example.report_layout.view;

import com.example.report_layout.model.Registro;
import com.example.report_layout.model.Report_Layout;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RegistroControler {
    @javafx.fxml.FXML
    private TextArea pantallaSeq;
    @javafx.fxml.FXML
    private Button aceptarId;
    @javafx.fxml.FXML
    private DatePicker pantallaCd;
    @javafx.fxml.FXML
    private TextArea pantallaName;
    @javafx.fxml.FXML
    private DatePicker pantallaWd;
    @javafx.fxml.FXML
    private TextArea pantallaId;
    @javafx.fxml.FXML
    private Button cancelarId;

    @javafx.fxml.FXML
    public void aceptado(ActionEvent actionEvent) {
        try {
            Integer id = Integer.parseInt(pantallaId.getText());
            Integer seq = Integer.parseInt(pantallaSeq.getText());
            java.sql.Date cd = java.sql.Date.valueOf(pantallaCd.getValue());
            java.sql.Date wd = java.sql.Date.valueOf(pantallaWd.getValue());
            char name = pantallaName.getText().isEmpty() ? '\0' : pantallaName.getText().charAt(0);

            int viewId = 1; // Asegúrate de tener un valor válido para view_id

            Report_Layout nuevoElemento = new Report_Layout(id, seq, cd, wd, name);
            nuevoElemento.meter(viewId); // Pasar el viewId aquí

            // Cerrar la ventana después de insertar
            ((Stage) pantallaId.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Manejo de errores de conversión
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores SQL
        }
    }


    @javafx.fxml.FXML
    public void cancelado(ActionEvent actionEvent) {
        ((Stage) pantallaId.getScene().getWindow()).close();
    }

    public void cargarDatos(Registro registro) {
        pantallaId.setText(String.valueOf(registro.getId()));
        pantallaSeq.setText(String.valueOf(registro.getSequence()));
        pantallaCd.setValue(registro.getCreateDate());
        pantallaWd.setValue(registro.getWriteDate());
        pantallaName.setText(String.valueOf(registro.getName()));
    }

}
