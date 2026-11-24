package com.goldencenter.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = System.getenv("GOLDEN_DB_URL");
    private static final String USUARIO = System.getenv("GOLDEN_DB_USER");
    private static final String PASSWORD = System.getenv("GOLDEN_DB_PASSWORD");

    // Constructor privado: la clase no se instancia
    private ConexionBD() {
    }

    public static Connection obtenerConexion() throws SQLException {
        if (URL == null || URL.isBlank()
                || USUARIO == null || USUARIO.isBlank()
                || PASSWORD == null) {
            throw new SQLException("Faltan variables de entorno de MySQL.");
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el controlador JDBC de MySQL.", e);
        }
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}