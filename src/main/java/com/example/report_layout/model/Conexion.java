package com.example.report_layout.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {


    public static Connection con=null;
    public static Connection conectar() {
        String URL = "jdbc:postgresql://localhost:5432/usuario";
        String USUARIO = "odoo";
        String CONTRASENA = "odoo";
        try {

            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection(URL, USUARIO, CONTRASENA);

            if (con != null) {
                System.out.println("Conectado a la base de datos");
            } else {
                System.out.println("No se pudo conectar a la base de datos");
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión SQL: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Controlador no encontrado: " + e.getMessage());
        }
        return con;
    }
}