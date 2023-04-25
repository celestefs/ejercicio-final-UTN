package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    Connection conectar = null;
    String usuario = "root";
    String contraseña = "root";
    String bd = "ejercicio_final";
    String ip = "localhost";
    String puerto = "3306";

    String ruta = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;

    public Connection estableceConexion() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conectar = DriverManager.getConnection(ruta, usuario, contraseña);

            System.out.println("Se conecto");

        } catch (Exception e) {
            System.out.println("No se conectó correctamente");
        }

        return conectar;
    }

    public void cerrarConexion() throws SQLException {
        try {
            conectar.close();
        } catch (Exception e) {
        }
    }
}
