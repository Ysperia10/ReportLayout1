package com.example.report_layout.view;

import com.example.report_layout.model.Registro;
import com.example.report_layout.model.Report_Layout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    private Registro registroActual;
    private Report_Layout layoutActual;

    // Método para cargar datos en el formulario al editar un registro
    public void cargarDatos(Registro registro) {
        this.registroActual = registro;
        this.layoutActual = new Report_Layout(
                registro.getId(),
                registro.getSequence(),
                java.sql.Date.valueOf(registro.getCreateDate()),
                java.sql.Date.valueOf(registro.getWriteDate()),
                registro.getName()
        );

        pantallaId.setText(String.valueOf(registro.getId()));
        pantallaSeq.setText(String.valueOf(registro.getSequence()));
        pantallaCd.setValue(registro.getCreateDate());
        pantallaWd.setValue(registro.getWriteDate());
        pantallaName.setText(String.valueOf(registro.getName()));
    }

    @javafx.fxml.FXML
    public void aceptado(ActionEvent actionEvent) {
        try {
            Integer id = Integer.parseInt(pantallaId.getText());
            Integer seq = Integer.parseInt(pantallaSeq.getText());
            java.sql.Date cd = java.sql.Date.valueOf(pantallaCd.getValue());
            java.sql.Date wd = java.sql.Date.valueOf(pantallaWd.getValue());
            char name = pantallaName.getText().isEmpty() ? '\0' : pantallaName.getText().charAt(0);

            if (registroActual == null) {
                // Añadir nuevo registro
                int viewId = 1; // Asegúrate de tener un valor válido para view_id
                Report_Layout nuevoElemento = new Report_Layout(id, seq, cd, wd, name);
                nuevoElemento.meter(viewId);
            } else {
                // Actualizar registro existente
                registroActual.setSequence(seq);
                registroActual.setCreateDate(cd.toLocalDate());
                registroActual.setWriteDate(wd.toLocalDate());
                registroActual.setName(name);

                Report_Layout reportLayout = new Report_Layout(
                        registroActual.getId(),
                        registroActual.getSequence(),
                        java.sql.Date.valueOf(registroActual.getCreateDate()),
                        java.sql.Date.valueOf(registroActual.getWriteDate()),
                        registroActual.getName()
                );
                reportLayout.actualizar();
            }

            // Cerrar la ventana después de insertar o actualizar
            ((Stage) pantallaId.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Manejo de errores de conversión
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores SQL
        }
    }


    @FXML
    public void cancelado(ActionEvent actionEvent) {
        ((Stage) pantallaId.getScene().getWindow()).close();
    }
}