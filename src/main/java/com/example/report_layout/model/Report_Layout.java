package com.example.report_layout.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Report_Layout {
    private Registro registro;

    public Report_Layout(int id, int secuencia, java.sql.Date createDate, java.sql.Date writeDate, Character name) {
        this.registro = new Registro(id, secuencia, createDate.toLocalDate(), writeDate.toLocalDate(), name);
    }

    public void meter(int viewId) throws SQLException {
        String sql = "INSERT INTO report_layout (id, sequence, create_date, write_date, name, view_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conectar = Conexion.conectar(); PreparedStatement pst = conectar.prepareStatement(sql)) {
            pst.setInt(1, registro.getId());
            pst.setInt(2, registro.getSequence());
            pst.setDate(3, java.sql.Date.valueOf(registro.getCreateDate()));
            pst.setDate(4, java.sql.Date.valueOf(registro.getWriteDate()));
            pst.setString(5, String.valueOf(registro.getName()));
            pst.setInt(6, viewId);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void actualizar() throws SQLException {
        String sql = "UPDATE report_layout SET sequence = ?, create_date = ?, write_date = ?, name = ? WHERE id = ?";
        try (Connection conectar = Conexion.conectar(); PreparedStatement pst = conectar.prepareStatement(sql)) {
            pst.setInt(1, registro.getSequence());
            pst.setDate(2, java.sql.Date.valueOf(registro.getCreateDate()));
            pst.setDate(3, java.sql.Date.valueOf(registro.getWriteDate()));
            pst.setString(4, String.valueOf(registro.getName()));
            pst.setInt(5, registro.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public static void eliminarRegistro(int id) throws SQLException {
        String sql = "DELETE FROM report_layout WHERE id = ?";
        try (Connection conectar = Conexion.conectar(); PreparedStatement pst = conectar.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar registro: " + e.getMessage());
        }
    }


}

